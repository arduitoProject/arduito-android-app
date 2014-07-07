package com.unlam.arduito;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.unlam.arduito.util.Constantes;

public class GCMIntentService extends GCMBaseIntentService {
	
	private static final String SENDER_ID = Constantes.SENDER_ID;
	private static final int NOTIF_ALERTA_ID = 212;
	
	
	public GCMIntentService() {
		super(SENDER_ID);
		}
	
	@Override
	protected void onError(Context arg0, String arg1) {
		Log.d("javahispano","Error recibido");
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		String msg = arg1.getExtras().getString("mensaje");
		String titulo_ = arg1.getExtras().getString("titulo");
		
		Log.d("arduito","SE RECIBE NOTIFICACION CON TITULO Y MENSAJE -> : " +titulo_ +" / " + msg);
		
		
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notManager = (NotificationManager) getSystemService(ns);
		
		//Configuramos la notificación
		int icono = android.R.drawable.stat_sys_warning;
		
		
		CharSequence textoEstado = "Mensaje de alerta!";
		
		if(titulo_ != null && !"".equals(titulo_)){
			textoEstado = titulo_;
		}
		
		
		
		long hora = System.currentTimeMillis();
		 
		Notification notif = new Notification(icono, textoEstado, hora);
		
		//Configuramos el Intent
		Context contexto = getApplicationContext();
		CharSequence titulo = textoEstado;
		CharSequence descripcion = msg;
		 
		Intent notIntent = new Intent(contexto,MainActivity.class);
		 
		PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, notIntent, 0);
		 
		notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);
		
		//AutoCancel: cuando se pulsa la notificaión ésta desaparece
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		

		
		//Añadir sonido, vibración y luces
		notif.defaults |= Notification.DEFAULT_SOUND;
		notif.defaults |= Notification.DEFAULT_VIBRATE;
		//notif.defaults |= Notification.DEFAULT_LIGHTS;
	
		
		
		
		//Enviar notificación
		notManager.notify(NOTIF_ALERTA_ID, notif);
		
	}

	
	@Override
	protected void onRegistered(Context arg0, String arg1) {
		//Posteriormente enviaremos el id al Tomcat
		Log.d("arduito", "Se recibe un GCM KEY y lo vamos a registrar en el servidor: " + arg1);
		JHHelper.registerTomcat(arg0, arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		//Posteriormente enviaremos el id al Tomcat
		Log.d("javahispano","Baja:" + arg1);
	}


}

