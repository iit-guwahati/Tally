package com.bhargav.tablayout;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SummaryDatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "SummaryManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "SummaryContacts";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_AMOUNT = "amount";
	

	public SummaryDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_NAME + " TEXT,"		
				+ KEY_AMOUNT + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	void addContact(SummaryContact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getName());
		values.put(KEY_AMOUNT, contact.getAmount()); 

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}
	//Integer.parseInt(cursor.getString(2)) + Integer.parseInt(value)
	
	void add(String friend, String value){
		SQLiteDatabase db = this.getWritableDatabase();		
		String selectQuery = "SELECT * FROM " +TABLE_CONTACTS+ " WHERE "+KEY_NAME+" = " + "'"+friend+"'";
		Cursor cursor = db.rawQuery(selectQuery, null);
	            // looping through all rows and adding to list
	            if (cursor.moveToFirst()) {
	                do {
	                	SummaryContact contact = getContact(Integer.parseInt(cursor.getString(0)));	
	            		ContentValues values = new ContentValues();
	            		values.put(KEY_NAME, contact.getName());
	            		values.put(KEY_AMOUNT, String.valueOf(Integer.valueOf(value) + Integer.valueOf(contact.getAmount())));
	            		db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
	                } while (cursor.moveToNext());
	            }
		db.close(); // Closing database connection
	}
	
	void ClearAll(){
		SQLiteDatabase db = this.getWritableDatabase();		
		String selectQuery = "SELECT * FROM " +TABLE_CONTACTS;
		Cursor cursor = db.rawQuery(selectQuery, null);
	            // looping through all rows and adding to list
	            if (cursor.moveToFirst()) {
	                do {
	                	SummaryContact contact = getContact(Integer.parseInt(cursor.getString(0)));	
	            		ContentValues values = new ContentValues();
	            		values.put(KEY_NAME, contact.getName());
	            		values.put(KEY_AMOUNT, "0");
	            		db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
	                } while (cursor.moveToNext());
	            }
		db.close(); // Closing database connection
	}
	
	void ClearOne(int id){
		SQLiteDatabase db = this.getWritableDatabase();		
		String selectQuery = "SELECT * FROM " +TABLE_CONTACTS + " WHERE "+ KEY_ID +" = " + "'"+String.valueOf(id)+"'";
		Cursor cursor = db.rawQuery(selectQuery, null);
	            // looping through all rows and adding to list
	            if (cursor.moveToFirst()) {
	                do {
	                	SummaryContact contact = getContact(Integer.parseInt(cursor.getString(0)));	
	            		ContentValues values = new ContentValues();
	            		values.put(KEY_NAME, contact.getName());
	            		values.put(KEY_AMOUNT, "0");
	            		db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.getID()) });
	                } while (cursor.moveToNext());
	            }
		db.close(); // Closing database connection
	}

	// Getting single contact
	SummaryContact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_NAME, KEY_AMOUNT}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		SummaryContact contact = new SummaryContact(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return contact;
	}
	
	SummaryContact getContact(String name) 
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
				KEY_NAME, KEY_AMOUNT}, KEY_NAME + "=?",
				new String[] { name }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		SummaryContact contact = new SummaryContact(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return contact;
	}
	
	// Getting All Contacts
	public ArrayList<SummaryContact> getAllContacts() {
		ArrayList<SummaryContact> contactList = new ArrayList<SummaryContact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SummaryContact contact = new SummaryContact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setAmount(cursor.getString(2));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}
	
	public ArrayList<SummaryContact> getresultforquery(String query) {
		ArrayList<SummaryContact> contactList = new ArrayList<SummaryContact>();
		// Select All Query
		String selectQuery = query;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SummaryContact contact = new SummaryContact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setAmount(cursor.getString(2));
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		return contactList;
	}

	// Deleting single contact
	public void deleteContact(SummaryContact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		db.close();
	}
	
	public void deleteContact(String name) 
	{
		String[] args = {name};
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_CONTACTS, KEY_NAME + "= ?",args);
	    db.close();
	}


	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int y = cursor.getCount();
		cursor.close();

		// return count
		return y;
	}
	
	public void deleteAll(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS,null,null);
		db.close();
	}

}