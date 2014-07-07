package com.unlam.arduito.util;

import com.unlam.arduito.exception.ServiceException;

import android.content.Context;

public interface  SharedPreferencesService {
	
	
	public void setValue(Context context, String key, String value) throws ServiceException;
	
	/**
	 * Devuelve el valor de la propiedad pasada por parametro o el string "no-encontrado" si la propiedad pedida no existe.
	 * @author Pablo Tamburro
	 */
	public String getValue(Context context, String key) throws ServiceException;
	
	public boolean existeKey(Context context, String key) throws ServiceException;
	
	/**
	 * Devuelve el dominio utilizado en el sistema.
	 */
	public String getDominio(Context context) throws ServiceException;

	/**
	 * Devuelve el usuario de la url de la camara
	 */
	public String getUsuarioCam(Context context) throws ServiceException;

	/**
	 * Devuelve el password de la url de la camara
	 */
	public String getPassCam(Context context) throws ServiceException;
}
