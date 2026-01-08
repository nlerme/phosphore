public class Matrix3x3 implements Cloneable
{
	double[][] m_Data;


	public Matrix3x3()
	{
		m_Data = new double[3][3];
		setIdentity();
	}

	public Matrix3x3( double m1, double m2, double m3, double m4, double m5, double m6, double m7, double m8, double m9 )
	{
		m_Data = new double[3][3];

		m_Data[0][0] = m1;
		m_Data[0][1] = m2;
		m_Data[0][2] = m3;

		m_Data[1][0] = m4;
		m_Data[1][1] = m5;
		m_Data[1][2] = m6;

		m_Data[2][0] = m7;
		m_Data[2][1] = m8;
		m_Data[2][2] = m9;
	}

	public void setIdentity()
	{
		m_Data[0][0] = 1.0;
		m_Data[0][1] = 0.0;
		m_Data[0][2] = 0.0;

		m_Data[1][0] = 0.0;
		m_Data[1][1] = 1.0;
		m_Data[1][2] = 0.0;

		m_Data[2][0] = 0.0;
		m_Data[2][1] = 0.0;
		m_Data[2][2] = 1.0;
	}

	public static Matrix3x3 rotationMatrix( Vector3D axis, double angle )
	{
		double angleConverted = Utils.degreesToRadians(angle);
		double cos            = Math.cos(angleConverted);
		double sin            = Math.sin(angleConverted);

		return new Matrix3x3(
					     1 + (1 - cos) * (Utils.Sq(axis.x) - 1), -axis.z * sin + (1 - cos) * axis.x * axis.y,  axis.y * sin + (1 - cos) * axis.x * axis.z,
					 axis.z * sin + (1 - cos) * axis.x * axis.y,      1 + (1 - cos) * (Utils.Sq(axis.y) - 1), -axis.x * sin + (1 - cos) * axis.y * axis.z,
					-axis.y * sin + (1 - cos) * axis.x * axis.z,  axis.x * sin + (1 - cos) * axis.y * axis.z,      1 + (1 - cos) * (Utils.Sq(axis.z) - 1)
				    );
	}

	public double get( int i, int j )
	{
		assert((i >= 0 && i <= 2) && (j >= 0 && j <= 2));
		return m_Data[i][j];
	}

	public void set( int i, int j, double value )
	{
		assert((i >= 0 && i <= 2) && (j >= 0 && j <= 2));
		m_Data[i][j] = value;
	}

	public double[][] getData()
	{
		return m_Data;
	}

	public Vector3D multiply( Vector3D v )
	{
		return new Vector3D(
					v.x * m_Data[0][0] + v.y * m_Data[0][1] + v.z * m_Data[0][2],
					v.x * m_Data[1][0] + v.y * m_Data[1][1] + v.z * m_Data[1][2],
					v.x * m_Data[2][0] + v.y * m_Data[2][1] + v.z * m_Data[2][2]
				   );
	}

	public static Vector3D multiply( Matrix3x3 m, Vector3D v )
	{
		double[][] data = m.getData();

		return new Vector3D(
					v.x * data[0][0] + v.y * data[0][1] + v.z * data[0][2],
					v.x * data[1][0] + v.y * data[1][1] + v.z * data[1][2],
					v.x * data[2][0] + v.y * data[2][1] + v.z * data[2][2]
				   );
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

	public String toString()
	{
		StringBuffer result = new StringBuffer("Matrix3x3\n");

		for( int i=0; i<3; i++ )
			result.append("[" + m_Data[i][0] + " " + m_Data[i][1] + " " + m_Data[i][2] + "]\n");

		return result.toString();
	}

	public boolean equals( Object o )
	{
		if ( !(o instanceof Matrix3x3) )
			return false;

		Matrix3x3 m = (Matrix3x3)o;

		for( int i=0; i<3; i++ )
			for( int j=0; j<3; j++ )
				if( get(i, j) != m.get(i, j) )
					return false;

		return true;
	}
}
