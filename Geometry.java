import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Geometry implements Cloneable
{
	private Vector<Point3D> m_PointsList;
	private Vector<Vector3D> m_VertexNormals; // [NOUVEAU] Normales par sommet
	private Vector<Facet> m_FacetsList;
	private AABB m_AABB;


	public Geometry()
	{
		m_PointsList    = new Vector<Point3D>();
		m_VertexNormals = new Vector<Vector3D>();
		m_FacetsList    = new Vector<Facet>();
		m_AABB          = new AABB();
	}

	public Geometry( Vector<Point3D> pointsList, Vector<Facet> facetsList )
	{
		m_PointsList = pointsList;
		m_FacetsList = facetsList;
		
		// Init vertex normals with 0
		m_VertexNormals = new Vector<Vector3D>();
		for( int i=0; i<m_PointsList.size(); i++ )
			m_VertexNormals.add(new Vector3D(0, 0, 0));

		m_AABB = new AABB();
		updateAABB();
	}

	public AABB getAABB()
	{
		return m_AABB;
	}

	public void updateAABB()
	{
		m_AABB.update(this);
	}

	public int getNumberOfPoints()
	{
		return m_PointsList.size();
	}

	public int getNumberOfFacets()
	{
		return m_FacetsList.size();
	}

	private boolean isValidIndexForPointsList( int index )
	{
		return m_PointsList.size() > 0 && index >= 0 && index < m_PointsList.size();
	}

	private boolean isValidIndexForFacetsList( int index )
	{
		return m_FacetsList.size() > 0 && index >= 0 && index < m_FacetsList.size();
	}

	public int addPoint( Point3D p )
	{
		m_PointsList.add(p);
		m_VertexNormals.add(new Vector3D(0, 0, 0)); // Maintain sync
		return getNumberOfPoints() - 1;
	}

	public int addFacet( Facet f )
	{
		m_FacetsList.add(f);
		return getNumberOfFacets() - 1;
	}

	public void removePoint( int index )
	{
		if( isValidIndexForPointsList(index) )
		{
			m_PointsList.remove(index);
			m_VertexNormals.remove(index);
		}
	}

	public void removeFacet( int index, Facet f )
	{
		if( isValidIndexForFacetsList(index) )
			m_FacetsList.remove(index);
	}

	public void setPoint( int index, Point3D p )
	{
		if( isValidIndexForPointsList(index) )
			m_PointsList.set(index, p);
	}

	// [NOUVEAU] Gestion des normales de sommets
	public void setVertexNormal( int index, Vector3D n )
	{
		if( isValidIndexForPointsList(index) )
			m_VertexNormals.set(index, n);
	}

	public Vector3D getVertexNormal( int index )
	{
		if( isValidIndexForPointsList(index) )
			return m_VertexNormals.get(index);
		return new Vector3D(0, 1, 0);
	}
	
	public Iterator<Vector3D> getVertexNormalsIterator()
	{
		return m_VertexNormals.listIterator();
	}

	public void setFacet( int index, Facet f )
	{
		if( isValidIndexForFacetsList(index) )
			m_FacetsList.set(index, f);
	}

	public Point3D getPoint( int index )
	{
		if( isValidIndexForPointsList(index) )
			return m_PointsList.get(index);

		return null;
	}

	public Facet getFacet( int index )
	{
		if( isValidIndexForFacetsList(index) )
			return m_FacetsList.get(index);

		return null;
	}

	public void clearPointsList()
	{
		m_PointsList.clear();
		m_VertexNormals.clear();
	}

	public void clearFacetsList()
	{
		m_FacetsList.clear();
	}

	public Iterator<Point3D> getPointsListIterator()
	{
		return m_PointsList.listIterator();
	}

	public Iterator<Facet> getFacetsListIterator()
	{
		return m_FacetsList.listIterator();
	}
}
