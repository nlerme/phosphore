public class Point2D implements Cloneable
{
	public double x, y;


	public Point2D()
	{
		this(0.0, 0.0);
	}

	public Point2D( double x, double y )
	{
		this.x = x;
		this.y = y;
	}

	public double[] toArray()
	{
		double[] result = {x, y};
		return result;
	}

	public void fromArray( double[] coords )
	{
		if( coords.length == 2 )
		{
			x = coords[0];
			y = coords[1];
		}
	}

	public Object clone()
	{
		Point2D p = null;

		try
		{
			p = (Point2D)super.clone();
		}
		catch( CloneNotSupportedException e )
		{
			throw (Error)new InternalError().initCause(e);
		}

		return p;
	}

	public void setLocation( double x, double y )
	{
		this.x = x;
		this.y = y;
	}

	public void setLocation( Point2D p )
	{
		setLocation(p.x, p.y);
	}

	public boolean equals( Object o )
	{
		if ( !(o instanceof Point2D) )
			return false;

		Point2D p = (Point2D)o;

		return Math.abs(x - p.x) <= Utils.EPSILON && Math.abs(y - p.y) <= Utils.EPSILON;
	}

	public String toString()
	{
		return "Point2D[" + x + ", " + y + "]";
	}
}
