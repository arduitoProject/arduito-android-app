package com.unlam.arduito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class JHHelper {
	
	//Shared preference
	private static UtilFactory utilFactory = new UtilFactory();
	
	public static void registerTomcat(final Context context, String registration) {

		Log.d("arduito", "REGISTO GCM KEY EN SERVIDOR: ENVIANDO");
		
		new AsyncTask<String, Void, String>() {
			
			@Override
			protected String doInBackground(String... registrations) {
			return register(context, registrations[0]);
			}

			@Override
			protected void onPostExecute(String registration) {
			
			}

 

			private String register(Context context, String registration) {

				    String dominio = Constantes.dominio;
					HttpClient client = new DefaultHttpClient();

					try {
							
							//lo guardo en el sharedPreferences para mostrarlo
							SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();				
							sharedPreferences.setValue(context, "keyGCM", registration);
							
							String pin1_ = sharedPreferences.getValue(context, "pin");
						    dominio = sharedPreferences.getDominio(context);
							
							
							
							String urlRegistro = dominio + Constantes.URL_SEND_KEY_GCM + pin1_ + "/"+ registration;
							
							HttpPost post = new HttpPost(urlRegistro);
							post.setEntity(new UrlEncodedFormEntity(Arrays.asList(new NameValuePair[] { new BasicNameValuePair("ID",registration) })));

							int httpStatus = client.execute(post).getStatusLine().getStatusCode();

							if (httpStatus < 400) {
								
								if(httpStatus == 200){				
									sharedPreferences.setValue(context, "envio_key", "si");
									sharedPreferences.setValue(context, "fecha_key",  String.valueOf(new Date()));
									Log.d("arduito", "REGISTO GCM KEY EN SERVIDOR: OK");
								}
								
								Log.d("arduito", " El https status " + httpStatus);
								return registration;
							} else {
								Log.d("arduito", " El https status es mas de 400 " + httpStatus);
							}

						} catch (ClientProtocolException exception) {
							Log.d("arduito", "REGISTO GCM KEY EN SERVIDOR: ERROR!" + exception.toString());
							exception.printStackTrace();
						} catch (IOException exception) {
							Log.d("arduito", "REGISTO GCM KEY EN SERVIDOR: ERROR! > " + exception.toString());
							exception.printStackTrace();
						} catch (ServiceException e) {
							Log.e("arduito", "REGISTO GCM KEY EN SERVIDOR: Error invocndo el servicio de setValue de sharedPreferences");
							e.printStackTrace(); 
						}
					
					return null;
			}

		}.execute(registration);

  }
}