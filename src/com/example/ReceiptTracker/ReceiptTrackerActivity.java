package com.example.ReceiptTracker;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReceiptTrackerActivity extends Activity {

	private View.OnClickListener snapListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        snapListener = new View.OnClickListener() {    		
    		public void onClick(View v) {
    			Intent intent = new Intent(ReceiptTrackerActivity.this, CaptureActivity.class);
    			startActivity(intent);    			    		    			
    		}
    	};
    	
    	ImageButton snap = (ImageButton) findViewById(R.id.snap);
    	snap.setOnClickListener(snapListener);
    	
    	List<Receipt> receipts = Receipt.findAll(this);
    	LinearLayout receiptScroller = (LinearLayout) findViewById(R.id.receiptScroller);
    	
    	for (Receipt receipt : receipts) {    		
    		LinearLayout receiptHolder = new LinearLayout(this);
    		receiptHolder.setOrientation(LinearLayout.VERTICAL);
    		
    		byte[] imageBytes = receipt.getImageBytes();    		
    		Bitmap theImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
    		
    		ImageButton view = new ImageButton(this);
    		view.setImageBitmap(theImage);
    		view.setMaxWidth(15);
    		
    		
    		TextView textView = new TextView(this);
    		textView.setText("Hello");    	
    		
    		receiptHolder.addView(textView);    	    	
    		receiptHolder.addView(view);
    		
    		
    		receiptScroller.addView(receiptHolder);
    	}
    	
    }
}