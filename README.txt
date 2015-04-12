Renato Pereyra
COMP 575 PA4 README

---------------------------------------------------------------------------------------

Compilation and Execution

To compile, simply use the java compiler. 
	$ javac ./*.java

To run, use the java virtual machine.

	$ java ImageDriver [args]

	where args may be the option below:
		-AA				Perform anti-aliasing (AA).

-----------------------------------------------------------------------------------------

Design Structure

A total of ten classes have been submitted. They are described below.

1) Framebuffer:

	Defines a harness on which the image can be generated.

2) ImageDriver:

	Drives the generation of an image of size NUM_PIXELS_X * NUM_PIXELS_Y with image plane defined by LEFT_OF_IMAGE, RIGHT_OF_IMAGE, TOP_OF_IMAGE, BOTTOM_OF_IMAGE, and DISTANCE_TO_IMAGE.

	Accepts command-line arguments -noSS and -noAA. -noSS will turn off shading and shadows, -noAA will turn off anti-aliasing.

	Use both -noSS and -noAA to obtain results for part one of PA4, -noAA for part two, and neither for part three. The arguments are not order-specific.

3) Scene:

	Maintains all information regarding the surfaces, lights, and camera being generated. Has one public method, computePixelRGB, which will determine if AA was required based on command-line arguments and run an underlying getRGBValues method AA_SAMPLE_SIZE times. Reflections will be computed via recursion as decribed in the assignment pdf and the recursion depth is determined by RECURSION_DEPTH in ImageDriver.java.

	Surfaces and lights can be added at will via the addSurface and addLight methods.
	
4) Surface:
	
	Abstract class from which the Sphere and Plane classes are subclassed. The surface class eases the handling of intersections in the scene. In pseudocode:

		<for each surface in SurfaceList>
			<intersect>
		<end for loop>

	Every geometric object added to the Scene object via the addSurface method must be subclassed from the Surface abstract class.

	The surface abstract class contains all the material and reflective properties of the objects in Scene.

5) Sphere
	
	Generates a sphere in the Scene. The constructor requires a center point and a radius.

6) Plane

	Generates a plane in the Scene. The contructor requires a point on the plane and vector normal to the plane.

7) Vector
	
	Generates a vector. The constructor requires an x, y, and z coordinate. The vector is assumed to start from the origin. If a vector does not start from the origin, subtract/add two points appropriately. The class provides multiple methods for handling vectors. Note that none of the methods change the calling vector.

8) Point

	Subclass of vector. Included for semantic purposes (vectors and points are not the same). Addition or subtraction of points returns a vector.

9) Intersection

	Manages/stores the surface where a ray intersected and the t scalar for the intersection.

10) Light

	Generates a light to be passed to Scene. The constructor requires a point for the light and an intensity.