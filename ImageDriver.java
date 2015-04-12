/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

   import javax.swing.JFrame;
   import javax.swing.JPanel;
   import java.lang.Math;

   public class ImageDriver
   {
   
   //number of pixels in each direction
      public static final int NUM_PIXELS_X = 512;
      public static final int NUM_PIXELS_Y = 512;
   
   //details regarding location and size of image plane
      public static final float DISTANCE_TO_IMAGE = -0.1f;
      public static final float RIGHT_OF_IMAGE = 0.1f;
      public static final float LEFT_OF_IMAGE = -0.1f;
      public static final float TOP_OF_IMAGE = 0.1f;
      public static final float BOTTOM_OF_IMAGE = -0.1f;
   
   //number of samples for AA
      public static final int AA_SAMPLE_SIZE = 64;
		
	//recursion depth for reflections
		public static final int RECURSION_DEPTH = 2;
   
   //gamma
      public static final float GAMMA = 2.2f;
   
   //light intensities
   public static final float LIGHT1_INTENSITY = 1.0f;
	public static final float AMBIENT_INTENSITY = 1.0f;
		
	public static boolean AA = false;
   
   /*
   	Display a image in a window.
   */
      public static void main( String [] args )
      {
      
         for( int i = 0; i < args.length; i++ ){
				if( args[i].compareTo( "-AA" ) == 0 ){
               AA = true;
            }
         }
      
      // The width and height of the window and framebuffer to create
         int width = NUM_PIXELS_X;
         int height = NUM_PIXELS_Y;
      
      // Create a window in which to display the rendered image, specifying it's title.
         JFrame window = new JFrame("Image Viewer");
      
      // Set the window's width and height.
         window.setSize( width, height );
      
      // Set the application to terminate when the window is closed.
         window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      // Create a framebuffer which will hold the rendered image data.
         Framebuffer framebuffer = new Framebuffer( width, height );
      
      // Tell the window that we are drawing the framebuffer's image
         window.getContentPane().add( framebuffer.getPanel() );
      
      // Raytracing is below
      
      //initialize surfaces in the scene
         Plane bottomPlane = new Plane( new Point( 0.0f, -2.0f, 0.0f ), new Vector( 0.0f, 1.0f, 0.0f ) );
         Sphere redSphere = new Sphere( new Point( -4.0f, 0.0f, -7.0f ), 1.0f );	
         Sphere greenSphere = new Sphere( new Point( 0.0f, 0.0f, -7.0f ), 2.0f );
         Sphere blueSphere = new Sphere( new Point( 4.0f, 0.0f, -7.0f ), 1.0f );
      
      //set ka's
         bottomPlane.setKa( 0.2f, 0.2f, 0.2f );
         redSphere.setKa( 0.2f, 0.0f, 0.0f );
         greenSphere.setKa( 0.0f, 0.2f, 0.0f );
         blueSphere.setKa( 0.0f, 0.0f, 0.2f );
      
      //set kd's
         bottomPlane.setKd( 1.0f, 1.0f, 1.0f );
         redSphere.setKd( 1.0f, 0.0f, 0.0f );
         greenSphere.setKd( 0.0f, 0.5f, 0.0f );
         blueSphere.setKd( 0.0f, 0.0f, 1.0f );
      
      //set ks's
         bottomPlane.setKs( 0.0f, 0.0f, 0.0f );
         redSphere.setKs( 0.0f, 0.0f, 0.0f);
         greenSphere.setKs( 0.5f, 0.5f, 0.5f );
         blueSphere.setKs( 0.0f, 0.0f, 0.0f );
      
      //set specular powers
         bottomPlane.setSpecularPower( 0.0f );
         redSphere.setSpecularPower( 0.0f );
         greenSphere.setSpecularPower( 32.0f );
         blueSphere.setSpecularPower( 0.0f );
			
		//set alphas for lighting
         bottomPlane.setAlpha( 0.5f );
         redSphere.setAlpha( 0.0f );
         greenSphere.setAlpha( 0.0f );
         blueSphere.setAlpha( 0.8f );
      
      //initialize camera and light for scene
         Point camera = new Point( 0.0f, 0.0f, 0.0f );
         Light light = new Light( new Point( -4.0f, 4.0f, -3.0f ), LIGHT1_INTENSITY );
			      
      //initialize scene and add surfaces generated
         Scene scene = new Scene( camera, AMBIENT_INTENSITY );
         scene.addLight( light );
         scene.addSurface( bottomPlane );
         scene.addSurface( redSphere );
         scene.addSurface( greenSphere );
         scene.addSurface( blueSphere );
      
      //determine the if a ray from the eyepiece intersects a pixel coordinates
         for( int i = 0; i < NUM_PIXELS_X; i++ ){
            for( int j = 0; j < NUM_PIXELS_Y; j++ ){
            
            //convert pixel indexes to pixel coordinates
               float u = LEFT_OF_IMAGE + (RIGHT_OF_IMAGE - LEFT_OF_IMAGE)*((float)i + 0.5f)/(float)NUM_PIXELS_X;
               float v = BOTTOM_OF_IMAGE + (TOP_OF_IMAGE - BOTTOM_OF_IMAGE)*((float)j + 0.5f)/(float)NUM_PIXELS_Y;
            
            //vector from origin to pixel coordinates				
               //Vector d = new Vector( u, v, DISTANCE_TO_IMAGE );
					Vector d = new Point( u, v, DISTANCE_TO_IMAGE ).subtract( camera );
            
            //compute pixel values
               float[] pixelValues = scene.computePixelRGB( d, 0 );
            
            //set pixel values
               framebuffer.setPixel( i, j, pixelValues[0], pixelValues[1], pixelValues[2] );
            }
         }
      
      // Display the window
         window.setVisible( true );
      }
   
   }
