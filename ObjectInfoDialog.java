import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;


@SuppressWarnings("deprecation")
public class ObjectInfoDialog extends JDialog
{
	private JLabel m_ObjectNameLabel;
	private JLabel m_NbVerticesLabel;
	private JLabel m_NbFacetsLabel;
	private JButton m_Ok;


	public ObjectInfoDialog( MainWindow mw, String title )
	{
		super(mw, title, true);
		setLayout(new BorderLayout());
		
		createComponents();
		fillPanel();
		
		pack();
		setLocationRelativeTo(mw); 
	}

	private void createComponents()
	{
		m_ObjectNameLabel = new JLabel("Name: ");
		m_NbVerticesLabel = new JLabel("Vertices: ");
		m_NbFacetsLabel   = new JLabel("Facets: ");

		m_Ok = new JButton("Ok");
		m_Ok.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	private void fillPanel()
	{
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		infoPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Statistics", TitledBorder.LEFT, TitledBorder.TOP));
		
		infoPanel.add(m_ObjectNameLabel);
		infoPanel.add(m_NbVerticesLabel);
		infoPanel.add(m_NbFacetsLabel);

		mainPanel.add(infoPanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(m_Ok);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setInfos(String name, int vertices, int facets)
	{
		m_ObjectNameLabel.setText("Name: " + name);
		m_NbVerticesLabel.setText("Vertices: " + vertices);
		m_NbFacetsLabel.setText("Facets: " + facets);
	}

	public void setListener( ObjectInfoDialogController c )
	{
		m_Ok.addActionListener(c.new ObjectInfoDialog_OkHandler());
	}
}
