import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class PreferencesDialogController
{
	private Model m_Model;
	private PreferencesDialog m_PreferencesDialog;


	public PreferencesDialogController( Model m, PreferencesDialog w )
	{
		m_Model             = m;
		m_PreferencesDialog = w;
		m_PreferencesDialog.setListener(this);
	}

	public class PreferencesDialog_OkHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Projection p;
			DrawingPanel dp = m_PreferencesDialog.getMainWindow().getDrawingPanel();

			dp.setBackground(m_PreferencesDialog.getBackgroundColor());
			dp.setNormalsColor(m_PreferencesDialog.getNormalsColor());
			
			Defines.AABBColorDefault     = m_PreferencesDialog.getAABBColorDefault();
			Defines.AABBColorSelection   = m_PreferencesDialog.getAABBColorSelection();
			
			Defines.DefaultFovY    = m_PreferencesDialog.getFovY();
			Defines.MouseZoomSpeed = m_PreferencesDialog.getZoomSpeed();

			Defines.LightDirection     = Vector3D.normalize(m_PreferencesDialog.getLightDirection());
			Defines.LightAmbientFactor = m_PreferencesDialog.getLightAmbient();
			Defines.LightDiffuseFactor = m_PreferencesDialog.getLightDiffuse();

			p = m_PreferencesDialog.getSelectedProjection();
			if( p != null )
				dp.setProjection(p);

			m_PreferencesDialog.dispose();
			dp.repaint();
		}
	}

	public class PreferencesDialog_BackgroundColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_PreferencesDialog, "Choose a background color", b.getBackground());
			if( c != null ) b.setBackground(c);
		}
	}

	public class PreferencesDialog_NormalsColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_PreferencesDialog, "Choose normals color", b.getBackground());
			if( c != null ) b.setBackground(c);
		}
	}

	public class PreferencesDialog_AABBColorDefaultHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_PreferencesDialog, "Choose default AABB color", b.getBackground());
			if( c != null ) b.setBackground(c);
		}
	}
	public class PreferencesDialog_AABBColorSelectionHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_PreferencesDialog, "Choose selection AABB color", b.getBackground());
			if( c != null ) b.setBackground(c);
		}
	}
	public class PreferencesDialog_CancelHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_PreferencesDialog.dispose();
		}
	}
}
