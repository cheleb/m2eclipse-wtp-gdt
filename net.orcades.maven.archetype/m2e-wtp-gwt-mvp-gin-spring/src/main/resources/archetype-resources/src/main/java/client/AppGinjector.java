package ${package}.client;

import ${package}.client.widget.MainView;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceHistoryHandler;

@GinModules({AppGinModule.class})
public interface AppGinjector extends Ginjector {

	PlaceHistoryHandler getPlaceHistoryHandler();
	MainView getMainView();
}
