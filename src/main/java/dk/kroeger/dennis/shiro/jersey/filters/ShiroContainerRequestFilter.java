package dk.kroeger.dennis.shiro.jersey.filters;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

/**
 * Common base class for all request filters.
 *
 * @author Dennis Du Krøger
 * @since 02-08-2014
 */
abstract class ShiroContainerRequestFilter implements ContainerRequestFilter {

	/**
	 * @return True if current filter is satisfied.
	 */
	protected abstract boolean checkAccess(Subject subject);

	/**
	 * In most cases, a filter requires the user to be remembered or authenticated,
	 * but some filters may skip that.
	 *
	 * @return True if filter consider user allowed
	 */
	boolean checkAuthentication(Subject subject) {
		return subject.isRemembered() || subject.isAuthenticated();
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext) {
		Subject subject = SecurityUtils.getSubject();
		if (!checkAuthentication(subject)) {
			containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(
					"Unauthorized").build());
		} else if (!checkAccess(subject)) {
			containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(
					"Forbidden").build());
		}
	}

}
