Shiro-Jersey-annotations
=========================

A JAX-WS 2 implementation of Shiro annotation processing. In theory this should work with any JAX-WS 2.0 implementation, so I guess the name is a bit wrong.

To use this library, you'll need to add ShiroDynamicFeature to your JAX-WS application, either programatically, or using web.xml. There is no need to use AOP/AspectJ to use these annotations, all annotations are parsed by the Shiro-Jersey filters.

This works a bit different from https://github.com/PE-INTERNATIONAL/shiro-jersey/ which this project was inspired by: If there is the same annotation on a method as on the class, the method annotation will override the class annotation.

All annotations except RequiresAuthentication implies RequireUser: If no user is remembered or authenticated, a 401 response will be sent.

To make it possible to override all annotations, new annotations (RequiresNo*) has been added. These simply cancels the class annotation for the method.
