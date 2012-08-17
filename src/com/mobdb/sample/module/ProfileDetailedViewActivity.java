package com.mobdb.sample.module;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobdb.android.DeleteRowData;
import com.mobdb.android.MobDB;
import com.mobdb.android.MobDBResponseListener;
import com.mobdb.android.UpdateRowData;
import com.mobdb.module.R;
import com.mobdb.sample.constants.Constants;

public class ProfileDetailedViewActivity extends Activity implements OnClickListener, MobDBResponseListener{

	byte[] imageData;
	Bitmap initImageBit;
	Button edit,delete,update,done;
	ImageView imgView;
	TextView nameTxtView, addressTxtView;
	Bundle valuesFromLists;
	String initName, initAddress;
	boolean isDelelet, isUpdate;
	boolean fromGallery = false;
	private ProgressDialog m_ProgressDialog = null;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		// TODO Auto-generated method stub

		super.onCreate( savedInstanceState );
		setContentView( R.layout.detailayout );	
		update = ( Button )findViewById( R.id.update );
		delete = ( Button )findViewById( R.id.Delete );	
		imgView = ( ImageView )findViewById( R.id.imageView1 );

		imgView.setOnClickListener( this );
		delete.setOnClickListener( this );
		update.setOnClickListener( this );		

	}

	private void setProfileValues(){

		valuesFromLists = getIntent().getExtras();
		initName = ( String )valuesFromLists.getString( "name" ).trim();
		initAddress = ( String )valuesFromLists.getString( "address" ).trim();
		( ( EditText )findViewById( R.id.Name ) ).setText( initName );
		( ( EditText )findViewById( R.id.Address ) ).setText( initAddress );

		initImageBit = ( ( Bitmap )valuesFromLists.get( "photo" ) );

		if( initImageBit != null ){

			( ( ImageView )findViewById( R.id.imageView1 ) ).setImageBitmap( initImageBit );

		}

	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		super.onStart();

		if( !fromGallery ){

			setProfileValues();

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		fromGallery = false;

		if( v.getId() == R.id.Delete ){

			isDelelet = true;
			isUpdate = false;

			m_ProgressDialog = ProgressDialog.show( this, "",
					"Deleting Profile from Employees table...", true );

			deleteRequest(initName, initAddress);

		}else if( v.getId() == R.id.update ){

			isDelelet = false;
			isUpdate = true;

			Log.i(Constants.TAG, "Update pressed!!!!");
			String nameEditText =  ( ( EditText )findViewById( R.id.Name ) ).getText().toString().trim() ;
			String addressEditText =  ( ( EditText )findViewById( R.id.Address ) ).getText().toString().trim() ;	

			if(  imageData == null && initName.equals( nameEditText ) && initAddress.equals( addressEditText ) ){

				return;

			}else if(  imageData == null && initName.equals( nameEditText ) && !initAddress.equals( addressEditText ) ){
				m_ProgressDialog = ProgressDialog.show( this, "",
						"Updating employee profile...", true );
				createAddressUpdateRequest( addressEditText);

			}else if( imageData == null && !initName.equals( nameEditText ) && initAddress.equals( addressEditText ) ){
				m_ProgressDialog = ProgressDialog.show( this, "",
						"Updating employee profile...", true );
				createNameUpdateRequest( nameEditText );

			}else if( imageData == null && !initName.equals( nameEditText ) && !initAddress.equals( addressEditText ) ){
				m_ProgressDialog = ProgressDialog.show( this, "",
						"Updating employee profile...", true );
				createNameAndAddressUpdateRequest(nameEditText, addressEditText) ;

			}else if( imageData != null ){

				m_ProgressDialog = ProgressDialog.show( this, "",
						"Updating employee profile...", true );



				editProfile( "image.png",imageData,nameEditText,addressEditText);

			}	

		}else if(v.getId() == R.id.imageView1){

			Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);

			photoPickerIntent.setType("image/*");

			startActivityForResult(photoPickerIntent, 1);

		}

	}

	
	protected void onActivityResult( int requestCode, int resultCode, Intent data )
	{

		super.onActivityResult(requestCode, resultCode, data);

		if ( resultCode == RESULT_OK )
		{

			try {

				Uri chosenImageUri = data.getData();		

				Bitmap bmp = Media.getBitmap(this.getContentResolver(), chosenImageUri);

				( ( ImageView )findViewById( R.id.imageView1 ) ).setImageBitmap( bmp );

				ByteArrayOutputStream stream = new ByteArrayOutputStream();

				bmp.compress( Bitmap.CompressFormat.PNG, 100, stream );

				imageData = stream.toByteArray();

				fromGallery = true;

			} catch ( FileNotFoundException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}

	}

	private void deleteRequest(String name, String address){
		
		DeleteRowData del = new DeleteRowData(Constants.EMPLOYEES);
		
		if( ( name == null || name.trim().equals( "" ) ) && ( address != null ) ){
		
			del.whereEqualsTo("address", address);
		

		}else if( ( address == null || address.trim().equals( "" ) ) && ( name != null ) ){

			del.whereEqualsTo("name", name);
			

		}else{
			
			del.whereEqualsTo("name", name);
			del.andEqualsTo("address", address);
			
		}
		
		MobDB.getInstance().execute(Constants.APP_KEY, del, Constants.DELETED_PROFILE, false, this);

		
	}

	private void createNameUpdateRequest( String update_name ){				
		
		UpdateRowData update = new UpdateRowData( Constants.EMPLOYEES );
		update.setValue( "name", update_name );
		update.whereEqualsTo( "address", initAddress );
		MobDB.getInstance().execute( Constants.APP_KEY,update, Constants.UPDATE_PROFILE, false, this );
	
	}

	private void createNameAndAddressUpdateRequest( String update_name, String update_address ){				
		
		UpdateRowData update = new UpdateRowData( Constants.EMPLOYEES );
		update.setValue( "name", update_name );
		update.setValue( "address", update_address );
		update.whereEqualsTo( "name", initName );
		update.andEqualsTo("address", initAddress);
		MobDB.getInstance().execute( Constants.APP_KEY,update, Constants.UPDATE_PROFILE, false, this );
		
	}

	private void createAddressUpdateRequest( String update_address ){				

		UpdateRowData update = new UpdateRowData( Constants.EMPLOYEES );
		update.setValue( "address", update_address );
		update.whereEqualsTo( "name", initName );
		MobDB.getInstance().execute( Constants.APP_KEY, update, Constants.UPDATE_PROFILE, false, this);
	
	}

	private void editProfile( String imageNameWithExtension, byte[] image, String name, String address ){

		try {

			UpdateRowData update = new UpdateRowData( Constants.EMPLOYEES );
			update.setValue( "name", name );
			update.setValue( "address", address );
			update.setValue( "photo",imageNameWithExtension, image );
			update.whereEqualsTo( "name", name );
			update.andEqualsTo("address", address);
			
			MobDB.getInstance().execute( Constants.APP_KEY, update, Constants.ADD_NEW_PROFILE, false, this);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Override
	public void mobDBSuccessResponse() {
		// TODO Auto-generated method stub

		m_ProgressDialog.dismiss();

		//Toast.makeText( this, isDelelet==true?"Deleted!!!":"Updated!!!", Toast.LENGTH_SHORT ).show();

		Intent activity = new Intent( this, ProfileListActivity .class );

		startActivity( activity );

	}



	@Override
	public void mobDBErrorResponse(Integer value, String msg) {
		// TODO Handle error here
		m_ProgressDialog.dismiss();
		Toast.makeText( this, isDelelet==true?"Not Deleted!!!":"Not Updated!!!", Toast.LENGTH_SHORT ).show();

	}

	@Override
	public void mobDBResponse(String xmlfile) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void mobDBFileResponse(String fileName, byte[] fileData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mobDBResponse(Vector<HashMap<String, Object[]>> result) {
		// TODO Auto-generated method stub

	}

}
