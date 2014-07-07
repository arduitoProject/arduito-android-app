package com.unlam.arduito.sensores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.json.JSONParser;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class SensorChartActivity extends Activity {

	//Contexto usado para el alert dialog
	final Context context = this; 
	
	private boolean error = false;
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	
	private XYPlot plot;
	
	private String titulo = "titulo";
	private String unidades = "unidades";
	private String id_sensor = "id";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		
        
        //seteo de valores adaptables
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        titulo = in.getStringExtra("title");
        unidades = in.getStringExtra("unidad");
        id_sensor = in.getStringExtra("id");

       new ObtenerUltimosValoresSensor().execute(id_sensor);
		
 
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sensor_chart, menu);
		return true;
	}
	
	
	
	
	
	
	// contacts JSONArray
	JSONArray mediciones = null;

	
	
	private class ObtenerUltimosValoresSensor extends AsyncTask<String, Void, List<Double>> {
		
		ProgressDialog progressDialog;
		
		
		    @Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(SensorChartActivity.this, null,"Generando gráfico con ultimas mediciones", true);            
		    };
				
		
		
		@Override
		protected List<Double> doInBackground(String... params) {
			
			
			List<Double> myList = new ArrayList<Double>(); 
			String id_sensor = params[0];
			String pin = "111";
			String dominio = Constantes.dominio;
			try {
				dominio = sharedPreferences.getDominio(context);
				pin = sharedPreferences.getValue(context, "pin");
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String url = dominio + Constantes.URL_MEDICIONES_SENSORES_1 + pin + Constantes.URL_MEDICIONES_SENSORES_2 + id_sensor;

			// Creating JSON Parser instance
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				
				  if(json!=null){
								// Getting Array of Contacts
								mediciones = json.getJSONArray("valores");
								
								// looping through All Contacts
								for(int i = 0; i < mediciones.length(); i++){
									JSONObject c = mediciones.getJSONObject(i);
									
									// Storing each json item in variable
									String valor_medido = c.getString("valorMedido");
									//String fecha = c.getString("fecha");

									Double valorDouble = Double.valueOf(valor_medido); 
				
									// adding HashList to ArrayList
									myList.add(valorDouble);
								}
				  }else{
					  error = true;
				  }
						
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return myList;
			
		}
		
		
		protected void onPostExecute(List<Double> results) {
		
			
			Number cotaSuperior = 100;
			setContentView(R.layout.activity_sensor_chart);
			
			//inicializo un list
		
			List<Double> lista = new ArrayList<Double>(); 
			
			lista.add(0D);
			lista.add(0D);
			lista.add(0D);
			lista.add(0D);
			lista.add(0D);
			lista.add(0D);
			lista.add(0D);
			lista.add(0D);
			lista.add(0D);
			lista.add(0D);

			
			
			for (int i = 0; i < results.size(); i++) {
			    if(i < 10)
			    	lista.set(9-i, results.get(i));
			}
			
			Double mayor = Collections.max(lista);
			
			if(mayor > 100){
				cotaSuperior = mayor;
			}
			
			//Collections.reverse(results);
			
			
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);

			plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
			
			plot.setTitle(titulo);
			plot.setRangeLabel(unidades);
			
			//con esto zafamos de que no muestre cuando todos los valores son iguales en la lista
			plot.setRangeBoundaries(0, cotaSuperior, BoundaryMode.FIXED);
			
			
			//color del fondo del grafico
			//plot.getBorderPaint().setColor(Color.WHITE);
			
			// Create a couple arrays of y-values to plot:
			
			/*  ASI SUBE DE IZQUIERDA A DERECHA LA GRAFICA*/
			/*
			myList.add(10.0);
			myList.add(20.0);
			myList.add(30.0);
			myList.add(40.0);
			myList.add(50.0);
			myList.add(60.0);
			myList.add(70.0);
			myList.add(70.5);
			myList.add(90.0);
			myList.add(100.0);
			*/
			
			// Turn the above arrays into XYSeries':
			XYSeries series1 = new SimpleXYSeries(lista,          // SimpleXYSeries takes a List so turn our array into a List
			   SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
			   "punto medición");                             // Set the display title of the series
			
			// same as above
			//XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2");
			
			//color de linea punto y relleno
			LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, Color.parseColor("#0101DF"), null);
			
			// add a new series' to the xyplot:
			plot.addSeries(series1, series1Format);
			
			// reduce the number of range labels
			plot.setTicksPerRangeLabel(3);
			plot.getGraphWidget().setDomainLabelOrientation(-45);
			

			//deshabilitar barra progreso
			super.onPostExecute(results);
	        progressDialog.dismiss();
			

		}
		  
		  @Override
		protected void onCancelled() {
			Toast.makeText(SensorChartActivity.this, "Error: La conexion no es la adecuada para recuperar los datos (cod 1000-M)" , Toast.LENGTH_SHORT).show();
			super.onCancelled();
		}
		
    }
	
	
/*
	
	Handler myHandler = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        switch (msg.what) {
	        case 0:
	            tengoLosDatos = false;
	            break;
	        default:
	            break;
	        }
	    }
	};

	*/
	
	
}
