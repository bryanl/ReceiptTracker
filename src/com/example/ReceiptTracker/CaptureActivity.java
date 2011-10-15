package com.example.ReceiptTracker;

import android.app.Activity;
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
				Log.d("CameraPreview", "snapped a picture");
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

	ShutterCallback mShutterCallback = new ShutterCallback() {

		@Override
		public void onShutter() {
		}
	};
	
	PictureCallback mPictureCallback = new PictureCallback() {		
		public void onPictureTaken(byte[] data, Camera c) {} 
	};
		
	
	PictureCallback mjpeg = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
			if (data !=null) {
				tempdata = data;
				done();
			}
		}
	};
	
	private void done() {
		// 1. save picture in database
		// 2. forward to Receipt Activity (populate bundle with receipt id)
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
