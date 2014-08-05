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
public class RequiresUserContainerRequestFilter extends ShiroContainerRequestFilter {
	@Override
	protected boolean checkAccess(Subject subject) {
		// If checkAuthorization succeeds, the user is allowed.
		return true;
	}
}
