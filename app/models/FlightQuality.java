package models;

import java.util.HashMap;
import java.util.Map;

import play.libs.Json;

public class FlightQuality {
	protected String flightNumber;
	protected int delay_depart;
	protected int delay_arrival;
	
	public FlightQuality(String flightNumber){
		this.flightNumber = flightNumber;
	}
	
	public void setDelay(int depart, int arrival){
		this.delay_depart = depart;
		this.delay_arrival = arrival;
	}
	public String toString(){
		return "Flight Number: " + this.flightNumber + "'s Service quality: \n" +
				"Delay on departure in " + this.delay_depart + " minutes\n" +
				"Delay on Arrival in " + this.delay_arrival + " minutes."; 
		
		
	}
	public String toJson(){
		Map<String,String> d = new HashMap<String,String>();
		d.put("flightNumber", this.flightNumber);
		d.put("delayDeparture", String.valueOf(this.delay_depart));
		d.put("delayArrival", String.valueOf(this.delay_arrival));
		Json json = new Json();
		return json.toJson(d).toString();
	}
}
