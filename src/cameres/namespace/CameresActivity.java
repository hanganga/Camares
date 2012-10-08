package cameres.namespace;

import java.util.List;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;

public class CameresActivity extends Activity {
	private static final int CAMERA_REQUEST = 1888;
	private ImageView imageView;
	private TextView tview;
//	private View topPanel;
	String colorText ;
    Camera mCamera ;
//    private CameraPreview mPreview;
	private Bitmap bitmap ;
    private FrameLayout preview;

//	private SurfaceView surfaceView;
//	private SurfaceHolder surfaceHolder;
//	private ImageView imageView;
//	  private Camera camera ;
//	  private HandlePicture pictureHandle ;
//	  private CameraSurfaceView cameraSurfaceView;
/*	private static final String[][][] colorstring = 
									    { { { "K ", "R ", "R " },
										    { "G ", "A ", "O " },
											{ "G ", "G ", "Y " } 
										  },
										  { { "B ", "V ", "R " },
											{ "G ", "A ", "R " },
											{ "G ", "G ", "Y " } 
										  },
										  { { "B ", "V ", "V " },
											{ "B ", "V ", "V " },
											{ "B ", "B ", "W " } 
										  }
	};
*/
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        setContentView(R.layout.main);
        this.tview = (TextView) this.findViewById(R.id.textv1);
        final CameraPreview mPreview = new CameraPreview(this);
        preview = (FrameLayout) findViewById(R.id.preview) ;
        preview.addView(mPreview); 
//        ((ViewGroup) preview).addView(mPreview);
//        mPreview = new CameraPreview(this, mCamera);
        this.imageView = (ImageView) this.findViewById(R.id.result);
//		topPanel=findViewById(R.id.top_panel);
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
       	
        public void onClick(View v)
        {
//        	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, CAMERA_REQUEST); 
        	if (mPreview.isPreviewing() == true) {
        		mPreview.mCamera.takePicture(null, null, mPicture);
        	}
        }});
        
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
       	
        public void onClick(View v)
        {
//        	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            mPreview.startPreview();
        	}
        });
        
        if (!getPackageManager()
            .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
          Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
              .show();
        }
    }
    /*
    public void pickImage(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
      }
*/
    
      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        // We need to recyle unused bitmaps
        {   /*if (bitmap != null) {
        		bitmap.recycle();
        	}*/
        	Bitmap bitmap ;
        	bitmap =  (Bitmap) data.getExtras().get("data");
        	int x = bitmap.getWidth();
        	int ymin = findcolorchange(bitmap, -1);
        	int ymax = findcolorchange(bitmap,  1);
        	Bitmap modbitmap = Bitmap.createBitmap(bitmap,x/4,ymin,x/2,ymax-ymin );
        	// modbitmap is a new bitmap with only the vertical center 1/3 of the image in it.
//        	String colorText = "Red =" + red  + " Green = " + green + " Blue = " + blue ;
        	findcolors(modbitmap) ;
        	tview.setText(colorText);
//        	imageView.setImageBitmap(modbitmap);
            /*            InputStream stream = getContentResolver().openInputStream(data.getData());
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            imageView.setImageBitmap(bitmap);

          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();*/
        }
 
