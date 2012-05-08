package ${package}.client.widget;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * This is a very simple example. In case you have multiple display area,
 * you would inject the ActivityMapper for each display area here in order
 * to register it and the display area widget. In this example, the MainView
 * IS the display area. In a more complex example, this widget would be the
 * container for the other display areas and therefore would know about
 * them.
 * 
 * @author slynn1324
 */
public class MainView extends SimplePanel {
	
	@Inject
	public MainView(ActivityMapper mapper, EventBus eventBus) {
		ActivityManager activityManager = new ActivityManager(mapper, eventBus);
		activityManager.setDisplay(this);
	}

}
