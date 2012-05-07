package ${package}.client.gin;


import ${package}.client.service.SimpleService;
import ${package}.client.service.SimpleServiceImpl;

import com.google.gwt.inject.client.AbstractGinModule;

public class ${classPrefix}GinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(SimpleService.class).to(SimpleServiceImpl.class);
		
	}

}