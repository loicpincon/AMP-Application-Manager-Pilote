package organisation.application.manager.pilote.datasource.modele;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MySqlDataSourceItem extends DataSourceItem {
	/**
	* 
	*/
	private static final long serialVersionUID = -6599238930270137115L;

	private List<String> tables;

}
