package org.mard.dt.bsl.validation.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.mard.dt.bsl.validation.ui.messages"; //$NON-NLS-1$
	public static String StructureCtorTooManyKeysQuickfixProvider_description;
	public static String StructureCtorTooManyKeysQuickfixProvider_title;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
