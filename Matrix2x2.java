public class Matrix2x2 implements Cloneable
{
	double[][] m_Data;


	public Matrix2x2()
	{
		m_Data = new double[2][2];
		setIdentity();
	}

	public Matrix2x2( double m1, double m2, double m3, double m4 )
	{
		m_Data = new double[2][2];

		m_Data[0][0] = m1;
		m_Data[0][1] = m2;

		m_Data[1][0] = m3;
		m_Data[1][1] = m4;
	}

	public void setIdentity()
	{
		m_Data[0][0] = 1.0;
		m_Data[0][1] = 0.0;

		m_Data[1][0] = 0.0;
		m_Data[1][1] = 1.0;
	}

	public double get( int i, int j )
	{
		assert((i >= 0 && i <= 1) && (j >= 0 && j <= 1));
		return m_Data[i][j];
	}

	public void set( int i, int j, double value )
	{
		assert((i >= 0 && i <= 1) && (j >= 0 && j <= 1));
		m_Data[i][j] = value;
	}

	public double[][] getData()
	{
		return m_Data;
	}

	public Vector2D multiply( Vector2D v )
	{
		return new Vector2D(
					v.x * m_Data[0][0] + v.y * m_Data[0][1],
					v.x * m_Data[1][0] + v.y * m_Data[1][1]
				   );
	}

	public static Vector2D multiply( Matrix2x2 m, Vector2D v )
	{
		double[][] data = m.getData();

		return new Vector2D(
					v.x * data[0][0] + v.y * data[0][1],
					v.x * data[1][0] + v.y * data[1][1]
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
		StringBuffer result = new StringBuffer("Matrix2x2\n");

		for( int i=0; i<2; i++ )
			result.append("[" + m_Data[i][0] + " " + m_Data[i][1] + "]\n");

		return result.toString();
	}

	public boolean equals( Object o )
	{
		if ( !(o instanceof Matrix2x2) )
			return false;

		Matrix2x2 m = (Matrix2x2)o;

		for( int i=0; i<2; i++ )
			for( int j=0; j<2; j++ )
				if( get(i, j) != m.get(i, j) )
					return false;

		return true;
	}
}
