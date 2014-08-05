package dk.kroeger.dennis.shiro.jersey.filters;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.Subject;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ResourceInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Dennis Du Krøger
 * @since 02-08-2014
 */
@Priority(Priorities.AUTHORIZATION)
public class RequiresPermissionContainerRequestFilter extends ShiroContainerRequestFilter {
	private final Collection<Permission> requiredPermissions = new ArrayList<>();
	private final Logical logical;

	public RequiresPermissionContainerRequestFilter(ResourceInfo resourceInfo) {
		RequiresPermissions permissions = resourceInfo.getResourceClass().getAnnotation(
				RequiresPermissions.class);
		RequiresPermissions methodPermissions = resourceInfo.getResourceMethod().getAnnotation(
				RequiresPermissions.class);

		if (methodPermissions != null) {
			permissions = methodPermissions;
		}

		Objects.requireNonNull(permissions);
		logical = permissions.logical();
		for (String permission : permissions.value()) {
			requiredPermissions.add(new WildcardPermission(permission));
		}
	}

	@Override
	protected boolean checkAccess(Subject subject) {
		if (logical == Logical.AND) {
			return subject.isPermittedAll(requiredPermissions);
		} else {
			for (Permission permission : requiredPermissions) {
				if (subject.isPermitted(permission)) {
					return true;
				}
			}
			return false;
		}
	}
}
