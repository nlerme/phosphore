import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


@SuppressWarnings("deprecation")
public class UserInteractionsDialog extends JDialog
{
	private MainWindow m_MainWindow;
	private JButton m_Ok;


	public UserInteractionsDialog( MainWindow mw, String title )
	{
		super(mw, title, true);

		setLayout(new BorderLayout());
		
		m_MainWindow = mw;

		createComponents();
		fillPanel();
		
		pack();
		setLocationRelativeTo(null); 
	}

	private void createComponents()
	{
		m_Ok = new JButton("Ok");
		m_Ok.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	private void fillPanel()
	{
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Mouse gestures panel
		JPanel mousePanel = new JPanel(new GridLayout(3, 1));
		mousePanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Mouse gestures", TitledBorder.LEFT, TitledBorder.TOP));
		mousePanel.add(new JLabel(" Left click + Drag : Orbit camera"));
		mousePanel.add(new JLabel(" Ctrl + Left click + Drag : Pan camera"));
		mousePanel.add(new JLabel(" Wheel scroll : Zoom (Shift for fine zoom)"));

		// Keyboard shortcuts panel
		JPanel keyPanel = new JPanel(new GridLayout(6, 1));
		keyPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Keyboard shortcuts", TitledBorder.LEFT, TitledBorder.TOP));
		keyPanel.add(new JLabel(" Arrow Left : Move object Left (X-)"));
		keyPanel.add(new JLabel(" Arrow Right : Move object Right (X+)"));
		keyPanel.add(new JLabel(" Arrow Up : Move object Back (Z-)"));
		keyPanel.add(new JLabel(" Arrow Down : Move object Front (Z+)"));
		keyPanel.add(new JLabel(" Page Up : Move object Higher (Y+)"));
		keyPanel.add(new JLabel(" Page Down : Move object Lower (Y-)"));

		mainPanel.add(mousePanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(keyPanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(m_Ok);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setListener( UserInteractionsDialogController c )
	{
		m_Ok.addActionListener(c.new UserInteractionsDialog_OkHandler());
	}
}
