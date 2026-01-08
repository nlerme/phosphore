import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class ScalingDialogController
{
	private Model m_Model;
	private ScalingDialog m_ScalingDialog;


	public ScalingDialogController( Model m, ScalingDialog w )
	{
		m_Model         = m;
		m_ScalingDialog = w;

		// Linking between model and view
		m_ScalingDialog.setListener(this);
	}

	public class ScalingDialog_ScaleHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			int index = m_ScalingDialog.getMainWindow().getShapesList().getSelectedIndex();
			Shape3D s = m_Model.getShape(index);

			s.scale(m_ScalingDialog.getCoords());
			m_Model.setShape(index, s);
			m_ScalingDialog.dispose();
		}
	}

	public class ScalingDialog_CancelHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			m_ScalingDialog.dispose();
		}
	}
}
