import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class Cuboid extends Object3D
{
	Vector3D m_Dimensions;


	public Cuboid()
	{
		this(new Vector3D(1.0, 1.0, 1.0));
	}

	public Cuboid( Vector3D dimensions )
	{
		if( dimensions.x <= 0 || dimensions.y <= 0 || dimensions.z <= 0 )
			return;

		m_Dimensions = dimensions;

		double halfX = m_Dimensions.x / 2.0;
		double halfY = m_Dimensions.y / 2.0;
		double halfZ = m_Dimensions.z / 2.0;

		m_Geometry.addPoint(new Point3D(-halfX,  halfY,  halfZ));
		m_Geometry.addPoint(new Point3D( halfX,  halfY,  halfZ));
		m_Geometry.addPoint(new Point3D( halfX, -halfY,  halfZ));
		m_Geometry.addPoint(new Point3D(-halfX, -halfY,  halfZ));
		m_Geometry.addPoint(new Point3D(-halfX,  halfY, -halfZ));
		m_Geometry.addPoint(new Point3D( halfX,  halfY, -halfZ));
		m_Geometry.addPoint(new Point3D( halfX, -halfY, -halfZ));
		m_Geometry.addPoint(new Point3D(-halfX, -halfY, -halfZ));

		// Front face
		m_Geometry.addFacet(new Facet(0, 1, 3));
		m_Geometry.addFacet(new Facet(2, 3, 1));

		// Back face
		m_Geometry.addFacet(new Facet(4, 7, 5));
		m_Geometry.addFacet(new Facet(6, 5, 7));

		// Left face
		m_Geometry.addFacet(new Facet(4, 0, 7));
		m_Geometry.addFacet(new Facet(3, 7, 0));

		// Right face
		m_Geometry.addFacet(new Facet(1, 5, 2));
		m_Geometry.addFacet(new Facet(6, 2, 5));

		// Top face
		m_Geometry.addFacet(new Facet(4, 5, 0));
		m_Geometry.addFacet(new Facet(1, 0, 5));

		// Bottom face
		m_Geometry.addFacet(new Facet(2, 6, 3));
		m_Geometry.addFacet(new Facet(7, 3, 6));

		processNormals();
	}

	public String getType()
	{
		return "Cuboid";
	}
}
