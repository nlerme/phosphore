import java.awt.*;
import java.util.*;
import java.awt.geom.*;


public class AABB
{
	private Point3D m_Min;
	private Point3D m_Max;

	public AABB()
	{
		reset();
	}

	public void reset()
	{
		m_Min = new Point3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		m_Max = new Point3D(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
	}

	public void update( Geometry geo )
	{
		reset();
		Iterator<Point3D> it = geo.getPointsListIterator();
		
		while( it.hasNext() )
		{
			Point3D p = it.next();
			
			if( p.x < m_Min.x ) m_Min.x = p.x;
			if( p.y < m_Min.y ) m_Min.y = p.y;
			if( p.z < m_Min.z ) m_Min.z = p.z;
			
			if( p.x > m_Max.x ) m_Max.x = p.x;
			if( p.y > m_Max.y ) m_Max.y = p.y;
			if( p.z > m_Max.z ) m_Max.z = p.z;
		}
	}

	public void draw( Graphics2D g2d, Viewport viewport, Projection projection, Camera camera, Color color )
	{
		if( m_Min.x > m_Max.x )
			return;

		Point3D[] corners = new Point3D[8];
		corners[0] = new Point3D(m_Min.x, m_Min.y, m_Min.z);
		corners[1] = new Point3D(m_Max.x, m_Min.y, m_Min.z);
		corners[2] = new Point3D(m_Max.x, m_Max.y, m_Min.z);
		corners[3] = new Point3D(m_Min.x, m_Max.y, m_Min.z);
		corners[4] = new Point3D(m_Min.x, m_Min.y, m_Max.z);
		corners[5] = new Point3D(m_Max.x, m_Min.y, m_Max.z);
		corners[6] = new Point3D(m_Max.x, m_Max.y, m_Max.z);
		corners[7] = new Point3D(m_Min.x, m_Max.y, m_Max.z);

		Point2D[] projCorners = new Point2D[8];

		for( int i=0; i<8; i++ )
		{
			projCorners[i] = viewport.toScreen(projection.transform(corners[i], camera));
		}

		g2d.setColor(color);
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));

		// Bottom face
		Utils.drawLine(g2d, projCorners[0], projCorners[1]);
		Utils.drawLine(g2d, projCorners[1], projCorners[2]);
		Utils.drawLine(g2d, projCorners[2], projCorners[3]);
		Utils.drawLine(g2d, projCorners[3], projCorners[0]);

		// Top face
		Utils.drawLine(g2d, projCorners[4], projCorners[5]);
		Utils.drawLine(g2d, projCorners[5], projCorners[6]);
		Utils.drawLine(g2d, projCorners[6], projCorners[7]);
		Utils.drawLine(g2d, projCorners[7], projCorners[4]);

		// Connecting edges
		Utils.drawLine(g2d, projCorners[0], projCorners[4]);
		Utils.drawLine(g2d, projCorners[1], projCorners[5]);
		Utils.drawLine(g2d, projCorners[2], projCorners[6]);
		Utils.drawLine(g2d, projCorners[3], projCorners[7]);
		
		g2d.setStroke(oldStroke);
	}
}
