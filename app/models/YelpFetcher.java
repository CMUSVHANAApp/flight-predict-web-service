package models;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class YelpFetcher {
	OAuthService service;
	Token accessToken;
	static String consumerKey = "s2faseG3-l7IW1DVixnOzw";
	static String consumerSecret = "OqGbhwzXGR-kOkS2AQEszdSWW_8";
	static String token = "gzV6x7e3rlelUrpYjunWCq6Qe3ekJsyX";
	static String tokenSecret = "C0f-55BXbqy3FJdu1g7AjxEKjRc";

	public YelpFetcher(String consumerKey, String consumerSecret, String token,
			String tokenSecret) {
		this.service = new ServiceBuilder().provider(YelpApi2.class)
				.apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}

	/**
	 * Search with term and location.
	 * 
	 * @param term
	 *            Search term
	 * @param latitude
	 *            Latitude
	 * @param longitude
	 *            Longitude
	 * @return JSON string response
	 */
	public static YelpRecommendations fetch(String term, GeoLocation city) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("ll", city.latitude + "," + city.longitude);
		return fetch(request);
	}
	public static YelpRecommendations fetch(String term, String city) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("location", city);
		return fetch(request);
	}
	protected static YelpRecommendations fetch(OAuthRequest request) {
		OAuthService service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
		Token accessToken = new Token(token, tokenSecret);
		service.signRequest(accessToken, request);
		Response response = request.send();
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(response.getBody());
		YelpRecommendations recommendations = null;
		try {
			recommendations = objectMapper.readValue(response.getBody(),
					YelpRecommendations.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return recommendations;
	}

	// CLI
	public static void main(String[] args) {
		// Update tokens here from Yelp developers site, Manage API access.
		String consumerKey = "s2faseG3-l7IW1DVixnOzw";
		String consumerSecret = "OqGbhwzXGR-kOkS2AQEszdSWW_8";
		String token = "gzV6x7e3rlelUrpYjunWCq6Qe3ekJsyX";
		String tokenSecret = "C0f-55BXbqy3FJdu1g7AjxEKjRc";
		System.out.println(YelpFetcher.fetch("restaurant", "94086").getRecommendations());
		
	}

}

class YelpBizDeserializer extends JsonDeserializer<YelpRecommendations> {

	@Override
	public YelpRecommendations deserialize(JsonParser jsonParser,
			DeserializationContext deserializationContext) throws IOException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser).get("businesses");
		YelpRecommendations yelpRecommendations = new YelpRecommendations("");
		if(node == null) return yelpRecommendations;
		for (int i = 0; i < node.size(); i++) {
			JsonNode business = node.get(i);
			String name = business.get("name").asText();
			double rating  = 0;
			try{
				rating = business.get("rating").asDouble();
			}
			catch(Exception e){
				
			}
			String address ="";
			try{
				address= business.get("location").get("address").get(0).asText();
			}
			catch(Exception e){
				
			}
			String city ="";
			try{
				city = business.get("location").get("city").asText();
			}
			catch (Exception e){
				
			}
			String zipcode ="";
			try{
				zipcode = business.get("location").get("postal_code").asText();
			}
			catch(Exception e){
				
			}
			double distance =0;
			try{
				distance = business.get("distance").asDouble() * 0.000621371;
			}
			catch (Exception e){
				
			}
			String category = "";
			try{
				category= business.get("categories").get(0).get(0).asText();
			}
			catch (Exception e){
				
			}
			double longitude = business.get("location").get("coordinate")
					.get("longitude").asDouble();
			double latitude = business.get("location").get("coordinate")
					.get("latitude").asDouble();
			YelpBiz b = new YelpBiz(name, category, latitude, longitude, address, city,
					zipcode);
			b.setRating(rating);
			b.setDistance(distance);
			yelpRecommendations.addBiz(b);
		}
		return yelpRecommendations;
	}
}
