package ${package}.client.home;

import ${package}.client.details.DetailsPlace;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class HomeActivity extends AbstractActivity implements HomeView.Presenter {

	private HomeView view;
	private PlaceController placeController;
	
	@Inject
	public HomeActivity(HomeView view, PlaceController placeController){
		this.view = view;
		this.placeController = placeController;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view.asWidget());
	}
	
	public void goToDetails(){
		placeController.goTo(new DetailsPlace(new Long(Random.nextInt(100))));
	}
	
}
