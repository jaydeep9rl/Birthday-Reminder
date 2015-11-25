package com.diligencia.BirthdayReminder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Bday_View extends Activity {

	private TextView _tvViewBdate, _tvViewAge, _tvViewBday, _tvViewName, _tvViewHeading;
	private ImageButton _ibViewPrevious;
	private int id;
	private Bundle bundle;
	private Cursor cur;
	private String _returnedName, _returnedBdate, _returnedAge,
			_returnedBday;
	final Age_DB info = new Age_DB(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bday_view);
		AdView adView =  (AdView) this.findViewById (R.id.adView);
		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
		layout.removeView(adView);
		layout.addView(adView);
		AdRequest request = new AdRequest.Builder().build();
		adView.loadAd(request);
		_ibViewPrevious = (ImageButton) findViewById(R.id.ibViewPrevious);
		_ibViewPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent _iBack = new Intent(Bday_View.this, MainActivity.class);
				Bday_View.this.startActivity(_iBack);
				Bday_View.this.finish();
			}
		});
		_tvViewHeading = (TextView) findViewById(R.id.tvViewHeading);
		_tvViewName = (TextView) findViewById(R.id.tvViewName);
		_tvViewBdate = (TextView) findViewById(R.id.tvViewBdate);
		_tvViewAge = (TextView) findViewById(R.id.tvViewAge);
		_tvViewBday = (TextView) findViewById(R.id.tvViewBday);
		
		bundle = getIntent().getExtras();
		id = bundle.getInt("ID");

		String s = String.valueOf(id);
		long l = Long.parseLong(s);

		info.open();
		cur = info.getContact(l);

		while (cur.moveToNext()) {

			_returnedName = cur.getString(1);
			_tvViewName.setText(_returnedName);
			_tvViewHeading.setText(_returnedName + "'s Profile");
			
			_returnedBdate = cur.getString(2);
			_tvViewBdate.setText(_returnedBdate);
			
			_returnedAge = cur.getString(3);
			_tvViewAge.setText(_returnedAge);

			_returnedBday = cur.getString(4);
			_tvViewBday.setText(_returnedBday);
		}
	}

	public void onBackPressed() {
		Intent _iBack = new Intent(Bday_View.this, MainActivity.class);
		Bday_View.this.startActivity(_iBack);
		Bday_View.this.finish();
	}
		@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.age_menu, menu);
		int i = 1;
		MenuItem hideExit = menu.findItem(R.id.menuExit);
		MenuItem hideAddNew = menu.findItem(R.id.menuAddNew);
		MenuItem hideSave = menu.findItem(R.id.menuSave);
		if(i == 1){
			hideExit.setVisible(false);
			hideAddNew.setVisible(false);
			hideSave.setVisible(false);
		}
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menuEdit:
	        	Intent _iEdit = new Intent(Bday_View.this, SqlView.class);
	        	_iEdit.putExtra("ID", id);
	        	Bday_View.this.startActivity(_iEdit);
				Bday_View.this.finish();
	            return true;
	        case R.id.menuCancel:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}