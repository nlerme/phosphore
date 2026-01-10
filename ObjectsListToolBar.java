import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class ObjectsListToolBar extends JPanel
{
	private JButton m_AddObject;
	private JButton m_DeleteObject;
	private JButton m_DeleteAllObjects;
	private JButton m_ModifyObject;
	private JButton m_ObjectInfo;
	private MainWindow m_MainWindow;


	public ObjectsListToolBar( MainWindow mw )
	{
		super(new BorderLayout(5, 5));
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		m_MainWindow = mw;

		init();
	}

	public void setListener( MainController c )
	{
		m_AddObject.addActionListener(c.new ObjectsListToolBar_AddObjectHandler());
		m_ModifyObject.addActionListener(c.new ObjectsListToolBar_ModifyObjectHandler());
		m_DeleteObject.addActionListener(c.new ObjectsListToolBar_DeleteObjectHandler());
		m_DeleteAllObjects.addActionListener(c.new ObjectsListToolBar_DeleteAllObjectsHandler());
		m_ObjectInfo.addActionListener(c.new ObjectsListToolBar_ObjectInfoHandler());
	}

	public void init()
	{
		JPanel gridPanel = new JPanel(new GridLayout(2, 2, 5, 5));

		m_AddObject = new JButton("Add an object");
		m_AddObject.setToolTipText("Add an object");
		gridPanel.add(m_AddObject);

		m_ModifyObject = new JButton("Modify an object");
		m_ModifyObject.setToolTipText("Modify an object");
		gridPanel.add(m_ModifyObject);

		m_DeleteObject = new JButton("Delete an object");
		m_DeleteObject.setToolTipText("Delete an object");
		gridPanel.add(m_DeleteObject);

		m_DeleteAllObjects = new JButton("Delete all objects");
		m_DeleteAllObjects.setToolTipText("Delete all objects");
		gridPanel.add(m_DeleteAllObjects);

		add(gridPanel, BorderLayout.CENTER);

		m_ObjectInfo = new JButton("Object information");
		m_ObjectInfo.setToolTipText("Object information");
		add(m_ObjectInfo, BorderLayout.SOUTH);
	}
}
