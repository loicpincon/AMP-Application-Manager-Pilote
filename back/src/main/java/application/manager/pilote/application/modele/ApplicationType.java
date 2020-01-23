package application.manager.pilote.application.modele;

public final class ApplicationType {

	private ApplicationType() {
	}

	public static final String NODEJS = "NODEJS";

	public static final String WAR = "WAR";

	public static final String BASH = "BASH";

	public static final String ANGULAR = "ANGULAR";

	private static final String[] APPLICATIONS_TYPE = { NODEJS, WAR, BASH, ANGULAR };

	/**
	 * 
	 * @return
	 */
	public static String[] getApplicationTypes() {
		return APPLICATIONS_TYPE;
	}

}
