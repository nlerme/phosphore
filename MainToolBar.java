import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


public class MainToolBar extends JToolBar
{
	private JButton m_Translation;
	private JButton m_Rotation;
	private JButton m_Scaling;


	public MainToolBar( int orientation )
	{
		super(orientation);
		setFloatable(true);
		init();
	}

	public void init()
	{
		m_Translation = new JButton("Translation ...");
		add(m_Translation);

		m_Rotation = new JButton("Rotation ...");
		add(m_Rotation);

		m_Scaling = new JButton("Scaling ...");
		add(m_Scaling);
	}

	public void setListener( MainController c )
	{
		m_Translation.addActionListener(c.new MainToolBar_TranslationHandler());
		m_Rotation.addActionListener(c.new MainToolBar_RotationHandler());
		m_Scaling.addActionListener(c.new MainToolBar_ScalingHandler());
	}
}
