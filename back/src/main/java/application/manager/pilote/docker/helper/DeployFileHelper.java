package application.manager.pilote.docker.helper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Component;

import application.manager.pilote.commun.helper.StringHelper;
import application.manager.pilote.docker.modele.DockerFile;

@Component
public class DeployFileHelper {

	protected static final Log LOG = LogFactory.getLog(DeployFileHelper.class);

	private static final String DOCKERFILE = "Dockerfile";

	private static final String WEB_INF_CLASSES_GCP_PROPERTIES = "/WEB-INF/classes/gcp.properties";

	private static final String EGAL = "=";

	@Autowired
	private StringHelper stringUtils;

	/**
	 * 
	 * @param pathFoldeTemporaire
	 * @param structure
	 * @return
	 */
	public File createDockerFile(String pathFoldeTemporaire, DockerFile structure) {
		InputStream stream = new ByteArrayInputStream(structure.getFile().getBytes());
		LOG.info(pathFoldeTemporaire);
		LOG.info(structure.getFile());
		return this.writeInputStream(pathFoldeTemporaire, DOCKERFILE, stream);
	}

	/**
	 * 
	 * @param path
	 * @param fileName
	 * @param is
	 * @return
	 */
	private File writeInputStream(String path, String fileName, InputStream is) {
		try {
			createDossierRecursif(path);
			Files.copy(is, Paths.get(stringUtils.concat(path, "/", fileName)));
			return new File(path);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw new ApplicationContextException(e.getMessage());
		}
	}

//	public void createGcpFile(String url, List<ItemParametreApplicatif> params) {
//		try {
//			String url_file = stringUtils.concat(url, WEB_INF_CLASSES_GCP_PROPERTIES);
//			PrintWriter writer;
//
//			writer = new PrintWriter(url_file, "UTF-8");
//
//			LOG.debug(stringUtils.concat("Nombre de param a creer : ", params.size()));
//			for (ItemParametreApplicatif p : params) {
//				writer.println(stringUtils.concat(p.getCle(), EGAL, p.getValeur()));
//				LOG.trace(stringUtils.concat(p.getCle(), EGAL, p.getValeur()));
//			}
//			writer.close();
//		} catch (FileNotFoundException | UnsupportedEncodingException e) {
//			LOG.error(e);
//			throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//		}
//	}

	/**
	 * 
	 * @param path
	 */
	private void createDossierRecursif(String path) {
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
	}
}
