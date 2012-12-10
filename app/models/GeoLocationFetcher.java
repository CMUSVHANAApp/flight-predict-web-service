package models;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.api.libs.json.JsValue;
import play.api.libs.json.Json;


import com.avaje.ebean.text.json.JsonElement;


public class GeoLocationFetcher {
	public static void main(String [ ] args){
		String city = "SFO";
		System.out.println("Weather of " + city + ":");
		System.out.println(GeoLocationFetcher.Fetch(city));
		
	}	
	public static GeoLocation Fetch(String airportCode){
		HttpClient httpclient = new DefaultHttpClient();
		try {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            qparams.add(new BasicNameValuePair("address", airportCode.replace(" ", "+")));
            qparams.add(new BasicNameValuePair("sensor", "false"));
            URI uri = URIUtils.createURI("http", "maps.googleapis.com", -1, "/maps/api/geocode/json",  URLEncodedUtils.format(qparams, "UTF-8"), null);
            HttpGet httpget = new HttpGet(uri);
            
            System.out.println("executing request " + httpget.getURI());

                       // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            /*
            Gson g=  new Gson();
            
            
            
            JSONObject myjson =  new JSONObject(responseBody.trim());
            JSONArray myjson = new JSONObject(responseBody).getJSONArray("result");
            */
            
           
            
            
            
            
        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		return new GeoLocation(100, 100);
	}

}

