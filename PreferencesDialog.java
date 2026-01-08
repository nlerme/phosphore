import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;


public class PreferencesDialog extends JDialog
{
	private JComboBox<String> m_ProjectionType;
	private JComboBox<String> m_DrawingMode;
	private JButton m_Ok;
	private JButton m_Cancel;
	private JButton m_BackgroundColor;
	private JButton m_NormalsColor;
	private JButton m_AABBColorDefault;
	private JButton m_AABBColorSelection;
	private JCheckBox m_DisplayNormals;
	
	private JSpinner m_FovY;
	private JSpinner m_ZoomSpeed;
	private JSpinner m_CameraDefaultDist;
	private JSpinner m_CameraMinDist;
	private JSpinner m_FocusRadiusMult;
	private JSpinner m_DefaultFocusRad;
	private JSpinner m_MousePanSens;
	private JSpinner m_MouseZoomFine;
	
	private JSpinner m_LightDirX;
	private JSpinner m_LightDirY;
	private JSpinner m_LightDirZ;
	private JSpinner m_LightAmbient;
	private JSpinner m_LightDiffuse;
	private MainWindow m_MainWindow;


	public PreferencesDialog( MainWindow mw, String title )
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
		String[] list   = {"Perspective", "Orthographic"};
		DrawingPanel dp = ((MainWindow)getParent()).getDrawingPanel();
		Projection p    = dp.getProjection();

		m_ProjectionType = new JComboBox<>(list);

		if( p.getType() == Projection.Type.PERSPECTIVE )
			m_ProjectionType.setSelectedIndex(0);
		else if( p.getType() == Projection.Type.ORTHOGRAPHIC )
			m_ProjectionType.setSelectedIndex(1);

		String[] list2 = {"Wireframe", "Fill", "Wireframe + Fill"};
		m_DrawingMode  = new JComboBox<>(list2);

		if( dp.getDrawingMode() == Defines.DrawingMode.WIREFRAME )
			m_DrawingMode.setSelectedIndex(0);
		else if( dp.getDrawingMode() == Defines.DrawingMode.FILL )
			m_DrawingMode.setSelectedIndex(1);
		else if( dp.getDrawingMode() == Defines.DrawingMode.WIREFRAME_FILL )
			m_DrawingMode.setSelectedIndex(2);

		m_BackgroundColor    = new JButton(" "); m_BackgroundColor.setBackground(dp.getBackground());
		m_NormalsColor       = new JButton(" "); m_NormalsColor.setBackground(dp.getNormalsColor());
		m_AABBColorDefault   = new JButton(" "); m_AABBColorDefault.setBackground(Defines.AABBColorDefault);
		m_AABBColorSelection = new JButton(" "); m_AABBColorSelection.setBackground(Defines.AABBColorSelection);

		m_DisplayNormals = new JCheckBox("");

		if( dp.getDisplayNormalsState() )
			m_DisplayNormals.setSelected(true);
			
		m_FovY              = new JSpinner(new SpinnerNumberModel(Defines.DefaultFovY, 10.0, 170.0, 1.0));
		m_CameraDefaultDist = new JSpinner(new SpinnerNumberModel(Defines.CameraDefaultDistance, 1.0, 1000.0, 0.5));
		m_CameraMinDist     = new JSpinner(new SpinnerNumberModel(Defines.CameraMinDistance, 0.01, 10.0, 0.05));
		m_FocusRadiusMult   = new JSpinner(new SpinnerNumberModel(Defines.FocusRadiusMultiplier, 0.1, 20.0, 0.1));
		m_DefaultFocusRad   = new JSpinner(new SpinnerNumberModel(Defines.DefaultFocusRadius, 0.1, 100.0, 0.5));
		m_MousePanSens      = new JSpinner(new SpinnerNumberModel(Defines.MousePanSensitivity, 0.0001, 0.1, 0.0001));
		m_MouseZoomFine     = new JSpinner(new SpinnerNumberModel(Defines.MouseZoomFactorFine, 0.01, 1.0, 0.05));
		m_ZoomSpeed         = new JSpinner(new SpinnerNumberModel(Defines.MouseZoomSpeed, 0.1, 5.0, 0.1));

