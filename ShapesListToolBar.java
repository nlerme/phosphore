import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class ShapesListToolBar extends JPanel
{
	private JButton m_AddShape;
	private JButton m_DeleteShape;
	private JButton m_DeleteAllShapes;
	private JButton m_ModifyShape;
	private MainWindow m_MainWindow;


	public ShapesListToolBar( MainWindow mw )
	{
		super(new GridLayout(0, 2, 5, 5));
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		m_MainWindow = mw;

		init();
	}

	public void setListener( MainController c )
	{
		m_AddShape.addActionListener(c.new ShapesListToolBar_AddShapeHandler());
		m_ModifyShape.addActionListener(c.new ShapesListToolBar_ModifyShapeHandler());
		m_DeleteShape.addActionListener(c.new ShapesListToolBar_DeleteShapeHandler());
		m_DeleteAllShapes.addActionListener(c.new ShapesListToolBar_DeleteAllShapesHandler());
	}

	public void init()
	{
		m_AddShape = new JButton("Add a shape");
		m_AddShape.setToolTipText("Add a shape");
		add(m_AddShape);

		m_ModifyShape = new JButton("Modify a shape");
		m_ModifyShape.setToolTipText("Modify a shape");
		add(m_ModifyShape);

		m_DeleteShape = new JButton("Delete a shape");
		m_DeleteShape.setToolTipText("Delete a shape");
		add(m_DeleteShape);

		m_DeleteAllShapes = new JButton("Delete all shapes");
		m_DeleteAllShapes.setToolTipText("Delete all shapes");
		add(m_DeleteAllShapes);
	}
}
