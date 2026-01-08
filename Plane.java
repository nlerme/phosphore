import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Plane
{
	public Vector3D n;
	public double d;


	public Plane( double a, double b, double c, double d )
	{
		this.n = new Vector3D(a, b, c);
		this.d = d;
	}

	public Plane( Vector3D n, double d )
	{
		this.n = n;
		this.d = d;
	}

	public Point3D findIntersection( Line l )
	{
		Vector3D a = new Vector3D(l.end, l.start);
		double t   = -(d + Vector3D.dotProduct(n, new Vector3D(l.end))) / Vector3D.dotProduct(n, a);
		return Vector3D.toPoint3D(Vector3D.add(Vector3D.multiplyByScalar(new Vector3D(l.start), t), Vector3D.multiplyByScalar(new Vector3D(l.end), 1.0 - t)));
	}
}
