import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("deprecation")
public class ObjectInfoDialogController
{
	private ObjectInfoDialog m_Dialog;


	public ObjectInfoDialogController( ObjectInfoDialog dialog )
	{
		m_Dialog = dialog;
		m_Dialog.setListener(this);
	}

	public class ObjectInfoDialog_OkHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_Dialog.dispose();
		}
	}
}
