package organisation.application.manager.pilote.application.modele;

public final class ApplicationType {

	private ApplicationType() {
	}

	public static final String NODEJS = "NODEJS";

	public static final String WAR = "WAR";

	public static final String BASH = "BASH";

	public static final String ANGULAR = "ANGULAR";

	public static final String JAR = "JAR";

	public static final String IONIC = "IONIC";

	private static final String[] APPLICATIONS_TYPE = { NODEJS, WAR, BASH, ANGULAR, JAR, IONIC };

	/**
	 * 
	 * @return
	 */
	public static String[] getApplicationTypes() {
		return APPLICATIONS_TYPE;
	}

}
