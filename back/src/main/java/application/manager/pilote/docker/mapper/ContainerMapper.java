package application.manager.pilote.docker.mapper;

import org.springframework.stereotype.Service;

import application.manager.pilote.docker.modele.Container;

@Service
public class ContainerMapper {

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public Container mapFrom(com.github.dockerjava.api.model.Container obj) {
		Container container = new Container();
		container.setName(obj.getNames()[0]);
		container.setId(obj.getId());
		container.setEtat(obj.getStatus());
		return container;
	}

}
