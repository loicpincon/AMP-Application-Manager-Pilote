package application.manager.pilote.commun.service;

import static java.nio.charset.StandardCharsets.UTF_8;

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

}
