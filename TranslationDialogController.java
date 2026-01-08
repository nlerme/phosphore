import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class TranslationDialogController
{
	private Model m_Model;
	private TranslationDialog m_TranslationDialog;


	public TranslationDialogController( Model m, TranslationDialog w )
	{
		m_Model             = m;
		m_TranslationDialog = w;

		// Linking between model and view
		m_TranslationDialog.setListener(this);
	}

	public class TranslationDialog_TranslateHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			int index = m_TranslationDialog.getMainWindow().getShapesList().getSelectedIndex();
			Shape3D s = m_Model.getShape(index);

			s.translate(m_TranslationDialog.getCoords());
			m_Model.setShape(index, s);
			m_TranslationDialog.dispose();
		}
	}

	public class TranslationDialog_CancelHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			m_TranslationDialog.dispose();
		}
	}
}
