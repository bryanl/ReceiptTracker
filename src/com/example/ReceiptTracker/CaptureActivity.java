package com.example.ReceiptTracker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class CaptureActivity extends Activity {

	private CameraPreview mPreview;
	Camera mCamera;
	int numberOfCameras;
	int cameraCurrentlyLocked;

	// The first rear facing camera
	int defaultCameraId;
	
	byte[] tempdata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mPreview = new CameraPreview(this);
		setContentView(mPreview);

		// Find the total number of cameras available
		numberOfCameras = Camera.getNumberOfCameras();

		LayoutInflater inflater = LayoutInflater.from(this);
		View cameraOverlay = inflater.inflate(R.layout.camera_overlay, null);
		addContentView(cameraOverlay, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		View.OnClickListener snapButtonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Camera.Parameters cameraParameters = mCamera.getParameters();
				cameraParameters.setPictureSize(640, 480);
				mCamera.setParameters(cameraParameters);
				mCamera.takePicture(null, null, mjpeg);
			}
		};

		Button snapButton = (Button) findViewById(R.id.snapButton);
		snapButton.setOnClickListener(snapButtonListener);

		// Find the ID of the default camera
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				defaultCameraId = i;
			}
		}

	}
	
	PictureCallback mjpeg = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
			if (data !=null) {
				done(data);				
			} else {
				Log.d("capture activity", "could not save");
			}
		}
	};
	
	private void done(byte[] bytes) {		
		Log.d("capture activity", "preparing to save");
		
		Log.d("capture activity", "our image is this big: " + bytes.length);
		
		Receipt receipt = new Receipt(this, bytes);
		receipt.save();
		
		mPreview.setCamera(null);
		mCamera.release();
		mCamera = null;
		
		Intent intent = new Intent(this, ShowReceiptActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong("RECEIPT_ID", receipt.getId());
		intent.putExtras(bundle);
		startActivity(intent);
				
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// Open the default i.e. the first rear facing camera.
		mCamera = Camera.open();
		cameraCurrentlyLocked = defaultCameraId;
		mPreview.setCamera(mCamera);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

}
