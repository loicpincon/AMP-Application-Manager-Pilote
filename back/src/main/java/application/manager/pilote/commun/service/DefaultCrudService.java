package application.manager.pilote.commun.service;

import java.util.List;

public interface DefaultCrudService<T, A> {

	public abstract T consulter(A id);

	public abstract List<T> recuperer();

	public abstract T inserer(T param);

	public abstract T modifier(T param);

}
