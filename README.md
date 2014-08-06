Shiro-Jersey-annotations
=========================

A JAX-WS 2 implementation of Shiro annotation processing. In theory this should work with any JAX-WS 2.0 implementation, so I guess the name is a bit wrong.

To use this library, you'll need to add ShiroDynamicFeature to your JAX-WS application, either programatically, or using web.xml. There is no need to use AOP/AspectJ to use these annotations, all annotations are parsed by the Shiro-Jersey filters.

This works a bit different from https://github.com/PE-INTERNATIONAL/shiro-jersey/ (thanks to @remast for the inspiration) which this project was inspired by: If there is the same annotation on a method as on the class, the method annotation will override the class annotation.

All annotations except RequiresAuthentication implies RequireUser: If no user is remembered or authenticated, a 401 response will be sent.

To make it possible to override all annotations, new annotations (RequiresNo*) has been added. These simply cancels the class annotation for the method.

RequiresRoles example:

	package dk.kroeger.dennis.shiro.jersey.example;
	
	import org.apache.shiro.authz.annotation.RequiresRoles; 
	import dk.kroeger.dennis.shiro.jersey.RequiresNoRoles; 
	import org.example.Example;
	import javax.ws.rs.*;
	import javax.ejb.Singleton;
	import java.util.Collection;
	import java.util.Map;
	import java.lang.String;
	
	@Path("/example")
	@RequiresRoles("role,example")
	@Singleton
	public class ExampleResource {
		Map<String, Example> examples = new ConcurrentHashMap<>();
	
		// Require only the "role" role.
		@RequiresRoles("role");
		@GET
		@Produces("application/json")
		public Collection<Example> listExamples(){
			return examples.values();
		}
		
		// Do not require any specific roles.
		@RequiresNoRoles
		@GET
		@Path("{id}")
		public Example example(@PathParam String id){
			return examples.get(id);
		}

		// Same requirements as class.
		@PUT
		@Path("{id}")
		public void add(@PathParam String id, Example example){
			examples.put(id, example);
		}
		
		// Requires another role
		@RequiresRoles("delete");
		@DELETE
		@Path("{id}")
		public void delete(@PathParam String id){
			examples.remove(id);
		}
	}
	
Adding to Application:

	package dk.kroeger.dennis.shiro.jersey.example;
	
	import dk.kroeger.dennis.shiro.jersey.ShiroDynamicFeature;
	
	import javax.ws.rs.ApplicationPath;
	import javax.ws.rs.core.Application;
	import java.util.HashSet;
	import java.util.Set;
	
	@ApplicationPath("/rest")
	public class ExampleApplication extends Application {
		@Override
		public Set<Class<?>> getClasses() {
			final Set<Class<?>> classes = new HashSet<>();
			classes.add(ShiroDynamicFeature.class);
			return classes;
		}
	
		@Override
		public Set<Object> getSingletons() {
			final Set<Object> singletons = new HashSet<>();
			singletons.add(new ExampleResource());
			return singletons;
		}
	}
