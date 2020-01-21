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

	public String getPomVersion() {
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model;
			model = reader.read(new FileReader("pom.xml"));
			return model.getVersion();
		} catch (IOException | XmlPullParserException e) {
			LOG.error(e);
			return "indisponible";
		}
	}

}
