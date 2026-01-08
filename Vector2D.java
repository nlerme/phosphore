public class Vector2D implements Cloneable
{
	public double x, y;


	public Vector2D()
	{
		this(0.0, 0.0);
	}

	public Vector2D( double x, double y )
	{
		this.x = x;
		this.y = y;
	}

	public Vector2D( double[] coords )
	{
		fromArray(coords);
	}

	public Vector2D( Point2D p )
	{
		x = p.x;
		y = p.y;
	}

	public Vector2D( Point2D p1, Point2D p2 )
	{
		x = p2.x - p1.x;
		y = p2.y - p1.y;
	}

	public Point2D toPoint2D()
	{
		return new Point2D(x, y);
	}

	public static Point3D toPoint3D( Vector3D v )
	{
		return new Point3D(v.x, v.y, v.z);
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

	public static Vector2D add( Vector2D v1, Vector2D v2 )
	{
		return new Vector2D(v1.x + v2.x, v1.y + v2.y);
	}

	public void add( Vector2D v )
	{
		x += v.x;
		y += v.y;
	}

	public static Vector2D unary( Vector2D v )
	{
		return new Vector2D(-v.x, -v.y);
	}

	public void unary()
	{
		x = -x;
		y = -y;
	}

	public static Vector2D substract( Vector2D v1, Vector2D v2 )
	{
		return new Vector2D(v1.x - v2.x, v1.y - v2.y);
	}

	public void substract( Vector2D v )
	{
		x -= v.x;
		y -= v.y;
	}

	public static Vector2D multiplyByScalar( Vector2D v, double k )
	{
		return new Vector2D(v.x * k, v.y * k);
	}

	public void multiplyByScalar( double k )
	{
		x *= k;
		y *= k;
	}

	public static Vector2D divideByScalar( Vector2D v, double k )
	{
		assert(k < -Utils.EPSILON && k > Utils.EPSILON);
		return new Vector2D(v.x / k, v.y / k);
	}

	public void divideByScalar( double k )
	{
		assert(k < -Utils.EPSILON && k > Utils.EPSILON);
		x /= k;
		y /= k;
	}

	public double norm()
	{
		return Math.sqrt(Utils.Sq(x) + Utils.Sq(y));
	}

	public static Vector2D normalize( Vector2D v )
	{
		double n = v.norm();
		assert(n < -Utils.EPSILON && n > Utils.EPSILON);
		return Vector2D.divideByScalar(v, n);
	}

	public void normalize()
	{
		double n = norm();
		assert(n < -Utils.EPSILON && n > Utils.EPSILON);
		divideByScalar(n);
	}

	public double dotProduct( Vector2D v )
	{
		return x * v.x + y * v.y;
	}

	public static double dotProduct( Vector2D v1, Vector2D v2 )
	{
		return v1.x * v2.x + v1.y * v2.y;
	}

	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch( CloneNotSupportedException e )
		{
			throw (Error)new InternalError().initCause(e);
		}
	}

	public void setLocation( double x, double y )
	{
		this.x = x;
		this.y = y;
	}

	public void setLocation( Vector2D v )
	{
		setLocation(v.x, v.y);
	}

	public boolean equals( Object o )
	{
		if ( !(o instanceof Vector2D) )
			return false;

		Vector2D v = (Vector2D)o;

		return Math.abs(x - v.x) <= Utils.EPSILON && Math.abs(y - v.y) <= Utils.EPSILON;
	}

	public String toString()
	{
		return "Vector2D[" + x + ", " + y + "]";
	}
}
