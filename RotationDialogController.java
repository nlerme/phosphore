import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class RotationDialogController
{
	private Model m_Model;
	private RotationDialog m_RotationDialog;


	public RotationDialogController( Model m, RotationDialog w )
	{
		m_Model          = m;
		m_RotationDialog = w;

		// Linking between model and view
		m_RotationDialog.setListener(this);
	}

	public class RotationDialog_RotateHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			int index = m_RotationDialog.getMainWindow().getShapesList().getSelectedIndex();
			Shape3D s = m_Model.getShape(index);

			s.rotate(m_RotationDialog.getAxis(), m_RotationDialog.getAngle());
			m_Model.setShape(index, s);
			m_RotationDialog.dispose();
		}
	}

	public class RotationDialog_CancelHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			m_RotationDialog.dispose();
		}
	}
}
