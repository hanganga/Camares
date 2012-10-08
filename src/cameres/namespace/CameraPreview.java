package cameres.namespace;

import java.io.IOException;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    public Camera mCamera;
    private int width, height;
    private Camera.Parameters parameters ;
    private boolean is_previewing = false ;

    public CameraPreview(Context context) {
        super(context);
//        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        this.mHolder = this.getHolder();
        this.mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        mHolder.setSizeFromLayout();

    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
        	this.mCamera = Camera.open();
            this.mCamera.setPreviewDisplay(this.getHolder());
            parameters = mCamera.getParameters();
//            mCamera.startPreview();
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.d(VIEW_LOG_TAG, "Error opening camera preview: " + e.getMessage());
            }
     }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }
        // stop preview before making changes
        width = w ;
        height = h ;
        startPreview() ;
        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        // Surface will be destroyed when replaced with a new screen
        //Always make sure to release the Camera instance
        mCamera.setParameters(parameters);  // resets to initial state
        mCamera.stopPreview();
        is_previewing = false ;
        mCamera.release();
        mCamera = null;
    }
    public Camera getCamera() {
    	return this.mCamera ;
    }
    public boolean isPreviewing() {
    	return is_previewing ;
    }
    public void startPreview() {
        try {
//          mCamera.setPreviewDisplay(mHolder);
          // Now that the size is known, set up the camera parameters and begin
          // the preview.
          try {
              mCamera.stopPreview();
          } catch (Exception e){
              Log.d(VIEW_LOG_TAG, "Error stoping camera preview: " + e.getMessage());
            // ignore: tried to stop a non-existent preview
          };
          Camera.Parameters nparameters = parameters ;
          nparameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
//          parameters.setPreviewSize(width, height);
//          parameters.setPictureFormat(PixelFormat.JPEG);
/*          if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
          {   
              parameters.set("orientation", "portrait");
              parameters.set("rotation",90);
          }
          if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
          {                               
              parameters.set("orientation", "landscape");          
              parameters.set("rotation", 90);
          }*/            
          mCamera.setParameters(nparameters);  // sets to new state
          mCamera.startPreview();
          is_previewing = true ;
      } catch (Exception e){
          Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
      }
    }    		
}
