package com.unlam.arduito;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gcm.GCMRegistrar;
import com.unlam.arduito.accesos.VerAccesosActivity;
import com.unlam.arduito.ayuda.AyudaActivity;
import com.unlam.arduito.camara.ListarCamarasActivity;
import com.unlam.arduito.configuracion.ConfiguracionActivity;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.expandList.ExpandListChild;
import com.unlam.arduito.expandList.ExpandListGroup;
import com.unlam.arduito.historial.HistorialActivity;
import com.unlam.arduito.sensores.SensoresActivity;
import com.unlam.arduito.util.Constantes;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;

public class MainActivity extends Activity {

	
	
	//GCM Sender id value (sacado de google api url)
	private static final String SENDER_ID = Constantes.SENDER_ID;

	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	      

			
	
		/************	inicio botones *****************************/
		//OnClick de boton Accesos
		Button	botonUltimosAccesos = (Button) findViewById(R.id.botonAcceso);
		botonUltimosAccesos.setOnClickListener(new OnClickListener() {
		       public void onClick(View v) {
		          Intent intent = new Intent(MainActivity.this, VerAccesosActivity.class);
		          startActivity(intent);
		       }
		});
		
		 
		//OnClick de boton sensores
		Button	botonSensores = (Button) findViewById(R.id.botonSensores);
			botonSensores.setOnClickListener(new OnClickListener() {
				       public void onClick(View v) {
				          Intent intent = new Intent(MainActivity.this, SensoresActivity.class);
				          startActivity(intent);
				       }
				});

				
			//OnClick de boton camara
			Button	botonCamara = (Button) findViewById(R.id.botonCamara);
				botonCamara.setOnClickListener(new OnClickListener() {
					       public void onClick(View v) {
					          Intent intent = new Intent(MainActivity.this, ListarCamarasActivity.class);
					          startActivity(intent);
					       }
					});

				//OnClick de boton ayuda
				Button	botonAyuda = (Button) findViewById(R.id.botonAyuda);
					botonAyuda.setOnClickListener(new OnClickListener() {
						      public void onClick(View v) {
						          Intent intent = new Intent(MainActivity.this, AyudaActivity.class);
						          startActivity(intent);
						       }
						 });
					
				//OnClick de boton historial
				Button	botonHistorial = (Button) findViewById(R.id.btnHistorial);
				botonHistorial.setOnClickListener(new OnClickListener() {
						      public void onClick(View v) {
						          Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
						          startActivity(intent);
						       }
						 });

				//OnClick de boton historial
				Button	botonConfiguracion = (Button) findViewById(R.id.botonConf);
				botonConfiguracion.setOnClickListener(new OnClickListener() {
						      public void onClick(View v) {
						          Intent intent = new Intent(MainActivity.this, ConfiguracionActivity.class);
						          startActivity(intent);
						       }
						 });
				
				
		/************	fin botones ***************************************************************************************/
        
		
		
		/*********** Inicio bloque GCM *****************************************************************************/
			String keyGCM_guardada = "";	
				try {
				keyGCM_guardada = 	sharedPreferences.getValue(context, "keyGCM");
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
		//Comprueba que el dispositivo tiene habilitada el API para GCM.
		GCMRegistrar.checkDevice(this);
		
		//Verifica la correcta configuración en el archivo AndroidManifest.xml.
		GCMRegistrar.checkManifest(this);
		
		//getRegistrationId -> Retorna el identificador asignado por el servicio GCM
		final String regId = GCMRegistrar.getRegistrationId(this);		
	
		
		if (regId.equals("")) {
			//Lanza el proceso de registro de la aplicación en el servicio GCM. 
			GCMRegistrar.register(this, SENDER_ID);
			//Toast.makeText(getApplicationContext(), "Registrando el movil", Toast.LENGTH_LONG).show();
			//envio al ninja
		} else if( !regId.equals(keyGCM_guardada)){
			JHHelper.registerTomcat(context, regId );
		}else{
			Log.v("arduito", "EL KEYGCM YA ESTA REGISTRADO EN EL SERVER");
			// Skips registration.              
			//Toast.makeText(getApplicationContext(), "Movil registrado", Toast.LENGTH_LONG).show();
		}
		
		
		/*********** fin bloque GCM *************************************************************************************/
		
		
		
		/*********** Inicio Seteo ExpandList ************/
	//	ExpandList = (ExpandableListView) findViewById(R.id.ExpList);
	//    ExpListItems = SetStandardGroups();
	//	ExpAdapter = new ExpandListAdapter(MainActivity.this, ExpListItems);
	//	ExpandList.setAdapter(ExpAdapter);
		/*********** fin Seteo ExpandList ************/
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
	
	/**
	 * Metodo usado en el ExpandList, popula los groups.
	 * @author ptamburro
	 */
	public ArrayList<ExpandListGroup> SetStandardGroups() {
		ArrayList<ExpandListGroup> list = new ArrayList<ExpandListGroup>();
		ArrayList<ExpandListChild> list2 = new ArrayList<ExpandListChild>();
		ExpandListGroup gru1 = new ExpandListGroup();
		gru1.setName("Monitor piso 1");
		ExpandListChild ch1_1 = new ExpandListChild();
		ch1_1.setName("Sensor Temperatura");
		ch1_1.setTag(null);
		list2.add(ch1_1);
		ExpandListChild ch1_2 = new ExpandListChild();
		ch1_2.setName("Sensor Polvo");
		ch1_2.setTag(null);
		list2.add(ch1_2);
		ExpandListChild ch1_3 = new ExpandListChild();
		ch1_3.setName("Sensor humedad");
		ch1_3.setTag(null);
		list2.add(ch1_3);
		gru1.setItems(list2);
		list2 = new ArrayList<ExpandListChild>();
			         
		ExpandListGroup gru2 = new ExpandListGroup();
		gru2.setName("Monitor piso 2");
		ExpandListChild ch2_1 = new ExpandListChild();
		ch2_1.setName("Sensor Temperatura");
		ch2_1.setTag(null);
		list2.add(ch2_1);
		ExpandListChild ch2_2 = new ExpandListChild();
		ch2_2.setName("Sensor Polvo");
		ch2_2.setTag(null);
		list2.add(ch2_2);
		ExpandListChild ch2_3 = new ExpandListChild();
		ch2_3.setName("Sensor X");
		ch2_3.setTag(null);
		list2.add(ch2_3);
		gru2.setItems(list2);
		list.add(gru1);
		list.add(gru2);
			         
		return list;
	}	
	
	
	

	
	
}
