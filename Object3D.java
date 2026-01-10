import java.awt.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class Object3D
{
	protected Geometry m_Geometry;
	protected Appearance m_Appearance;
	protected String m_Name;


	public Object3D()
	{
		m_Geometry   = new Geometry();
		m_Appearance = new Appearance();
		m_Name       = "unknown";
	}
	
	public String getType()
	{
		return "custom";
	}

	public Appearance getAppearance()
	{
		return m_Appearance;
	}

	public Geometry getGeometry()
	{
		return m_Geometry;
	}

	public AABB getAABB()
	{
		return m_Geometry.getAABB();
	}

	public String getName()
	{
		return m_Name;
	}

	public void setAppearance( Appearance a )
	{
		m_Appearance = a;
	}

	public void setGeometry( Geometry g )
	{
		m_Geometry = g;
		m_Geometry.updateAABB();
	}

	public void setName( String name )
	{
		m_Name = name;
	}
	
	public void setFaceColor( Color c )
	{
		m_Appearance.setFaceColor(c);
		
		Iterator<Facet> it = m_Geometry.getFacetsListIterator();
		int i = 0;
		while( it.hasNext() )
		{
			Facet f = it.next();
			f.color = c;
			m_Geometry.setFacet(i, f);
			i++;
		}
	}

	public Point3D getCenter()
	{
		Iterator<Point3D> it = m_Geometry.getPointsListIterator();
		double x = 0, y = 0, z = 0;
		int count = 0;

		while( it.hasNext() )
		{
			Point3D p = it.next();
			x += p.x;
			y += p.y;
			z += p.z;
			count++;
		}

		if( count == 0 ) return new Point3D(0, 0, 0);

		return new Point3D(x / count, y / count, z / count);
	}

	public double getBoundingRadius()
	{
		Point3D center       = getCenter();
		Iterator<Point3D> it = m_Geometry.getPointsListIterator();
		double maxDistSq     = 0.0;

		while( it.hasNext() )
		{
			Point3D p     = it.next();
			double distSq = Utils.Sq(p.x-center.x) + Utils.Sq(p.y-center.y) + Utils.Sq(p.z-center.z);

			if( distSq > maxDistSq )
				maxDistSq = distSq;
		}

		return Math.sqrt(maxDistSq);
	}

	public void processNormals()
	{
		for( int i=0; i<m_Geometry.getNumberOfPoints(); i++ )
			m_Geometry.setVertexNormal(i, new Vector3D(0.0, 0.0, 0.0));

		Iterator<Facet> itFacets = m_Geometry.getFacetsListIterator();
		int i = 0;

		while( itFacets.hasNext() )
		{
			Facet f    = itFacets.next();
			Point3D p1 = m_Geometry.getPoint(f.v1);
			Point3D p2 = m_Geometry.getPoint(f.v2);
			Point3D p3 = m_Geometry.getPoint(f.v3);

			if( p1 != null && p2 != null && p3 != null )
			{
				Vector3D edge1 = new Vector3D(p1, p2);
				Vector3D edge2 = new Vector3D(p1, p3);
				
				// [CORRECTION] Inversion de l'ordre (edge2, edge1) pour pointer vers l'extérieur
				Vector3D faceNormal = Vector3D.normalize(Vector3D.crossProduct(edge2, edge1));
				
				f.normal = faceNormal;
				m_Geometry.setFacet(i, f);

				m_Geometry.getVertexNormal(f.v1).add(faceNormal);
				m_Geometry.getVertexNormal(f.v2).add(faceNormal);
				m_Geometry.getVertexNormal(f.v3).add(faceNormal);
			}
			++i;
		}

		Iterator<Vector3D> itNormals = m_Geometry.getVertexNormalsIterator();
		while( itNormals.hasNext() )
		{
			itNormals.next().normalize();
		}
	}

	public void translate( Vector3D t )
	{
		Iterator<Point3D> it = m_Geometry.getPointsListIterator();
		int i = 0;

		while( it.hasNext() )
		{
			Vector3D v = new Vector3D(it.next());
			v.add(t);
			m_Geometry.setPoint(i, v.toPoint3D());
			++i;
		}

		processNormals();
		m_Geometry.updateAABB();
	}

	public void scale( Vector3D s )
	{
		Iterator<Point3D> it = m_Geometry.getPointsListIterator();
		int i = 0;

		while( it.hasNext() )
		{
			Vector3D v = new Vector3D(it.next());
			v.x *= s.x;
			v.y *= s.y;
			v.z *= s.z;
			m_Geometry.setPoint(i, v.toPoint3D());
			++i;
		}

		processNormals();
		m_Geometry.updateAABB();
	}

	public void rotate( Vector3D axis, double angle )
	{
		Iterator<Point3D> it = m_Geometry.getPointsListIterator();
		int i = 0;

		while( it.hasNext() )
		{
			Vector3D v = new Vector3D(it.next());
			v.rotate(axis, angle);
			m_Geometry.setPoint(i, v.toPoint3D());
			++i;
		}

		processNormals();
		m_Geometry.updateAABB();
	}
}
