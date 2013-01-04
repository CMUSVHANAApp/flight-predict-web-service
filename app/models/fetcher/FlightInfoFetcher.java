package models.fetcher;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import models.FlightQuality;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FlightInfoFetcher {
	public static void main(String [ ] args) throws IOException, ParseException{
		 FlightInfoFetcher.fetch("UAL937");
	}
	public FlightInfoFetcher() throws IOException{
		
	}
	public static FlightQuality fetch(String flightNumber){
		Document doc;
		try {
			doc = Jsoup.connect("http://travel.flightexplorer.com/FlightTracker/" + flightNumber).get();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		Elements table = doc.select("#FastTrack1_resultGrid tr");
		
		int i=0;
		for (Element tr : table) {
			//skip first row
			if (i==0) {
				i+= 1;
				continue;
			}
			if(tr.children().size() < 6){
				break;
			}
			String flightID = tr.child(0).child(0).text();
			String departCity = tr.child(1).child(0).text();
			String arrivalCity = tr.child(2).child(0).text();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mma zzz");
			Date departureTime = null, arrivalTime = null;
			try {
				departureTime = format.parse(tr.child(3).text());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				departureTime = new Date();
				
			}
			try {
				arrivalTime = format.parse(tr.child(4).text());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				arrivalTime = new Date();
				e.printStackTrace();
			}
			String aircraft = tr.child(5).text();
			String status = tr.child(6).text();
			
			//System.out.println("aircraft");
			return new FlightQuality(flightID, departureTime, arrivalTime);
		}
		return null;
		

	}
}
