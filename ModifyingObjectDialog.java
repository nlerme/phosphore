import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class ModifyingObjectDialog extends JDialog
{
	private JTextField m_ObjectNameEntry;
	private JButton m_ObjectOutlineColor;
	private JButton m_ObjectColor;
	private JSpinner m_ObjectOutlineWidth;
	private JButton m_ModifyObject;
	private JButton m_Cancel;
	private MainWindow m_MainWindow;


	public ModifyingObjectDialog( MainWindow mw, String title )
	{
		super(mw, title, true);
		setLayout(new BorderLayout());
		setLocationRelativeTo(mw);
		m_MainWindow = mw;
		createComponents();
		fillPanel();

		pack();
		setLocationRelativeTo(null);
	}

	private void createComponents()
	{
		int index    = m_MainWindow.getObjectsList().getSelectedIndex();
		Object3D s    = m_MainWindow.getModel().getObject(index);
		Appearance a = s.getAppearance();

		m_ObjectNameEntry = new JTextField(s.getName(), 10);

		m_ObjectOutlineColor = new JButton(" ");
		m_ObjectOutlineColor.setBackground(a.getOutlineColor());
		
		m_ObjectColor = new JButton(" ");
		Color faceC = a.getFaceColor();

		if( faceC == null )
			faceC = Defines.DefaultFaceColor;

		m_ObjectColor.setBackground(faceC);

		m_ObjectOutlineWidth = new JSpinner(new SpinnerNumberModel(Float.valueOf(a.getOutlineWidth()), Float.valueOf(1.0f), Float.valueOf(10.0f), Float.valueOf(0.5f)));

		m_ModifyObject = new JButton("Modify shape");
		m_Cancel = new JButton("Cancel");
	}

	private void fillPanel()
	{
		GridBagLayout gbl = new GridBagLayout();
		JPanel mainPanel  = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// Panel 1
		JPanel panel1 = new JPanel(gbl);
		JComponent[] leftComponents1 = { new JLabel("Enter a new name for the shape :") };
		JComponent[] rightComponents1 = { m_ObjectNameEntry };
		Utils.addDataToPanel(panel1, gbl, leftComponents1, rightComponents1);
		panel1.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "General Properties", TitledBorder.LEFT, TitledBorder.TOP));
		mainPanel.add(panel1);

		// Panel 2
		JPanel panel2 = new JPanel(gbl);
		JComponent[] leftComponents2 = {
							new JLabel("Choose an outline color :"),
							new JLabel("Choose an outline width :"),
							new JLabel("Choose object color :")
					       };

		JComponent[] rightComponents2 = {
							m_ObjectOutlineColor,
							m_ObjectOutlineWidth,
							m_ObjectColor
					        };

		Utils.addDataToPanel(panel2, gbl, leftComponents2, rightComponents2);
		panel2.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Graphical Properties", TitledBorder.LEFT, TitledBorder.TOP));
		mainPanel.add(panel2);

		// Panel 3
		JPanel panel3 = new JPanel();
		panel3.add(m_ModifyObject);
		panel3.add(m_Cancel);
		mainPanel.add(panel3);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setListener( ModifyingObjectDialogController c )
	{
		m_ObjectOutlineColor.addActionListener(c.new ModifyingObjectDialog_ObjectOutlineColorHandler());
		m_ObjectColor.addActionListener(c.new ModifyingObjectDialog_ObjectColorHandler());
		m_ModifyObject.addActionListener(c.new ModifyingObjectDialog_ModifyObjectHandler());
		m_Cancel.addActionListener(c.new ModifyingObjectDialog_CancelHandler());
	}

	public MainWindow getMainWindow()
	{
		return m_MainWindow;
	}

	public String getObjectNameEntry()
	{
		return m_ObjectNameEntry.getText();
	}

	public Color getOutlineColor()
	{
		return m_ObjectOutlineColor.getBackground();
	}

	public void setOutlineColor( Color outlineColor )
	{
		m_ObjectOutlineColor.setBackground(outlineColor);
	}

	public float getOutlineWidth()
	{
		return ((SpinnerNumberModel)m_ObjectOutlineWidth.getModel()).getNumber().floatValue();
	}
	
	public Color getObjectColor()
	{
		return m_ObjectColor.getBackground();
	}

	public void setObjectColor( Color c )
	{
		m_ObjectColor.setBackground(c);
	}
}
