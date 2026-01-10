import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("unchecked")
public class MainController
{
	private Model m_Model;
	private MainWindow m_MainWindow;


	@SuppressWarnings("deprecation")
	public MainController( Model m, MainWindow w )
	{
		m_Model      = m;
		m_MainWindow = w;

		// Linking between model and view
		m_MainWindow.setListener(this);
		m_Model.addObserver(m_MainWindow.getObjectsList());
		m_Model.addObserver(m_MainWindow.getDrawingPanel());
	}

	public class ObjectsListToolBar_AddObjectHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			AddingObjectDialog asd         = new AddingObjectDialog(m_MainWindow, "Add an object");
			AddingObjectDialogController c = new AddingObjectDialogController(m_Model, asd);

			asd.pack();
			asd.setSize(asd.getPreferredSize());
			asd.setVisible(true);
		}
	}

	public class ObjectsListToolBar_ObjectInfoHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			int index = m_MainWindow.getObjectsList().getSelectedIndex();

			if( index < 0 )
				return;

			Object3D s = m_Model.getObject(index);

			if( s==null )
				return;

			ObjectInfoDialog sid = new ObjectInfoDialog(m_MainWindow, "Object Information");
			Geometry geo         = s.getGeometry();

			sid.setInfos(s.getName(), geo.getNumberOfPoints(), geo.getNumberOfFacets());

			ObjectInfoDialogController c = new ObjectInfoDialogController(sid);

			sid.pack();

			if( sid.getWidth()<250 )
				sid.setSize(250, sid.getHeight());
			
			sid.setVisible(true);
		}
	}

	public class ObjectsListToolBar_ModifyObjectHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			int index = m_MainWindow.getObjectsList().getSelectedIndex();

			if( index < 0 )
				return;

			ModifyingObjectDialog msd         = new ModifyingObjectDialog(m_MainWindow, "Modify an object");
			ModifyingObjectDialogController c = new ModifyingObjectDialogController(m_Model, msd);

			msd.pack();
			msd.setSize(msd.getPreferredSize());
			msd.setVisible(true);
		}
	}

	public class ObjectsListToolBar_DeleteObjectHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			m_Model.removeObject(m_MainWindow.getObjectsList().getSelectedIndex());
		}
	}

	public class ObjectsListToolBar_DeleteAllObjectsHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			m_Model.removeAllObjects();
		}
	}

	public class ObjectsListHandler implements ListSelectionListener
	{
		public void valueChanged( ListSelectionEvent e )
		{
			m_MainWindow.getDrawingPanel().repaint();
		}
	}

	public class MenuBar_QuitHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JMenuItem mi = (JMenuItem)e.getSource();
			System.exit(0);
		}
	}

	public class MenuBar_VersionHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JMenuItem mi = (JMenuItem)e.getSource();
			JOptionPane.showMessageDialog(m_MainWindow, Defines.SoftwareName + " - " + Defines.SoftwareVersion, "Version", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public class MenuBar_AboutHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JMenuItem mi = (JMenuItem)e.getSource();

			AboutDialog ad          = new AboutDialog(m_MainWindow, "About");
			AboutDialogController c = new AboutDialogController(m_Model, ad);

			ad.pack();
			ad.setSize(ad.getPreferredSize());
			ad.setVisible(true);
		}
	}

	public class MenuBar_PreferencesHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JMenuItem mi = (JMenuItem)e.getSource();

			PreferencesDialog pd          = new PreferencesDialog(m_MainWindow, "Preferences");
			PreferencesDialogController c = new PreferencesDialogController(m_Model, pd);

			pd.pack();
			pd.setSize(pd.getPreferredSize());
			pd.setVisible(true);
		}
	}

	public class MenuBar_UserInteractionsHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JMenuItem mi = (JMenuItem)e.getSource();

			UserInteractionsDialog uid          = new UserInteractionsDialog(m_MainWindow, "User Interactions");
			UserInteractionsDialogController c  = new UserInteractionsDialogController(uid);

			uid.pack();
			uid.setSize(uid.getPreferredSize());
			uid.setVisible(true);
		}
	}

	public class MainToolBar_FocusHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_MainWindow.getDrawingPanel().performFocus();
		}
	}

	public class MainToolBar_ToggleAABBHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_MainWindow.getDrawingPanel().toggleAABB();
		}
	}

	public class MainToolBar_ToggleNormalsHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			DrawingPanel dp = m_MainWindow.getDrawingPanel();
			dp.setDisplayNormalsState(!dp.getDisplayNormalsState());
			dp.repaint();
		}
	}
	
	public class MainToolBar_DrawingModeHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			DrawingPanel dp      = m_MainWindow.getDrawingPanel();
			int idx              = cb.getSelectedIndex();
			
			if( idx == 0 )
				dp.setDrawingMode(Defines.DrawingMode.WIREFRAME);
			else if( idx == 1 )
				dp.setDrawingMode(Defines.DrawingMode.FILL);
			else
				dp.setDrawingMode(Defines.DrawingMode.WIREFRAME_FILL);
			
			dp.repaint();
		}
	}

	public class MainToolBar_ShadingModeHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			DrawingPanel dp      = m_MainWindow.getDrawingPanel();
			int idx              = cb.getSelectedIndex();
			
			if( idx == 0 )
				dp.setShadingMode(Defines.ShadingMode.FLAT);
			else if( idx == 1 )
				dp.setShadingMode(Defines.ShadingMode.GOURAUD);
			else
				dp.setShadingMode(Defines.ShadingMode.PHONG);
			
			dp.repaint();
		}
	}
}
