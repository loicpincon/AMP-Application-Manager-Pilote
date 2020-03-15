//package organisation.application.manager.pilote.google.publisher;
//
//import org.springframework.stereotype.Service;
//
//import organisation.application.manager.pilote.commun.service.DefaultService;
//
//@Service
//public class PlayStorePublisher extends DefaultService {
//
//	public String getPathOf(String of) {
//		return getClass().getResource(of).getFile();
//	}
//
//	public void go() {
//		try {
//			tet.builder().jsonKeyPath("src/main/resources/playstore/key.json").proxyHost("localhost").proxyPort("8888")
//					.apkPath("src/main/resources/playstore/app.aab").trackName("alpha").appName("tmp").notes("new test")
//					.build().upload();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(2);
//		}
//	}
//
//	/**
//	 * Entry point
//	 *
//	 * @param args process arguments
//	 */
//	public static void main(String... args) {
//		new PlayStorePublisher().go();
//	}
//
//}
