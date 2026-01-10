import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class Ellipsoid extends Object3D
{
	double m_RadiusX;
	double m_RadiusY;
	double m_RadiusZ;
	int m_Stacks;
	int m_Slices;


	public Ellipsoid()
	{
		this(1.0, 1.0, 1.0, 20, 20);
	}

	public Ellipsoid( double rx, double ry, double rz, int stacks, int slices )
	{
		if( rx <= 0 || ry <= 0 || rz <= 0 || stacks < 2 || slices < 2 )
			return;

		m_RadiusX = rx;
		m_RadiusY = ry;
		m_RadiusZ = rz;
		m_Stacks  = stacks;
		m_Slices  = slices;

		for( int i = 0; i <= m_Stacks; ++i )
		{
			double v   = (double)i / (double)m_Stacks;
			double phi = v * Math.PI;

			for (int j = 0; j <= m_Slices; ++j)
			{
				double u     = (double)j / (double)m_Slices;
				double theta = u * 2.0 * Math.PI;

				double x = m_RadiusX * Math.sin(phi) * Math.cos(theta);
				double y = m_RadiusY * Math.cos(phi);
				double z = m_RadiusZ * Math.sin(phi) * Math.sin(theta);

				m_Geometry.addPoint(new Point3D(x, y, z));
			}
		}

		int ringVertexCount = m_Slices + 1;
		for( int i = 0; i < m_Stacks; ++i )
		{
			for( int j = 0; j < m_Slices; ++j )
			{
				int v0 = i * ringVertexCount + j;
				int v1 = v0 + 1;
				int v2 = (i + 1) * ringVertexCount + j;
				int v3 = v2 + 1;

				m_Geometry.addFacet(new Facet(v0, v1, v2));
				m_Geometry.addFacet(new Facet(v2, v1, v3));
			}
		}

		processNormals();
	}

	public String getType()
	{
		return "Ellipsoid";
	}
}
