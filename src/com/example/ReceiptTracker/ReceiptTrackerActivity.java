package com.example.ReceiptTracker;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReceiptTrackerActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View.OnClickListener snapListener = new View.OnClickListener() {    		
    		public void onClick(View v) {
    			Intent intent = new Intent(ReceiptTrackerActivity.this, CaptureActivity.class);
    			startActivity(intent);    			    		    			
    		}
    	};
    	
    	ImageButton snap = (ImageButton) findViewById(R.id.snap);
    	snap.setOnClickListener(snapListener);
    	
    	List<Receipt> receipts = Receipt.findAll(this);
    	LinearLayout receiptScroller = (LinearLayout) findViewById(R.id.receiptScroller);
    	
    	for (final Receipt receipt : receipts) {    		
    		LinearLayout receiptHolder = new LinearLayout(this);
    		receiptHolder.setOrientation(LinearLayout.VERTICAL);
    		
    		byte[] imageBytes = receipt.getImageBytes();    		
    		Bitmap theImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
    		
    		ImageButton view = new ImageButton(this);
    		view.setImageBitmap(theImage);
    		view.setMaxWidth(15);
    		
    		
    		TextView textView = new TextView(this);
    		textView.setTextSize(18);    		
    		
    		String description = receipt.getDescription();
    		if (description.length() < 1) {
    			description = "Receipt";
    		}
    		
    		textView.setText(description);    	
    		
    		View.OnClickListener listener = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ReceiptTrackerActivity.this, ShowReceiptActivity.class);
					Bundle bundle = new Bundle();
					bundle.putLong("RECEIPT_ID", receipt.getId());
					intent.putExtras(bundle);
					startActivity(intent);
					
				}
			};
    		
    		
			view.setOnClickListener(listener);
			
    		receiptHolder.addView(textView);    	    	
    		receiptHolder.addView(view);    		    		    	    		
    		
    		receiptScroller.addView(receiptHolder);
    	}
    	
    }
}