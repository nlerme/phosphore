import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Cylinder extends Shape3D
{
	double m_Radius;
	int m_NbDivisions;
	double m_Height;


	public Cylinder()
	{
		this(0.5, 12, 1.0);
	}

	public Cylinder( double radius, int nbDivisions, double height )
	{
		if( radius <= 0 || nbDivisions <= 0 || height <= 0 )
			return;

		m_Radius      = radius;
		m_NbDivisions = nbDivisions;
		m_Height      = height;

		int indexOfNegativePoint = 2 * m_NbDivisions;
		int indexOfPositivePoint = indexOfNegativePoint + 1;
		double halfHeight        = m_Height / 2.0;
		double angle             = (2.0 * Math.PI) / m_NbDivisions;
		Point3D p1               = new Point3D(m_Radius * Math.cos(0.0), -halfHeight, m_Radius * Math.sin(0.0));
		Point3D p2               = new Point3D(m_Radius * Math.cos(0.0),  halfHeight, m_Radius * Math.sin(0.0));

		for( int i=0; i<m_NbDivisions; i++ )
		{
			m_Geometry.addPoint(p1);
			m_Geometry.addPoint(p2);

			p1 = new Point3D(m_Radius * Math.cos(angle * (double)(i + 1)), -halfHeight, m_Radius * Math.sin(angle * (double)(i + 1)));
			p2 = new Point3D(m_Radius * Math.cos(angle * (double)(i + 1)),  halfHeight, m_Radius * Math.sin(angle * (double)(i + 1)));

			m_Geometry.addFacet(new Facet(indexOfNegativePoint, (2 * i + 2) % (2 * m_NbDivisions), 2 * i));
			m_Geometry.addFacet(new Facet(indexOfPositivePoint, 2 * i + 1, (2 * i + 3) % (2 * m_NbDivisions)));
			m_Geometry.addFacet(new Facet(2 * i, (2 * i + 2) % (2 * m_NbDivisions), 2 * i + 1));
			m_Geometry.addFacet(new Facet((2 * i + 3) % (2 * m_NbDivisions), 2 * i + 1, (2 * i + 2) % (2 * m_NbDivisions)));
		}

		m_Geometry.addPoint(new Point3D(0.0, -halfHeight, 0.0));
		m_Geometry.addPoint(new Point3D(0.0,  halfHeight, 0.0));

		processNormals();
	}

	public String getType()
	{
		return "Cylinder";
	}
}
