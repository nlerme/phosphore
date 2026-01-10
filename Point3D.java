@SuppressWarnings("deprecation")
public class Point3D implements Cloneable
{
	public double x, y, z;


	public Point3D()
	{
		this(0.0, 0.0, 0.0);
	}

	public Point3D( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D( double[] coords )
	{
		fromArray(coords);
	}

	public double[] toArray()
	{
		double[] result = {x, y, z};
		return result;
	}

	public void fromArray( double[] coords )
	{
		if( coords.length == 3 )
		{
			x = coords[0];
			y = coords[1];
			z = coords[2];
		}
	}

	public Object clone()
	{
		Point3D p = null;

		try
		{
			p = (Point3D)super.clone();
		}
		catch( CloneNotSupportedException e )
		{
			throw (Error)new InternalError().initCause(e);
		}

		return p;
	}

	public void setLocation( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setLocation( Point3D p )
	{
		setLocation(p.x, p.y, p.z);
	}

	public boolean equals( Object o )
	{
		if ( !(o instanceof Point3D) )
			return false;

		Point3D p = (Point3D)o;

		return Math.abs(x - p.x) <= Utils.EPSILON && Math.abs(y - p.y) <= Utils.EPSILON && Math.abs(z - p.z) <= Utils.EPSILON;
	}

	public String toString()
	{
		return "Point2D[" + x + ", " + y + ", " + z + "]";
	}
}
