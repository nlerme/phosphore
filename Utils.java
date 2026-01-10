import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class Utils
{
	public static double EPSILON = 10e-5;


	public static int randomMinMax( int min, int max )
	{
		if( min >= max )
			throw new IllegalArgumentException("The min parameter must be lower to the max parameter");

		return (int)(Math.random() * (max - min + 1) + min);
	}

	public static double radiansToDegrees( double radians )
	{
		return (radians * 180.0) / Math.PI;
	}

	public static double degreesToRadians( double degrees )
	{
		return (degrees * Math.PI) / 180.0;
	}

	public static double Sq( double value )
	{
		return value * value;
	}

	public static Color randomColor()
	{
		return new Color(randomMinMax(0, 255), randomMinMax(0, 255), randomMinMax(0, 255));
	}
	
	public static Color applyLighting( Color c, double intensity )
	{
		// Clamp intensity between 0 and 1
		if( intensity > 1.0 ) intensity = 1.0;
		if( intensity < 0.0 ) intensity = 0.0;
		
		int r = (int)(c.getRed() * intensity);
		int g = (int)(c.getGreen() * intensity);
		int b = (int)(c.getBlue() * intensity);
		
		return new Color(r, g, b);
	}

	public static void drawPoint( Graphics g, Point2D p )
	{
		g.drawLine((int)p.x, (int)p.y, (int)p.x, (int)p.y);
	}

	public static void drawLine( Graphics g, Point2D p1, Point2D p2 )
	{
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
	}

	public static double intersection( Point2D p1, Point2D p2, int y0 )
	{
		double a, b, x;

		if( p1.x == p2.x )
			return p1.x;

		a = (p2.y - p1.y) / (p2.x - p1.x);
		b = p1.y - a * p1.x;

		return (y0 - b) / a;
	}

	public static void addDataToPanel( JPanel panel, GridBagLayout gbl, JComponent[] leftComponents, JComponent[] rightComponents )
	{
		if( leftComponents.length != rightComponents.length )
			return;

		GridBagConstraints gbc = new GridBagConstraints();

		for( int i=0; i<leftComponents.length; i++ )
		{
			gbc.anchor    = GridBagConstraints.WEST;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			gbc.fill      = GridBagConstraints.NONE; 
			gbc.insets    = new Insets(8, 8, 8, 5);
			gbc.weightx   = 0.0;

			gbl.setConstraints(leftComponents[i], gbc);
			panel.add(leftComponents[i]);

			gbc.anchor    = GridBagConstraints.WEST;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.fill      = GridBagConstraints.NONE;
			gbc.insets    = new Insets(8, 5, 8, 8);
			gbc.weightx   = 1.0;

			gbl.setConstraints(rightComponents[i], gbc);
			panel.add(rightComponents[i]);
		}
	}
}
