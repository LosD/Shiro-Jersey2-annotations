package dk.kroeger.dennis.shiro.jersey;

import dk.kroeger.dennis.shiro.jersey.annotations.*;
import dk.kroeger.dennis.shiro.jersey.filters.*;
import org.apache.shiro.authz.annotation.*;

import javax.ws.rs.Path;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;

/**
 * This DynamicFeature must be added to your {@link javax.ws.rs.core.Application},
 * either through web.xml or by making a custom class.
 *
 * @author Dennis Du Krøger
 * @since 02-08-2014
 */
@Provider
public class ShiroDynamicFeature implements DynamicFeature {
	private boolean filterNeeded(Class<? extends Annotation> requireAnnotationClass,
			Class<? extends Annotation> antiRequireAnnotationClass,
			ResourceInfo resourceInfo) {
		return (resourceInfo.getResourceClass().getAnnotation(requireAnnotationClass) != null
				|| resourceInfo.getResourceMethod().getAnnotation(requireAnnotationClass) != null) &&
				resourceInfo.getResourceMethod().getAnnotation(antiRequireAnnotationClass) == null;
	}

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
		// No need to check non-Jax-rs classes
		if (resourceInfo.getResourceClass().getAnnotation(Path.class) != null) {
			if (filterNeeded(RequiresAuthentication.class, RequiresNoAuthentication.class, resourceInfo)) {
				featureContext.register(new RequiresAuthenticationContainerRequestFilter());
			}

			if (filterNeeded(RequiresGuest.class, RequiresNoGuest.class, resourceInfo)) {
				featureContext.register(new RequiresGuestContainerRequestFilter());
			}

			if (filterNeeded(RequiresPermissions.class, RequiresNoPermission.class, resourceInfo)) {
				featureContext.register(new RequiresPermissionContainerRequestFilter(resourceInfo));
			}

			if (filterNeeded(RequiresRoles.class, RequiresNoRoles.class, resourceInfo)) {
				featureContext.register(new RequiresRolesContainerRequestFilter(resourceInfo));
			}

			if (filterNeeded(RequiresUser.class, RequiresNoUser.class, resourceInfo)) {
				featureContext.register(new RequiresUserContainerRequestFilter());
			}
		}
	}
}
