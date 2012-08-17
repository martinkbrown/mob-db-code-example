package com.mobdb.sample.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobdb.android.GetFile;
import com.mobdb.android.GetRowData;
import com.mobdb.android.MobDB;
import com.mobdb.android.MobDBJSONHandler;
import com.mobdb.android.MobDBResponseListener;
import com.mobdb.module.R;
import com.mobdb.sample.constants.Constants;

/* 
 * let's say following table you created in mobDB:
 *
 * Table Name: employees 
 *   			 --------------------------------
 * Table fields: |   name  |  address  |  photo |
 *               --------------------------------
 *  
 */

public class ProfileListActivity  extends ListActivity implements MobDBResponseListener{

	private int empCount                     = 0;
	private final int ADD_NEW_PROFILE        = 0;
	private final int REFERESH_PROFILES      = 1;
	private int currentImageIndexDownloading = 0;
	
	private boolean isInitialized = false;
	
	String currentProfileDownloading         = null;
	private ProgressDialog m_ProgressDialog  = null; 
	private ArrayList<Profiles> m_profiles   = null;
	private ProfileAdapter m_adapter         = null;	
	

	@Override
	public void onCreate( Bundle savedInstanceState ) {

		super.onCreate( savedInstanceState );

		setContentView( R.layout.main );

		isInitialized = false;
	
		
//		SharedPreferences prefs = getSharedPreferences("mobdb_prefs", this.MODE_PRIVATE);
//		
//		if( prefs.contains( "registrationID" ) ) {
//		    //the reg ID is already in shared prefs
//		}
//		else {
//		    //the reg ID is not exists, 1)	Send request to C2DM for a device registration ID 
//		    Intent intent = new Intent( Constants.REQUEST_REGISTRATION_INTENT );
//		    intent.putExtra( "app",PendingIntent.getBroadcast( this, 0, new Intent(), 0 ) );
//		    //pass the same  email address you used to generate your ClientLogin token
//		    intent.putExtra( "sender", "youruser@gmail.com" ); 
//		    startService(intent);
//		}
		
	}
	
//	/*
//	public String oneLaterPush(){
//		
//		JSONObject o = new JSONObject();
//		
//		try {
//		
//			o.put( Constants.KEY, Constants.APP_KEY );
//			JSONObject push = new JSONObject();
//			push.put( Constants.DEVICE_TOKEN, "safasfjghfjasfgjhsagfljgasfljahsgfLJHASGFLJASHGFJAHSFG");
//			
//			//----------Android Payload---------------
//			/*
//			JSONArray android_payload = new JSONArray();
//			
//			JSONObject keyValue = new JSONObject();
//			keyValue.put(Constants.KEY, "alert");
//			keyValue.put(Constants.VALUE, "Hello from mobDB!");
//			android_payload.put(keyValue);
//			
//			keyValue = new JSONObject();
//			keyValue.put(Constants.KEY, "sound");
//			keyValue.put(Constants.VALUE, "ring.mp3");
//			android_payload.put(keyValue);
//			push.put(Constants.PAYLOAD, android_payload);
//			push.put(Constants.WHEN, "03-17-2012,14:30,GMT-08:00");
//			*/
//			//---------------------------------------
//			
//			//------------iOS Payload----------------
//			JSONObject ios_payload = new JSONObject();
//			JSONObject ios_payload_value = new JSONObject();
//			
//			ios_payload_value.put(Constants.ALERT, "Hi from Reeya!");
//			ios_payload_value.put(Constants.SOUND, "default");
//			ios_payload_value.put(Constants.BADEG, 1);
//			
//			ios_payload.put("aps", ios_payload_value);
//			push.put(Constants.PAYLOAD, ios_payload);
//			push.put(Constants.WHEN, "05/23/2012,14:30,GMT-08:00");
//			
//			//---------------------------------------
//			
//			o.put(Constants.PUSH, push);
//			System.out.println(o.toString(1));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return o.toString();
//		
//	}
//	
	
