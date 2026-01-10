import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class MenuBar extends JMenuBar
{
	private JMenuItem m_QuitItem;
	private JMenuItem m_VersionItem;
	private JMenuItem m_AboutItem;
	private JMenuItem m_PreferencesItem;
	private JMenuItem m_UserInteractionsItem;


	public MenuBar()
	{
		super();
		init();
	}

	private void init()
	{
		JMenu menu;
		JMenuItem item;

		//--------- File menu ---------
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		// Quit item
		m_QuitItem = new JMenuItem("Quit");
		m_QuitItem.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
		m_QuitItem.setToolTipText("Quit");
		menu.add(m_QuitItem);
		add(menu);

		//--------- Edit menu ---------
		menu = new JMenu("Edit");
		menu.setMnemonic(KeyEvent.VK_E);
		// Preferences item
		m_PreferencesItem = new JMenuItem("Preferences ...");
		m_PreferencesItem.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));
		m_PreferencesItem.setToolTipText("Preferences");
		menu.add(m_PreferencesItem);
		add(menu);

		//--------- Help menu ---------
		add(Box.createHorizontalGlue());
		menu = new JMenu("Help menu");
		menu.setMnemonic(KeyEvent.VK_H);
		// User interactions item
		m_UserInteractionsItem = new JMenuItem("User interactions");
		m_UserInteractionsItem.setAccelerator(KeyStroke.getKeyStroke('U', InputEvent.CTRL_DOWN_MASK));
		m_UserInteractionsItem.setToolTipText("Show user interactions");
		menu.add(m_UserInteractionsItem);
		// Version item
		m_VersionItem = new JMenuItem("Version");
		m_VersionItem.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_DOWN_MASK));
		m_VersionItem.setToolTipText("Program version");
		menu.add(m_VersionItem);
		// About item
		m_AboutItem = new JMenuItem("About");
		m_AboutItem.setAccelerator(KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK));
		m_AboutItem.setToolTipText("About");
		menu.add(m_AboutItem);
		add(menu);
	}

	public void setListener( MainController c )
	{
		m_QuitItem.addActionListener(c.new MenuBar_QuitHandler());
		m_VersionItem.addActionListener(c.new MenuBar_VersionHandler());
		m_AboutItem.addActionListener(c.new MenuBar_AboutHandler());
		m_PreferencesItem.addActionListener(c.new MenuBar_PreferencesHandler());
		m_UserInteractionsItem.addActionListener(c.new MenuBar_UserInteractionsHandler());
	}
}
