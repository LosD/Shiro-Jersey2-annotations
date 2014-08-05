package dk.kroeger.dennis.shiro.jersey.filters;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Dennis Du Krøger
 * @since 02-08-2014
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class RequiresRolesContainerRequestFilter extends ShiroContainerRequestFilter {
	private final Collection<String> requiredRoles = new ArrayList<>();
	private final Logical logical;

	public RequiresRolesContainerRequestFilter(ResourceInfo resourceInfo) {
		RequiresRoles roles = resourceInfo.getResourceClass().getAnnotation(RequiresRoles.class);
		RequiresRoles methodRoles = resourceInfo.getResourceMethod().getAnnotation(RequiresRoles.class);

		if (methodRoles != null) {
			roles = methodRoles;
		}

		Objects.requireNonNull(roles);
		logical = roles.logical();
		Collections.addAll(requiredRoles, roles.value());
	}

	@Override
	protected boolean checkAccess(Subject subject) {
		if (logical == Logical.AND) {
			return subject.hasAllRoles(requiredRoles);
		} else {
			for (String role : requiredRoles) {
				if (subject.hasRole(role)) {
					return true;
				}
			}
			return false;
		}
	}
}
