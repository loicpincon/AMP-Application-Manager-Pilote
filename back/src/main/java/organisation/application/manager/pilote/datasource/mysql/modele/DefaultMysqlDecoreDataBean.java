package organisation.application.manager.pilote.datasource.mysql.modele;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import organisation.application.manager.pilote.datasource.mysql.exception.NoDataFoundException;
import organisation.application.manager.pilote.docker.service.DockerContainerService;

public class DefaultMysqlDecoreDataBean<T> {

	protected static final Log logger = LogFactory.getLog(DockerContainerService.class);

	protected ResultSet result;

	protected Class<T> obj;

	/**
	 * 
	 * @param e
	 * @throws SQLException
	 * @throws NoDataFoundException
	 */
	public DefaultMysqlDecoreDataBean(ResultSet e, Class<T> classs) throws NoDataFoundException {
		result = e;
		obj = classs;
		ifNoData();
	}

	/**
	 * Permet de realiser le mapping entre le resultSet et l'objet JAVA
	 * 
	 * @param r
	 * @return
	 * @throws NoDataFoundException
	 * @throws SQLException
	 */
	protected T mapping(ResultSet r) {
		try {
			T person = obj.newInstance();
			Field[] fields = person.getClass().getDeclaredFields();
			for (Field name : fields) {
				name.setAccessible(true);
				if (!name.getName().equals("serialVersionUID")) {
					if (name.isAnnotationPresent(Mapping.class)) {
						Mapping mapp = name.getDeclaredAnnotation(Mapping.class);
						if (!mapp.ignore()) {
							if (mapp.collection()) {
								name.set(person, mappCollection(r.getObject(mapp.value()), mapp.separator()));
							} else {
								name.set(person, r.getObject(mapp.value()));
							}
						}
					} else {
						name.set(person, r.getObject(name.getName()));
					}
				}
			}
			return person;
		} catch (IllegalArgumentException | IllegalAccessException | SQLException | InstantiationException e) {
			throw new NoDataFoundException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

	}

	private List<String> mappCollection(Object tTab, String sepataor) {
		return Arrays.asList(((String) tTab).split(sepataor));
	}

	/**
	 * Permet de renvoyer une List d'objet mappe de la base de données
	 * 
	 * @return
	 */
	public List<T> getAll() throws NoDataFoundException {
		try {
			ArrayList<T> temp = new ArrayList<>();
			while (result.next()) {
				temp.add(mapping(result));
			}
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * Permet de renvoyer un objet mappe de la base de données
	 * 
	 * @return
	 * @throws SQLException
	 */
	public T get() throws NoDataFoundException {
		try {
			result.next();
			return mapping(result);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NoDataFoundException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/**
	 * 
	 * @throws NoDataFoundException
	 */
	private void ifNoData() throws NoDataFoundException {
		try {
			result.last();
			int nombreLignes = result.getRow();
			result.beforeFirst();
			if (nombreLignes == 0) {
				throw new NoDataFoundException(HttpStatus.NOT_FOUND, "La recherche n'a retourné aucun resultat.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	private Object getObject(String key) {
		try {
			if (result.getObject(key) != null || !result.getObject(key).equals("")) {
				return result.getObject(key);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	protected String getString(String key) {
		return (String) getObject(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	protected Integer getInteger(String key) {
		return (Integer) getObject(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	protected Boolean getBoolean(String key) {
		return (Boolean) getObject(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	protected Double getDouble(String key) {
		return (Double) getObject(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	protected Date getDate(String key) {
		return (Date) getObject(key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	protected Character getChar(String key) {
		return (Character) getObject(key);
	}
}
