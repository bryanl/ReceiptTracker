package com.example.ReceiptTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
    }
}