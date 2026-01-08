import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;


public class AddingShapeDialogController
{
	private Model m_Model;
	private AddingShapeDialog m_AddingShapeDialog;


	public AddingShapeDialogController( Model m, AddingShapeDialog w )
	{
		m_Model             = m;
		m_AddingShapeDialog = w;
		m_AddingShapeDialog.setListener(this);
	}

	public class AddingShapeDialog_AddShapeHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Shape3D s = null;

			if( m_AddingShapeDialog.isChoice1Selected() )
			{
				s = m_AddingShapeDialog.getPredefinedShape();

				if( s == null )
				{
					JOptionPane.showMessageDialog(null, "Unable to add the predefined shape", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			else if( m_AddingShapeDialog.isChoice2Selected() )
			{
				String shapeFileName = m_AddingShapeDialog.getFileName();

				if( shapeFileName.isEmpty() )
				{
					JOptionPane.showMessageDialog(null, "Please select a file to load", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					s = ShapeLoader.load(shapeFileName);

					if( s == null )
					{
						JOptionPane.showMessageDialog(null, "Unable to add the custom shape named '" + shapeFileName + "'", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}

			if( s != null )
			{
				String shapeName = m_AddingShapeDialog.getShapeNameEntry();

				if( !shapeName.isEmpty() )
					s.setName(shapeName);

				Appearance a = s.getAppearance();
				a.setOutlineColor(m_AddingShapeDialog.getOutlineColor());
				a.setOutlineWidth(m_AddingShapeDialog.getOutlineWidth());
				s.setAppearance(a);

				s.setFaceColor(m_AddingShapeDialog.getShapeColor());

				s.translate(m_AddingShapeDialog.getCoords());
				m_Model.addShape(s);
			}

			m_AddingShapeDialog.dispose();
		}
	}

	public class AddingShapeDialog_ShapeOutlineColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_AddingShapeDialog, "Choose an outline color", m_AddingShapeDialog.getOutlineColor());

			if( c != null )
				m_AddingShapeDialog.setOutlineColor(c);
		}
	}

	public class AddingShapeDialog_ShapeColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_AddingShapeDialog, "Choose object color", m_AddingShapeDialog.getShapeColor());

			if( c != null )
				m_AddingShapeDialog.setShapeColor(c);
		}
	}

	public class AddingShapeDialog_LoadFileHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			JFileChooser chooser           = new JFileChooser(new File("../data"));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Objects files", "ply", "obj");

			chooser.setFileFilter(filter);
			if( chooser.showOpenDialog(m_AddingShapeDialog) == JFileChooser.APPROVE_OPTION )
				m_AddingShapeDialog.setFileName(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	public class AddingShapeDialog_CancelHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_AddingShapeDialog.dispose();
		}
	}

	public class AddingShapeDialog_Choice1Handler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_AddingShapeDialog.setChoice1();
		}
	}

	public class AddingShapeDialog_Choice2Handler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_AddingShapeDialog.setChoice2();
		}
	}
}
