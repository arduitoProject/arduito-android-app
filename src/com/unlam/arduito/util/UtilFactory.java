package com.unlam.arduito.util;

public class UtilFactory {
	
	
	private SharedPreferencesService sharedPreferencesService = new SharedPreferencesServiceImpl(); 
	
	public SharedPreferencesService getSharedPreferencesService(){
		return sharedPreferencesService;
	}

}
