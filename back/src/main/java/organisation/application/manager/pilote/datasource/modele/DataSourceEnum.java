package organisation.application.manager.pilote.datasource.modele;

import lombok.Getter;

@Getter
public enum DataSourceEnum {

	MYSQL("latest");

	private String version;

	DataSourceEnum(String version) {
		this.version = version;
	}

}
