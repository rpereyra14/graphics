/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

//create a light source
public class Light{

	private Point location;				//ligth source location
	private float intensity;			//light source intensity
	
	//constructor
	public Light( Point location, float intensity ){
	
		this.location = location;
		this.intensity = intensity;
	
	}
	
	//get location
	public Point getLocation(){
		return this.location;
	}
	
	//get intensity
	public float getIntensity(){
		return this.intensity;
	}

}