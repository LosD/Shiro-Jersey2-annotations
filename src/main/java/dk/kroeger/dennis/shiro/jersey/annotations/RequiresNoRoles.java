package dk.kroeger.dennis.shiro.jersey.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this marker annotation to remove all required permissions of a
 * {@link org.apache.shiro.authz.annotation.RequiresPermissions} on a method.
 *
 * @author Dennis Du Krøger
 * @since 02-08-2014
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresNoRoles {
}
