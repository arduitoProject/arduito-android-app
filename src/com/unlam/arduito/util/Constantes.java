package com.unlam.arduito.util;

public class Constantes {


	//DOMINIO GENERAL
	public final static String dominio = "http://192.168.1.200:8080";
	public final static String camara = "http://arduito.no-ip.info/videostream.cgi?user=";
	//private final static String dominio = "http://192.168.0.4:8080";
	
	//TIMEOUTS PEDIDOS HTTP (Excepto la conexion de la camara)
	public final static int MS_TIMEOUT_ESTABLECER_CONEXION = 5000;    
	public final static int MS_TIMEOUT_OBTENER_DATOS = 5000;
	
	
	
	//WS DE PRUEBA
	public final static String url_test = "http://api.androidhive.info/contacts/";

	
	//ACCESOS
	public final static String URL_TODOS_ACCESOS_1 = "/a2-web/api/";   // (/a2-web/api/PIN DEL USUARIO/accesos/todosLosAccesos
	public final static String URL_TODOS_ACCESOS_2 = "/accesos/todosLosAccesos";
	public final static String URL_ACCESOS_POR_ID_1= "/a2-web/api/";    //a2-web/api/pin del usr/accesos/ultimosAccesos/ id sensor
	public final static String URL_ACCESOS_POR_ID_2= "/accesos/ultimosAccesos/";
	
	//SENSORES 
	public final static String URL_SENSORES_1 = "/a2-web/api/"; ///a2-web/api/PIN DEL USUARIO/sensores/todos
	public final static String URL_SENSORES_2 = "/sensores/todos";
	public final static String URL_MEDICIONES_SENSORES_1 = "/a2-web/api/"; //http://localhost:8080/a2-web/api/1381360959745/sensores/sensor/1
	public final static String URL_MEDICIONES_SENSORES_2 = "/sensores/sensor/";
	
	//ULTIMAS ALERTAS
	public final static String URL_ALERTAS_1= "/a2-web/api/";   //http://192.168.56.1:8080/a2-web/api/ PIN USUARIO /alertas/ultimas/ o http://192.168.56.1:8080/a2-web/api/ PIN USUARIO /alertas/ultimas/ID SENSOR HABITACION
	public final static String URL_ALERTAS_2= "/alertas/ultimas/";  
	
	
	
	//LOGIN
	//parametros $pinValidacion/$usuario ejemplo: http://localhost:8080/a2-web/validar/12121/usuario2
	public final static String URL_VALIDAR_LOGIN= "/a2-web/validar/"; 
	
	//KEYGCM  ( ... registro/pin/gcmKEy)
	public final static String URL_SEND_KEY_GCM= "/a2-web/registro/"; 
	
	
	//GCM VALUES
	public final static String SENDER_ID = "276162907196";
	
	//NOTIFICACION DE PRUEBA
	public final static String URL_TEST_1= "/a2-web/";
	public final static String URL_TEST_2= "/notificacion/prueba";
	
	//CAMARA
	public final static String URL_LISTADO_CAMARAS_1= "/a2-web/api/";  //http://localhost:8080/a2-web/api/1383601097481/camaras
	public final static String URL_LISTADO_CAMARAS_2= "/camaras";
	public final static String URL_CAMARA= "/a2-web/api/";
	public final static String URL_CAMARA_PERMISO = "/camara/";     //http://localhost:8080/a2-web/api/1383601097481/camara/1
	public final static String URL_MOVER= "/camara/mover/";  //http://localhost:8080/a2-web/api/1383601097481/camara/mover/izquierda/1
	public final static String URL_PARAR= "/camara/frenar/";  //http://localhost:8080/a2-web/api/1383601097481/camara/parar/izquierda/1
	public final static String USER_CAM = "arduito";
	public final static String PASS_CAM = "4rdu1t0"; //4rdu1t0
	

	
	
}
