package planit.project.utils;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.print.attribute.HashAttributeSet;

import org.json.JSONObject;

import planit.project.dto.MessageDTO;

public class SendPushNotification {
	public final static String AUTH_KEY_FCM = "AAAAFVJ3vSc:APA91bG3EowGi-fPJEJi4Bvw1LyfFRLseddD6nYx5kNnyO-TOGW4YUy_VH5T5BwwkJLErvZD2EYOA3keqvnpqYx7zEMoFY8kMmBZN5EYtzAgBX9-vYe9aRVxhGcS9TnPgGnyCfCIt4BS";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

	public static void pushFCMNotification(String userId, String teamName, MessageDTO messageDTO) throws Exception{

	   String authKey = AUTH_KEY_FCM; // You FCM AUTH key
	   String FMCurl = API_URL_FCM; 

	   URL url = new URL(FMCurl);
	   HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	   conn.setUseCaches(false);
	   conn.setDoInput(true);
	   conn.setDoOutput(true);

	   conn.setRequestMethod("POST");
	   conn.setRequestProperty("Authorization","key="+authKey);
	   conn.setRequestProperty("Content-Type","application/json");

	   JSONObject json = new JSONObject();
	   json.put("to", "/topics/"+userId+"-"+messageDTO.getServerTeamId());
	   JSONObject info = new JSONObject();
	   info.put("title", "New message from " + teamName); 
	   info.put("body", messageDTO.getMessage()); 
	   info.put("sender", messageDTO.getSender());
	   json.put("notification", info);

	   OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	   wr.write(json.toString());
	   wr.flush();
	   conn.getInputStream();
	}

}
