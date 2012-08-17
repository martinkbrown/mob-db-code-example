package com.mobdb.sample.constants;

public class Constants {

	//public static String URL = "http://10.0.2.2:8080/ServiceRequest/ClientRequest";
	public static String URL           = "http://api.mobdb.net/ClientRequest";
	public static String APP_KEY       = "CIWWP3-1Ss-Vy7mOvi36kGTtDU-G0210JFRsWoBSoScBJpadeatrtTYy5Qqa";
	public static String BOUNDERY      = "---------------------------7d159c1302d0y0";
	public static String SEPARATOR     = "\r\n\r\n";
	
	public static final String TAG  = "mobDB";
	public static final String JSON_CONTENT  = "application/json";
	public static final String STATUS  		 = "status";
	public static final String KEY     		 = "key";
	public static final String SQL     		 = "sql";
	public static final String ROW     		 = "row";
	public static final String QUARY   		 = "query";
	public static final String PARAM   		 = "param";
	public static final String BAR_GRAPH     = "bargraph";
	public static final String PUSH    		 = "push";
	public static final String DEVICE_TYPE   = "device";
	public static final String IOS  		 = "ios";
	public static final String ANDROID  	 = "android";
	public static final String DEVICE_TOKEN  = "token";
	public static final String PAYLOAD 		 = "payload";
	public static final String WHEN 		 = "when";
	public static final String ALERT 		 = "alert";
	public static final String SOUND 		 = "sound";
	public static final String BADEG 		 = "badeg";
	public static final String VALUE 		 = "value";
	public static final String FILE_NAME 	 = "name";
	public static final String FILE_DATA 	 = "data";
	
	public static final int COL_DATA = 0;
	public static final int COL_TYPE = 1;
	
	//----------------------Data types-------------
	public static final String INTEGER = "integer";
	public static final String STRING  = "string";
	public static final String FLOAT   = "float";
	public static final String FILE   = "jsonObject";
	//----------------------END--------------------
	
	//----------------Analytics tags---------------
	public static final String EMP_LIST 	   = "Employee list";
	public static final String ADD_NEW_PROFILE = "Add new profile";
	public static final String IMAGE_DOWNLOAD = "Image DownLoad";
	public static final String DELETED_PROFILE = "Deleted profile";
	public static final String UPDATE_PROFILE = "Update Emp profile name";
	//----------------------END--------------------
	
	//-------------------PUSH Message------------------------
    public static final String EXTRA_SENDER = "sender";
    public static final String EXTRA_APPLICATION_PENDING_INTENT = "app";
    public static final String REQUEST_UNREGISTRATION_INTENT = "com.google.android.c2dm.intent.UNREGISTER";
    public static final String REQUEST_REGISTRATION_INTENT = "com.google.android.c2dm.intent.REGISTER";
    public static final String LAST_REGISTRATION_CHANGE = "last_registration_change";
    public static final String BACKOFF = "backoff";
    public static final String GSF_PACKAGE = "com.google.android.gsf";
    // package
    static final String PREFERENCE = "com.google.android.c2dm";
    
    private static final long DEFAULT_BACKOFF = 30000;
    
    
    //Table name
    public static final String EMPLOYEES = "employees";
  //-------------------END------------------------
    
}
