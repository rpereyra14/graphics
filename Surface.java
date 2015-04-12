/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

abstract public class Surface{

	//abstract class used for implementing the various surfaces used in this raytracer

	public static final float NO_INTERSECT = -1.0f;
	protected final float ZERO = 0.0f;
	
	//material properties
	protected float[] ka = new float[3];
	protected float[] kd = new float[3];
	protected float[] ks = new float[3];
	protected float p;
	protected float alpha;

	abstract public float intersect( Vector d, Point camera );
	
	abstract public Vector getNormal( Point point );
	
	public void setAlpha( float a ){
		this.alpha = a;
	}
	
	public float getAlpha(){
		return this.alpha;
	}
	
	//set ka of the surface
	public void setKa( float red, float green, float blue ){
		this.ka[0] = red;
		this.ka[1] = green;
		this.ka[2] = blue;
	}
	
	//set kd of the surface
	public void setKd( float red, float green, float blue ){
		this.kd[0] = red;
		this.kd[1] = green;
		this.kd[2] = blue;
	}
	
	//set ks of the surface
	public void setKs( float red, float green, float blue ){
		this.ks[0] = red;
		this.ks[1] = green;
		this.ks[2] = blue;
	}
	
	//set specular power of the surface
	public void setSpecularPower( float sp ){
		this.p = sp;
	}
	
	public float[] getKa(){
		return this.ka;
	}
	
	public float[] getKd(){
		return this.kd;
	}
	
	public float[] getKs(){
		return this.ks;
	}
	
	public float getSpecularPower(){
		return this.p;
	}
	
}