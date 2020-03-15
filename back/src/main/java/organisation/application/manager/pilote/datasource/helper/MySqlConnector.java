package organisation.application.manager.pilote.datasource.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import organisation.application.manager.pilote.commun.exception.ApplicationException;

@AllArgsConstructor
public class MySqlConnector {

	private String ip;

	private String port;

	private String user;

	private String password;

	private String base;

	public void executeRequeteSystem(String requete) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String urm = "jdbc:mysql://" + ip + ":" + port + "/" + base;
			Connection con = DriverManager.getConnection(urm, user, password);
			Statement stmt = con.createStatement();
			stmt.execute(requete);
		} catch (ClassNotFoundException | SQLException e) {

		}

	}

	public List<List<String>> execute(String requete) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String urm = "jdbc:mysql://" + ip + ":" + port + "/" + base;
			Connection con = DriverManager.getConnection(urm, user, password);
			Statement stmt = con.createStatement();
			ResultSet resultset;
			resultset = stmt.executeQuery(requete);
			ResultSetMetaData rsmd = resultset.getMetaData();
			int columnsNum = rsmd.getColumnCount();
			List<List<String>> tmp = new ArrayList<>();
			List<String> header = new ArrayList<>();
			for (int i = 1; i < columnsNum + 1; i++) {
				header.add(rsmd.getColumnLabel(i));
			}
			tmp.add(header);
			while (resultset.next()) {
				List<String> corps = new ArrayList<>();
				for (int i = 1; i < columnsNum + 1; i++) {
					if (resultset.getObject(i) != null) {
						System.out.println(resultset.getObject(i).toString());
						corps.add(resultset.getObject(i).toString());
					} else {
						corps.add("NULL");
					}
				}
				tmp.add(corps);
			}
			resultset.close();
			stmt.close();
			con.close();
			tmp.remove(0);
			return tmp;
		} catch (SQLException | ClassNotFoundException e) {
			throw new ApplicationException(401, e.getMessage());
		}
	}

}
