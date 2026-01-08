import java.awt.*;


public class Camera
{
	private double m_Distance;
	private double m_RotationX; // Elevation
	private double m_RotationY; // Azimuth
	private double m_PanX;      // Target X
	private double m_PanY;      // Target Y
	private double m_PanZ;      // Target Z

	public Camera()
	{
		reset();
	}

	public void reset()
	{
		m_Distance  = Defines.CameraDefaultDistance;
		m_RotationX = 0.0;
		m_RotationY = 0.0;
		m_PanX      = 0.0;
		m_PanY      = 0.0;
		m_PanZ      = 0.0;
	}

	// World -> camera (points)
	public Point3D transform( Point3D p )
	{
		Vector3D v = new Vector3D(p.x-m_PanX, p.y-m_PanY, p.z-m_PanZ);
		v.rotate(new Vector3D(0,1,0), m_RotationY);
		v.rotate(new Vector3D(1,0,0), m_RotationX);
		return v.toPoint3D();
	}

	// View -> world (vectors)
	public Vector3D transformViewToWorld( Vector3D vInput )
	{
		Vector3D v = new Vector3D(vInput.x, vInput.y, vInput.z);
		v.rotate(new Vector3D(1,0,0), -m_RotationX);
		v.rotate(new Vector3D(0,1,0), -m_RotationY);
		return v;
	}

	public void addZoom( double amount )
	{
		m_Distance += amount;

		if( m_Distance < Defines.CameraMinDistance ) 
			m_Distance = Defines.CameraMinDistance;
	}

	public void addOrbit( double dX, double dY )
	{
		m_RotationY += dX;
		m_RotationX += dY;
	}

	public void addPan( double dX, double dY )
	{
		double factor    = m_Distance * Defines.MousePanSensitivity; 
		Vector3D panMove = new Vector3D(dX * factor, -dY * factor, 0);

		panMove.rotate(new Vector3D(1, 0, 0), -m_RotationX);
		panMove.rotate(new Vector3D(0, 1, 0), -m_RotationY);

		m_PanX -= panMove.x;
		m_PanY -= panMove.y;
	}

	public void lookAt( double x, double y, double z, double radius )
	{
		m_PanX     = x;
		m_PanY     = y;
		m_PanZ     = z;
		m_Distance = radius*Defines.FocusRadiusMultiplier;
	}

	public double getDistance()
	{
		return m_Distance;
	}
}
