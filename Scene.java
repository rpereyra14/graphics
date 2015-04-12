/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

import java.util.LinkedList;
import java.util.HashMap;
import java.lang.Math;

//maintains a collection of the objects/surfaces in the current scene
public class Scene{

	//constants for setting RGB values
	private static final float MIN_INTENSITY = 0.0f;
	private static final float MAX_INTENSITY = 1.0f;
	
	private static boolean AA = ImageDriver.AA;
	
	//number of samples for AA
	private static final int AA_SAMPLE_SIZE = ImageDriver.AA_SAMPLE_SIZE;
	
	//recursion depth for reflections
	public static final int RECURSION_DEPTH = ImageDriver.RECURSION_DEPTH;
	
	//height and width of a pixel; used in AA
	private static final float HEIGHT_OF_PIXEL = 
										(ImageDriver.RIGHT_OF_IMAGE - ImageDriver.LEFT_OF_IMAGE)/ImageDriver.NUM_PIXELS_X;
	private static final float WIDTH_OF_PIXEL = 
										(ImageDriver.TOP_OF_IMAGE - ImageDriver.BOTTOM_OF_IMAGE)/ImageDriver.NUM_PIXELS_Y;
	
	//gamma for gamma correction
	private static final float GAMMA = ImageDriver.GAMMA;

	//maintains the list of surfaces
	LinkedList<Surface> surfaceList = new LinkedList<Surface>();
	
	//maintains the list of surfaces
	LinkedList<Light> lightList = new LinkedList<Light>();
	
	//scene camera
	Point camera;
	
	//ambient intensity
	float ambientIntensity;
	
	//constructor
	public Scene( Point camera, float ambientIntensity ){
		this.camera = camera;
		this.ambientIntensity = ambientIntensity;
	}
	
	//adds a surface to the list of surfaces
	public void addSurface( Surface s ){
		surfaceList.add( s );
	}
	
	//adds a light to the list of surfaces
	public void addLight( Light l ){
		lightList.add( l );
	}
	
	//determines if AA is needed and if so averages the results of the getRGBValue method run AA_SAMPLE_SIZE times
	public float[] computePixelRGB( Vector d, int depth ){
		
		//AA turned off
		if( !AA ){
			return getRGBValue( d, this.camera, depth );
		
		//AA left on
		}else{
			
			//return variables
			float[] rgbValues = new float[3];
			
			//sample AA_SAMPLE_SIZE times
			for( int i = 0; i < AA_SAMPLE_SIZE; i++ ){
				
				//get random offsets within pixel defined by Vector d
				float rand_x = (float)(Math.random() * WIDTH_OF_PIXEL);
				float rand_y = (float)(Math.random() * HEIGHT_OF_PIXEL);
				
				//compute rgb values at offset
				float[] temp = getRGBValue( d.add( new Vector(rand_x, rand_y, 0.0f)), this.camera, depth );
				
				//sum values from previous runs
				for( int j = 0; j < 3; j++ ){
					rgbValues[j] += temp[j];
				}
				
			}
			//average rgb values
			for( int j = 0; j < 3; j++ ){
				rgbValues[j] = rgbValues[j]/AA_SAMPLE_SIZE;
			}
			
			//return
			return rgbValues;
		}
	}
	
