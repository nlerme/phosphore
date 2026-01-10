import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;
import java.io.*;


@SuppressWarnings("deprecation")
public class AboutDialog extends JDialog
{
	private MainWindow m_MainWindow;
	private JTextArea m_LicenseText;
	private JButton m_Ok;


	public AboutDialog( MainWindow mw, String title )
	{
		super(mw, title, true);

		setLayout(new BorderLayout());
		
		m_MainWindow = mw;

		createComponents();
		fillPanel();
		
		pack();
		setLocationRelativeTo(null); 
	}

	private String loadLicenseFromFile()
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			File f = new File("LICENSE");

			if( f.exists() )
			{
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;

				while( (line = br.readLine()) != null )
				{
					sb.append(line).append("\n");
				}

				br.close();
			}
			else
			{
				sb.append("LICENSE file not found. Please ensure a file named 'LICENSE' is present in the application directory.");
			}
		}
		catch( IOException e )
		{
			sb.append("Error loading LICENSE file: " + e.getMessage());
		}

		return sb.toString();
	}

	private void createComponents()
	{
		String licenseContent = loadLicenseFromFile();

		m_LicenseText = new JTextArea(licenseContent);
		m_LicenseText.setEditable(false);
		m_LicenseText.setLineWrap(true);
		m_LicenseText.setWrapStyleWord(true);
		m_LicenseText.setCaretPosition(0);

		m_Ok = new JButton("Ok");
		m_Ok.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	private void fillPanel()
	{
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JScrollPane scrollPane = new JScrollPane(m_LicenseText);
		scrollPane.setPreferredSize(new Dimension(600, 400));
		
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(m_Ok, BorderLayout.SOUTH);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setListener( AboutDialogController c )
	{
		m_Ok.addActionListener(c.new AboutDialog_OkHandler());
	}
}
