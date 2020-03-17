package organisation.application.manager.pilote.datasource.mysql.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MysqlHelper {

	protected static final Log logger = LogFactory.getLog(MysqlHelper.class);

	public static Connection getInstance(String url, String login, String password) throws SQLException {
		return DriverManager.getConnection(url, login, password);
	}

}
