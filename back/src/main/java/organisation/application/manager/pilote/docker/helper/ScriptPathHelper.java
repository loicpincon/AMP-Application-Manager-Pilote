package organisation.application.manager.pilote.docker.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import organisation.application.manager.pilote.commun.exception.ApplicationException;

@Component
public class ScriptPathHelper {

	protected static final Log LOG = LogFactory.getLog(ScriptPathHelper.class);

	public String getPathOfFile(String path, String filename) {
		String pathRetour = "";
		if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX) {
			if (path == null || path.equals("")) {
				pathRetour = "/script/unix/" + filename + ".sh";
			} else {
				pathRetour = "/script/unix/" + path + "/" + filename + ".sh";
			}
		} else if (SystemUtils.IS_OS_WINDOWS) {
			if (path == null || path.equals("")) {
				pathRetour = "/script/windows/" + filename + ".bat";
			} else {
				pathRetour = "/script/windows/" + path + "/" + filename + ".bat";
			}
		} else {
			throw new ApplicationException(500, "Impossible de detecter l'os cible");
		}
		String realPath = null;
		try {
			realPath = URLDecoder.decode(getClass().getResource(pathRetour).getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.debug(realPath);
		return realPath;
	}

	public String getRelativePath(String def) {
		String path = getClass().getResource(def).getFile();
		LOG.debug("base path : " + path);
		return path;
	}

}
