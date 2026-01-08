import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class Facet
{
	public int v1, v2, v3;
	public Vector3D normal;
	public Color color;


	Facet()
	{
		this(0, 0, 0);
	}

	Facet( int v1, int v2, int v3 )
	{
		this(v1, v2, v3, Defines.DefaultFaceColor, null);
	}

	Facet( int v1, int v2, int v3, Color color, Vector3D normal )
	{
		this.v1     = v1;
		this.v2     = v2;
		this.v3     = v3;
		this.color  = color;
		this.normal = normal;
	}

	public String toString()
	{
		return "Facet[" + v1 + ", " + v2 + ", " + v3 + ", " + normal + ", " + color + "]\n";
	}
}
