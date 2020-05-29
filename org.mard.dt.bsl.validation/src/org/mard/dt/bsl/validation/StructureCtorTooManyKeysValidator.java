package org.mard.dt.bsl.validation;

import static com._1c.g5.v8.dt.bsl.model.BslPackage.Literals.STRING_LITERAL__LINES;

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.util.CancelIndicator;

import com._1c.g5.v8.dt.bsl.model.OperatorStyleCreator;
import com._1c.g5.v8.dt.bsl.model.StringLiteral;
import com._1c.g5.v8.dt.bsl.validation.CustomValidationMessageAcceptor;
import com._1c.g5.v8.dt.bsl.validation.IExternalBslValidator;
import com._1c.g5.v8.dt.platform.IEObjectTypeNames;

public class StructureCtorTooManyKeysValidator implements IExternalBslValidator {

	private static final int MAX_STRUCTURE_KEYS = 3;

	public static final String ERROR_CODE = "StructureConstructorHasTooManyKeys"; //$NON-NLS-1$

	@Override
	public boolean needValidation(EObject object) {

		return object instanceof OperatorStyleCreator;
	}

	@Override
	public void validate(EObject object, CustomValidationMessageAcceptor messageAcceptor, CancelIndicator monitor) {

		if (monitor.isCanceled())
			return;

		OperatorStyleCreator osc = (OperatorStyleCreator) object;
		if (IEObjectTypeNames.STRUCTURE.equals(osc.getType().getName()) && !osc.getParams().isEmpty()
				&& osc.getParams().get(0) instanceof StringLiteral) {
			StringLiteral literal = (StringLiteral) osc.getParams().get(0);

			String content = String.join("", literal.lines(true)); //$NON-NLS-1$
			String[] keys = content.replace(" ", "").split(","); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			if (!monitor.isCanceled() && keys.length > MAX_STRUCTURE_KEYS) {
				String message = MessageFormat.format(
						Messages.StructureCtorTooManyKeysValidator_Structure_constructor_has_more_than__N__keys,
						MAX_STRUCTURE_KEYS);
				messageAcceptor.warning(message, literal, STRING_LITERAL__LINES, ERROR_CODE);

			}
		}

	}

}
