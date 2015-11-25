package com.diligencia.BirthdayReminder;

import java.util.Calendar;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ImageButton _bAddNew;
	private Intent add;
	private Cursor cur;
	private TextView _tvDynamicName, _tvDynamicBdate, _tvDynamicEdit,
			_tvDynamicDelete, _tvDynamicCake, _tvClickAdd;
	private ViewGroup _tl;
	private TableRow _dynamicrow;
	private int count = 0, lm;
	private final int _pid1[] = new int[100];
	private final String _s1[] = new String[100];
	private String sss;
	private int year, byear;
	private int month, bmonth;
	private int day, bday;

	final Age_DB info = new Age_DB(this);

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		AdView adView =  (AdView) this.findViewById (R.id.adView);
		LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
		layout.removeView(adView);
        layout.addView(adView);
		AdRequest request = new AdRequest.Builder().build();
		adView.loadAd(request);

		_tvClickAdd = (TextView) findViewById(R.id.tvClickAdd);
		
		_bAddNew = (ImageButton) findViewById(R.id.bAddNew);
		_bAddNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				add = new Intent(MainActivity.this, Add_new.class);
				MainActivity.this.startActivity(add);
				MainActivity.this.finish();
			}
		});

		final Display dis = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		final int wid = ((dis.getWidth()) / (5));

		// final Age_DB info = new Age_DB(this);
		info.open();
		_tl = (TableLayout) findViewById(R.id.tl);
		_dynamicrow = new TableRow(this);

		_tvDynamicCake = new TextView(this);
		_tvDynamicCake.setText("");
		_tvDynamicCake.setTextSize(17);
		_tvDynamicCake.setTextColor(Color.parseColor("#DC143C"));
		_tvDynamicCake.setTypeface(null, Typeface.BOLD);
		_tvDynamicCake.setWidth(wid - 40);
		_tvDynamicCake.setPadding(5, 5, 0, 5);
		_tvDynamicCake.setVisibility(View.GONE);
		_dynamicrow.addView(_tvDynamicCake);

		_tvDynamicName = new TextView(this);
		_tvDynamicName.setText("Name");
		_tvDynamicName.setTextSize(17);
		_tvDynamicName.setTextColor(Color.parseColor("#DC143C"));
		_tvDynamicName.setTypeface(null, Typeface.BOLD);
		_tvDynamicName.setWidth(wid+40);
		_tvDynamicName.setPadding(5, 5, 0, 5);
		_tvDynamicName.setVisibility(View.GONE);
		_dynamicrow.addView(_tvDynamicName);

		_tvDynamicBdate = new TextView(this);
		_tvDynamicBdate.setText("B'Date");
		_tvDynamicBdate.setTextSize(17);
		_tvDynamicBdate.setWidth(wid + 20);
		_tvDynamicBdate.setTextColor(Color.parseColor("#DC143C"));
		_tvDynamicBdate.setTypeface(null, Typeface.BOLD);
		_tvDynamicBdate.setPadding(5, 5, 0, 5);
		_tvDynamicBdate.setVisibility(View.GONE);
		_dynamicrow.addView(_tvDynamicBdate);

		_tvDynamicEdit = new TextView(this);
		_tvDynamicEdit.setText("Edit");
		_tvDynamicEdit.setTextSize(17);
		_tvDynamicEdit.setWidth(wid - 10);
		_tvDynamicEdit.setTextColor(Color.parseColor("#DC143C"));
		_tvDynamicEdit.setTypeface(null, Typeface.BOLD);
		_tvDynamicEdit.setPadding(5, 5, 0, 5);
		_tvDynamicEdit.setVisibility(View.GONE);
		_dynamicrow.addView(_tvDynamicEdit);

		_tvDynamicDelete = new TextView(this);
		_tvDynamicDelete.setText("Del");
		_tvDynamicDelete.setTextSize(17);
		_tvDynamicDelete.setWidth(wid - 10);
		_tvDynamicDelete.setTextColor(Color.parseColor("#DC143C"));
		_tvDynamicDelete.setTypeface(null, Typeface.BOLD);
		_tvDynamicDelete.setPadding(5, 5, 5, 5);
		_tvDynamicDelete.setVisibility(View.GONE);
		_dynamicrow.addView(_tvDynamicDelete);

		_tl.addView(_dynamicrow);

		// Error Alert Dialog
		final AlertDialog errorAlertDialog = new AlertDialog.Builder(
				MainActivity.this).create();
		errorAlertDialog.setTitle("Error");
		errorAlertDialog.setButton2("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
					}
				});

		cur = info.getAllContact();
		while (cur.moveToNext()) {
			final int pid = cur.getInt(0);
			String pname = cur.getString(1);
			String pbdate = cur.getString(2);

			final TableRow _dynamicrow2[] = new TableRow[pid];
			final TextView _tvDynamicName2[] = new TextView[pid];
			final TextView _tvDynamicBdate2[] = new TextView[pid];
			final ImageButton _tvDynamicEdit2[] = new ImageButton[pid];
			final ImageButton _tvDynamicDelete2[] = new ImageButton[pid];
			final ImageView _tvDynamicCake2[] = new ImageView[pid];
			final int _pid2[] = new int[pid];

			if (count < pid) {
				_dynamicrow2[count] = new TableRow(this);
				_dynamicrow2[count].setId(count + 1);
				_pid2[count] = pid;

				_tvDynamicCake2[count] = new ImageView(this);
				_tvDynamicCake2[count].setId(count);
				_tvDynamicCake2[count].setMaxWidth(wid - 40);
				_tvDynamicCake2[count].setPadding(5, 5, 0, 5);
				_dynamicrow2[count].addView(_tvDynamicCake2[count]);

				_tvDynamicName2[count] = new TextView(this);
				_tvDynamicName2[count].setText(pname);
				_tvDynamicName2[count].setId(count);
				_tvDynamicName2[count].setTextSize(15);
				_tvDynamicName2[count]
						.setTextColor(Color.parseColor("#800080"));
				_tvDynamicName2[count].setWidth(wid + 40);
				_tvDynamicName2[count].setPadding(5, 5, 0, 5);
				_dynamicrow2[count].addView(_tvDynamicName2[count]);

				_tvDynamicBdate2[count] = new TextView(this);
				_tvDynamicBdate2[count].setText(pbdate);
				_tvDynamicBdate2[count].setId(count);
				_tvDynamicBdate2[count].setTextSize(15);
				_tvDynamicBdate2[count].setTextColor(Color
						.parseColor("#800080"));
				_tvDynamicBdate2[count].setWidth(wid + 20);
				_tvDynamicBdate2[count].setPadding(5, 5, 0, 5);
				_dynamicrow2[count].addView(_tvDynamicBdate2[count]);

				_tvDynamicEdit2[count] = new ImageButton(this);
				_tvDynamicEdit2[count].setImageResource(R.drawable.user_edit);
				_tvDynamicEdit2[count].setBackgroundColor(Color
						.parseColor("#ffe4e1"));
				_tvDynamicEdit2[count].setId(count);
				_tvDynamicEdit2[count].setMaxWidth(wid - 10);
				_tvDynamicEdit2[count].setPadding(5, 5, 0, 5);
				_dynamicrow2[count].addView(_tvDynamicEdit2[count]);

				_tvDynamicDelete2[count] = new ImageButton(this);
				_tvDynamicDelete2[count]
						.setImageResource(R.drawable.delete_user);
				_tvDynamicDelete2[count].setBackgroundColor(Color
						.parseColor("#ffe4e1"));
				_tvDynamicDelete2[count].setId(count);
				_tvDynamicDelete2[count].setMaxWidth(wid - 10);
				_tvDynamicDelete2[count].setPadding(5, 5, 5, 5);
				_dynamicrow2[count].addView(_tvDynamicDelete2[count]);

				_tl.addView(_dynamicrow2[count]);
				_tvClickAdd.setVisibility(View.GONE);
				
				_pid1[count + 1] = _pid2[count];
				_s1[count + 1] = _tvDynamicName2[count].getText().toString();
				registerForContextMenu(_dynamicrow2[count]);

				_tvDynamicEdit2[count]
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent i = new Intent(MainActivity.this,
										SqlView.class);
								i.putExtra("ID", pid);
								MainActivity.this.startActivity(i);
								MainActivity.this.finish();
							}
						});
				final String s1 = _tvDynamicName2[count].getText().toString();
				_tvDynamicDelete2[count]
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								final AlertDialog deleteAlertDialog = new AlertDialog.Builder(
										MainActivity.this).create();
								// deleteAlertDialog.setTitle("Are you sure?");
								deleteAlertDialog
										.setMessage("Do you want to delete "
												+ s1 + "?");
								deleteAlertDialog.setButton2("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										});
								deleteAlertDialog.setButton3("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												String s = String.valueOf(pid);
												long l = Long.parseLong(s);

												try {
													info.open();
													info.deleteContact(l);
													info.close();

													Intent del = new Intent(
															MainActivity.this,
															MainActivity.class);
													MainActivity.this
															.startActivity(del);
													MainActivity.this.finish();
												} catch (Exception e) {
													String error = e.toString();
													errorAlertDialog
															.setMessage(error);
													errorAlertDialog.show();
												}
											}
										});
								deleteAlertDialog.show();
							}
						});
			}
			String ss = _tvDynamicBdate2[count].getText().toString();
			String[] fields = ss.split("[ \\-]");

			bday = (int) Integer.parseInt(fields[0]);
			bmonth = (int) Integer.parseInt(fields[1]);
			byear = (int) Integer.parseInt(fields[2]);

			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			if (day == bday && year >= byear && (bmonth - 1) == month) {

				Animation anim = new AlphaAnimation(0.0f, 1.0f);
				anim.setDuration(400);
				anim.setStartOffset(20);
				anim.setRepeatMode(Animation.REVERSE);
				anim.setRepeatCount(Animation.INFINITE);
				_tvDynamicName2[count].startAnimation(anim);

				_tvDynamicCake2[count].setImageResource(R.drawable.cake3);
				_dynamicrow2[count].setBackgroundColor(Color
						.parseColor("#FFB6C1"));
				_tvDynamicName2[count]
						.setTextColor(Color.parseColor("#C71585"));
				_tvDynamicName2[count].setTypeface(null, Typeface.BOLD);
				_tvDynamicName2[count].setTextSize(16);
				_tvDynamicName2[count]
						.setText(pname + "\n" + "Happy Birthday!");
				_tvDynamicBdate2[count].setTextColor(Color
						.parseColor("#C71585"));
				_tvDynamicBdate2[count].setTypeface(null, Typeface.BOLD);
				_tvDynamicBdate2[count].setTextSize(16);

				_tvDynamicEdit2[count].setBackgroundColor(Color
						.parseColor("#FFB6C1"));
				_tvDynamicDelete2[count].setBackgroundColor(Color
						.parseColor("#FFB6C1"));

			}
			_tvDynamicName.setVisibility(View.VISIBLE);
			_tvDynamicBdate.setVisibility(View.VISIBLE);
			_tvDynamicEdit.setVisibility(View.VISIBLE);
			_tvDynamicDelete.setVisibility(View.VISIBLE);
			_tvDynamicCake.setVisibility(View.VISIBLE);
			count++;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, v.getId(), 0, "View");
		menu.add(0, v.getId(), 0, "Edit");
		menu.add(0, v.getId(), 0, "Delete");

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "View") {
			funView(item.getItemId());
		} else if (item.getTitle() == "Edit") {
			funEdit(item.getItemId());
		} else if (item.getTitle() == "Delete") {
			funDelete(item.getItemId());
		} else {
			return false;
		}
		return true;
	}

	private void funView(int itemId) {
		// TODO Auto-generated method stub
		lm = _pid1[itemId];
		// Toast.makeText(getApplicationContext(), "pid " + lm,
		// Toast.LENGTH_LONG).show();
		Intent i = new Intent(MainActivity.this, Bday_View.class);
		i.putExtra("ID", lm);
		MainActivity.this.startActivity(i);
		MainActivity.this.finish();
	}

	private void funEdit(int itemId) {
		// TODO Auto-generated method stub
		lm = _pid1[itemId];
		// Toast.makeText(getApplicationContext(), "pid " + lm,
		// Toast.LENGTH_LONG).show();
		Intent i = new Intent(MainActivity.this, SqlView.class);
		i.putExtra("ID", lm);
		MainActivity.this.startActivity(i);
		MainActivity.this.finish();
	}

	@SuppressWarnings("deprecation")
	private void funDelete(int itemId) {
		// TODO Auto-generated method stub
		lm = _pid1[itemId];
		sss = _s1[itemId];
		// Error Alert Dialog
		final AlertDialog errorAlertDialog = new AlertDialog.Builder(
				MainActivity.this).create();
		errorAlertDialog.setTitle("Error");
		errorAlertDialog.setButton2("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// here you can add functions
					}
				});

		final AlertDialog deleteAlertDialog = new AlertDialog.Builder(
				MainActivity.this).create();
		// deleteAlertDialog.setTitle("Are you sure?");
		deleteAlertDialog.setMessage("Do you want to delete " + sss + "?");
		deleteAlertDialog.setButton2("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		deleteAlertDialog.setButton3("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String s = String.valueOf(lm);
						long l = Long.parseLong(s);

						try {
							info.open();
							info.deleteContact(l);
							info.close();

							Intent del = new Intent(MainActivity.this,
									MainActivity.class);
							MainActivity.this.startActivity(del);
							MainActivity.this.finish();
						} catch (Exception e) {
							String error = e.toString();
							errorAlertDialog.setMessage(error);
							errorAlertDialog.show();
						}
					}
				});
		deleteAlertDialog.show();
	}

	public void onBackPressed() {
		onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.age_menu, menu);
		int i = 1;
		MenuItem hideSave = menu.findItem(R.id.menuSave);
		MenuItem hideCancel = menu.findItem(R.id.menuCancel);
		MenuItem hideEdit = menu.findItem(R.id.menuEdit);
		if (i == 1) {
			hideSave.setVisible(false);
			hideCancel.setVisible(false);
			hideEdit.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menuAddNew:
			add = new Intent(MainActivity.this, Add_new.class);
			MainActivity.this.startActivity(add);
			MainActivity.this.finish();
			return true;
		case R.id.menuExit:
			onDestroy();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		finish();
	}
}