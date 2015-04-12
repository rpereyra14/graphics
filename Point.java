/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

import java.lang.Math;

//implements a point structure which is handled slightly differently than vector
//the subtraction or addition of Point objects returns a Vector.
public class Point extends Vector{

	//constructor
	public Point( float x, float y, float z ){
		super( x, y, z );
	}
	
	//addition
	public Vector add( Point B ){
		return new Vector( this.x + B.x, this.y + B.y, this.z + B.z );
	}

	//subtraction
	public Vector subtract( Point B ){
		return new Vector( this.x - B.x, this.y - B.y, this.z - B.z );
	}
	
}