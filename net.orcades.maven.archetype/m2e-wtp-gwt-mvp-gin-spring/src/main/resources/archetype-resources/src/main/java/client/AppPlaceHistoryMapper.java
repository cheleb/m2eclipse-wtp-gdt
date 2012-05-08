package ${package}.client;

import ${package}.client.details.DetailsPlace;
import ${package}.client.home.HomePlace;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * Specify all of the tokenizer classes for each place here,
 * so that the PlaceHistoryMapper can construct the proper
 * place objects from url history tokens.
 */
@WithTokenizers( {
	HomePlace.Tokenizer.class, 
	DetailsPlace.Tokenizer.class
})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
