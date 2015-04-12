/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

//stores information about an intersection between a ray and a surface
public class Intersection{

	private Surface s; 		//the intersecting surface
	private float t;			//the t for which the intersection occurred	

	//constructor
	public Intersection( Surface s, float t ){
		this.s = s;
		this.t = t;
	}
	
	//getter method for surface
	public Surface getSurface(){
		return this.s;
	}
	
	//getter method for the intersection scalar
	public float getT(){
		return this.t;
	}

}