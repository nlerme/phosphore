import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class MainToolBar extends JToolBar
{
	private JButton m_Focus;
	private JButton m_ToggleAABB;
	private JButton m_ToggleNormals;
	private JComboBox<String> m_DrawingMode;
	private JComboBox<String> m_ShadingMode;


	public MainToolBar( int orientation )
	{
		super(orientation);
		setFloatable(true);
		init();
	}

	public void init()
	{
		// Boutons d'action
		m_Focus = new JButton("Focus (F)");
		add(m_Focus);

		addSeparator();

		m_ToggleAABB = new JButton("AABB (B)");
		add(m_ToggleAABB);

		m_ToggleNormals = new JButton("Normals (N)");
		add(m_ToggleNormals);
		
		addSeparator();
		
		add(new JLabel(" Mode: "));
		String[] drawModes = {"Wireframe", "Fill", "Wireframe + Fill"};
		m_DrawingMode = new JComboBox<>(drawModes);
		m_DrawingMode.setSelectedIndex(0); // Wireframe par défaut
		m_DrawingMode.setMaximumSize(m_DrawingMode.getPreferredSize());
		add(m_DrawingMode);

		add(new JLabel(" Shading: "));
		String[] shadeModes = {"Flat", "Gouraud", "Phong"};
		m_ShadingMode = new JComboBox<>(shadeModes);
		m_ShadingMode.setSelectedIndex(0); // Flat par défaut
		m_ShadingMode.setMaximumSize(m_ShadingMode.getPreferredSize());
		add(m_ShadingMode);
	}

	public void setListener( MainController c )
	{
		m_Focus.addActionListener(c.new MainToolBar_FocusHandler());
		m_ToggleAABB.addActionListener(c.new MainToolBar_ToggleAABBHandler());
		m_ToggleNormals.addActionListener(c.new MainToolBar_ToggleNormalsHandler());
		m_DrawingMode.addActionListener(c.new MainToolBar_DrawingModeHandler());
		m_ShadingMode.addActionListener(c.new MainToolBar_ShadingModeHandler());
	}
	
	public JComboBox<String> getDrawingModeComboBox()
	{
		return m_DrawingMode;
	}
	
	public JComboBox<String> getShadingModeComboBox()
	{
		return m_ShadingMode;
	}
}
