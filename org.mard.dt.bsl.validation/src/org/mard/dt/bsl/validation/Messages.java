package org.mard.dt.bsl.validation;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.mard.dt.bsl.validation.messages"; //$NON-NLS-1$
	public static String StructureCtorTooManyKeysValidator_Structure_constructor_has_more_than__N__keys;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
