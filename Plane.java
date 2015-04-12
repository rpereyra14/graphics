/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

import java.lang.Math;

//implements a plane for the purposes of this raytracer
public class Plane extends Surface{

	private Point point;					//a point on the plane
	private Vector normal;				//vector normal to the plane

	//constructor
	public Plane( Point point, Vector normal ){
		this.point = point;
		this.normal = normal;	
	}
	
	//get the plane's normal vector
	//point is passed so that the abstraction Surface can be easily used in the Scene class.
	public Vector getNormal( Point point ){
		return normal;
	}
	
	//implements intersection of a vector d with the plane
	public float intersect( Vector d, Point camera ){

		//do not waste time calculating intersection with a zero vector
		if( d.length() == ZERO ){
			return NO_INTERSECT;
		}
		
		//compute a dot product needed to determine the t which solves the intersection equation,
		//and do some error checking
		float dDotn = d.dottedWith(normal);
		if( dDotn == ZERO ){
			return NO_INTERSECT;
		}
		
		//compute t
		float t = point.subtract(camera).dottedWith(normal)/dDotn;

		//make sense of solution. A negative t implies that the intersection occurs behind the camerapiece (i.e. no intersection).
		if( t < ZERO ){
			return NO_INTERSECT;
		}else{
			return t;
		}
	}
	
}