		m_LightDirX    = new JSpinner(new SpinnerNumberModel(Defines.LightDirection.x, -10.0, 10.0, 0.1));
		m_LightDirY    = new JSpinner(new SpinnerNumberModel(Defines.LightDirection.y, -10.0, 10.0, 0.1));
		m_LightDirZ    = new JSpinner(new SpinnerNumberModel(Defines.LightDirection.z, -10.0, 10.0, 0.1));
		m_LightAmbient = new JSpinner(new SpinnerNumberModel(Defines.LightAmbientFactor, 0.0, 1.0, 0.05));
		m_LightDiffuse = new JSpinner(new SpinnerNumberModel(Defines.LightDiffuseFactor, 0.0, 1.0, 0.05));

		m_Ok     = new JButton("Ok");
		m_Cancel = new JButton("Cancel");
	}

	private void fillPanel()
	{
		GridBagLayout gbl = new GridBagLayout();
		JPanel mainPanel  = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel horizontalContainer = new JPanel();
		horizontalContainer.setLayout(new BoxLayout(horizontalContainer, BoxLayout.X_AXIS));

		//------------------------- Display panel -------------------------
		JPanel panel1 = new JPanel(gbl);
		JComponent[] leftComponents1 = {new JLabel("Type of projection :"), 
						new JLabel("Drawing mode :"),
						new JLabel("Background color :"), 
						new JLabel("Normals color :"), 
						new JLabel("AABB default color :"),
						new JLabel("AABB selection color :"), 
						new JLabel("Show normals :")};
		JComponent[] rightComponents1 = {m_ProjectionType, m_DrawingMode, m_BackgroundColor, 
						 m_NormalsColor, m_AABBColorDefault, m_AABBColorSelection, m_DisplayNormals};
		
		addDataToPanel(panel1, gbl, leftComponents1, rightComponents1);
		panel1.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Display Properties", TitledBorder.LEFT, TitledBorder.TOP));
		
		//------------------------- Camera panel -------------------------
		JPanel panelCam        = new JPanel(gbl);
		JComponent[] leftCam   = {new JLabel("Field of View (Y) :"), 
					  new JLabel("Default Distance :"),
					  new JLabel("Min Distance :"),
					  new JLabel("Focus Radius Mult. :"),
					  new JLabel("Default Focus Radius :"),
					  new JLabel("Mouse Pan Sens. :"),
					  new JLabel("Mouse Zoom Fine :"),
					  new JLabel("Zoom Speed :")};
		JComponent[] rightCam  = {m_FovY, 
					  m_CameraDefaultDist,
					  m_CameraMinDist,
					  m_FocusRadiusMult,
					  m_DefaultFocusRad,
					  m_MousePanSens,
					  m_MouseZoomFine,
					  m_ZoomSpeed};
		
		addDataToPanel(panelCam, gbl, leftCam, rightCam);
		panelCam.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Camera Properties", TitledBorder.LEFT, TitledBorder.TOP));

		//------------------------- Lighting panel -----------------------
		JPanel panelLight = new JPanel(gbl);
		JComponent[] leftLight = {new JLabel("Direction X :"), new JLabel("Direction Y :"), new JLabel("Direction Z :"),
					  new JLabel("Ambient Factor :"), new JLabel("Diffuse Factor :")};
		JComponent[] rightLight = {m_LightDirX, m_LightDirY, m_LightDirZ, 
					   m_LightAmbient, m_LightDiffuse};
		
		addDataToPanel(panelLight, gbl, leftLight, rightLight);
		panelLight.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Lighting Properties", TitledBorder.LEFT, TitledBorder.TOP));

		horizontalContainer.add(panel1);
		horizontalContainer.add(Box.createHorizontalStrut(5));
		horizontalContainer.add(panelCam);
		horizontalContainer.add(Box.createHorizontalStrut(5));
		horizontalContainer.add(panelLight);

		mainPanel.add(horizontalContainer);

		//------------------------- Buttons panel ------------------------
		JPanel panel2 = new JPanel();
		panel2.add(m_Ok);
		panel2.add(m_Cancel);
		mainPanel.add(panel2);

		add(mainPanel, BorderLayout.CENTER);
	}

	private void addDataToPanel( JPanel panel, GridBagLayout gbl, JComponent[] leftComponents, JComponent[] rightComponents )
	{
		if( leftComponents.length != rightComponents.length )
			return;

		GridBagConstraints gbc = new GridBagConstraints();

		for( int i=0; i<leftComponents.length; i++ )
		{
			gbc.anchor    = GridBagConstraints.WEST;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			gbc.fill      = GridBagConstraints.NONE;
			gbc.insets    = new Insets(8, 8, 8, 5);
			gbc.weightx   = 0.0;

			gbl.setConstraints(leftComponents[i], gbc);
			panel.add(leftComponents[i]);

			gbc.anchor    = GridBagConstraints.WEST;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.fill      = GridBagConstraints.HORIZONTAL;
			gbc.insets    = new Insets(8, 5, 8, 8);
			gbc.weightx   = 1.0;

			gbl.setConstraints(rightComponents[i], gbc);
			panel.add(rightComponents[i]);
		}
	}

	public void setListener( PreferencesDialogController c )
	{
		m_Ok.addActionListener(c.new PreferencesDialog_OkHandler());
		m_Cancel.addActionListener(c.new PreferencesDialog_CancelHandler());
		m_BackgroundColor.addActionListener(c.new PreferencesDialog_BackgroundColorHandler());
		m_NormalsColor.addActionListener(c.new PreferencesDialog_NormalsColorHandler());
		m_AABBColorDefault.addActionListener(c.new PreferencesDialog_AABBColorDefaultHandler());
		m_AABBColorSelection.addActionListener(c.new PreferencesDialog_AABBColorSelectionHandler());
	}

	public MainWindow getMainWindow()
	{
		return m_MainWindow;
	}

	public Color getBackgroundColor()
	{
		return m_BackgroundColor.getBackground();
	}

	public boolean getDisplayNormalsState()
	{
		return m_DisplayNormals.isSelected();
	}

	public Color getNormalsColor()
	{
		return m_NormalsColor.getBackground();
	}

	public Color getAABBColorDefault()
	{
		return m_AABBColorDefault.getBackground();
	}

	public Color getAABBColorSelection()
	{
		return m_AABBColorSelection.getBackground();
	}

	public double getFovY()
	{
		return ((SpinnerNumberModel)m_FovY.getModel()).getNumber().doubleValue();
	}

	public double getZoomSpeed()
	{
		return ((SpinnerNumberModel)m_ZoomSpeed.getModel()).getNumber().doubleValue();
	}
	
	public double getCameraDefaultDistance()
	{
		return ((SpinnerNumberModel)m_CameraDefaultDist.getModel()).getNumber().doubleValue();
	}

	public double getCameraMinDistance()
	{
		return ((SpinnerNumberModel)m_CameraMinDist.getModel()).getNumber().doubleValue();
	}

	public double getFocusRadiusMultiplier()
	{
		return ((SpinnerNumberModel)m_FocusRadiusMult.getModel()).getNumber().doubleValue();
	}

	public double getDefaultFocusRadius()
	{
		return ((SpinnerNumberModel)m_DefaultFocusRad.getModel()).getNumber().doubleValue();
	}

	public double getMousePanSensitivity()
	{
		return ((SpinnerNumberModel)m_MousePanSens.getModel()).getNumber().doubleValue();
	}

	public double getMouseZoomFactorFine()
	{
		return ((SpinnerNumberModel)m_MouseZoomFine.getModel()).getNumber().doubleValue();
	}

	public void setAABBColorDefault(Color c)
	{
		m_AABBColorDefault.setBackground(c);
	}

	public void setAABBColorSelection(Color c)
	{
		m_AABBColorSelection.setBackground(c);
	}

	public Projection getSelectedProjection()
	{
		int index    = m_ProjectionType.getSelectedIndex();
		Projection p = null;

		if( index==0 )
			p = new PerspectiveProjection(Defines.DefaultDMin, Defines.DefaultDMax, Defines.DefaultCameraDistance, getFovY());
		else if( index==1 )
			p = new OrthographicProjection(Defines.DefaultCameraDistance);

		return p;
	}

	public Defines.DrawingMode getSelectedDrawingMode()
	{
		int idx = m_DrawingMode.getSelectedIndex();

		if( idx==0 )
			return Defines.DrawingMode.WIREFRAME;
		else if( idx==1 )
			return Defines.DrawingMode.FILL;
		else
			return Defines.DrawingMode.WIREFRAME_FILL;
	}

	public Vector3D getLightDirection()
	{
		double x = ((SpinnerNumberModel)m_LightDirX.getModel()).getNumber().doubleValue();
		double y = ((SpinnerNumberModel)m_LightDirY.getModel()).getNumber().doubleValue();
		double z = ((SpinnerNumberModel)m_LightDirZ.getModel()).getNumber().doubleValue();
		return new Vector3D(x, y, z);
	}

	public double getLightAmbient()
	{
		return ((SpinnerNumberModel)m_LightAmbient.getModel()).getNumber().doubleValue();
	}

	public double getLightDiffuse()
	{
		return ((SpinnerNumberModel)m_LightDiffuse.getModel()).getNumber().doubleValue();
	}
}
