package organisation.application.manager.pilote.commun.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;

@Service
public class HashService {

	/**
	 * Permet de Hasher une chaine en SHA 256
	 * 
	 * @param chaine
	 * @return
	 */
	public String hash(String chaine) {
		return Hashing.sha256().hashString(chaine, UTF_8).toString();
	}

	/**
	 * 
	 * @return
	 */
	public Integer randomInt() {
		UUID idOne = UUID.randomUUID();
		String str = "" + idOne;
		int uid = str.hashCode();
		String filterStr = "" + uid;
		str = filterStr.replaceAll("-", "");
		return Integer.parseInt(str);
	}
}
