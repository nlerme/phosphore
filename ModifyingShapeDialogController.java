import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class ModifyingShapeDialogController
{
	private Model m_Model;
	private ModifyingShapeDialog m_ModifyingShapeDialog;


	public ModifyingShapeDialogController( Model m, ModifyingShapeDialog w )
	{
		m_Model                = m;
		m_ModifyingShapeDialog = w;
		m_ModifyingShapeDialog.setListener(this);
	}

	public class ModifyingShapeDialog_ModifyShapeHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b        = (JButton)e.getSource();
			MainWindow mw    = m_ModifyingShapeDialog.getMainWindow();
			int index        = mw.getShapesList().getSelectedIndex();
			Shape3D s        = mw.getModel().getShape(index);
			String shapeName = m_ModifyingShapeDialog.getShapeNameEntry();

			if( !shapeName.equals("") )
				s.setName(shapeName);

			Appearance a = s.getAppearance();
			a.setOutlineColor(m_ModifyingShapeDialog.getOutlineColor());
			a.setOutlineWidth(m_ModifyingShapeDialog.getOutlineWidth());
			s.setAppearance(a);

			s.setFaceColor(m_ModifyingShapeDialog.getShapeColor());

			m_Model.setShape(index, s);
			m_ModifyingShapeDialog.dispose();
		}
	}

	public class ModifyingShapeDialog_ShapeOutlineColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_ModifyingShapeDialog, "Choose an outline color", m_ModifyingShapeDialog.getOutlineColor());

			if( c != null )
				m_ModifyingShapeDialog.setOutlineColor(c);
		}
	}

	public class ModifyingShapeDialog_ShapeColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_ModifyingShapeDialog, "Choose object color", m_ModifyingShapeDialog.getShapeColor());

			if( c != null )
				m_ModifyingShapeDialog.setShapeColor(c);
		}
	}

	public class ModifyingShapeDialog_CancelHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_ModifyingShapeDialog.dispose();
		}
	}
}
