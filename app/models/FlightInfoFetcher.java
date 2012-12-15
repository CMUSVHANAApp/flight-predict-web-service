package models;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FlightInfoFetcher {
	public static void main(String [ ] args) throws IOException, ParseException{
		System.out.println(new FlightInfoFetcher().fetch());
	}
	public FlightInfoFetcher() throws IOException{
		
	}
	public ArrayList<FlightQuality> fetch() throws IOException, ParseException{
		Document doc = Jsoup.connect("http://www.phl.org/passengerinfo/Pages/FlightInformation.aspx").get();
		Elements table = doc.select("#ctl00_m_g_c8b2de17_9e20_49ea_b527_51ac8eed7317_ctl00_flightGrid_ctl00 tbody tr");
		ArrayList<FlightQuality> fs = new ArrayList<FlightQuality>();
		for (Element tr : table) {
			
			if(tr.hasClass("rgRow") || tr.hasClass("rgAltRow")){
				String airline = tr.child(0).child(0).attr("title");
				String flightNumber = tr.child(1).text();
				String City = tr.child(2).text();
				String time = tr.child(3).text();
				String gate = tr.child(4).text();
				String Direction = tr.child(5).text();
				String Status = tr.child(6).text();
				String arrival = "", departure = "";
				int delayArrival=0, delayDeparture=0;
				int diff = 0;
				SimpleDateFormat format = new SimpleDateFormat("HH:mm a");
				Date schedule = format.parse(time);
				if(!Status.equals("Arrived") && !Status.equals("On Time") && !Status.equals("At Gate") && !Status.equals("Customs") && !Status.equals("Departed") && !Status.equals("Closed")){
					
					
					Date actual = format.parse(Status);
					diff = (int) ((actual.getTime() - schedule.getTime())/(60*1000));
					System.out.println("'" + Status+ "'/" + "'" + time + "': " + String.valueOf(diff));
				}
				if(Direction == "Departure"){
					departure = "PHL";
					arrival = City;
					delayDeparture = diff;
				}
				else{
					arrival = "PHL";
					departure = City;
					delayArrival = diff;
				}	
				Date date = new Date();
				date.setHours(schedule.getHours());
				date.setMinutes(schedule.getMinutes());
				date.setSeconds(0);
				FlightQuality f = new FlightQuality(airline, flightNumber, date , departure, arrival, false);
				f.setDelay(delayDeparture, delayArrival);
				fs.add(f);
			}
		}
		
		return fs;
	}
}
