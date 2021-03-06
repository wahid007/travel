package com.folklore.tourengine;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

public class FolkloreMain extends Activity {
    /** Called when the activity is first created. */
	private static final int STOPSPLASH = 0;
    //time in milliseconds
    private static final long SPLASHTIME = 3000;
   
    private ImageView splash;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Message msg = new Message();
        
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
        
        //setContentView(R.layout.main);
        
        setContentView(R.layout.splash);
    }
    
    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                switch (msg.what) {
                case STOPSPLASH:
                        //remove SplashScreen from view
                        
                        Intent loginIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(loginIntent);
                        //splash.setVisibility(View.GONE);
                        break;
                }
                super.handleMessage(msg);
        }
	};
}