	/**
	 * Starts communication with mobDB for table data,  
	 */
	private void startLoadingProfileList(){

		currentImageIndexDownloading = 0;

		currentProfileDownloading = "";

		setListAdapter(null);		
		
		GetRowData getEmpList = new GetRowData(Constants.EMPLOYEES);
		
		MobDB.getInstance().execute( Constants.APP_KEY, getEmpList, Constants.EMP_LIST, true, this);
		
		m_ProgressDialog = ProgressDialog.show( this, "",
				"Retriving updated profiles from employee table...", true );	

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub	
		super.onStart();

		if(!isInitialized ){

			isInitialized = true;

			startLoadingProfileList();

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		moveTaskToBack(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// TODO Auto-generated method stub

		menu.add( 0, ADD_NEW_PROFILE, 0, "Add new profile" );

		menu.add( 0, REFERESH_PROFILES, 1, "Referesh" );

		return super.onCreateOptionsMenu( menu );

	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		// TODO Auto-generated method stub

		if( item.getItemId() == ADD_NEW_PROFILE ){

			Intent activity = new Intent( this, AddProfileActivity .class );

			startActivity( activity );

		}else if( item.getItemId() == REFERESH_PROFILES ){

			startLoadingProfileList();

		} 

		return super.onOptionsItemSelected( item );

	}


	@Override
	protected void onListItemClick( ListView l, View v, int position, long id ) {
		// TODO Auto-generated method stub		

		super.onListItemClick( l, v, position, id );

		Intent activity = new Intent( this,ProfileDetailedViewActivity .class );

		String name = (String) ( ( TextView ) v.findViewById( R.id.toptext ) ).getText() ;

		String address = (String) ( ( TextView ) v.findViewById( R.id.bottomtext ) ).getText();

		Bitmap bit= ( ( BitmapDrawable )( ( ImageView ) v.findViewById( R.id.icon ) ).getDrawable() ).getBitmap();

		activity.putExtra( "name", name );

		activity.putExtra( "address", address );

		activity.putExtra( "photo",  bit);

		startActivity( activity );

	}			

	/**
	 * To download list of images, initiate backend communication with mobDB    
	 */
	private void startImageDownload(){

		String fileQueary = m_profiles.get( currentImageIndexDownloading ).getPhoto();
		
		
		if(fileQueary.trim().length() > 0 ){
			
			GetFile getFile = new GetFile(fileQueary);
			
			com.mobdb.android.MobDB.getInstance().execute( Constants.APP_KEY, getFile,  Constants.IMAGE_DOWNLOAD, false, this );

			currentProfileDownloading = fileQueary;			

		}else{

			currentImageIndexDownloading++;

			if( currentImageIndexDownloading < m_profiles.size() ){

				startImageDownload();

			}
		}
	}

	@Override
	public void mobDBSuccessResponse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mobDBResponse( Vector<HashMap< String , Object[] >> row ) {
		// TODO Auto-generated method stub
		
		if( row == null || row.size() <= 0 ){
			m_ProgressDialog.dismiss();
			return;
		}
		
		m_profiles = new ArrayList<Profiles>();

		empCount = row.size();

		for ( int i = 0; i < empCount; i++ ) {

			HashMap< String , Object[] > colValue = row.get( i );
			
			Profiles p = new Profiles();
			
			//p.setId     ( colValue.get( "photo" )[Constants.COL_DATA]   );
			Object[] name = (Object[])colValue.get( "name" );
			Object[] address = (Object[])colValue.get( "address" );
			Object[] photo = (Object[])colValue.get( "photo" );
			
			if(name != null){
				p.setName   ((String)name[Constants.COL_DATA]);	
			}
			
			if(address != null){
				p.setAddress( (String)address[Constants.COL_DATA]);
			}
			
			if(photo != null){
				p.setPhoto  ( (String)photo[Constants.COL_DATA] );
			}
									
			
			m_profiles.add( p );

		}

		this.m_adapter = new ProfileAdapter( this, R.layout.row, m_profiles );		
		
		setListAdapter( this.m_adapter );	
		
		m_adapter.notifyDataSetChanged();
		
		m_ProgressDialog.dismiss();
		
		startImageDownload();
	
	}

	@Override
	public void mobDBErrorResponse(Integer value, String msg) {
		// TODO Handle error here
		
		m_ProgressDialog.dismiss();
		
		Log.i(Constants.TAG, "Value: " + value +" Message: " + msg);
			
	}

	@Override
	public void mobDBResponse(String jsonStr) {
		
	}

	@Override
	public void mobDBFileResponse(String fileName, byte[] fileData) {
		// TODO Auto-generated method stub
		
		m_profiles.get( currentImageIndexDownloading ).setId( currentProfileDownloading );

		m_profiles.get( currentImageIndexDownloading ).setImage( BitmapFactory.decodeByteArray( fileData, 0, fileData.length )  );		

		m_adapter.notifyDataSetChanged();		

		currentImageIndexDownloading++;

		if( currentImageIndexDownloading < m_profiles.size() ){
			
			startImageDownload();
			
		}
		
	}


}