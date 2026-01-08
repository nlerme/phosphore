import java.awt.*;


public class OrthographicProjection extends Projection
{
	public OrthographicProjection( double cameraDistance )
	{
		super();
	}

	public OrthographicProjection( Dimension screenDimensions )
	{
		super(screenDimensions);
	}

	public Point3D transform( Point3D pInput, Camera camera )
	{
		// View Transform: World -> Camera
		Point3D p = camera.transform(pInput);

		// Projection transform: Camera -> Screen (zoom is just inverse scaling)
		double zoom = 1.0/camera.getDistance();

		return Vector3D.multiplyByScalar(new Vector3D(p), zoom).toPoint3D();
	}

	public Type getType()
	{
		return Type.ORTHOGRAPHIC;
	}
}
