import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Ellipse extends Shape3D
{
	double m_Radius;
	int m_NbEdges;


	public Ellipse()
	{
		this(0.5, 15);
	}

	public Ellipse( double radius, int nbEdges  )
	{
		if( radius <= 0 || nbEdges <= 0 )
			return;

		m_Radius  = radius;
		m_NbEdges = nbEdges;

		int indexOfOriginPoint = m_NbEdges;
		double angle           = (2.0 * Math.PI) / m_NbEdges;
		Point3D p              = new Point3D(m_Radius * Math.cos(0.0), m_Radius * Math.sin(0.0), 0.0);

		for( int i=0; i<m_NbEdges; i++ )
		{
			m_Geometry.addPoint(p);

			p = new Point3D(m_Radius * Math.cos(angle * (double)(i + 1)), m_Radius * Math.sin(angle * (double)(i + 1)), 0.0);

			m_Geometry.addFacet(new Facet(indexOfOriginPoint, (i + 1) % m_NbEdges, i));
		}

		m_Geometry.addPoint(new Point3D(0.0, 0.0, 0.0));

		processNormals();
	}

	public String getType()
	{
		return "Ellipse";
	}
}
