package com.diligencia.BirthdayReminder;

import java.util.Calendar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Add_new extends Activity {

	private EditText name;
	private Button save, cancel, bdate;
	private ImageButton _ibAddNewPrevious;
	private TextView tvAge, tvBday, tvBdate;
	private Intent sav, canc;

	private int year, byear;
	private int month, bmonth;
	private int day, bday;

	private static final int DATE_DIALOG_ID = 999;

	final Age_DB db = new Age_DB(Add_new.this);

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new);

		AdView adView =  (AdView) this.findViewById (R.id.adView);
		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
		layout.removeView(adView);
		layout.addView(adView);
		AdRequest request = new AdRequest.Builder().build();
		adView.loadAd(request);

		_ibAddNewPrevious = (ImageButton) findViewById(R.id.ibAddNewPrevious);
		_ibAddNewPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent _iBack = new Intent(Add_new.this, MainActivity.class);
				Add_new.this.startActivity(_iBack);
				Add_new.this.finish();
			}
		});
		name = (EditText) findViewById(R.id.txtName);

		tvAge = (TextView) findViewById(R.id.Age);
		tvBday = (TextView) findViewById(R.id.Bday);
		tvBdate = (TextView) findViewById(R.id.Bdate);

		bdate = (Button) findViewById(R.id.bBdate);

		save = (Button) findViewById(R.id.bSave);
		cancel = (Button) findViewById(R.id.bCancel);

		setCurrentDateOnView();
		addListenerOnButton();

		// Error Alert Dialog
		final AlertDialog errorAlertDialog = new AlertDialog.Builder(
				Add_new.this).create();
		errorAlertDialog.setTitle("Error");
		errorAlertDialog.setButton2("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
					}
				});
		// Alert Dialog
		final AlertDialog alertDialog = new AlertDialog.Builder(Add_new.this)
				.create();
		alertDialog.setTitle("Alert");
		alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
			}
		});

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String name2, bdate2, age2, bday2;
				name2 = name.getText().toString();
				bdate2 = tvBdate.getText().toString();
				age2 = tvAge.getText().toString();
				bday2 = tvBday.getText().toString();

				if (validation()) {
					try {
						db.open();
						db.createEntry(name2, bdate2, age2, bday2);
						db.close();

						sav = new Intent(Add_new.this, MainActivity.class);
						sav.putExtra("BYEAR", byear);
						sav.putExtra("BMONTH", bmonth);
						sav.putExtra("BDAY", bday);
						Add_new.this.startActivity(sav);
						Add_new.this.finish();

					} catch (Exception e) {
						String error = e.toString();
						errorAlertDialog.setMessage(error);
						errorAlertDialog.show();
					}
				}
			}

			@SuppressLint("NewApi")
			public boolean validation() {
				boolean validate = false;
				String name2 = name.getText().toString();

				if (name2.length() == 0) {
					alertDialog.setMessage("Name is Mandatory!!");
					alertDialog.show();
				} else if (name2.trim().isEmpty()) {
					alertDialog.setMessage("White Space not allowed.");
					alertDialog.show();
				} else if (name2.length() < 3) {
					alertDialog
							.setMessage("Name must be greater than 3 letters.");
					alertDialog.show();
				} else
					validate = true;
				return validate;
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				canc = new Intent(Add_new.this, MainActivity.class);
				Add_new.this.startActivity(canc);
				Add_new.this.finish();
			}
		});
	}

	public void onBackPressed() {
		Intent _iBack = new Intent(Add_new.this, MainActivity.class);
		Add_new.this.startActivity(_iBack);
		Add_new.this.finish();
	}

	public void setCurrentDateOnView() {

		tvAge = (TextView) findViewById(R.id.Age);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		tvBdate.setText(new StringBuilder().append(day).append("-")
				.append(month + 1).append("-").append(year).append(" "));

		tvAge.setText("0 Year(s), 0 Month(s), 0 Day(s)");
		tvBday.setText("12 Month(s), 0 Day(s)");
	}

	public void addListenerOnButton() {

		// Hiding keyboard after using it on EditText "name"
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromInputMethod(name.getWindowToken(), 0);

		bdate.setOnClickListener(new OnClickListener() {

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
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);
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
			tvBdate.setText(new StringBuilder().append(bday).append("-")
					.append(bmonth + 1).append("-").append(byear));

			// Alert Dialog
			final AlertDialog alertDialog = new AlertDialog.Builder(
					Add_new.this).create();
			alertDialog.setTitle("Alert");
			alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// here you can add functions
				}
			});

			if (year < byear) {
				alertDialog.setMessage("Birth Year cannot be in future!!");
				alertDialog.show();
				tvBdate.setText(new StringBuilder().append(day).append("-")
						.append(month + 1).append("-").append(year).append(" "));
			} else if (year == byear && month < bmonth) {
				alertDialog.setMessage("Birth Month cannot be in future!!");
				alertDialog.show();
				tvBdate.setText(new StringBuilder().append(day).append("-")
						.append(month + 1).append("-").append(year).append(" "));
			} else if (year == byear && month == bmonth && day < bday) {
				alertDialog.setMessage("Birth Day cannot be in future!!");
				alertDialog.show();
				tvBdate.setText(new StringBuilder().append(day).append("-")
						.append(month + 1).append("-").append(year).append(" "));
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
				tvAge.setText(yearr + " " + " Year(s)" + ", " + monthh + " "
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
				} else {
				}
				tvBday.setText(monthh + " " + "Months" + ", " + dayy + " "
						+ "Days");
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.age_menu, menu);
		int i = 1;
		MenuItem hideExit = menu.findItem(R.id.menuExit);
		MenuItem hideAddNew = menu.findItem(R.id.menuAddNew);
		MenuItem hideEdit = menu.findItem(R.id.menuEdit);
		if (i == 1) {
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
		// Error Alert Dialog
		final AlertDialog errorAlertDialog = new AlertDialog.Builder(
				Add_new.this).create();
		errorAlertDialog.setTitle("Error");
		errorAlertDialog.setButton2("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
					}
				});

		switch (item.getItemId()) {
		case R.id.menuSave:

			String name2,
			bdate2,
			age2,
			bday2;
			name2 = name.getText().toString();
			bdate2 = tvBdate.getText().toString();
			age2 = tvAge.getText().toString();
			bday2 = tvBday.getText().toString();

			if (validation()) {
				try {
					db.open();
					db.createEntry(name2, bdate2, age2, bday2);
					db.close();

					sav = new Intent(Add_new.this, MainActivity.class);
					sav.putExtra("BYEAR", byear);
					sav.putExtra("BMONTH", bmonth);
					sav.putExtra("BDAY", bday);
					Add_new.this.startActivity(sav);
					Add_new.this.finish();

				} catch (Exception e) {
					String error = e.toString();
					errorAlertDialog.setMessage(error);
					errorAlertDialog.show();
				}
			}

			return true;
		case R.id.menuCancel:
			canc = new Intent(Add_new.this, MainActivity.class);
			Add_new.this.startActivity(canc);
			Add_new.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public boolean validation() {
		// Alert Dialog
		final AlertDialog alertDialog = new AlertDialog.Builder(Add_new.this)
				.create();
		alertDialog.setTitle("Alert");
		alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
			}
		});

		boolean validate = false;
		String name2 = name.getText().toString();

		if (name2.length() == 0) {
			alertDialog.setMessage("Name is Mandatory!!");
			alertDialog.show();
		} else if (name2.trim().isEmpty()) {
			alertDialog.setMessage("White Space not allowed.");
			alertDialog.show();
		} else if (name2.length() < 3) {
			alertDialog.setMessage("Name must be greater than 3 letters.");
			alertDialog.show();
		} else
			validate = true;
		return validate;
	}
}