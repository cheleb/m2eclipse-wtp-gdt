package ${package}.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ${gwtModule}EntryPoint implements EntryPoint {
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// create ginjector - replace with factory so we can replace 
		// with different impls in gwt.xml
		AppGinjector ginjector = GWT.create(AppGinjector.class);
		RootPanel.get().add(ginjector.getMainView());

		// goes to the place represented on url, or the default place
		ginjector.getPlaceHistoryHandler().handleCurrentHistory();
		
	}
}
