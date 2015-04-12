/*
 * Renato Pereyra
 * COMP 575 PA 4
 *
*/

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.Hashtable;
import javax.swing.JPanel;

	/*
	 * Note: I refurbished the code below such that (x,y) = (0,0) maps to the lower left corner of the image.
	 *
	 */
public class Framebuffer
{
	private SampleModel sampleModel;
	private WritableRaster raster;
	private BufferedImage image;
	private Viewer panel;
	
	private int width;
	private int height;
	
	/// Create an RGB framebuffer with the specified width and height.
	public Framebuffer( int width, int height )
	{
		this.width = width;
		this.height = height;
		ColorSpace colorSpace = ColorSpace.getInstance( ColorSpace.CS_sRGB );
		ColorModel colorModel = new ComponentColorModel( colorSpace, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE );
		sampleModel = colorModel.createCompatibleSampleModel( width, height );
		raster = colorModel.createCompatibleWritableRaster( width, height );
		image = new BufferedImage( colorModel, raster, false, new Hashtable<String,Object>() );
		panel = new Viewer( this );
	}
	 
	/// Set the value of the specified pixel using floating point color values.
	/**
	 * Color values range from 0.0f to 1.0f. x and y are specified from the 
	 * bottom left corner of the framebuffer.
	 */
	public void setPixel( int x, int y, float red, float green, float blue )
	{
		float [] array = { red*255.0f, green*255.0f, blue*255.0f };
		raster.setPixel( x, height - y - 1, array );
	}
	
	
	/// Get the value of a particular pixel as an array of color components.
	/**
	 * Color values range from 0.0f to 1.0f. x and y are specified from the 
	 * bottom left corner of the framebuffer.
	 */
	public float [] getPixel( int x, int y )
	{
		float [] array = raster.getPixel( x, height - y - 1, new float[3] );
		
		// multiply the pixel values so that they are between 0.0f and 1.0f
		for ( int i = 0; i < array.length; i++ )
			array[i] /= 255.0f;
		
		return array;
	}
	
	
	/// Get a JPanel object used to draw the framebuffer to a window.
	public JPanel getPanel()
	{
		return panel;
	}
	
	
	/// Get the width in pixels
	public int getWidth()
	{
		return sampleModel.getWidth();
	}
	
	
	/// Get the height in pixels of the framebuffer
	public int getHeight()
	{
		return sampleModel.getHeight();
	}
	
	
	/// Class used to draw a framebuffer in a window
	private static class Viewer extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private Framebuffer framebuffer;
		
		public Viewer( Framebuffer f )
		{
			framebuffer = f;
		}
		
		
		public void update( Graphics g )
		{
			paint( g );
		}
		
		
		public void paint( Graphics g )
		{
			g.drawImage( framebuffer.image, 0, 0, null );
		}
	}
}
