package com.unlam.arduito.camara;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class ListarCamarasActivity extends ListActivity {
	
	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	private boolean error = false;
	
	// contacts JSONArray
	JSONArray camaras = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listar_camaras);
		
		new GetCamarasAccesibles().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listar_camaras, menu);
		return true;
	}

	
	
	private class GetCamarasAccesibles extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>> {
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(ListarCamarasActivity.this, null,"Chequeando cámaras accesibles", true);            
		    };
				
		
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
			
			String dominio = "";
			String pin = "111";
			try {
				dominio = sharedPreferences.getDominio(context);
				pin = sharedPreferences.getValue(context, "pin");
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}

			String url = dominio + Constantes.URL_LISTADO_CAMARAS_1 + pin + Constantes.URL_LISTADO_CAMARAS_2; 
			
			
			// Hashmap for ListView
			ArrayList<HashMap<String, String>> listadoCamaras = new ArrayList<HashMap<String, String>>();

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				
				  if(json!=null){
								// Getting Array of Contacts
								camaras = json.getJSONArray("camaras");
								
								// looping through All Contacts
								for(int i = 0; i < camaras.length(); i++){
									JSONObject c = camaras.getJSONObject(i);
									
									// Storing each json item in variable
									String id_cam = c.getString("id");
									String url_cam = c.getString("url");
									String habitacion_cam = c.getString("habitacion");

									// creating new HashMap
									HashMap<String, String> map = new HashMap<String, String>();
									
									// adding each child node to HashMap key => value
									//map.put(TAG_ID, id);
									map.put("id", id_cam);
									map.put("url", url_cam);
									map.put("habitacion", habitacion_cam);
				
									// adding HashList to ArrayList
									listadoCamaras.add(map);
								}
				  }else{
					  error = true;
				  }
						
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return listadoCamaras;
			
		}
		
		
		protected void onPostExecute(ArrayList<HashMap<String, String>> results) {
		
			if(!error){
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(getApplicationContext(), results,
							R.layout.activity_listar_camaras_item,
							new String[] { "id", "habitacion", "url"}, new int[] {
									R.id.name,R.id.ubicacion,R.id.urlOculto});
		
					setListAdapter(adapter);
		
					// selecting single ListView item
					ListView lv = getListView();
		
					// Launching new screen on Selecting Single ListItem
					lv.setOnItemClickListener(new OnItemClickListener() {
		
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							
							// getting values from selected ListItem
							String id_cam = ((TextView) view.findViewById(R.id.name)).getText().toString();
							String url_cam = ((TextView) view.findViewById(R.id.urlOculto)).getText().toString();
							
							
							Intent in = new Intent(getApplicationContext(), VisualizarCamaraActivity.class);
							in.putExtra("id_cam", id_cam);
							in.putExtra("url_cam", url_cam);
							startActivity(in);
						}
					});
			}else{
				//setContentView(R.layout.error_obteniendo_datos);
			}
			
			//deshabilitar barra progreso
			super.onPostExecute(results);
	        progressDialog.dismiss();
	        
	        if(results != null && results.size() <= 0){
	        	Toast.makeText(ListarCamarasActivity.this, "No se obtuvo información para listar" , Toast.LENGTH_LONG).show();
	        }
	        
			
		}
		  
		  @Override
		protected void onCancelled() {
			Toast.makeText(ListarCamarasActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1000-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
    }
	
	
	
	
	
	
	
	
}
