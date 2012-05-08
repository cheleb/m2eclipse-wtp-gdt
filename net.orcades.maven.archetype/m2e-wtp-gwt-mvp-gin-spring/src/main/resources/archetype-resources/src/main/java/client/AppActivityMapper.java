package ${package}.client;

import com.google.code.ginmvp.client.ActivityAsyncProxy;
import com.google.code.ginmvp.client.SimpleActivityMapper;
import ${package}.client.details.DetailsActivity;
import ${package}.client.details.DetailsPlace;
import ${package}.client.home.HomeActivity;
import ${package}.client.home.HomePlace;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * AppActivityMapper implementation that is Gin friendly.  
 * 
 * We use Gin injection to get an instance of the activity providers
 * that we need for our application, and then pass each to the underlying
 * 'addProvider' method creating a relationship between Places and Activity providers.
 * 
 * To have an activity code-split, inject a Provider<ActivityAsyncProxy<Activity>>, 
 * rather than a Provider<Activity>.  
 * 
 * @author slynn1324
 */
public class AppActivityMapper extends SimpleActivityMapper {

	@Inject
	public AppActivityMapper(
			final Provider<HomeActivity> homeActivityProvider,
			final Provider<ActivityAsyncProxy<DetailsActivity>> detailsActivityProvider) {
		
		addProvider(HomePlace.class, homeActivityProvider);
		addProvider(DetailsPlace.class, detailsActivityProvider);
	}

}
