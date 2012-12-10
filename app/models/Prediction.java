package models;

import java.util.Date;

import scala.util.Random;

public class Prediction {
	public void buildModel(){
		return;
	}
	public FlightQuality predict(String airline, String flightNumber, Date d, String departure, String arrival){
		FlightQuality quality =  new FlightQuality(airline, flightNumber, d, departure, arrival);
		Random random = new Random();
		int departDelay = 0;
		if(random.nextBoolean()){
			departDelay = random.nextInt(120);
		}
		int arrivalDelay = 0;
		if(random.nextBoolean()){
			arrivalDelay = random.nextInt(120);
		}
		quality.setDelay(departDelay, arrivalDelay);
		return quality;
	}
}
