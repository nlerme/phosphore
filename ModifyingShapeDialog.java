import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


public class ModifyingShapeDialog extends JDialog
{
	private JTextField m_ShapeNameEntry;
	private JButton m_ShapeOutlineColor;
	private JButton m_ShapeColor;
	private JSpinner m_ShapeOutlineWidth;
	private JButton m_ModifyShape;
	private JButton m_Cancel;
	private MainWindow m_MainWindow;


	public ModifyingShapeDialog( MainWindow mw, String title )
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
		int index    = m_MainWindow.getShapesList().getSelectedIndex();
		Shape3D s    = m_MainWindow.getModel().getShape(index);
		Appearance a = s.getAppearance();

		m_ShapeNameEntry = new JTextField(s.getName(), 10);

		m_ShapeOutlineColor = new JButton(" ");
		m_ShapeOutlineColor.setBackground(a.getOutlineColor());
		
		m_ShapeColor = new JButton(" ");
		Color faceC = a.getFaceColor();

		if( faceC == null )
			faceC = Defines.DefaultFaceColor;

		m_ShapeColor.setBackground(faceC);

		m_ShapeOutlineWidth = new JSpinner(new SpinnerNumberModel(Float.valueOf(a.getOutlineWidth()), Float.valueOf(1.0f), Float.valueOf(10.0f), Float.valueOf(0.5f)));

		m_ModifyShape = new JButton("Modify shape");
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
		JComponent[] rightComponents1 = { m_ShapeNameEntry };
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
							m_ShapeOutlineColor,
							m_ShapeOutlineWidth,
							m_ShapeColor
					        };

		Utils.addDataToPanel(panel2, gbl, leftComponents2, rightComponents2);
		panel2.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Graphical Properties", TitledBorder.LEFT, TitledBorder.TOP));
		mainPanel.add(panel2);

		// Panel 3
		JPanel panel3 = new JPanel();
		panel3.add(m_ModifyShape);
		panel3.add(m_Cancel);
		mainPanel.add(panel3);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setListener( ModifyingShapeDialogController c )
	{
		m_ShapeOutlineColor.addActionListener(c.new ModifyingShapeDialog_ShapeOutlineColorHandler());
		m_ShapeColor.addActionListener(c.new ModifyingShapeDialog_ShapeColorHandler());
		m_ModifyShape.addActionListener(c.new ModifyingShapeDialog_ModifyShapeHandler());
		m_Cancel.addActionListener(c.new ModifyingShapeDialog_CancelHandler());
	}

	public MainWindow getMainWindow()
	{
		return m_MainWindow;
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
	
	public Color getShapeColor()
	{
		return m_ShapeColor.getBackground();
	}

	public void setShapeColor( Color c )
	{
		m_ShapeColor.setBackground(c);
	}
}
