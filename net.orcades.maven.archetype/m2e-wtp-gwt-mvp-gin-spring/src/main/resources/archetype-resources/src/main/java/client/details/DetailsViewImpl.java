package ${package}.client.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DetailsViewImpl extends Composite implements DetailsView {

	private static DetailsViewImplUiBinder uiBinder = GWT
			.create(DetailsViewImplUiBinder.class);

	interface DetailsViewImplUiBinder extends UiBinder<Widget, DetailsViewImpl> {
	}

	public DetailsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	Presenter presenter;

	@UiField
	Button homeButton;
	
	@UiHandler("homeButton")
	void handleHomeButtonClick(ClickEvent e){
		presenter.goToHome();
	}
	
	public void setPresenter(Presenter presenter){
		this.presenter = presenter;
	}
	
}
