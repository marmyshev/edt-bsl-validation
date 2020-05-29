package org.mard.dt.bsl.validation.ui;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;
import org.mard.dt.bsl.validation.StructureCtorTooManyKeysValidator;

import com._1c.g5.v8.dt.bsl.model.BslPackage;
import com._1c.g5.v8.dt.bsl.model.Expression;
import com._1c.g5.v8.dt.bsl.model.OperatorStyleCreator;
import com._1c.g5.v8.dt.bsl.model.SimpleStatement;
import com._1c.g5.v8.dt.bsl.model.StringLiteral;
import com._1c.g5.v8.dt.bsl.ui.quickfix.AbstractExternalQuickfixProvider;
import com.google.common.base.Function;

public class StructureCtorTooManyKeysQuickfixProvider extends AbstractExternalQuickfixProvider {

	private static final String INSERT_NAME_RU = "Вставить"; //$NON-NLS-1$

	@Fix(StructureCtorTooManyKeysValidator.ERROR_CODE)
	public void refactoringStructureConstrucote(final Issue issue, IssueResolutionAcceptor acceptor) {

		acceptor.accept(issue, Messages.StructureCtorTooManyKeysQuickfixProvider_title,
				Messages.StructureCtorTooManyKeysQuickfixProvider_description, null,
				new ExternalQuickfixModification<StringLiteral>(issue, StringLiteral.class,
						new Function<StringLiteral, TextEdit>() {
							@Override
							public TextEdit apply(StringLiteral literal) {
								EObject parent = literal.eContainer();
								SimpleStatement statement;
								if (parent != null && parent.eContainer() instanceof SimpleStatement)
									statement = (SimpleStatement) parent.eContainer();
								else
									statement = null;

								if (parent instanceof OperatorStyleCreator && statement != null) {
									OperatorStyleCreator osc = (OperatorStyleCreator) parent;
									return createEdits(literal, osc, statement);
								}
								return null;
							}

						}));
	}

	private TextEdit createEdits(StringLiteral literal, OperatorStyleCreator osc, SimpleStatement statement) {
		List<INode> nodes = NodeModelUtils.findNodesForFeature(literal, BslPackage.Literals.STRING_LITERAL__LINES);
		if (nodes != null && !nodes.isEmpty()) {
			INode literalNode = nodes.get(0);
			int removeLength = literalNode.getTotalLength();
			MultiTextEdit edit = new MultiTextEdit();

			ICompositeNode statementNode = NodeModelUtils.getNode(statement);
			int insertOffset = statementNode.getTotalEndOffset() + 1;
			Expression left = statement.getLeft();
			ICompositeNode leftNode = NodeModelUtils.getNode(left);
			String leftText = leftNode.getText().trim();

			EList<Expression> params = osc.getParams();
			for (int i = 1; i < params.size(); i++) {
				Expression param = params.get(i);
				ICompositeNode node = NodeModelUtils.findActualNodeFor(param);
				removeLength = node.getTotalEndOffset() - literalNode.getTotalOffset();
			}
			DeleteEdit remove = new DeleteEdit(literalNode.getTotalOffset(), removeLength);
			edit.addChild(remove);

			InsertEdit insert = new InsertEdit(insertOffset, System.lineSeparator());
			edit.addChild(insert);

			String[] keys = String.join("", literal.lines(true)).replace(" ", "").split(","); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i];

				StringBuilder sb = new StringBuilder();
				sb.append(leftText);
				sb.append("."); //$NON-NLS-1$
				sb.append(INSERT_NAME_RU);
				sb.append("(\""); //$NON-NLS-1$
				sb.append(key);
				sb.append("\""); //$NON-NLS-1$
				if (i + 1 < params.size()) {
					Expression param = params.get(i + 1);
					ICompositeNode node = NodeModelUtils.findActualNodeFor(param);
					sb.append(", "); //$NON-NLS-1$
					sb.append(node.getText().trim());
				}
				sb.append(");"); //$NON-NLS-1$
				sb.append(System.lineSeparator());
				insert = new InsertEdit(insertOffset, sb.toString());
				edit.addChild(insert);
			}
			return edit;

		}
		return null;
	}
}
