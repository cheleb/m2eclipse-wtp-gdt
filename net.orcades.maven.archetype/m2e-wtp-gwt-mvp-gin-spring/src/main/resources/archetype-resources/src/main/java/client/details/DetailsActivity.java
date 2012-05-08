package ${package}.client.details;

import ${package}.client.home.HomePlace;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class DetailsActivity extends AbstractActivity implements DetailsView.Presenter {

	private DetailsView view;
	private PlaceController placeController;
	
	@Inject
	public DetailsActivity(DetailsView view, PlaceController placeController){
		this.view = view;
		this.placeController = placeController;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view.setPresenter(this);
		panel.setWidget(view.asWidget()); 
		
		GWT.log("currentPlace = " + getPlace() + " id=" + getPlace().getId() );
	}

	@Override
	public void goToHome() {
		placeController.goTo(new HomePlace());
	}
	
	public DetailsPlace getPlace(){
		return (DetailsPlace) placeController.getWhere();
	}
	
}
