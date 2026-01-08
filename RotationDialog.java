import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


public class RotationDialog extends JDialog
{
	private JComboBox<String> m_Axis;
	private JSpinner m_Angle;
	private JButton m_Rotate;
	private JButton m_Cancel;
	private MainWindow m_MainWindow;


	public RotationDialog( MainWindow mw, String title )
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
		String[] list = {"Axis X", "Axis Y", "Axis Z"};

		m_Axis   = new JComboBox<>(list);
		m_Angle  = new JSpinner(new SpinnerNumberModel(5.0, -360.0, 360.0, 5.0));
		m_Rotate = new JButton("Rotate");
		m_Cancel = new JButton("Cancel");
	}

	private void fillPanel()
	{
		GridBagLayout gbl = new GridBagLayout();
		JPanel mainPanel  = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


		//------------------------- First panel -------------------------
		JPanel panel1 = new JPanel(gbl);

		// Array containing labels
		JComponent[] leftComponents1 = {
						new JLabel("Choose an axis :"),
						new JLabel("Choose an angle :")
				   };

		// Array containing two components
		JComponent[] rightComponents1 = {
						m_Axis,
						m_Angle
					   };

		// Layout
		Utils.addDataToPanel(panel1, gbl, leftComponents1, rightComponents1);
		panel1.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Properties", TitledBorder.LEFT, TitledBorder.TOP));
		mainPanel.add(panel1);


		//------------------------ Second panel -------------------------
		JPanel panel2 = new JPanel();
		panel2.add(m_Rotate);
		panel2.add(m_Cancel);
		mainPanel.add(panel2);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setListener( RotationDialogController c )
	{
		m_Rotate.addActionListener(c.new RotationDialog_RotateHandler());
		m_Cancel.addActionListener(c.new RotationDialog_CancelHandler());
	}

	public MainWindow getMainWindow()
	{
		return m_MainWindow;
	}

	public Vector3D getAxis()
	{
		int index = m_Axis.getSelectedIndex();

		if( index == 0 )
			return new Vector3D(1.0, 0.0, 0.0);
		else if( index == 1 )
			return new Vector3D(0.0, 1.0, 0.0);
		else
			return new Vector3D(0.0, 0.0, 1.0);
	}

	public double getAngle()
	{
		return ((SpinnerNumberModel)m_Angle.getModel()).getNumber().doubleValue();
	}
}
