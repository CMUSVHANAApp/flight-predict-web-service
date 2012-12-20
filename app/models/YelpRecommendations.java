package models;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

public @JsonDeserialize(using = YelpBizDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown=true)  
class YelpRecommendations {
	protected String type;
	protected ArrayList<YelpBiz> list;

	public YelpRecommendations(String type) {
		this.type = type;
		this.list = new ArrayList<YelpBiz>();
	}

	public ArrayList<YelpBiz> getRecommendations() {
		return this.list;
	}

	public void addBiz(YelpBiz newBiz) {
		this.list.add(newBiz);
	}
}

class YelpBiz {
	protected double rating;
	protected String name;
	protected GeoLocation geoLocation;

	public YelpBiz(String name, double latitude, double longitude,
			String address, String city, String zipcode) {
		this.name = name;
		this.geoLocation = new GeoLocation(address, city, zipcode, longitude,
				latitude);
	}

	public String getName() {
		return this.name;
	}

	public GeoLocation getGeoLocation() {
		return this.geoLocation;
	}
	public double getRating() {
		return this.rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
}