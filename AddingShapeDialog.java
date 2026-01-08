import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


public class AddingShapeDialog extends JDialog
{
	private JRadioButton m_Choice1;
	private JLabel m_LabelForChoice1;
	private JRadioButton m_Choice2;
	private JLabel m_LabelForChoice2;
	private JComboBox<String> m_ShapesList;
	private JButton m_LoadFile;
	private JTextField m_ShapeNameEntry;
	private JButton m_AddShape;
	private JButton m_Cancel;
	private JButton m_ShapeOutlineColor;
	private JButton m_ShapeColor;
	private JSpinner m_ShapeOutlineWidth;
	private JSpinner m_CoordX;
	private JSpinner m_CoordY;
	private JSpinner m_CoordZ;
	private String m_FileName;
	private MainWindow m_MainWindow;


	public AddingShapeDialog( MainWindow mw, String title )
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
		m_LoadFile          = new JButton("Load"); m_LoadFile.setEnabled(false);
		m_Choice2           = new JRadioButton(""); bg.add(m_Choice2);
		m_LabelForChoice2   = new JLabel("Select a file to load :"); m_LabelForChoice2.setEnabled(false);
		m_FileName          = "";
		String[] list       = {"Cuboid", "Rectangle", "Ellipse", "Cone", "Cylinder"};
		m_ShapesList        = new JComboBox<>(list);
		m_ShapeNameEntry    = new JTextField(Defines.DefaultShapeName, 10);
		m_ShapeOutlineWidth = new JSpinner(new SpinnerNumberModel(Float.valueOf(Defines.DefaultOutlineWidth), Float.valueOf(1.0f), Float.valueOf(10.0f), Float.valueOf(0.5f)));

		m_ShapeOutlineColor = new JButton(" ");
		m_ShapeOutlineColor.setBackground(Defines.DefaultOutlineColor);
		
		m_ShapeColor = new JButton(" ");
		m_ShapeColor.setBackground(Defines.DefaultFaceColor);

		m_CoordX = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.1));
		m_CoordY = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.1));
		m_CoordZ = new JSpinner(new SpinnerNumberModel(0.0, -100.0, 100.0, 0.1));

		m_AddShape = new JButton("Add shape");
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
		JComponent[] leftComponents1  = { hb1, hb2, new JLabel("Enter a name for the shape :") };
		JComponent[] rightComponents1 = { m_ShapesList, m_LoadFile, m_ShapeNameEntry };
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
				m_ShapeOutlineColor,
				m_ShapeOutlineWidth,
				m_ShapeColor
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
		panel4.add(m_AddShape);
		panel4.add(m_Cancel);
		mainPanel.add(panel4);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setListener( AddingShapeDialogController c )
	{
		m_AddShape.addActionListener(c.new AddingShapeDialog_AddShapeHandler());
		m_Cancel.addActionListener(c.new AddingShapeDialog_CancelHandler());
		m_ShapeOutlineColor.addActionListener(c.new AddingShapeDialog_ShapeOutlineColorHandler());
		m_ShapeColor.addActionListener(c.new AddingShapeDialog_ShapeColorHandler());
		m_LoadFile.addActionListener(c.new AddingShapeDialog_LoadFileHandler());
		m_Choice1.addActionListener(c.new AddingShapeDialog_Choice1Handler());
		m_Choice2.addActionListener(c.new AddingShapeDialog_Choice2Handler());
	}

	public void setChoice1()
	{
		m_LabelForChoice1.setEnabled(true);
		m_ShapesList.setEnabled(true);
		m_LabelForChoice2.setEnabled(false);
		m_LoadFile.setEnabled(false);
	}

	public void setChoice2()
	{
		m_LabelForChoice1.setEnabled(false);
		m_ShapesList.setEnabled(false);
		m_LabelForChoice2.setEnabled(true);
		m_LoadFile.setEnabled(true);
	}

	public boolean isChoice1Selected()
	{
		return m_Choice1.isSelected();
	}

	public boolean isChoice2Selected()
	{
		return m_Choice2.isSelected();
	}

	public String getFileName()
	{
		return m_FileName;
	}

	public void setFileName( String fileName )
	{
		m_FileName = fileName;
	}

	public String getShapeNameEntry()
	{
		return m_ShapeNameEntry.getText();
	}

	public Color getOutlineColor()
	{
		return m_ShapeOutlineColor.getBackground();
	}

	public void setOutlineColor( Color outlineColor )
	{
		m_ShapeOutlineColor.setBackground(outlineColor);
	}

	public float getOutlineWidth()
	{
		return ((SpinnerNumberModel)m_ShapeOutlineWidth.getModel()).getNumber().floatValue();
	}

	public Vector3D getCoords()
	{
		double x = ((SpinnerNumberModel)m_CoordX.getModel()).getNumber().doubleValue();
		double y = ((SpinnerNumberModel)m_CoordY.getModel()).getNumber().doubleValue();
		double z = ((SpinnerNumberModel)m_CoordZ.getModel()).getNumber().doubleValue();
		return new Vector3D(x, y, z);
	}

	public Shape3D getPredefinedShape()
	{
		Shape3D s = null;
		int index = m_ShapesList.getSelectedIndex();

		if(      index == 0 ) s = new Cuboid();
		else if( index == 1 ) s = new Rectangle();
		else if( index == 2 ) s = new Ellipse();
		else if( index == 3 ) s = new Cone();
		else if( index == 4 ) s = new Cylinder();

		return s;
	}

	public Color getShapeColor()
	{
		return m_ShapeColor.getBackground();
	}

	public void setShapeColor( Color c )
	{
		m_ShapeColor.setBackground(c);
	}
}
