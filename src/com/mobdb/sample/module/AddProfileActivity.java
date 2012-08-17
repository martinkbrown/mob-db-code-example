package com.mobdb.sample.module;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;


import com.mobdb.android.InsertRowData;
import com.mobdb.android.MobDB;
import com.mobdb.android.MobDBJSONHandler;
import com.mobdb.android.MobDBResponseListener;
import com.mobdb.module.R;
import com.mobdb.sample.constants.Constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddProfileActivity extends Activity implements OnClickListener, MobDBResponseListener{

	Button save;
	byte[] imageData;
	private ImageView imgView;
	private ProgressDialog m_ProgressDialog = null; 
	

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		// TODO Auto-generated method stub

		super.onCreate( savedInstanceState );

		setContentView( R.layout.addnew );

		save = ( Button )findViewById( R.id.save );		

		imgView = ( ImageView )findViewById( R.id.imageView1 );

		imgView.setOnClickListener( this );

		save.setOnClickListener( this );		

	}

	@Override
	public void onClick( View v ) {
		// TODO Auto-generated method stub

		if( v.getId() == R.id.save ){			

			m_ProgressDialog = ProgressDialog.show( this, "",
					"Saving profile in employee table...", true );
			
			//Drawable d = getResources().getDrawable(R.drawable.icon2);  
			//ByteArrayOutputStream stream = new ByteArrayOutputStream();
			//Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
			//bitmap.compress(CompressFormat.PNG,100, stream);
			//imageData = stream.toByteArray();
			
			if( imageData == null ){

				createProfileWithoutImage( ( ( EditText )findViewById(R.id.Name)).getText().toString(), ((EditText)findViewById(R.id.Address)).getText().toString());

			}else{

				createProfileWithImage("image.png", imageData, ( ( EditText)findViewById(R.id.Name)).getText().toString(), ((EditText)findViewById(R.id.Address)).getText().toString());

			}

		}else if( v.getId() == R.id.imageView1 ){

			Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);

			photoPickerIntent.setType("image/*");

			startActivityForResult(photoPickerIntent, 1);

		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK)
		{

			try {

				Uri chosenImageUri = data.getData();	
				
				Bitmap bmp = Media.getBitmap(this.getContentResolver(), chosenImageUri);

				((ImageView)findViewById(R.id.imageView1)).setImageBitmap(bmp);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				if(	bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)){
					imageData = stream.toByteArray();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void createProfileWithoutImage( String name, String address ){
		
		InsertRowData add = new InsertRowData(Constants.EMPLOYEES);
		
		if( name != null && address==null ){
			add.setValue("name", name);
			MobDB.getInstance().execute( Constants.APP_KEY, add, Constants.ADD_NEW_PROFILE, false, this );
		}else if( address != null && name == null ){
			add.setValue("address", address);
			MobDB.getInstance().execute( Constants.APP_KEY, add, Constants.ADD_NEW_PROFILE, false, this);
		}else{
			add.setValue("name", name);
			add.setValue("address", address);
			MobDB.getInstance().execute( Constants.APP_KEY,add, Constants.ADD_NEW_PROFILE, false, this);
		}

	}

	private void createProfileWithImage( String imageNameWithExtension, byte[] image, String name, String address ){
			
			InsertRowData add = new InsertRowData(Constants.EMPLOYEES);
			add.setValue("name", name);
			add.setValue("address", address);
			add.setValue("photo",imageNameWithExtension, image);
			MobDB.getInstance().execute(Constants.APP_KEY, add, Constants.ADD_NEW_PROFILE, false, this);
	
	}

	@Override
	public void mobDBSuccessResponse() {
		// TODO Auto-generated method stub

		m_ProgressDialog.dismiss();
		
		//Toast.makeText( this, "Profile added!!!", Toast.LENGTH_SHORT ).show();

		Intent activity = new Intent( this, ProfileListActivity .class );

		startActivity( activity );
		
	}

	@Override
	public void mobDBErrorResponse(Integer value, String msg) {
		// TODO Handle error here
		m_ProgressDialog.dismiss();
		Toast.makeText( this, "Profile not added!!!", Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void mobDBResponse(String jsonString) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mobDBFileResponse(String fileName, byte[] fileData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mobDBResponse(Vector result) {
		// TODO Auto-generated method stub
		
	}

	
}
