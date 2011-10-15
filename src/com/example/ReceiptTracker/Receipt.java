package com.example.ReceiptTracker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Receipt {

	public static final String COLUMN_ID = "_id";
	private static final String COLUMN_IMAGE = "image";
	private static final String COLUMN_DESCRIPTION = "description";

	private static final String TABLE_NAME = "receipts";
	private static final String TAG = "Receipt";
	private Context context;
	private byte[] imageBytes;
	private long id;
	private String description;

	public Receipt(Context context, byte[] bytes) {
		this.context = context;
		setImageBytes(bytes);
	}

	public Receipt(Context context, long id) {
		this.context = context;
		this.id = id;
	}

	public void setImageBytes(byte[] bytes) {
		imageBytes = bytes;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void save() {
		ContentValues values = new ContentValues();
		values.put(COLUMN_IMAGE, getImageBytes());
		values.put(COLUMN_DESCRIPTION, getDescription());

		SQLiteDatabase db = new Helper(context).getDb(context);

		if (notSaved()) {
			id = db.insert(TABLE_NAME, null, values);
		} else {
			String where = COLUMN_ID + "=?";
			String[] whereArgs = { Long.toString(id) };
			db.update(TABLE_NAME, values, where, whereArgs);
		}

		Log.d("Receipt", "Previously saved id: " + id);
	}

	private boolean notSaved() {
		return id == 0L;
	}

	public static Receipt findById(Context context, long id) {
		String where = COLUMN_ID + "=?";
		String[] whereArgs = { Long.toString(id) };

		Log.d(TAG, "receipt id we are searching for: " + id);
		return query(context, where, whereArgs);
	}

	public static List<Receipt> findAll(Context context) {
		ArrayList<Receipt> receipts = new ArrayList<Receipt>();
		Cursor cursor = findAllAsCursor(context);
		while (cursor.moveToNext()) {
			Receipt receipt = createNewReceiptFromCursor(context, cursor);
			receipts.add(receipt);
		}

		return receipts;
	}

	public static Cursor findAllAsCursor(Context context) {
		SQLiteDatabase db = new Helper(context).getDb(context);
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}

	private static Receipt query(Context context, String where,
			String[] whereArgs) {

		SQLiteDatabase db = new Helper(context).getDb(context);

		Cursor cursor = db.query(TABLE_NAME, null, where, whereArgs, null,
				null, null);

		Log.d(TAG, "Cursor row count: " + cursor.getCount());
		Log.d(TAG, "Current cursor position: " + cursor.getPosition());

		cursor.moveToLast();

		Receipt receipt = createNewReceiptFromCursor(context, cursor);
		cursor.close();

		return receipt;
	}

	private static Receipt createNewReceiptFromCursor(Context context,
			Cursor cursor) {

		int imageColumn = cursor.getColumnIndexOrThrow(COLUMN_IMAGE);
		int idColumn = cursor.getColumnIndexOrThrow(COLUMN_ID);
		int descriptionColumn = cursor
				.getColumnIndexOrThrow(COLUMN_DESCRIPTION);

		Log.d(TAG, "image Column: " + imageColumn);
		Log.d(TAG, "id Column: " + idColumn);

		Receipt receipt = new Receipt(context, cursor.getLong(idColumn));
		receipt.setImageBytes(cursor.getBlob(imageColumn));
		receipt.setDescription(cursor.getString(descriptionColumn));

		return receipt;
	}
}

class Helper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "receipt_tracker.db";

	public Helper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public Helper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public SQLiteDatabase getDb(Context context) {
		return getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table receipts(" + "_id integer primary key,"
				+ "image blob," + "description text" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
