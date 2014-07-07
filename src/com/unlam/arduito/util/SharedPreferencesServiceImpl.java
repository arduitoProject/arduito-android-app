package com.unlam.arduito.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.unlam.arduito.exception.ServiceException;



public class SharedPreferencesServiceImpl implements SharedPreferencesService {

	@Override
	public void setValue(Context context, String key, String value) throws ServiceException {
			
		SharedPreferences settings = context.getSharedPreferences("perfil", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(key, value);
			editor.commit();
			
	}
	
	
	public String getValue(Context context, String key){
		
		SharedPreferences settings = context.getSharedPreferences("perfil", Context.MODE_PRIVATE);
		String value = settings.getString(key, "no-encontrado");
		return value;
	}
	

	
	public boolean existeKey(Context context, String key){
		
		SharedPreferences settings = context.getSharedPreferences("perfil", Context.MODE_PRIVATE);
		String value = settings.getString(key, "no-encontrado");
        
		if(value.equals("no-encontrado")){
			return false;
		}
		
		return true;
	}


	@Override
	public String getDominio(Context context) throws ServiceException {
		
		String dominio = this.getValue(context, "url_servidor");
		
		if("no-encontrado".equals(dominio) || dominio == null || dominio.length() < 5 || dominio == "" ){
			dominio = Constantes.dominio;
		}
		
		return dominio;
		
	}

	@Override
	public String getUsuarioCam(Context context) throws ServiceException {
		
		String usuario = this.getValue(context, "camara_user");
		
		if("no-encontrado".equals(usuario) || usuario == null || usuario.length() < 2 || usuario == "" ){
			usuario = Constantes.USER_CAM;
		}
		
		return usuario;
		
	}	
	
	@Override
	public String getPassCam(Context context) throws ServiceException {
		
		String pass = this.getValue(context, "camara_pass");
		
		if("no-encontrado".equals(pass) || pass == null || pass.length() < 2 || pass == "" ){
			pass = Constantes.PASS_CAM;
		}
		
		return pass;
		
	}		
	
}
