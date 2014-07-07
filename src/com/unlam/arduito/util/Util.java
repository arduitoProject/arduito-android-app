package com.unlam.arduito.util;

import android.content.Context;
import android.widget.Toast;

public class Util {

	
	   public static Toast toast;
	   private static String lastToastText;

	   public static void showToast(Context _context, String _text) {
	      if (toast != null) {
	         toast.cancel();
	      }
	      if (_text != null && _text.equals(lastToastText)) {
	         toast.show();
	      } else {
	         lastToastText = _text;
	         toast = Toast.makeText(_context, _text, Toast.LENGTH_SHORT);
	         toast.show();
	      }
	   } 
	   
	   
	   

		/*
		private void mostrarAlertDialogPin(){
			
			//inicio el alertDialog	
	    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
	    	alert.setTitle("No se detecto PIN asociado"); //Set Alert dialog title here
	    	alert.setMessage("Ingrese su PIN (4 digitos)"); //Message here

	        //seteo inicial del primer edit text 
	        final EditText input = new EditText(context);
	        alert.setView(input);

	    	//seteo boton OK
		    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int whichButton) {
		    	 String srt = input.getEditableText().toString();
			    	 if(setPin(srt)){
				    	 Toast.makeText(getApplicationContext(),"PIN registrado",Toast.LENGTH_LONG).show();
			    	 }else{
			    		 Toast.makeText(getApplicationContext(),"PIN incorrecto (solo 4 caracteres). Desde configuración puede intentarlo nuevamente (codigo error: 500-M)",Toast.LENGTH_LONG).show();
			    	 }
		    	} 
		      });
	        
	        //seteo boton CANCEL
	    	alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
	    	  public void onClick(DialogInterface dialog, int whichButton) {
	    		  dialog.cancel();
	    		  Toast.makeText(getApplicationContext(),"Recuerde que puede ingresarlo desde Configuración",Toast.LENGTH_LONG).show();
	    	  }
	    	}); 
	    	
	    	AlertDialog alertDialog = alert.create();
	    	alertDialog.show();
		}
		
		
		*/
		
	   
	   
	
}
