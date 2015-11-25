package com.diligencia.BirthdayReminder;

import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SqlView extends Activity {

	private TextView _tviewBdate, _tvAge, _tvBday, _tvSqlViewHeading;
	private EditText _eviewName;
	private Button _bESave, _bECancel, _bEBdate;
	private ImageButton _ibSqlViewPrevious;
	private int id, iyear, imonth, iday;
	private Bundle bundle;
	private Cursor cur;
	private int year, byear;
	private int month, bmonth;
	private int day, bday;
	private String ss, _returnedName, _returnedBdate, _returnedAge,
			_returnedBday;
	private static final int DATE_DIALOG_ID = 999;


	final Age_DB info = new Age_DB(this);
	final Age_DB db = new Age_DB(SqlView.this);
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlview);
		AdView adView =  (AdView) this.findViewById (R.id.adView);
		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
		layout.removeView(adView);
		layout.addView(adView);
		AdRequest request = new AdRequest.Builder().build();
		adView.loadAd(request);
		_ibSqlViewPrevious = (ImageButton) findViewById(R.id.ibSqlViewPrevious);
		_ibSqlViewPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent _iBack = new Intent(SqlView.this, MainActivity.class);
				SqlView.this.startActivity(_iBack);
				SqlView.this.finish();
			}
		});
		
		_eviewName = (EditText) findViewById(R.id.eviewName);
		_tviewBdate = (TextView) findViewById(R.id.viewBdate);
		
		_tvSqlViewHeading= (TextView) findViewById(R.id.tvSqlViewHeading);
		_tvAge = (TextView) findViewById(R.id.tvAge);
		_tvBday = (TextView) findViewById(R.id.tvBday);

		_bEBdate = (Button) findViewById(R.id.bEBdate);
		_bESave = (Button) findViewById(R.id.bESave);
		_bECancel = (Button) findViewById(R.id.bECancel);

		addListenerOnButton();

		bundle = getIntent().getExtras();
		id = bundle.getInt("ID");

		String s = String.valueOf(id);
		long l = Long.parseLong(s);

		info.open();
		cur = info.getContact(l);

		while (cur.moveToNext()) {

			_returnedName = cur.getString(1);
			_eviewName.setText(_returnedName);
			_tvSqlViewHeading.setText(_returnedName+ "'s info");
			_returnedBdate = cur.getString(2);
			_tviewBdate.setText(_returnedBdate);
			ss = _returnedBdate;

			_returnedAge = cur.getString(3);
			_tvAge.setText(_returnedAge);

			_returnedBday = cur.getString(4);
			_tvBday.setText(_returnedBday);
		}

		_bECancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent cancel = new Intent(SqlView.this, MainActivity.class);
				SqlView.this.startActivity(cancel);
				SqlView.this.finish();
			}
		});

	
		// Error Alert Dialog
		final AlertDialog errorAlertDialog = new AlertDialog.Builder(
				SqlView.this).create();
		errorAlertDialog.setTitle("Error");
		errorAlertDialog.setButton2("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
					}
				});

		_bESave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String s = String.valueOf(id);
				final long l = Long.parseLong(s);

				final String _editedName = _eviewName.getText().toString();
				final String _editedBdate = _tviewBdate.getText().toString();
				final String _editedAge = _tvAge.getText().toString();
				final String _editedBday = _tvBday.getText().toString();

				if (validation()) {
					final AlertDialog saveAlertDialog = new AlertDialog.Builder(
							SqlView.this).create();
					saveAlertDialog
							.setMessage("Do you want to save this changes?");
					saveAlertDialog.setButton2("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					saveAlertDialog.setButton3("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									try {

										db.open();
										db.updateContact(l, _editedName,
												_editedBdate, _editedAge,
												_editedBday);
										db.close();

										Intent sav = new Intent(SqlView.this,
												MainActivity.class);
										SqlView.this.startActivity(sav);
										SqlView.this.finish();
									} catch (Exception e) {
										String error = e.toString();
										errorAlertDialog.setMessage(error);
										errorAlertDialog.show();
									}

								}
							});

					saveAlertDialog.show();
				}
			}

			@SuppressLint("NewApi")
			public boolean validation() {
				boolean validate = false;
				String _editedName = _eviewName.getText().toString();

				if (_editedName.length() == 0) {
					errorAlertDialog.setMessage("Name is Mandatory!!");
					errorAlertDialog.show();
				} else if (_editedName.trim().isEmpty()) {
					errorAlertDialog.setMessage("White Space not allowed.");
					errorAlertDialog.show();

				} else if (_editedName.length() < 3) {
					errorAlertDialog
							.setMessage("Name must be greater than 2 letters.");
					errorAlertDialog.show();
				} else
					validate = true;
				return validate;
			}
		});
		String[] fields = ss.split("[ \\-]");

		iday = (int) Integer.parseInt(fields[0]);
		imonth = (int) Integer.parseInt(fields[1]);
		iyear = (int) Integer.parseInt(fields[2]);

	}

	public void onBackPressed() {
		Intent _iBack = new Intent(SqlView.this, MainActivity.class);
		SqlView.this.startActivity(_iBack);
		SqlView.this.finish();
	}

	public void addListenerOnButton() {
		// Current Date
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		_bEBdate.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, iyear,
					imonth - 1, iday);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@SuppressWarnings("deprecation")
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			byear = selectedYear;
			bmonth = selectedMonth;
			bday = selectedDay;

			// set selected date into textview
			_tviewBdate.setText(new StringBuilder().append(bday).append("-")
					.append(bmonth + 1).append("-").append(byear).append(" "));

			// Alert Dialog
			AlertDialog alertDialog = new AlertDialog.Builder(SqlView.this)
					.create();
			alertDialog.setTitle("Alert");
			alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// here you can add functions
				}
			});

			if (year < byear) {
				alertDialog.setMessage("Birth Year cannot be in future!!");
				alertDialog.show();
				_tviewBdate.setText(ss);
			} else if (year == byear && month < bmonth) {
				alertDialog.setMessage("Birth Month cannot be in future!!");
				alertDialog.show();
			} else if (year == byear && month == bmonth && day < bday) {
				alertDialog.setMessage("Birth Day cannot be in future!!");
				alertDialog.show();
			} else {
				int monthh = 0, dayy = 0, yearr = 0;
				yearr = year - byear;
				if (month >= bmonth)
					monthh = month - bmonth;
				else {
					yearr--;
					monthh = 12 + month - bmonth;
				}
				if (day >= bday)
					dayy = day - bday;
				else {
					monthh--;
					dayy = 30 + day - bday;
					if (monthh < 0) {
						monthh = 11;
						yearr--;
					}
				}
				_tvAge.setText(yearr + " " + " Year(s)" + ", " + monthh + " "
						+ "Month(s)" + ", " + dayy + " " + "Day(s)");

				// Next Birthday Calculation

				if (bmonth > month)
					monthh = bmonth - month;
				else {
					yearr--;
					monthh = 12 + bmonth - month;
				}

				if (bday >= day)
					dayy = bday - day;
				else {
					monthh--;
					dayy = 30 + bday - day;
					if (monthh < 0) {
						monthh = 11;
						yearr--;
					}
				}

				if (bmonth == month && bday == day) {
					monthh = 12;
				}
				_tvBday.setText(monthh + " " + "Months" + ", " + dayy
						+ " " + "Days");
			}
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.age_menu, menu);
		int i = 1;
		MenuItem hideExit = menu.findItem(R.id.menuExit);
		MenuItem hideAddNew = menu.findItem(R.id.menuAddNew);
		MenuItem hideEdit = menu.findItem(R.id.menuEdit);	
		if(i == 1){
			hideExit.setVisible(false);
			hideAddNew.setVisible(false);
			hideEdit.setVisible(false);
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menuSave:
	        	// Error Alert Dialog
	    		final AlertDialog errorAlertDialog = new AlertDialog.Builder(
	    				SqlView.this).create();
	    		errorAlertDialog.setTitle("Error");
	    		errorAlertDialog.setButton2("OK",
	    				new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog, int which) {
	    						// here you can add functions
	    					}
	    				});
	        	String s = String.valueOf(id);
				final long l = Long.parseLong(s);

				final String _editedName = _eviewName.getText().toString();
				final String _editedBdate = _tviewBdate.getText().toString();
				final String _editedAge = _tvAge.getText().toString();
				final String _editedBday = _tvBday.getText().toString();

				if (validation()) {
					final AlertDialog saveAlertDialog = new AlertDialog.Builder(
							SqlView.this).create();
					saveAlertDialog
							.setMessage("Do you want to save this changes?");
					saveAlertDialog.setButton2("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					saveAlertDialog.setButton3("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									try {

										db.open();
										db.updateContact(l, _editedName,
												_editedBdate, _editedAge,
												_editedBday);
										db.close();

										Intent sav = new Intent(SqlView.this,
												MainActivity.class);
										SqlView.this.startActivity(sav);
										SqlView.this.finish();
									} catch (Exception e) {
										String error = e.toString();
										errorAlertDialog.setMessage(error);
										errorAlertDialog.show();
									}

								}
							});

					saveAlertDialog.show();
				
			}

	            return true;
	        case R.id.menuCancel:
	        	Intent cancel = new Intent(SqlView.this, MainActivity.class);
				SqlView.this.startActivity(cancel);
				SqlView.this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public boolean validation() {
		// Error Alert Dialog
				final AlertDialog errorAlertDialog = new AlertDialog.Builder(
						SqlView.this).create();
				errorAlertDialog.setTitle("Error");
				errorAlertDialog.setButton2("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// here you can add functions
							}
						});
				
		boolean validate = false;
		String _editedName = _eviewName.getText().toString();

		if (_editedName.length() == 0) {
			errorAlertDialog.setMessage("Name is Mandatory!!");
			errorAlertDialog.show();
		} else if (_editedName.trim().isEmpty()) {
			errorAlertDialog.setMessage("White Space not allowed.");
			errorAlertDialog.show();

		} else if (_editedName.length() < 3) {
			errorAlertDialog
					.setMessage("Name must be greater than 2 letters.");
			errorAlertDialog.show();
		} else
			validate = true;
		return validate;
	}
}