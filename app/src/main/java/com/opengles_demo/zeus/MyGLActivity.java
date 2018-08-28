package asiainnovations.com.opengles_demo.zeus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import java.util.Random;

public class MyGLActivity extends Activity {

	private GLSurfaceView glView;   // Use GLSurfaceView
	  Random rnd = new Random();
	   // Call back when the activity is started, to initialize the view
	   @SuppressLint("NewApi")
	@Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      glView = new GLSurfaceView(this);           // Allocate a GLSurfaceView
	      glView.setRenderer(new MyGLRenderer(this)); // Use a custom renderer
	      glView.getAlpha();
	     
	      this.setContentView(glView);                // This activity sets to GLSurfaceView
	      
	   }
	   

	// Call back when the activity is going into the background
	   @Override
	   protected void onPause() {
	      super.onPause();
	      glView.onPause();
	   }
	   
	   // Call back after onPause()
	   @Override
	   protected void onResume() {
	      super.onResume();
	      glView.onResume();
//	      android.provider.Settings.System.putInt(this.getContentResolver(),
//	    	        android.provider.Settings.System.SCREEN_BRIGHTNESS, rnd.nextInt(256));
	      
	   }

}
