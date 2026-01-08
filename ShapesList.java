import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class ShapesList extends JList<String> implements Observer
{
	private MainWindow m_MainWindow;


	public ShapesList( MainWindow mw )
	{
		super(new DefaultListModel<String>());
		m_MainWindow = mw;
	}

	public void setListener( MainController c )
	{
		addListSelectionListener(c.new ShapesListHandler());
	}

	public String getToolTipText( MouseEvent e )
	{
		int index = locationToIndex(e.getPoint());
		Shape3D s = m_MainWindow.getModel().getShape(index);

		if( s != null )
		{
			Geometry geo = s.getGeometry();
			return geo.getNumberOfPoints() + " vertices, " + geo.getNumberOfFacets() + " facets";
		}

		return "";
	}

	public void update( Observable o, Object arg )
	{
		DefaultListModel<String> dlm = ((DefaultListModel<String>)getModel());
		Iterator<Shape3D> it         = m_MainWindow.getModel().getIterator();

		dlm.clear();

		while( it.hasNext() )
		{
			Shape3D s = it.next();
			dlm.addElement(s.getType() + " - " + s.getName());
		}
	}
}
