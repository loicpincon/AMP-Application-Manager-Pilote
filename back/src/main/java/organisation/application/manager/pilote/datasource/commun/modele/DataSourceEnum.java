package organisation.application.manager.pilote.datasource.commun.modele;

import lombok.Getter;

@Getter
public enum DataSourceEnum {

	MYSQL("latest"), MONGO("latest");

	private String version;

	DataSourceEnum(String version) {
		this.version = version;
	}

}
