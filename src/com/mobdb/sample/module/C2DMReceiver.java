package com.mobdb.sample.module;

import java.util.HashMap;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;


import com.mobdb.android.MobDB;
import com.mobdb.android.MobDBResponseListener;
import com.mobdb.android.Push;
import com.mobdb.android.SDKConstants;
import com.mobdb.module.R;
import com.mobdb.sample.constants.Constants;

public class C2DMReceiver extends IntentService{

	public static final String REGISTRATION_CALLBACK_INTENT = "com.google.android.c2dm.intent.REGISTRATION";
	private static final String C2DM_INTENT = "com.google.android.c2dm.intent.RECEIVE";
	private static final String C2DM_RETRY = "com.google.android.c2dm.intent.RETRY";
	public static final String EXTRA_REGISTRATION_ID = "registration_id";

	//Email ID used for sing up application on C2DM and ClientLogin token generation 
	private String senderID = "youremailID@gmail.com";

	public C2DMReceiver() {
		super("youremailID@gmail.com");
		// TODO Auto-generated constructor stub
	}


	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
		/*
		final String payload = intent.getStringExtra("payload");

		NotificationManager notificationManager = (NotificationManager) context
		.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon,
				"Push message from mobDB", System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent prifileIntent = new Intent(context, ProfileListActivity.class);
		intent.putExtra("payload", payload);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				prifileIntent, 0);
		notification.setLatestEventInfo(context, "mobDB",
				"Message: "+payload, pendingIntent);
		notificationManager.notify(0, notification);*/

	}


	public void onError(Context context, String errorId) {
		// TODO Auto-generated method stub

	}


	public void onRegistered( Context context, String registrationId ) {
		try {
			// TODO Auto-generated method stub
			Log.i(Constants.TAG, "" +registrationId);

			//save the registration ID in shared prefs
			SharedPreferences.Editor prefs = context.getSharedPreferences("mobdb_prefs", Context.MODE_PRIVATE).edit();
			prefs.putString("registrationID", registrationId);
			prefs.commit();

			createRegisteredNotification( getApplicationContext(), registrationId );
			
			Push push = new Push();
			push.sendDeviceTokenToMobDB(SDKConstants.ANDROID, registrationId);
			
			MobDB.getInstance().execute(Constants.APP_KEY, push, null, false, new MobDBResponseListener() {
				
				@Override
				public void mobDBSuccessResponse() {
					Log.i(Constants.TAG, "registration ID sucessfully saved in mobDB!");
				}
				
				@Override
				public void mobDBErrorResponse(Integer errValue, String errorMsg) {
					Log.e(Constants.TAG, "Error while saving registration ID!");
				}
												
				@Override
				public void mobDBResponse(Vector<HashMap<String, Object[]>> result) {}
				
				@Override
				public void mobDBResponse(String jsonObj) {}
				
				@Override
				public void mobDBFileResponse(String fileName, byte[] fileData) {}
				
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	


	public void createRegisteredNotification(Context context, String registrationId) {

		NotificationManager notificationManager = (NotificationManager) context
		.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon,
				"Registration successful", System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(context, ProfileListActivity.class);
		intent.putExtra("registration_id", registrationId);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, "Registration",
				"Successfully ", pendingIntent);
		notificationManager.notify(0, notification);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(Constants.TAG, "C2DMReceiver");

		Context context = getApplicationContext();
		if (intent.getAction().equals(REGISTRATION_CALLBACK_INTENT)) {
			onRegistered(context, intent.getExtras().getString(EXTRA_REGISTRATION_ID));
		} else if (intent.getAction().equals(C2DM_INTENT)) {
			onMessage(context, intent);
		} else if (intent.getAction().equals(C2DM_RETRY)) {
			Intent registrationIntent = new Intent(Constants.REQUEST_REGISTRATION_INTENT);
	        registrationIntent.setPackage(Constants.GSF_PACKAGE);
	        registrationIntent.putExtra(Constants.EXTRA_APPLICATION_PENDING_INTENT,
	                PendingIntent.getBroadcast(context, 0, new Intent(), 0));
	        registrationIntent.putExtra(Constants.EXTRA_SENDER, senderID);
	        context.startService(registrationIntent);
		}

	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();


	}


}
