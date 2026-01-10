import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class AddingObjectDialog extends JDialog
{
	private JRadioButton m_Choice1;
	private JLabel m_LabelForChoice1;
	private JRadioButton m_Choice2;
	private JLabel m_LabelForChoice2;
	private JRadioButton m_Choice3;
	private JLabel m_LabelForChoice3;
	private JTextField m_PromptEntry;
	private JComboBox<String> m_ObjectsList;
	private JButton m_LoadFile;
	private JTextField m_ObjectNameEntry;
	private JButton m_AddObject;
	private JButton m_Cancel;
	private JButton m_ObjectOutlineColor;
	private JButton m_ObjectColor;
	private JSpinner m_ObjectOutlineWidth;
	private JSpinner m_CoordX;
	private JSpinner m_CoordY;
	private JSpinner m_CoordZ;
	private String m_FileName;
	private MainWindow m_MainWindow;


	public AddingObjectDialog( MainWindow mw, String title )
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
		ButtonGroup bg      = new ButtonGroup();
		
		m_Choice1           = new JRadioButton(""); m_Choice1.setSelected(true); bg.add(m_Choice1);
		m_LabelForChoice1   = new JLabel("Choose a predefined type of shape :");

		m_Choice2           = new JRadioButton(""); bg.add(m_Choice2);
		m_LabelForChoice2   = new JLabel("Select a file to load :"); m_LabelForChoice2.setEnabled(false);
		m_LoadFile          = new JButton("Load"); m_LoadFile.setEnabled(false);
		
		m_Choice3           = new JRadioButton(""); bg.add(m_Choice3);
		m_LabelForChoice3   = new JLabel("Generate from prompt :"); m_LabelForChoice3.setEnabled(false);
		m_PromptEntry       = new JTextField(15); m_PromptEntry.setEnabled(false);
		
		m_FileName          = "";
		
		String[] list       = {"Cuboid", "Ellipsoid", "Cone", "Cylinder"};
		
		m_ObjectsList        = new JComboBox<>(list);
		m_ObjectNameEntry    = new JTextField(Defines.DefaultObjectName, 10);
		m_ObjectOutlineWidth = new JSpinner(new SpinnerNumberModel(Float.valueOf(Defines.DefaultOutlineWidth), Float.valueOf(1.0f), Float.valueOf(10.0f), Float.valueOf(0.5f)));

		m_ObjectOutlineColor = new JButton(" ");
		m_ObjectOutlineColor.setBackground(Defines.DefaultOutlineColor);
		
		m_ObjectColor = new JButton(" ");
		m_ObjectColor.setBackground(Defines.DefaultFaceColor);

		m_CoordX = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.1));
		m_CoordY = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.1));
		m_CoordZ = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.1));

		m_AddObject = new JButton("Add shape");
		m_Cancel   = new JButton("Cancel");
	}

	private void fillPanel()
	{
		GridBagLayout gbl = new GridBagLayout();
		JPanel mainPanel  = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel panel1 = new JPanel(gbl);
		
		Box hb1 = Box.createHorizontalBox();
		hb1.add(m_Choice1);
		hb1.add(m_LabelForChoice1);
		
		Box hb2 = Box.createHorizontalBox();
		hb2.add(m_Choice2);
		hb2.add(m_LabelForChoice2);
		
		Box hb3 = Box.createHorizontalBox();
		hb3.add(m_Choice3);
		hb3.add(m_LabelForChoice3);

		JComponent[] leftComponents1  = { hb1, hb2, hb3, new JLabel("Enter a name for the shape :") };
		JComponent[] rightComponents1 = { m_ObjectsList, m_LoadFile, m_PromptEntry, m_ObjectNameEntry };
		
		Utils.addDataToPanel(panel1, gbl, leftComponents1, rightComponents1);
		panel1.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "General properties", TitledBorder.LEFT, TitledBorder.TOP));
		mainPanel.add(panel1);

		//------------------------- Second panel -------------------------
		JPanel panel2 = new JPanel(gbl);

		JLabel[] leftComponents2 = {
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
		panel2.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Graphical properties", TitledBorder.LEFT, TitledBorder.TOP));
		mainPanel.add(panel2);

		JPanel panel3                 = new JPanel(gbl);
		JLabel[] leftComponents3      = {new JLabel("Coord X :"), new JLabel("Coord Y :"), new JLabel("Coord Z :")};
		JComponent[] rightComponents3 = {m_CoordX, m_CoordY, m_CoordZ};
		Utils.addDataToPanel(panel3, gbl, leftComponents3, rightComponents3);
		panel3.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Geometric properties", TitledBorder.LEFT, TitledBorder.TOP));
		mainPanel.add(panel3);

		JPanel panel4 = new JPanel();
		panel4.add(m_AddObject);
		panel4.add(m_Cancel);
		mainPanel.add(panel4);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setListener( AddingObjectDialogController c )
	{
		m_AddObject.addActionListener(c.new AddingObjectDialog_AddObjectHandler());
		m_Cancel.addActionListener(c.new AddingObjectDialog_CancelHandler());
		m_ObjectOutlineColor.addActionListener(c.new AddingObjectDialog_ObjectOutlineColorHandler());
		m_ObjectColor.addActionListener(c.new AddingObjectDialog_ObjectColorHandler());
		m_LoadFile.addActionListener(c.new AddingObjectDialog_LoadFileHandler());
		m_Choice1.addActionListener(c.new AddingObjectDialog_Choice1Handler());
		m_Choice2.addActionListener(c.new AddingObjectDialog_Choice2Handler());
		m_Choice3.addActionListener(c.new AddingObjectDialog_Choice3Handler());
	}

	public void setChoice1()
	{
		m_LabelForChoice1.setEnabled(true);
		m_ObjectsList.setEnabled(true);
		m_LabelForChoice2.setEnabled(false);
		m_LoadFile.setEnabled(false);
		m_LabelForChoice3.setEnabled(false);
		m_PromptEntry.setEnabled(false);
	}

	public void setChoice2()
	{
		m_LabelForChoice1.setEnabled(false);
		m_ObjectsList.setEnabled(false);
		m_LabelForChoice2.setEnabled(true);
		m_LoadFile.setEnabled(true);
		m_LabelForChoice3.setEnabled(false);
		m_PromptEntry.setEnabled(false);
	}

	public void setChoice3()
	{
		m_LabelForChoice1.setEnabled(false);
		m_ObjectsList.setEnabled(false);
		m_LabelForChoice2.setEnabled(false);
		m_LoadFile.setEnabled(false);
		m_LabelForChoice3.setEnabled(true);
		m_PromptEntry.setEnabled(true);
	}

	public boolean isChoice1Selected()
	{
		return m_Choice1.isSelected();
	}

	public boolean isChoice2Selected()
	{
		return m_Choice2.isSelected();
	}

	public boolean isChoice3Selected()
	{
		return m_Choice3.isSelected();
	}
	
	public String getPromptText()
	{
		return m_PromptEntry.getText();
	}

	public String getFileName()
	{
		return m_FileName;
	}

	public void setFileName( String fileName )
	{
		m_FileName = fileName;
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

	public Vector3D getCoords()
	{
		double x = ((SpinnerNumberModel)m_CoordX.getModel()).getNumber().doubleValue();
		double y = ((SpinnerNumberModel)m_CoordY.getModel()).getNumber().doubleValue();
		double z = ((SpinnerNumberModel)m_CoordZ.getModel()).getNumber().doubleValue();
		return new Vector3D(x, y, z);
	}

	public Object3D getPredefinedObject()
	{
		Object3D s = null;
		int index = m_ObjectsList.getSelectedIndex();

		if(      index == 0 ) s = new Cuboid();
		else if( index == 1 ) s = new Ellipsoid();
		else if( index == 2 ) s = new Cone();
		else if( index == 3 ) s = new Cylinder();

		return s;
	}

	public Color getObjectColor()
	{
		return m_ObjectColor.getBackground();
	}

	public void setObjectColor( Color c )
	{
		m_ObjectColor.setBackground(c);
	}

	public JTextField getObjectNameEntryComponent()
	{
		return m_ObjectNameEntry;
	}
}
