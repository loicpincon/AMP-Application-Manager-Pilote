package organisation.application.manager.pilote.commun.helper;

import java.net.Socket;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomPortHelper {

	static Random r = new Random();

	private Integer random() {
		int low = 7002;
		int high = 7999;
		return r.nextInt(high - low) + low;
	}

	public Integer randomPort() {
		return randomPort("localhost");
	}

	public Integer randomPort(String ip) {
		Integer r = random();

		while (canConnect(ip, r)) {
			r = random();
		}
		return r;
	}

	public boolean canConnect(String host, int port) {
		Socket s = null;
		try {
			s = new Socket(host, port);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (Exception e) {
				}
		}
	}

}
