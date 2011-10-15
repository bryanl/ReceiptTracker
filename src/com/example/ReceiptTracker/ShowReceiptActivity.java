package com.example.ReceiptTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ShowReceiptActivity extends Activity {

	private static final String TAG = "ShowReceiptActivity";
	private Receipt receipt;

	private View.OnClickListener saveListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			EditText descriptionEditor = (EditText) ShowReceiptActivity.this
					.findViewById(R.id.descriptionEditor);
			receipt.setDescription(descriptionEditor.getText().toString());
			receipt.save();

			Intent intent = new Intent(ShowReceiptActivity.this,
					ReceiptTrackerActivity.class);
			startActivity(intent);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.receipt);
		Bundle bundle = getIntent().getExtras();

		long receiptId = bundle.getLong("RECEIPT_ID");
		Log.d(TAG, "Showing receipt: " + receiptId);

		receipt = Receipt.findById(this, receiptId);
		ImageView view = (ImageView) findViewById(R.id.receiptImage);

		byte[] imageBytes = receipt.getImageBytes();

		Bitmap theImage = BitmapFactory.decodeByteArray(imageBytes, 0,
				imageBytes.length);
		view.setImageBitmap(theImage);

		Button receiptSaveButton = (Button) findViewById(R.id.receiptSaveButton);
		receiptSaveButton.setOnClickListener(saveListener);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.share:
			share();
			return true;
		default:
			return false;
		}
	}
	
	private void share() {
		String filename = "receipt.jpg";
		FileOutputStream fos;
		try {
			fos = openFileOutput(filename, Context.MODE_WORLD_READABLE);
			fos.write(receipt.getImageBytes());
			fos.close();			
			
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("image/jpg");
			
			File jpg = getFileStreamPath(filename);
			
			URI uri = new URI(new File(filename).getCanonicalPath());
			
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(jpg));
			 startActivity(Intent.createChooser(intent, "Send"));
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.receipt_menu, menu);

		return true;
	}

}
