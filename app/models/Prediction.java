package models;

import scala.util.Random;

public class Prediction {
	public void buildModel(){
		return;
	}
	public FlightQuality predict(String flightNumber){
		FlightQuality quality =  new FlightQuality(flightNumber);
		Random random = new Random();
		quality.setDelay(random.nextInt(120), random.nextInt(120));
		return quality;
	}
}
