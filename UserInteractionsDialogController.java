import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("deprecation")
public class UserInteractionsDialogController
{
	private UserInteractionsDialog m_UserInteractionsDialog;


	public UserInteractionsDialogController( UserInteractionsDialog w )
	{
		m_UserInteractionsDialog = w;
		m_UserInteractionsDialog.setListener(this);
	}

	public class UserInteractionsDialog_OkHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();

			m_UserInteractionsDialog.dispose();
		}
	}
}
