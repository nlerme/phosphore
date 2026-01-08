import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


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
		m_Model.addObserver(m_MainWindow.getShapesList());
		m_Model.addObserver(m_MainWindow.getDrawingPanel());
	}

	// Handlers for ShapesListToolBar
	public class ShapesListToolBar_AddShapeHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			AddingShapeDialog asd         = new AddingShapeDialog(m_MainWindow, "Adding of a shape");
			AddingShapeDialogController c = new AddingShapeDialogController(m_Model, asd);
			asd.pack();
			asd.setSize(asd.getPreferredSize());
			asd.setVisible(true);
		}
	}

	public class ShapesListToolBar_ModifyShapeHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			int index = m_MainWindow.getShapesList().getSelectedIndex();

			if( index < 0 )
				return;

			ModifyingShapeDialog msd         = new ModifyingShapeDialog(m_MainWindow, "Modifying of a shape");
			ModifyingShapeDialogController c = new ModifyingShapeDialogController(m_Model, msd);
			msd.pack();
			msd.setSize(msd.getPreferredSize());
			msd.setVisible(true);
		}
	}

	public class ShapesListToolBar_DeleteShapeHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			m_Model.removeShape(m_MainWindow.getShapesList().getSelectedIndex());
		}
	}

	public class ShapesListToolBar_DeleteAllShapesHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			m_Model.removeAllShapes();
		}
	}

	// Handlers for ShapesList
	public class ShapesListHandler implements ListSelectionListener
	{
		public void valueChanged( ListSelectionEvent e )
		{
			m_MainWindow.getDrawingPanel().repaint();
		}
	}

	// Handlers for MenuBar
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

	// Handlers for MainToolBar
	public class MainToolBar_TranslationHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			int index = m_MainWindow.getShapesList().getSelectedIndex();
			JButton b = (JButton)e.getSource();

			if( index < 0 )
				return;

			TranslationDialog td          = new TranslationDialog(m_MainWindow, "Translation");
			TranslationDialogController c = new TranslationDialogController(m_Model, td);
			td.pack();
			td.setSize(td.getPreferredSize());
			td.setVisible(true);

			m_MainWindow.getShapesList().setSelectedIndex(index);
		}
	}

	public class MainToolBar_RotationHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			int index = m_MainWindow.getShapesList().getSelectedIndex();
			JButton b = (JButton)e.getSource();

			if( index < 0 )
				return;

			RotationDialog rd          = new RotationDialog(m_MainWindow, "Rotation");
			RotationDialogController c = new RotationDialogController(m_Model, rd);
			rd.pack();
			rd.setSize(rd.getPreferredSize());
			rd.setVisible(true);

			m_MainWindow.getShapesList().setSelectedIndex(index);
		}
	}

	public class MainToolBar_ScalingHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			int index = m_MainWindow.getShapesList().getSelectedIndex();
			JButton b = (JButton)e.getSource();

			if( index < 0 )
				return;

			ScalingDialog sd          = new ScalingDialog(m_MainWindow, "Rotation");
			ScalingDialogController c = new ScalingDialogController(m_Model, sd);
			sd.pack();
			sd.setSize(sd.getPreferredSize());
			sd.setVisible(true);

			m_MainWindow.getShapesList().setSelectedIndex(index);
		}
	}
}