//        super.onActivityResult(requestCode, resultCode, data);
      }
      protected int findcolor(Bitmap bitmap) {
    	  int xmax = bitmap.getWidth();
    	  int ymax = bitmap.getHeight();
    	  int color = bitmap.getPixel(xmax/2,ymax/2);
    	  return color ;
      }
      protected int findcolorchange(Bitmap bitmap, int direction) {
    	  int xmax = bitmap.getWidth();
    	  int ymax = bitmap.getHeight();
    	  boolean diff = false ;
    	  int color = bitmap.getPixel(xmax/=2,ymax/=2);
    	  int red   = Color.red(color) ;
      	  int green = Color.green(color) ;
      	  int blue  = Color.blue(color) ;
//      	  colorText = "Red =" + red  + " Green = " + green + " Blue = " + blue ;
      	  for ( int i = ymax; (i > 1) && (diff == false) ; --i )
      	  {					// used 1 because rounding crashed the bitmap
      		  int newcolor = bitmap.getPixel(xmax,ymax+=direction);
      		  int rednew   = Color.red(newcolor) ;
      		  int greennew = Color.green(newcolor) ;
      		  int bluenew  = Color.blue(newcolor)  ;
      		  int absolute = Math.abs(red - rednew) + 
      				  		 Math.abs(green - greennew) + 
      				  		 Math.abs(blue - bluenew) ;
      		  if ( absolute > 25 )  // having trouble here finding color change
      			  diff = true ;
      		  else {
      			  red = rednew ;
      			  blue = bluenew;
      			  green = greennew;
      		  }
      	  }
      	  return ymax ;
      }
      protected void findcolors(Bitmap bitmap) {
    	  int xmax = bitmap.getWidth();
    	  int ymax = bitmap.getHeight();
    	  int color = bitmap.getPixel(0,ymax/=2);
    	  int red   = Color.red(color) ;
      	  int green = Color.green(color) ;
      	  int blue  = Color.blue(color) ;
      	  colorText = "" ;
      	  int rednew = 0 ;
      	  int greennew = 0;
      	  int bluenew = 0 ;
      	  int xold = 0 ;
      	  for ( int i = 1 ; i < xmax ; i++ )
      	  {				
      		  int newcolor = bitmap.getPixel(i,ymax);
        	  rednew   = Color.red(newcolor) ;
          	  greennew = Color.green(newcolor) ;
          	  bluenew  = Color.blue(newcolor)  ;
      		  int absolute = Math.abs(red - rednew) + 
      				  		 Math.abs(green - greennew) +
      				  		 Math.abs(blue - bluenew) ;
      		  if ( absolute > 50 ) {
      			  int tcolor = bitmap.getPixel(((i-xold)/2)+xold, ymax);
      			  colorText += stringColor(tcolor) ;
      			  red = rednew ;
      			  blue = bluenew;
      			  green = greennew;
      			  xold = i ;
      		  }
      	  }
      }

      private String stringColor(int tcolor) {
		// return a short string describing the color found
		int tred   = Color.red(tcolor) ;
		int tgreen = Color.green(tcolor) ;
		int tblue  = Color.blue(tcolor)  ;
		int total = tred + tgreen + tblue ;
		int redindex =   ( tred   * 7 ) / total ; 
		int greenindex = ( tgreen * 8 ) / total ; 
//		int blueindex =  ( tblue  * 6 ) / total ; 
		switch ((int)total/230) {
			case 0: return "K " ;
			case 2: return "W " ;
			case 1:
			default:
				{
					switch (redindex) {
						case 0: return "B " ;
						case 1: return "G " ;
						case 2: 
							switch (greenindex) {
								case 1: return "V " ;
								case 2: return "N " ;
								case 3: return "Y " ;
								default: return "X " ;
							}
						case 3: 
							switch (greenindex) {
								case 1: return "R " ;
								case 2: return "O " ;
								default: return "X " ;
							}
						default: return "X " ;
					}
				}
		}
	}
    private PictureCallback mPicture = new PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {

        	bitmap =  (Bitmap) BitmapFactory.decodeByteArray(data, 0, data.length);
           	int x = bitmap.getWidth();
           	int ymin = findcolorchange(bitmap, -1);
           	int ymax = findcolorchange(bitmap,  1);
           	Bitmap modbitmap = Bitmap.createBitmap(bitmap,x/4,ymin,x/2,ymax-ymin );
            	// modbitmap is a new bitmap with only the vertical center 1/3 of the image in it.
//            	String colorText = "Red =" + red  + " Green = " + green + " Blue = " + blue ;
           	findcolors(modbitmap) ;
           	tview.setText(colorText);
           	imageView.setImageBitmap(modbitmap);
                /*            InputStream stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                imageView.setImageBitmap(bitmap);

              } catch (FileNotFoundException e) {
                e.printStackTrace();
              } catch (IOException e) {
                e.printStackTrace();*/
            }
      };
      /** A safe way to get an instance of the Camera object. */
/*      public static Camera getCameraInstance(){
          Camera c = null;
          try {
              c = Camera.open(); // attempt to get a Camera instance
          }
          catch (Exception e){
              // Camera is not available (in use or does not exist)
          }
          return c; // returns null if camera is unavailable
      }
      private void releaseCamera(){
          if (mCamera != null){
              mCamera.release();        // release the camera for other applications
              mCamera = null;
          }
      }*/
      protected void onPause() {
          super.onPause();
          //releaseCamera();              // release the camera immediately on pause event
      }
}