package ${package}.client.details;

import com.google.gwt.user.client.ui.IsWidget;

public interface DetailsView extends IsWidget {

	void setPresenter(Presenter presenter);
	
	public interface Presenter {
		void goToHome();
	}
	
}
