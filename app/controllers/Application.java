package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import models.GeoLocation;
import models.Prediction;
import models.WeatherFetcher;
import models.Weather;
import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;
import views.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok(index.render("Hello, World!"));
  }
  public static Result weathers(String city){
	  ArrayList<Weather> ws = WeatherFetcher.Fetch(city);
	  ArrayList<HashMap> r = new ArrayList<HashMap>(); 
	  //return TODO;
	  HashMap<String, Object> fs= new HashMap<String, Object>();
	  fs.put("Geo", GeoLocation.getGeoLocation().toHashMap());
	  Iterator<Weather> w_it = ws.iterator();
	  while(w_it.hasNext()){
		  Weather w = w_it.next();
		  r.add(w.toHashMap());
	  }	  
	  fs.put("weather", r);
	  
	  return ok(new Json().toJson(fs).toString());
  }
  public static Result predictions(String flightNumber){
	  Prediction p = new Prediction();
	  
	  //return ok(p.predict(flightNumber).toString());
	  return ok(p.predict(flightNumber).toJson());
  }
  
}