	//computes the rgb value with location pointed to by vector d
	//allows for turning off of shading and shadows and anti-aliasing via flags
	private float[] getRGBValue( Vector d, Point origin, int depth ){
	
		//return value
		float[] rgbValues = new float[3];
		
		//determine intersection of a viewing ray and surface
		Intersection intersectionFromOrigin = this.intersect( d, origin );
		
		//no intersection occurred
		if( intersectionFromOrigin == null ){
			rgbValues[0] = MIN_INTENSITY;
			rgbValues[1] = MIN_INTENSITY;
			rgbValues[2] = MIN_INTENSITY;
			return rgbValues;
		}
		
		//get surface from intersection
		Surface s = intersectionFromOrigin.getSurface();
		
		//get the intersection point of a surface and viewing ray
		Point intersectionPointOrigin = d.scaledBy(intersectionFromOrigin.getT()).getEndPoint();
		Vector normal = s.getNormal(intersectionPointOrigin);
		
		//get the surface's alpha
		float alpha = s.getAlpha();
		
		//loop through all lights in the scene and add ks and kd dependent rgb values
		for( Light L : lightList ){
			
			//get light details
			Point light = L.getLocation();
			float intensity = L.getIntensity();
			
			//compute shadow rays and their (possible) intersection with surfaces
			//note that numerical issues in generating shadows are handled by offsetting the starting point of the shadow ray by
			// 0.01 * normal vector of the surface
			Point shadowRayOrigin = intersectionPointOrigin.add(normal.scaledBy(0.01f)).getEndPoint();
			Vector shadowRay = light.subtract(shadowRayOrigin).normalized();
			Intersection intersectionToLight = this.intersect( shadowRay, shadowRayOrigin );
			
			//location does not belong to a shadow
			if( intersectionToLight == null ){
				
				//get material properties
				float[] ks = s.getKs();
				float[] kd = s.getKd();
				
				//compute needed vectors
				Vector l = light.subtract(intersectionPointOrigin).normalized();
				Vector c = camera.subtract(intersectionPointOrigin).normalized();
				Vector h = c.add(l).normalized();
				
				//intermediary dot product computations
				float nDotl = normal.dottedWith( l );
				float nDoth = normal.dottedWith( h );
				
				//compute light properties for each material property
				float kd_term = intensity * Math.max( 0.0f, nDotl );
				float ks_term = intensity * (float)Math.pow( Math.max( 0.0f, nDoth ), s.getSpecularPower() );
				
				//add rgb values
				rgbValues[0] += (kd[0] * kd_term) + (ks[0] * ks_term);
				rgbValues[1] += (kd[1] * kd_term) + (ks[1] * ks_term);
				rgbValues[2] += (kd[2] * kd_term) + (ks[2] * ks_term);
				
			}
			
		}
		
		//only need to add ambient light
		float[] ka = s.getKa();
		float ka_term = ambientIntensity;
		rgbValues[0] += ka[0] * ka_term;
		rgbValues[1] += ka[1] * ka_term;
		rgbValues[2] += ka[2] * ka_term;
		
		if( depth < RECURSION_DEPTH ){
		
			Vector r = ((Vector)intersectionPointOrigin).normalized().subtract( normal.scaledBy( 2.0f * ((Vector)intersectionPointOrigin).normalized().dottedWith(normal) ) );
			float Lm[] = getRGBValue( r.normalized() , intersectionPointOrigin.add(normal.scaledBy(0.01f)).getEndPoint(), depth + 1 );
		
			//computed weighted pixel value based on alpha
			for( int k = 0; k < 3; k++ ){
				rgbValues[k] = (1.0f - alpha) * rgbValues[k] + alpha * Lm[k];
			}
		
		}
		
		if( depth == 0 ){
		
			//handle overflows and underflows
			for( int i = 0; i < 3; i++ ){
				if( rgbValues[i] > MAX_INTENSITY ){
					rgbValues[i] = MAX_INTENSITY;
				}else if( rgbValues[i] < MIN_INTENSITY ){
					rgbValues[i] = MIN_INTENSITY;
				}
			}
		
			//perform gamma correction
			for( int k = 0; k < 3; k++ ){
				rgbValues[k] = (float)Math.pow( rgbValues[k], 1/GAMMA );
			}
			
		}
		
		return rgbValues;
		
	}
	
	//computes the min t that intersects vector d for any surface
	//the min t would belong to the object closest to the camerapiece for the particular coordinate
	private Intersection intersect( Vector d, Point start ){
	
		//initializations
		float t = Surface.NO_INTERSECT;
		float u;
		
		Intersection intersection = null;
		
		//determine min
		for (Surface s : surfaceList){
			u = s.intersect( d, start );
			if( u != Surface.NO_INTERSECT && ( t > u || t == Surface.NO_INTERSECT ) ){
				t = u;
				intersection = new Intersection( s, t );
			}
		}
		return intersection;
	}

}