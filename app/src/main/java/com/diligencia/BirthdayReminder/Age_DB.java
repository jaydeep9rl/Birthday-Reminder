package com.diligencia.BirthdayReminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View.OnClickListener;

public class Age_DB {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_BDATE = "bdate";
	public static final String KEY_AGE = "age";
	public static final String KEY_BDAY = "bday";
	
	public static final String TAG = "Age_DB";
	
	private static final String DATABASE_NAME = "BirthdayReminderDB";
	private static final String DATABASE_TABLE = "birthdays";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE =
			"create table birthdays (_id integer primary key autoincrement, "
					+ "name text not null, bdate text not null,age text not null, bday text not null);";
	
	private DbHelper ageHelper;
	private final Context ageContext;
	private SQLiteDatabase ageDatabse;
	
	
	public Age_DB(Context c){
		this.ageContext = c;
		ageHelper = new DbHelper(ageContext);
	}
	
	public Age_DB(OnClickListener onClickListener) {
		// TODO Auto-generated constructor stub
		this.ageContext = (Context) onClickListener;
		ageHelper = new DbHelper(ageContext);
	}

	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try{
				db.execSQL(DATABASE_CREATE);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	public Age_DB open() throws SQLException {
		ageDatabse = ageHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		ageHelper.close();
	}

	public long createEntry(String name1, String bdate1, String age1,
			String bday1) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		
		cv.put(KEY_NAME, name1);
		cv.put(KEY_BDATE, bdate1);
		cv.put(KEY_AGE, age1);
		cv.put(KEY_BDAY, bday1);
		
		return ageDatabse.insert(DATABASE_TABLE, null, cv);
		}

	public Cursor getAllContact() {
		// TODO Auto-generated method stub
		Cursor c = ageDatabse.query(DATABASE_TABLE, null, null, null, null, null, null);

		return c;
	}
	public void deleteContact(long l) throws SQLException {
		// TODO Auto-generated method stub
		ageDatabse.delete(DATABASE_TABLE, KEY_ROWID + "=" + l, null);
	}
	public void updateContact(long l, String _editedName, String _editedBdate, String _editedAge, String _editedBday) throws SQLException {
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_NAME, _editedName);
		cvUpdate.put(KEY_BDATE, _editedBdate);
		cvUpdate.put(KEY_AGE, _editedAge);
		cvUpdate.put(KEY_BDAY, _editedBday);
		
		ageDatabse.update(DATABASE_TABLE,cvUpdate, KEY_ROWID + "=" + l, null);
	}
	public Cursor getContact (long row_id) throws SQLException {
		Cursor c = ageDatabse.query(DATABASE_TABLE, null, KEY_ROWID + "=" + row_id, null, null, null, null);
	return c;
	}
}