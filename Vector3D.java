@SuppressWarnings("deprecation")
public class Vector3D implements Cloneable
{
	public double x, y, z;


	public Vector3D()
	{
		this(0.0, 0.0, 0.0);
	}

	public Vector3D( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D( double[] coords )
	{
		fromArray(coords);
	}

	public Vector3D( Point3D p )
	{
		x = p.x;
		y = p.y;
		z = p.z;
	}

	public Vector3D( Point3D p1, Point3D p2 )
	{
		x = p2.x - p1.x;
		y = p2.y - p1.y;
		z = p2.z - p1.z;
	}

	public Point3D toPoint3D()
	{
		return new Point3D(x, y, z);
	}

	public static Point3D toPoint3D( Vector3D v )
	{
		return new Point3D(v.x, v.y, v.z);
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

	public static Vector3D rotate( Vector3D v, Vector3D axis, double angle )
	{
		return Matrix3x3.multiply(Matrix3x3.rotationMatrix(axis, angle), v);
	}

	public void rotate( Vector3D axis, double angle )
	{
		Vector3D v = Matrix3x3.multiply(Matrix3x3.rotationMatrix(axis, angle), this);
		x          = v.x;
		y          = v.y;
		z          = v.z;
	}

	public static Vector3D add( Vector3D v1, Vector3D v2 )
	{
		return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	public void add( Vector3D v )
	{
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public static Vector3D unary( Vector3D v )
	{
		return new Vector3D(-v.x, -v.y, -v.z);
	}

	public void unary()
	{
		x = -x;
		y = -y;
		z = -z;
	}

	public static Vector3D substract( Vector3D v1, Vector3D v2 )
	{
		return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	public void substract( Vector3D v )
	{
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}

	public static Vector3D multiplyByScalar( Vector3D v, double k )
	{
		return new Vector3D(v.x * k, v.y * k, v.z * k);
	}

	public void multiplyByScalar( double k )
	{
		x *= k;
		y *= k;
		z *= k;
	}

	public static Vector3D divideByScalar( Vector3D v, double k )
	{
		assert(k < -Utils.EPSILON && k > Utils.EPSILON);
		return new Vector3D(v.x / k, v.y / k, v.z / k);
	}

	public void divideByScalar( double k )
	{
		assert(k < -Utils.EPSILON && k > Utils.EPSILON);
		x /= k;
		y /= k;
		z /= k;
	}

	public double norm()
	{
		return Math.sqrt(Utils.Sq(x) + Utils.Sq(y) + Utils.Sq(z));
	}

	public static Vector3D normalize( Vector3D v )
	{
		double n = v.norm();
		assert(n < -Utils.EPSILON && n > Utils.EPSILON);
		return Vector3D.divideByScalar(v, n);
	}

	public void normalize()
	{
		double n = norm();
		assert(n < -Utils.EPSILON && n > Utils.EPSILON);
		divideByScalar(n);
	}

	public double dotProduct( Vector3D v )
	{
		return x * v.x + y * v.y + z * v.z;
	}

	public static double dotProduct( Vector3D v1, Vector3D v2 )
	{
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	public static Vector3D crossProduct( Vector3D v1, Vector3D v2 )
	{
		return new Vector3D(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z, v1.x * v2.y - v1.y * v2.x);
	}

	public void crossProduct( Vector3D v )
	{
		x = y * v.z - z * v.y;
		y = z * v.x - x * v.z;
		z = x * v.y - y * v.x;
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

	public void setLocation( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setLocation( Vector3D v )
	{
		setLocation(v.x, v.y, v.z);
	}

	public boolean equals( Object o )
	{
		if ( !(o instanceof Vector3D) )
			return false;

		Vector3D v = (Vector3D)o;

		return Math.abs(x - v.x) <= Utils.EPSILON && Math.abs(y - v.y) <= Utils.EPSILON && Math.abs(z - v.z) <= Utils.EPSILON;
	}

	public String toString()
	{
		return "Vector3D[" + x + ", " + y + ", " + z + "]";
	}
}
