package application.manager.pilote.docker.mapper;

import org.springframework.stereotype.Service;

import application.manager.pilote.docker.modele.Container;

@Service
public class ContainerMapper {

	public Container mapFrom(com.github.dockerjava.api.model.Container obj) {
		Container container = new Container();
		container.setName(obj.getId());
		return container;
	}

}
