package com.mobdb.sample.module;

import java.util.ArrayList;

import com.mobdb.module.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileAdapter extends ArrayAdapter<Profiles>  {

	private ArrayList<Profiles> persons;
	Context context;
	LayoutInflater inflater;

	public ProfileAdapter( Context context, int textViewResourceId, ArrayList<Profiles> persons ) {
		super( context, textViewResourceId, persons );
		this.context = context;
		this.persons = persons;
		inflater = ( LayoutInflater )this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	}
	
	public static class ViewHolder{
		public String id;
		public String name;
		public String address;
		public Bitmap image;
	}
	
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {

		View v = convertView;
		ViewHolder holder;
		Profiles o;
		
		if ( v == null ) {

			v = inflater.inflate( R.layout.row, null );
			holder = new ViewHolder();
			o = persons.get( position );

			if ( o != null ) {
				
				(( TextView ) v.findViewById( R.id.toptext )).setText(o.getName());
				(( TextView ) v.findViewById( R.id.bottomtext )).setText(o.getAddress());
				
				holder.id 	   = o.getId();
				holder.name    = o.getName();
				holder.address = o.getAddress();
							
				if( o.getImage() != null ){
					//(( ImageView ) v.findViewById( R.id.icon )).setImageBitmap(o.getImage());
					holder.image = o.getImage();
				}else{					
					holder.image = null;					
				}
			}

			v.setTag(holder);

		}else{

			holder=(ViewHolder)v.getTag();

			o = persons.get( position );
			(( TextView ) v.findViewById( R.id.toptext )).setText(o.getName());
			(( TextView ) v.findViewById( R.id.bottomtext )).setText(o.getAddress());
			
			if( o.getImage() != null ){
				
				(( ImageView ) v.findViewById( R.id.icon )).setImageBitmap(o.getImage());
				holder.image = o.getImage();
				
			}else{
				
				(( ImageView ) v.findViewById( R.id.icon )).setImageResource(R.drawable.icon);
				holder.image = null;
				
			}

		}

		return v;

	}

}
