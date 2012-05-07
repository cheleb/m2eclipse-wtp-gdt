package ${package}.client;

import ${package}.shared.FieldVerifier;
import ${package}.client.gin.${classPrefix}Ginjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ${gwtModule} implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final ${classPrefix}ServiceAsync ${classPrefix.toLowerCase()}Service = GWT
			.create(${classPrefix}Service.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		${classPrefix}Ginjector ginjector = GWT.create(${classPrefix}Ginjector.class);
		RootPanel.get().add(ginjector.getAppWidget());
	}
}
