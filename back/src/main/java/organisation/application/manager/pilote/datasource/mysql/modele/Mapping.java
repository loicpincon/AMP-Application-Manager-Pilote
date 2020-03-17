package organisation.application.manager.pilote.datasource.mysql.modele;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

	/**
	 * Nom du champ en base
	 * 
	 * @return
	 */
	String value() default "";

	String separator() default ",";

	boolean collection() default false;

	boolean ignore() default false;

}
