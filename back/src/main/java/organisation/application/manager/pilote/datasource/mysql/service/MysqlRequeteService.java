package organisation.application.manager.pilote.datasource.mysql.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import organisation.application.manager.pilote.datasource.mysql.exception.NoDataFoundException;
import organisation.application.manager.pilote.datasource.mysql.exception.NullParameterException;
import organisation.application.manager.pilote.datasource.mysql.helper.CheckNotNullHelper;
import organisation.application.manager.pilote.datasource.mysql.helper.MysqlHelper;
import organisation.application.manager.pilote.datasource.mysql.modele.ServeurParam;

@Service
public class MysqlRequeteService {

	protected static final Log logger = LogFactory.getLog(MysqlRequeteService.class);

	private Connection connectionSecondaire;

	public MysqlRequeteService() {
	}

	public MysqlRequeteService(Connection conn) {
		connectionSecondaire = conn;
	}

	/**
	 * 
	 */
	public ResultSet select(String requete, ServeurParam conf) {
		return readSQL(requete, conf);
	}

	/**
	 * @throws NullParameterException
	 * 
	 */
	public ResultSet select(String requete, ServeurParam conf, Object... param) throws NullParameterException {
		return readSQL(requete, conf, param);
	}

	/**
	 * 
	 */
	public int update(String requete, ServeurParam conf) {
		return writeSQL(requete, conf);
	}

	/**
	 * @throws NullParameterException
	 * 
	 */
	public int update(String requete, ServeurParam conf, Object... param) throws NullParameterException {
		return writeSQL(requete, conf, param);
	}

	/**
	 * 
	 */
	public int insert(String requete, ServeurParam conf) {
		return writeSQL(requete, conf);
	}

	/**
	 * @throws NullParameterException
	 * 
	 */
	public int insert(String requete, ServeurParam conf, Object... param) throws NullParameterException {
		return writeSQL(requete, conf, param);
	}

	public int delete(String requete, ServeurParam conf) {
		return writeSQL(requete, conf);
	}

	public int delete(String requete, ServeurParam conf, Object... param) throws NullParameterException {
		return writeSQL(requete, conf, param);
	}

	/**
	 * 
	 * @param requete
	 * @return
	 * @throws NoDataFoundException
	 * @throws SQLException
	 */
	private int writeSQL(String requete, ServeurParam conf) {
		logger.debug(requete);
		try {
			return getConn(conf).createStatement().executeUpdate(requete);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * 
	 * @param requete
	 * @return
	 * @throws NoDataFoundException
	 * @throws NullParameterException
	 * @throws SQLException
	 */
	private int writeSQL(String requete, ServeurParam conf, Object... param) throws NullParameterException {
		logger.debug(requete);
		try {
			CheckNotNullHelper.verifyParamNull(param);
			return prepare(requete, conf, param).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * 
	 * @param requete
	 * @return
	 * @throws NoDataFoundException
	 * @throws SQLException
	 */
	private ResultSet readSQL(String requete, ServeurParam conf) {
		logger.debug(requete);
		try {
			return getConn(conf).createStatement().executeQuery(requete);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * 
	 * @param requete
	 * @return
	 * @throws NoDataFoundException
	 * @throws NullParameterException
	 * @throws SQLException
	 */
	private ResultSet readSQL(String requete, ServeurParam conf, Object... param) throws NullParameterException {
		logger.debug(requete);
		try {
			CheckNotNullHelper.verifyParamNull(param);
			return prepare(requete, conf, param).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * Construit la requete
	 * 
	 * @param requete
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement prepare(String requete, ServeurParam conf, Object... param) throws SQLException {
		PreparedStatement preparedStatement = getConn(conf).prepareStatement(requete);
		for (int i = 0; i < param.length; i++) {
			preparedStatement.setObject(i + 1, param[i]);
		}
		return preparedStatement;
	}

	private Connection getConn(ServeurParam conf) throws SQLException {
		if (connectionSecondaire == null) {
			return MysqlHelper.getInstance(conf.getUrl(), conf.getLogin(), conf.getPassword());
		}
		return connectionSecondaire;
	}

}
