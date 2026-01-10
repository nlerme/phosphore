import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class ObjectsList extends JList<String> implements Observer
{
	private MainWindow m_MainWindow;


	public ObjectsList( MainWindow mw )
	{
		super(new DefaultListModel<String>());
		m_MainWindow = mw;
	}

	public void setListener( MainController c )
	{
		addListSelectionListener(c.new ObjectsListHandler());
	}

	public String getToolTipText( MouseEvent e )
	{
		int index = locationToIndex(e.getPoint());
		Object3D s = m_MainWindow.getModel().getObject(index);

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
		Iterator<Object3D> it        = m_MainWindow.getModel().getIterator();
		boolean wasNoSelection       = (getSelectedIndex() == -1);

		dlm.clear();

		while( it.hasNext() )
		{
			Object3D s = it.next();
			dlm.addElement(s.getType() + " - " + s.getName());
		}

		if( wasNoSelection && !dlm.isEmpty() )
			setSelectedIndex(0);
	}
}
