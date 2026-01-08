import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class MainWindow extends JFrame
{
	private Dimension m_Dimensions;
	private Model m_Model;
	private MainToolBar m_MainToolBar;
	private ShapesListToolBar m_ShapesListToolBar;
	private MenuBar m_MenuBar;
	// Two views
	private DrawingPanel m_DrawingPanel;
	private ShapesList m_ShapesList;


	public MainWindow( String title, Dimension dims, Model m )
	{
		super(title);
		setSize(dims);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_Model      = m;
		m_Dimensions = dims;

		m_MenuBar = new MenuBar();
		setJMenuBar(m_MenuBar);

		m_MainToolBar = new MainToolBar(MainToolBar.HORIZONTAL);
		add(m_MainToolBar, BorderLayout.NORTH);

		// First SplitPane (up / down)
		m_ShapesList        = new ShapesList(this);
		m_ShapesListToolBar = new ShapesListToolBar(this);

		JSplitPane sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(m_ShapesList), m_ShapesListToolBar);
		sp1.setOneTouchExpandable(true);
		sp1.setDividerLocation(getHeight() - 210);
		add(sp1, BorderLayout.WEST);

		// Second SplitPane (left / right)
		m_DrawingPanel = new DrawingPanel(this, Defines.DefaultBackgroundColor);

		JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp1, m_DrawingPanel);
		sp2.setDividerLocation(300);
		sp2.setOneTouchExpandable(false);
		add(sp2, BorderLayout.CENTER);
	}

	public DrawingPanel getDrawingPanel()
	{
		return m_DrawingPanel;
	}

	public ShapesList getShapesList()
	{
		return m_ShapesList;
	}

	public Model getModel()
	{
		return m_Model;
	}

	public void setListener( MainController c )
	{
		m_ShapesList.setListener(c);
		m_MenuBar.setListener(c);
		m_MainToolBar.setListener(c);
		m_ShapesListToolBar.setListener(c);
	}
}
