package com.example.ReceiptTracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ShowReceiptActivity extends Activity {

	private static final String TAG = "ShowReceiptActivity";
	
	private View.OnClickListener saveListener = new View.OnClickListener() {		 
		@Override
		public void onClick(View v) {			
			Intent intent = new Intent(ShowReceiptActivity.this, ReceiptTrackerActivity.class);
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
				
		Receipt receipt = Receipt.findById(this, receiptId);
		
		ImageView view = (ImageView) findViewById(R.id.receiptImage);
		
		byte[] imageBytes = receipt.getImageBytes();
		
		Bitmap theImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
		view.setImageBitmap(theImage);
		
		Button receiptSaveButton = (Button) findViewById(R.id.receiptSaveButton);
		
		
		receiptSaveButton.setOnClickListener(saveListener);
		
		
	}
}
