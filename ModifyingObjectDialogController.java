import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class ModifyingObjectDialogController
{
	private Model m_Model;
	private ModifyingObjectDialog m_ModifyingObjectDialog;


	public ModifyingObjectDialogController( Model m, ModifyingObjectDialog w )
	{
		m_Model                 = m;
		m_ModifyingObjectDialog = w;
		m_ModifyingObjectDialog.setListener(this);
	}

	public class ModifyingObjectDialog_ModifyObjectHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b        = (JButton)e.getSource();
			MainWindow mw    = m_ModifyingObjectDialog.getMainWindow();
			int index        = mw.getObjectsList().getSelectedIndex();
			Object3D s        = mw.getModel().getObject(index);
			String shapeName = m_ModifyingObjectDialog.getObjectNameEntry();

			if( !shapeName.equals("") )
				s.setName(shapeName);

			Appearance a = s.getAppearance();
			a.setOutlineColor(m_ModifyingObjectDialog.getOutlineColor());
			a.setOutlineWidth(m_ModifyingObjectDialog.getOutlineWidth());
			s.setAppearance(a);

			s.setFaceColor(m_ModifyingObjectDialog.getObjectColor());

			m_Model.setObject(index, s);
			m_ModifyingObjectDialog.dispose();
		}
	}

	public class ModifyingObjectDialog_ObjectOutlineColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_ModifyingObjectDialog, "Choose an outline color", m_ModifyingObjectDialog.getOutlineColor());

			if( c != null )
				m_ModifyingObjectDialog.setOutlineColor(c);
		}
	}

	public class ModifyingObjectDialog_ObjectColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_ModifyingObjectDialog, "Choose object color", m_ModifyingObjectDialog.getObjectColor());

			if( c != null )
				m_ModifyingObjectDialog.setObjectColor(c);
		}
	}

	public class ModifyingObjectDialog_CancelHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_ModifyingObjectDialog.dispose();
		}
	}
}
