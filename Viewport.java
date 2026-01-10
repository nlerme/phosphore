import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class Viewport
{
	private Dimension m_ScreenDimensions;


	public Viewport()
	{
		this(new Dimension(0, 0));
	}

	public Viewport( Dimension screenDimensions )
	{
		m_ScreenDimensions = screenDimensions;
	}

	public void reshape( Dimension screenDimensions )
	{
		m_ScreenDimensions = screenDimensions;
	}

	public Point2D toScreen( Point3D p )
	{
		return new Point2D(
					(m_ScreenDimensions.width / 2)  * (p.x + 1),
					(m_ScreenDimensions.height / 2) * (1 - p.y)
				  );
	}
}
