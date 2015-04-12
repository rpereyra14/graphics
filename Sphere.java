/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

import java.lang.Math;

public class Sphere extends Surface{

	private float radius;			//the radius
	private Point center;			//the sphere's center

	//constructor
	public Sphere( Point center, float radius ){
		this.radius = radius;
		this.center = center;
	}
	
	//get the sphere's normal vector at a point
	public Vector getNormal( Point point ){
		return point.subtract( center ).normalized();
	}
	
	//calculate intersection
	public float intersect( Vector d, Point camera ){
	
		//do not waste time calculating intersection with a zero vector
		if( d.length() == ZERO ){
			return NO_INTERSECT;
		}
		
		//calculate the t at which d intersects sphere. The calculation is broken down into parts.
		//we only want the first (i.e. front of object) intersection, t1, unless we are inside object.
		float notSqrtd = d.scaledBy(-1.0f).dottedWith(camera.subtract(center));
		float sqrtd = (float)Math.pow(notSqrtd, 2.0) - d.squared() * (camera.subtract(center).squared() - (float)Math.pow(radius, 2.0));
		float t1 = (notSqrtd - (float)Math.sqrt(sqrtd))/d.squared();
		float t2 = (notSqrtd + (float)Math.sqrt(sqrtd))/d.squared();
		
		//camera is inside object
		if( t1 < ZERO && t2 > ZERO ){
			return t2;
		}

		//check for errors and or determine if the interection occurs behind camerapiece (i.e. no real intersection occurs)
		if( sqrtd < ZERO || t1 < ZERO ){
			return NO_INTERSECT;
		}else{
			return t1;
		}
	}
	
}