package dk.kroeger.dennis.shiro.jersey.filters;

import org.apache.shiro.subject.Subject;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

/**
 * @author Dennis Du Krøger
 * @since 02-08-2014
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class RequiresAuthenticationContainerRequestFilter extends ShiroContainerRequestFilter {
	@Override
	protected boolean checkAccess(Subject subject) {
		return true;
	}

	@Override
	protected boolean checkAuthentication(Subject subject) {
		return subject.isAuthenticated();
	}
}
