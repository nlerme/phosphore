import java.awt.*;


public class PerspectiveProjection extends Projection
{
	private double m_DMin;
	private double m_DMax;
	private double m_FovY;


	public PerspectiveProjection( double dMin, double dMax, double cameraDistance, double fovY )
	{
		super();
		m_DMin = dMin;
		m_DMax = dMax;
		m_FovY = fovY;
	}

	public PerspectiveProjection( double dMin, double dMax, double cameraDistance, double fovY, Dimension screenDimensions )
	{
		super(screenDimensions);
		m_DMin = dMin;
		m_DMax = dMax;
		m_FovY = fovY;
	}

	public Point3D transform( Point3D pInput, Camera camera )
	{
		// View transform: World -> Camera
		Point3D p   = camera.transform(pInput);
		double dist = camera.getDistance();

		// Projection transform: Camera -> Screen
		double uMax = m_DMin*Math.tan(Utils.degreesToRadians(m_FovY)/2.0);
		double rMax = m_Rho*uMax;

		// The point 'p' is relative to the pivot. The actual Z distance from the eye is (p.z - dist)
		double zEye = p.z-dist;

		return new Point3D(
					(-p.x * m_DMin) / (rMax * zEye),
					(-p.y * m_DMin) / (uMax * zEye),
					(m_DMax * (zEye - m_DMin)) / (m_DMax - m_DMin) * zEye
				  );
	}

	public Type getType()
	{
		return Type.PERSPECTIVE;
	}
}
