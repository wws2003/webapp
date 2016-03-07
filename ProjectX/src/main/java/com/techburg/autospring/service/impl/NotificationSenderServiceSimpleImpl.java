package com.techburg.autospring.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techburg.autospring.bo.abstr.IGCMNotificationBo;
import com.techburg.autospring.model.business.GCMNotification;
import com.techburg.autospring.model.entity.GCMNotificationSerializableEntity;
import com.techburg.autospring.service.abstr.INotificationSenderService;

public class NotificationSenderServiceSimpleImpl implements INotificationSenderService {

	private static final String API_KEY = "AIzaSyDsekL3sECT7yjLMlzOuzMwxx0U2iEdvzE";

	private IGCMNotificationBo mGCMNotificationBo = null;
	private ExecutorService mTaskExecutor = Executors.newCachedThreadPool();
			
	@Autowired
	public void setGCMNotificationBo(IGCMNotificationBo gcmNotificationBo) {
		mGCMNotificationBo = gcmNotificationBo;
	}

	@Override
	public void sendGCMNotification(GCMNotification gcmNotification) {
		final GCMNotificationSerializableEntity notificationContent = mGCMNotificationBo.getGCMNotificationDictionary(gcmNotification);
		mTaskExecutor.submit(new Runnable() {
			@Override
			public void run() {
				sendPostMessage(notificationContent);
			}
		});
	}

	private void sendPostMessage(GCMNotificationSerializableEntity notificationContent) {
		//Reference: http://hmkcode.com/send-http-post-request-from-java-application-to-google-messaging-service/
		try{
			// 1. URL
			URL url = new URL("https://android.googleapis.com/gcm/send");

			// 2. Open connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 3. Specify POST method
			conn.setRequestMethod("POST");

			// 4. Set the headers
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "key=" + API_KEY);

			conn.setDoOutput(true);

			// 5. Add JSON data into POST request body 

			//`5.1 Use Jackson object mapper to convert Content object into JSON
			ObjectMapper mapper = new ObjectMapper();

			// 5.2 Get connection output stream
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

			// 5.3 Copy Content "JSON" into 
			mapper.writeValue(wr, notificationContent);

			// 5.4 Send the request
			wr.flush();

			// 5.5 close
			wr.close();

			// 6. Get the response
			int responseCode = conn.getResponseCode();
			System.out.println("\n----------------------Sending 'POST' request to URL : " + url);
			System.out.println("-----------------------Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
