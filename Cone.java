import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class Cone extends Object3D
{
	double m_Radius;
	int m_NbDivisions;
	double m_Height;


	public Cone()
	{
		this(1.0, 20, 2.0);
	}

	public Cone( double radius, int nbDivisions, double height )
	{
		if( radius <= 0 || nbDivisions <= 0 || height <= 0 )
			return;

		m_Radius      = radius;
		m_NbDivisions = nbDivisions;
		m_Height      = height;

		int indexOfOriginPoint = m_NbDivisions;
		int indexOfApex        = indexOfOriginPoint + 1;
		double halfHeight      = m_Height / 2.0;
		double angle           = (2.0 * Math.PI) / m_NbDivisions;
		Point3D p              = new Point3D(m_Radius * Math.cos(0.0), -halfHeight, m_Radius * Math.sin(0.0));

		for( int i=0; i<m_NbDivisions; i++ )
		{
			m_Geometry.addPoint(p);

			p = new Point3D(m_Radius * Math.cos(angle * (double)(i + 1)), -halfHeight, m_Radius * Math.sin(angle * (double)(i + 1)));

			m_Geometry.addFacet(new Facet(indexOfOriginPoint, (i + 1) % m_NbDivisions, i));
			m_Geometry.addFacet(new Facet(       indexOfApex, i, (i + 1) % m_NbDivisions));
		}

		indexOfOriginPoint = m_Geometry.addPoint(new Point3D(0.0, -halfHeight, 0.0));
		indexOfApex        = m_Geometry.addPoint(new Point3D(0.0, halfHeight,  0.0));

		processNormals();
	}

	public String getType()
	{
		return "Cone";
	}
}
