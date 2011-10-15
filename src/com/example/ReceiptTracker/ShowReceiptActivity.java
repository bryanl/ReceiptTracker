package com.example.ReceiptTracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class ShowReceiptActivity extends Activity {

	private static final String TAG = "ShowReceiptActivity";
	
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
	}
}
