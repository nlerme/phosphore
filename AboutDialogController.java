import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class AboutDialogController
{
	private Model m_Model;
	private AboutDialog m_AboutDialog;


	public AboutDialogController( Model m, AboutDialog w )
	{
		m_Model       = m;
		m_AboutDialog = w;

		// Linking between model and view
		m_AboutDialog.setListener(this);
	}

	public class AboutDialog_OkHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			m_AboutDialog.dispose();
		}
	}
}
