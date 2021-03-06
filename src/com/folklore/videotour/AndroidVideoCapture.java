package com.folklore.videotour;




import java.io.File;
import java.io.IOException;

import com.folklore.tourengine.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
//import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AndroidVideoCapture extends Activity{
	
	private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;

	Button myButton;
	//SurfaceHolder surfaceHolder;
	boolean recording;
	
	String title;
	
    /** Called when the activity is first created. */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle oi = getIntent().getExtras();
        
        
        
        if(oi != null)
        {
        	title = oi.getString("title");
    		
    			
        }
        else
        {
        	System.out.println("No Value...");
        }
        
		
		System.out.println("@@@@@@@@@@@user tiltle  ==="+title);
        
        recording = false;
        
        setContentView(R.layout.savevideo);
        
        //Get Camera for preview
        myCamera = getCameraInstance();
        if(myCamera == null){
        	Toast.makeText(AndroidVideoCapture.this, 
        			"Fail to get Camera", 
        			Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        myCameraPreview.addView(myCameraSurfaceView);
        
        myButton = (Button)findViewById(R.id.mybutton);
        myButton.setOnClickListener(myButtonOnClickListener);
        
       
    }
    
    protected void onStart(Intent savei) 
    
    
	{
    	
//    	 File folder = new File(Environment.getExternalStorageDirectory() + "/folklore/videos");
//       boolean success = false;
//       if(!folder.exists())
//       {
//           success = folder.mkdir();
//       }        
//       if (!success)
//       {
//           // Do something on success
//               System.out.println("Its Success!!");
//               mediaRecorder.setOutputFile("/sdcard/folklore/videos/"+title+".mp4");
//               
//               
//       }
//       else
//       {
//           // Do something else on failure
//               System.out.println("Its Failure!!");
//               mediaRecorder.setOutputFile("/sdcard/folklore/videos/"+title+".mp4");
//               
//
//               
////       }
		super.onStart();
		
		
	};

    
    Button.OnClickListener myButtonOnClickListener
    = new Button.OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(recording){
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                
                //Exit after saved
                finish();
			}else{
				
				//Release Camera before MediaRecorder start
				releaseCamera();
				
		        if(!prepareMediaRecorder()){
		        	Toast.makeText(AndroidVideoCapture.this, 
		        			"Fail in prepareMediaRecorder()!\n - Ended -", 
		        			Toast.LENGTH_LONG).show();
		        	finish();
		        }
				
				mediaRecorder.start();
				
				recording = true;
				myButton.setText("STOP");
			}
		}};
    
    private Camera getCameraInstance(){
		// TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
	}
    
    
	
	private boolean prepareMediaRecorder(){
	    
		
		
		myCamera = getCameraInstance();
		System.out.println("myCamera: "+myCamera);
	   

	    myCamera.unlock();
	    //myCamera.startPreview();
	    
	    mediaRecorder = new MediaRecorder();
	    
	    
	    mediaRecorder.setCamera(myCamera);
	    
	    

	    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
	    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
	    
	    //mediaRecorder.setCamera(myCamera);
	    
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        
        File folder = new File(Environment.getExternalStorageDirectory() + "/folklore/videos");
        boolean success = false;
        if(!folder.exists())
        {
            success = folder.mkdir();
        }  
        
        if (!success)
        {
            // Do something on success
                System.out.println("Its Success!!");
                mediaRecorder.setOutputFile(folder+"/"+title+".mp4");
                
                
        }
        else
        {
            // Do something else on failure
                System.out.println("Its Failure!!");
                mediaRecorder.setOutputFile(folder+"/"+title+".mp4");
                

                
        }
        mediaRecorder.setMaxDuration(5*60000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(15000000); // Set max file size 5M
	   // mediaRecorder.setOutputFile("/sdcard/Video/"+title+".mp4");
       

	    mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());
	    

	    try {
	        mediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        releaseMediaRecorder();
	        e.printStackTrace();
	        return false;
	    } catch (IOException e) {
	        releaseMediaRecorder();
	        e.printStackTrace();
	        return false;
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    return true;
		
	}
	

    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            //myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }
	
	public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

		private SurfaceHolder mHolder;
	    private Camera mCamera;
		
		public MyCameraSurfaceView(Context context, Camera camera) {
	        super(context);
	        mCamera = camera;

	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = getHolder();
	        mHolder.addCallback(this);
	        // deprecated setting, but required on Android versions prior to 3.0
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }


		public void surfaceChanged(SurfaceHolder holder, int format, int weight,
				int height) {
	        // If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.

	        if (mHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try {
	            mCamera.stopPreview();
	        } catch (Exception e){
	          // ignore: tried to stop a non-existent preview
	        }

	        // make any resize, rotate or reformatting changes here

	        // start preview with new settings
	        try {
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();

	        } catch (Exception e){
	        }
		}

		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// The Surface has been created, now tell the camera where to draw the preview.
	        try {
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	        } catch (IOException e) {
	        }
		}


		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}
	}
}