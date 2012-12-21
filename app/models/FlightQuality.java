package models;


import java.util.Date;
import java.util.HashMap;

public class FlightQuality{
	protected String flightNumber;
	protected int departDelay;
	protected int arrivalDelay;
	protected City departAirport;
	protected City arrivalAirport;
	protected Weather departWeather;
	protected Weather arrivalWeather;
	protected String airline;
	protected Date date;
	protected HashMap<String, YelpRecommendations> recommendations;
	public FlightQuality(String airline, String flightNumber, Date d, String departure, String arrival){
		this(airline, flightNumber, d, departure, arrival, true);
	}
	public FlightQuality(String airline, String flightNumber, Date d, String departure, String arrival, boolean fetchData){
		this.flightNumber = flightNumber;
		this.date = d;
		this.departAirport = new City(departure, fetchData);
		this.airline = airline;
		this.arrivalAirport = new City(arrival, fetchData);
		this.recommendations = new HashMap<String, YelpRecommendations>();
		if(fetchData){
			this.departWeather = WeatherFetcher.Fetch(this.departAirport.getGeoLocation().city, d);
			this.arrivalWeather = WeatherFetcher.Fetch(this.arrivalAirport.getGeoLocation().city, d);
		}
		
	}
	public String getAirline(){
		return this.airline;
	}
	public String getDate(){
		return this.date.toGMTString();
	}
	public Weather getDepartWeather(){
		return this.departWeather;
	}
	public Weather getArrivalWeather(){
		return this.arrivalWeather;
	}
	public City getDepartAirport(){
		return this.departAirport;
	}
	public City getArrivalAirport(){
		return this.arrivalAirport;
	}
	public HashMap<String, YelpRecommendations> getRecommendations(){
		return this.recommendations;
	}
	
	public int  getDepartDelay(){
		return this.departDelay;
	}
	public int getArrivalDelay(){
		return this.arrivalDelay;
	}
	public String getFlightNumber(){
		return this.flightNumber;
	}
	public void setDelay(int depart, int arrival){
		this.departDelay = depart;
		this.arrivalDelay = arrival;
		
	}
	public void fetchRecommendations(){

		if(this.departDelay <= 120 && this.departDelay > 0){
			this.recommendations.put("Dining", YelpFetcher.fetch("fast+food", this.departAirport.geoLocation));
		}
		
		else if(this.departDelay >= 120){
			this.recommendations.put("Dining", YelpFetcher.fetch("restaurant", this.departAirport.geoLocation));
		}
		
		else{
			this.recommendations.put("Dining", YelpFetcher.fetch("restaurant", this.arrivalAirport.geoLocation));
		}
		
		if(this.departDelay > 0){
			this.recommendations.put("Accomendation", YelpFetcher.fetch("hotel", this.departAirport.geoLocation));
			this.recommendations.put("Transportation", YelpFetcher.fetch("transportation", this.departAirport.geoLocation));
		}
		
		else{
			this.recommendations.put("Accomendation", YelpFetcher.fetch("hotel", this.arrivalAirport.geoLocation));
			this.recommendations.put("Transportation", YelpFetcher.fetch("transportation", this.departAirport.geoLocation));
		}
		
	}
	public String toString(){
		return "Flight Number: " + this.flightNumber + "'s Service quality: \n" +
				"Delay on departure in " + this.departDelay + " minutes\n" +
				"Delay on Arrival in " + this.arrivalDelay + " minutes."; 
	}
}
