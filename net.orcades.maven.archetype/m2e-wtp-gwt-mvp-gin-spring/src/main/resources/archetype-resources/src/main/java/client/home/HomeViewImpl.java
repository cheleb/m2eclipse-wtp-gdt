package ${package}.client.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class HomeViewImpl extends Composite implements HomeView {

	private static HomeViewImplUiBinder uiBinder = GWT
			.create(HomeViewImplUiBinder.class);

	interface HomeViewImplUiBinder extends UiBinder<Widget, HomeViewImpl> {
	}
	
	private Presenter presenter;
	
	@UiField
	Button detailsButton;
	
	public HomeViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	@UiHandler("detailsButton")
	void handleDetailsButtonClick(ClickEvent e){
		presenter.goToDetails();
	}
	
	
	public void setPresenter(Presenter presenter){
		this.presenter = presenter;
	}
}
