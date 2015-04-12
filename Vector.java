/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

import java.lang.Math;

//implements vectors and needed vector operations
//none of the methods provided alter the calling vector itself
public class Vector{

	//coordinates of vector. Assume vectors "start" at the origin.
	protected float x, y, z;

	//constructor
	public Vector( float x, float y, float z ){
			this.x = x;
			this.y = y;
			this.z = z;
	}
	
	//Return the dot product of a vector with itself
	public float squared(){
		return this.dottedWith( this );
	}
	
	//get the vector's endpoint
	public Point getEndPoint(){
		return new Point( this.x, this.y, this.z );
	}
	
	//scale a vector
	public Vector scaledBy( float a ){
		return new Vector( this.x * a, this.y * a, this.z * a );
	}
	
	//vector addition
	public Vector add( Vector B ){
		return new Vector( this.x + B.x, this.y + B.y, this.z + B.z );
	}

	//vector subtraction
	public Vector subtract( Vector B ){
		return new Vector( this.x - B.x, this.y - B.y, this.z - B.z );
	}
	
	//dot product of vectors
	public float dottedWith( Vector B ){
		return ((this.x * B.x) + (this.y * B.y) + (this.z * B.z));
	}
	
	//two-norm of vector
	public float length(){
		return (float)Math.sqrt( this.dottedWith( this ) );
	}
	
	//returns a normalized version of the vector
	public Vector normalized(){
		float len = this.length();
		if( len != 0.0f && len != 1.0f ){
			return new Vector( this.x / len, this.y / len, this.z / len );
		}
		return this;
	}

}