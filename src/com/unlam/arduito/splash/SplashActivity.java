package com.unlam.arduito.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.unlam.arduito.MainActivity;
import com.unlam.arduito.R;
import com.unlam.arduito.exception.ServiceException;
import com.unlam.arduito.login.LoginActivity;
import com.unlam.arduito.util.SharedPreferencesService;
import com.unlam.arduito.util.UtilFactory;


public class SplashActivity extends Activity {
	
	//Contexto usado para el alert dialog
	final Context context = this; 
	
	//Shared preference
	private UtilFactory utilFactory = new UtilFactory();
	private SharedPreferencesService sharedPreferences = utilFactory.getSharedPreferencesService();
	 
    // Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 3000; // 3 segundos
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        String key_validado = "no-encontrado";
        try {
			key_validado = sharedPreferences.getValue(context, "esta_validado");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
        
        final String key_validado_final = key_validado;
        
        // Tenemos una plantilla llamada splash.xml donde mostraremos la información que queramos (logotipo, etc.)
        setContentView(R.layout.activity_splash);
 
        new Handler().postDelayed(new Runnable(){
            public void run(){
            Intent intent;	
            
		    // Cuando pasen los 3 segundos, pasamos a la actividad principal solo si el usuario esta validado
            if("YES".equals(key_validado_final)){
            	intent = new Intent(SplashActivity.this, MainActivity.class);
            }else{
            	intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
         
            startActivity(intent);
        	finish();
            };
        }, DURACION_SPLASH);
    }
}