package ${package}.client.details;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class DetailsPlace extends Place {

	private Long id;
	
	public DetailsPlace(Long id){
		this.id = id;
	}

	public static class Tokenizer implements PlaceTokenizer<DetailsPlace>
	{

		@Override
		public String getToken(DetailsPlace place)
		{
			return place.getId().toString();
		}

		@Override
		public DetailsPlace getPlace(String token)
		{
			Long id = null;
			try{
				id = new Long(token);
			} catch (Exception e){
				// ignore
			}
			
			return new DetailsPlace(id);
		}

	}
	
	public Long getId(){
		return id;
	}
}
