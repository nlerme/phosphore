import java.awt.*;


@SuppressWarnings("deprecation")
public abstract class Projection
{
	protected double m_Rho;

	public static enum Type
	{
		PERSPECTIVE,
		ORTHOGRAPHIC
	}

	public Projection()
	{
		m_Rho = 0.0;
	}

	public Projection( Dimension screenDimensions )
	{
		processRho(screenDimensions);
	}

	public void processRho( Dimension screenDimensions )
	{
		if( screenDimensions.width >= screenDimensions.height )
			m_Rho = (double)screenDimensions.width / (double)screenDimensions.height;
		else
			m_Rho = (double)screenDimensions.height / (double)screenDimensions.width;
	}

	public void reshape( Dimension screenDimensions )
	{
		processRho(screenDimensions);
	}

	public abstract Point3D transform( Point3D p, Camera camera );
	
	public abstract Type getType();
}
