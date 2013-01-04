package models.fetcher;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import models.GeoLocation;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;


import play.api.libs.json.JsValue;
import play.api.libs.json.Json;
import play.libs.WS;



import com.avaje.ebean.text.json.JsonElement;


public class GeoLocationFetcher {
	public static void main(String [ ] args){
		String city = "PHL";
		System.out.println("geo of " + city + ":");
		System.out.println(GeoLocationFetcher.Fetch(city));
		
	}	
	public static GeoLocation Fetch(String airportCode){
		HttpClient httpclient = new DefaultHttpClient();
		GeoLocation geo = null;
		try {
            URIBuilder uri = new URIBuilder("http://maps.googleapis.com/maps/api/geocode/json");
            uri.addParameter("address", airportCode.replace(" ", "+"));
            uri.addParameter("sensor", "false");
            HttpGet httpget = new HttpGet(uri.build());

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println(responseBody);
            ObjectMapper objectMapper = new ObjectMapper();  
            try  
            {  
            	JsonFactory factory = objectMapper.getJsonFactory();
    			JsonParser jsonParser = factory.createJsonParser(responseBody);
            	ObjectCodec oc = jsonParser.getCodec();
        		JsonNode node = oc.readTree(jsonParser).get("results").get(0);
        		JsonNode geometry = node.get("geometry").get("location");
        		JsonNode address = node.get("address_components");
        		String city = "";
        		String zipcode = "";
        		for(int i=0;i<address.size();i++){
        			JsonNode component = address.get(i);
        			String types = component.get("types").get(0).asText();
        			if(types.equals("administrative_area_level_2") || types.equals("locality")){
        				city = component.get("long_name").asText();
        			}
        			if(types.equals("administrative_area_level_1")){
        				city += ", " + component.get("long_name").asText();
        			}
        			/*
        			if(type.equals("country")){
        				city += "," + component.get("long_name");
        			}
        			*/
        			if(types.equals("postal_code")){
        				zipcode = component.get("long_name").asText();
        			}
        		}
        		return  new GeoLocation(city, zipcode, geometry.get("lng").asDouble(), geometry.get("lat").asDouble());  
               
            }  
            catch(Exception e)  
            {  
                e.printStackTrace();  
            }  
         
        } catch (ClientProtocolException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		} catch (URISyntaxException e) {
		
			e.printStackTrace();
		} finally {
            httpclient.getConnectionManager().shutdown();
        }
		return geo;
	}

}