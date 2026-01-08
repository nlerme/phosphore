import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Rectangle extends Shape3D
{
	Vector2D m_Dimensions;


	public Rectangle()
	{
		this(new Vector2D(1.0, 1.0));
	}

	public Rectangle( Vector2D dimensions )
	{
		if( dimensions.x <= 0 || dimensions.y <= 0 )
			return;

		m_Dimensions = dimensions;

		double halfX = m_Dimensions.x / 2;
		double halfY = m_Dimensions.y / 2;

		// Points
		m_Geometry.addPoint(new Point3D(-halfX,  halfY,  0.0));
		m_Geometry.addPoint(new Point3D( halfX,  halfY,  0.0));
		m_Geometry.addPoint(new Point3D( halfX, -halfY,  0.0));
		m_Geometry.addPoint(new Point3D(-halfX, -halfY,  0.0));

		// Facets
		m_Geometry.addFacet(new Facet(0, 1, 3));
		m_Geometry.addFacet(new Facet(2, 3, 1));

		processNormals();
	}

	public String getType()
	{
		return "Rectangle";
	}

	public double getVolume()
	{
		return 0.0;
	}

	public double getSurface()
	{
		return m_Dimensions.x * m_Dimensions.y;
	}
}
