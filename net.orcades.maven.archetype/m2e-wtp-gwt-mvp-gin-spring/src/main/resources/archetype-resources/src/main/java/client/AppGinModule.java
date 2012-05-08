package ${package}.client;



import ${package}.client.details.DetailsView;
import ${package}.client.details.DetailsViewImpl;
import ${package}.client.home.HomePlace;
import ${package}.client.home.HomeView;
import ${package}.client.home.HomeViewImpl;

import com.google.code.ginmvp.client.GinMvpModule;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class AppGinModule extends AbstractGinModule {

	@Override
	protected void configure() {

		install(new GinMvpModule(AppPlaceHistoryMapper.class, HomePlace.class));
		
		// ActivityMapper maps the place to a new activity instance.
		// you should have one activity mapper for each display area.
		bind(ActivityMapper.class).to(AppActivityMapper.class).in(Singleton.class);
		
		bind(HomeView.class).to(HomeViewImpl.class).in(Singleton.class);
		bind(DetailsView.class).to(DetailsViewImpl.class).in(Singleton.class);
		
	}

}
