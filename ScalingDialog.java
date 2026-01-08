import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


public class ScalingDialog extends JDialog
{
	private JSpinner m_CoordX;
	private JSpinner m_CoordY;
	private JSpinner m_CoordZ;
	private JButton m_Scale;
	private JButton m_Cancel;
	private MainWindow m_MainWindow;


	public ScalingDialog( MainWindow mw, String title )
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
		m_CoordX = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 100.0, 0.1));
		m_CoordY = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 100.0, 0.1));
		m_CoordZ = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 100.0, 0.1));
		m_Scale  = new JButton("Scale");
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
						new JLabel("Coord X :"),
						new JLabel("Coord Y :"),
						new JLabel("Coord Z :")
				   };

		// Array containing two components
		JComponent[] rightComponents1 = {
						m_CoordX,
						m_CoordY,
						m_CoordZ
					   };

		// Layout
		Utils.addDataToPanel(panel1, gbl, leftComponents1, rightComponents1);
		panel1.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Properties", TitledBorder.LEFT, TitledBorder.TOP));
		mainPanel.add(panel1);


		//------------------------- Second panel ------------------------
		JPanel panel2 = new JPanel();
		panel2.add(m_Scale);
		panel2.add(m_Cancel);
		mainPanel.add(panel2);

		add(mainPanel, BorderLayout.CENTER);
	}

	public void setListener( ScalingDialogController c )
	{
		m_Scale.addActionListener(c.new ScalingDialog_ScaleHandler());
		m_Cancel.addActionListener(c.new ScalingDialog_CancelHandler());
	}

	public MainWindow getMainWindow()
	{
		return m_MainWindow;
	}

	public Vector3D getCoords()
	{
		double x = ((SpinnerNumberModel)m_CoordX.getModel()).getNumber().doubleValue();
		double y = ((SpinnerNumberModel)m_CoordY.getModel()).getNumber().doubleValue();
		double z = ((SpinnerNumberModel)m_CoordZ.getModel()).getNumber().doubleValue();
		return new Vector3D(x, y, z);
	}
}
