package application.manager.pilote.commun.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class PropertiesReader {

	private static Properties properties = new Properties();

	public PropertiesReader() {
		this.init();
	}

	public String getProperty(String key) {
		return (String) properties.get(key);
	}

	public String getPropertyOrElse(String key, Object value) {
		Object tmp = properties.get(key);
		if (tmp == null) {
			return (String) value;
		}
		return (String) tmp;
	}

	private PropertiesReader init() {
		try (InputStream input = PropertiesReader.class.getClassLoader().getResourceAsStream("gcp.properties")) {
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return this;
	}
}
