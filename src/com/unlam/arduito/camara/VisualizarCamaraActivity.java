package com.unlam.arduito.camara;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class VisualizarCamaraActivity extends Activity {

	private static final String TAG = "arduito";
	private MjpegView mv;
	private Button btn_cam_up, btn_cam_down, btn_cam_left, btn_cam_right;

	
	//context
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	// TODO generar un archivo de properties
	private String usuario = Constantes.USER_CAM;
	private String password = Constantes.PASS_CAM;
	private String CameraURL = "http://arduito.no-ip.info/videostream.cgi?user="
			+ usuario + "&pwd=" + password; // + "&resolution=320x240";
	//private String CameraControlURL = "http://arduito.no-ip.info/decoder_control.cgi?command=";

	// comandos de la camara
	/*
	private String commandLeft = "4";
	private String commandLeftStop = "5";
	private String commandRight = "6";
	private String commandRightStop = "7";
	private String commandUp = "0";
	private String commandUpStop = "1";
	private String commandDown = "2";
	private String commandDownStop = "3";
     */
	
	//cam comandos servidor
	private String puedo_mover = "no";
	private String puedo_ver = "no";
	private String derecha = "derecha/";
	private String izquierda = "izquierda/";
	private String arriba = "arriba/";
	private String abajo = "abajo/";
	private String id_camara = "1";
	private String url_camara = "";
	
	/*
	 * extras comandos 25 center 26 Vertical patrol 27 Stop vertical patrol 28
	 * Horizontal patrol
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
        //recupero valores obtenidos de activity de listar camaras (ListarCamarasActivity)
        Intent in = getIntent();
        id_camara = in.getStringExtra("id_cam");
        url_camara = in.getStringExtra("url_cam");
        
        //obtengo credenciales para obtener stream de camara
        try {
			usuario = sharedPreferences.getUsuarioCam(context);
			password = sharedPreferences.getPassCam(context);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //armo la url de la camara a stremear
        url_camara = url_camara + "/videostream.cgi?user=" + usuario + "&pwd=" + password;
		
        //valido conexion
		if(!validarConexion()){
			Toast.makeText(VisualizarCamaraActivity.this, "No es posible reproducir video por no tener conexión adecuada (cod. 1004-M)" , Toast.LENGTH_LONG).show();
		}
		
		//hago cosas propias del stream
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		 //veo si puedo mover la camara y de ahi stremeo
		new pedirPermiso().execute();
	}

	@Override
	public void onPause() {
		super.onPause();
		mv.stopPlayback();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualizar_camara, menu);
		return true;
	}

	public class PegarACamara extends AsyncTask<String, Void, MjpegInputStream> {

		ProgressDialog progressDialog;

		protected MjpegInputStream doInBackground(String... url) {

			DefaultHttpClient client = null;
			HttpResponse res = null;

			try {
				Log.d(TAG, "1 . CAMARA: creando url");

				URI uri = URI.create(url[0]);
				client = new DefaultHttpClient();

				Log.d(TAG, "2. CAMARA: seteo credenciales");

				client.getCredentialsProvider().setCredentials(
						new AuthScope(uri.getHost(), uri.getPort(),
								AuthScope.ANY_SCHEME),
						new UsernamePasswordCredentials(usuario, password));

				Log.d(TAG, "3. CAMARA: ejecuto");

				res = client.execute(new HttpGet(uri));

				Log.d(TAG, "4. CAMARA: Request finished, status = "
						+ res.getStatusLine().getStatusCode());

				if (res.getStatusLine().getStatusCode() == 401) {
					// SIN PERMISOS MOSTRO
					Log.d(TAG, "SIN PERMISOS DE ACCESO A LA CAMARA");
					return null;
				}

				return new MjpegInputStream(res.getEntity().getContent());

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.d(TAG, "Request failed-ClientProtocolException", e);
				// Error connecting to camera
			} catch (IOException e) {
				e.printStackTrace();
				Log.d(TAG, "Request failed-IOException", e);
				// Error connecting to camera
			}

			return null;
		}

		protected void onPostExecute(MjpegInputStream result) {
			
			String isFullSize = "";
			try {
				isFullSize = sharedPreferences.getValue(context, "camara_full_size");
			} catch (ServiceException e) {
				e.printStackTrace();
			} 
			
			mv.setSource(result);
			
			//veo preferencia del usuario en cuanto a visualizacion
			if("YES".equals(isFullSize)){
				mv.setDisplayMode(MjpegView.SIZE_FULLSCREEN);  // SIZE_BEST_FIT
			}else{
				mv.setDisplayMode(MjpegView.SIZE_STANDARD); 
			}
			
			
			mv.showFps(true);
		}
	}

	private class WebPageTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();

				// autorizacion
				URI uri = URI.create(url);
				client.getCredentialsProvider().setCredentials(
						new AuthScope(uri.getHost(), uri.getPort(),
								AuthScope.ANY_SCHEME),
						new UsernamePasswordCredentials(usuario, password));

				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// textView.setText(result);
		}
	}


	private void showTextInfo(String txt) {
		TextView textCmdSend = (TextView) findViewById(R.id.textEnviosACamara);
		if (txt != null) {
			textCmdSend.setText(String.valueOf(txt));
		} else {
			textCmdSend.setText("");
		}
	}
	
	
	private boolean validarConexion(){
        boolean enabled = true;
        
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
         
        if ((info == null || !info.isConnected() || !info.isAvailable())){
            enabled = false;
        }
        
        return enabled;  
	}
	
	
	private class pedirPermiso extends AsyncTask<Void, Void, ArrayList<String>> {
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(VisualizarCamaraActivity.this, null,"Verificando si es posible mover la cámara", true);            
		    };
				
		
		
		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			
			String dominio = "";
			String pin = "111";
			try {
				dominio = sharedPreferences.getDominio(context);
				pin = sharedPreferences.getValue(context, "pin");
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}

			String url = dominio + Constantes.URL_CAMARA + pin + Constantes.URL_CAMARA_PERMISO + "1"; 
			
			
			// Hashmap for ListView
			ArrayList<String> myList = new ArrayList<String>(); 

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				  if(json!=null){
									String ver = json.getString("ver");
									String mover = json.getString("mover");
									myList.add(ver);
									myList.add(mover);
								}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return myList;
		}
		
		
		protected void onPostExecute(ArrayList<String> results) {
					
			if(results.size() == 2 ){
			    String ver = results.get(0);
				String mover = results.get(1);
				if("si".equals(mover)){
					puedo_mover = "si";
				}
			}
			
			String dominio = "";
			String pin = "111";
			try {
				dominio = sharedPreferences.getDominio(context);
				pin = sharedPreferences.getValue(context, "pin");
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			
			final String url_mover = dominio + Constantes.URL_CAMARA + pin + Constantes.URL_MOVER;
			final String url_parar = dominio + Constantes.URL_CAMARA + pin + Constantes.URL_PARAR;
			final String id_cam = id_camara;
				
				//deshabilitar barra progreso
				super.onPostExecute(results);
		        progressDialog.dismiss();
				
				////de aca en adelante tuto 

				setContentView(R.layout.activity_visualizar_camara);

				//consultar permisos mover camara
				showTextInfo("Puede mover la cámara");
				
				
				
				mv = new MjpegView(context);
				View stolenView = mv;

				View view = (findViewById(R.id.Vid));
				((ViewGroup) view).addView(stolenView);

				btn_cam_up = (Button) findViewById(R.id.moveCamUp);
				btn_cam_down = (Button) findViewById(R.id.moveCamDown);
				btn_cam_left = (Button) findViewById(R.id.moveCamLeft);
				btn_cam_right = (Button) findViewById(R.id.moveCamRight);

				
				if("si".equals(puedo_mover)){
				
				btn_cam_up.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {

						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							Log.d(TAG, "MUEVO CAMARA PARA ARRIBA");
							//String URL = CameraControlURL + commandUp;
							// Toast.makeText(VisualizarCamaraActivity.this, "subo : " +
							// URL , Toast.LENGTH_SHORT).show();
							
							String URL = url_mover + arriba + id_cam;
							
							showTextInfo("Pidiendo mover arriba");
							new WebPageTask().execute(URL);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							Log.d(TAG, "FRENO MOVER");
							//String URL = CameraControlURL + commandUpStop;
							String URL = url_parar + arriba + id_cam;
							// Toast.makeText(VisualizarCamaraActivity.this,
							// "bajo - url: " + URL , Toast.LENGTH_SHORT).show();
							showTextInfo("Puede mover la cámara");
							new WebPageTask().execute(URL);
						}
						return false;
					}
				});

				btn_cam_down.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							//String URL = CameraControlURL + commandDown;
							String URL = url_mover + abajo + id_cam;
							showTextInfo("Pidiendo mover abajo");
							new WebPageTask().execute(URL);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							//String URL = CameraControlURL + commandDownStop;
							String URL = url_parar + abajo + id_cam;
							showTextInfo("Puede mover la cámara");
							new WebPageTask().execute(URL);
						}
						return false;
					}
				});

				btn_cam_left.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							//String URL = CameraControlURL + commandLeft;
							String URL = url_mover + izquierda + id_cam;
							showTextInfo("Pidiendo mover a izquierda");
							new WebPageTask().execute(URL);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							//String URL = CameraControlURL + commandLeftStop;
							String URL = url_parar + izquierda + id_cam;
							showTextInfo("Puede mover la cámara");
							new WebPageTask().execute(URL);
						}
						return false;
					}
				});

				btn_cam_right.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							//String URL = CameraControlURL + commandRight;
							String URL = url_mover + derecha + id_cam;
							// Toast.makeText(VisualizarCamaraActivity.this,
							// "derecha derecha - url: " + URL ,
							// Toast.LENGTH_SHORT).show();
							showTextInfo("Pidiendo mover a derecha");
							new WebPageTask().execute(URL);
						} else if (event.getAction() == MotionEvent.ACTION_UP) {
							//String URL = CameraControlURL + commandRightStop;
							String URL = url_parar + derecha + id_cam;
							// Toast.makeText(VisualizarCamaraActivity.this,
							// "derecga iz - url: " + URL , Toast.LENGTH_SHORT).show();
							showTextInfo("Puede mover la cámara");
							new WebPageTask().execute(URL);
						}
						return false;
					}
				});

				}else{
					showTextInfo("NO PUEDE MOVER LA CAMARA");
				}
				// mHandler.postDelayed(sRunnable, 600000);

				//new PegarACamara().execute(CameraURL);
				new PegarACamara().execute(url_camara);
				

		}
	
	
	
	}

}
