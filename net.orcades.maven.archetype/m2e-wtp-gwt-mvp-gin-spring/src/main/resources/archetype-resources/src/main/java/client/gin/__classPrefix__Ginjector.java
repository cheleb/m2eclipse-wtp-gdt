package ${package}.client.gin;

import ${package}.client.ui.SimpleWidget;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(${classPrefix}GinModule.class)
public interface ${classPrefix}Ginjector extends Ginjector {

	SimpleWidget getAppWidget();
	
}