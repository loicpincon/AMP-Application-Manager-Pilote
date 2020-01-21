package application.manager.pilote.commun.service;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Service;

import application.manager.pilote.commun.modele.ApplicationInfoRessource;

@Service
public class ApplicationInfoService {

	protected static final Log LOG = LogFactory.getLog(ApplicationInfoService.class);

	public ApplicationInfoRessource get() {
		ApplicationInfoRessource appinfo = new ApplicationInfoRessource();
		appinfo.setVersion(getPomVersion());
		return appinfo;
	}

	/**
	 * recupere la version de l'application
	 * 
	 * @return
	 */
	private String getPomVersion() {
		String version = this.getClass().getPackage().getImplementationVersion();
		if (version == null) {
			try {
				MavenXpp3Reader reader = new MavenXpp3Reader();
				Model model;
				model = reader.read(new FileReader("pom.xml"));
				version = model.getVersion();
			} catch (IOException | XmlPullParserException e) {
				LOG.error(e);
				version = "indisponible";
			}
		}
		return version;
	}

}
