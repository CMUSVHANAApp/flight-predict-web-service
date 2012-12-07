package models;

import java.util.HashMap;
import java.util.Random;

public class GeoLocation {
	protected double longitutde;
	protected double latitude;
	public GeoLocation(double longtitude, double latitude){
		this.longitutde = longtitude;
		this.latitude = latitude;
	}
	public static GeoLocation getGeoLocation(){
		Random random = new Random();
		int i = (random.nextBoolean())? -1:1;
		System.out.println(i);
		double longtitude = i * random.nextInt(18000)/100.0;
		i = (random.nextBoolean())? -1:1;
		System.out.println(i);
		double latitude = i * random.nextInt(9000)/100.0;
		return new GeoLocation(latitude, longtitude);
	}

	public String toString(){
		return "";
	}
	public String toJSONString(){
		return "";
	}
	public HashMap<String, Object> toHashMap(){
		HashMap<String, Object> r = new HashMap<String, Object>();
		r.put("longtitude", this.longitutde);
		r.put("latitude", this.latitude);
		return r;
	}
}
