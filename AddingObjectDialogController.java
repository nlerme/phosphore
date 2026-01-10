import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;


@SuppressWarnings("deprecation")
public class AddingObjectDialogController
{
	private Model m_Model;
	private AddingObjectDialog m_AddingObjectDialog;


	public AddingObjectDialogController( Model m, AddingObjectDialog w )
	{
		m_Model              = m;
		m_AddingObjectDialog = w;
		m_AddingObjectDialog.setListener(this);
	}

	public class AddingObjectDialog_AddObjectHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			final JButton b = (JButton)e.getSource();
			Object3D s      = null;

			if( m_AddingObjectDialog.isChoice1Selected() )
			{
				s = m_AddingObjectDialog.getPredefinedObject();

				if( s == null )
				{
					JOptionPane.showMessageDialog(null, "Unable to add the predefined shape", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				finalizeObjectAdding(s);
				m_AddingObjectDialog.dispose();
			}
			else if( m_AddingObjectDialog.isChoice2Selected() )
			{
				String shapeFileName = m_AddingObjectDialog.getFileName();

				if( shapeFileName.isEmpty() )
				{
					JOptionPane.showMessageDialog(null, "Please select a file to load", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else
				{
					s = ObjectLoader.load(shapeFileName);

					if( s == null )
					{
						JOptionPane.showMessageDialog(null, "Unable to add the custom shape named '" + shapeFileName + "'", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					finalizeObjectAdding(s);
					m_AddingObjectDialog.dispose();
				}
			}
			else if( m_AddingObjectDialog.isChoice3Selected() )
			{
				final String prompt = m_AddingObjectDialog.getPromptText();
				
				if( prompt == null || prompt.trim().isEmpty() )
				{
					JOptionPane.showMessageDialog(null, "Please enter a prompt to generate the object", "Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				b.setEnabled(false);
				b.setText("Generating...");
				m_AddingObjectDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				Thread thread = new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							final Object3D generatedObject = GeminiClient.generateObjectFromPrompt(prompt);
							
							SwingUtilities.invokeLater(new Runnable()
							{
								public void run()
								{
									if( generatedObject != null )
									{
										if( m_AddingObjectDialog.getObjectNameEntry().isEmpty() )
										{
											String safeName = prompt.length() > 15 ? prompt.substring(0, 15) : prompt;
											m_AddingObjectDialog.getObjectNameEntryComponent().setText(safeName);
										}
										
										finalizeObjectAdding(generatedObject);
										m_AddingObjectDialog.dispose();
									}
									else
									{
										JOptionPane.showMessageDialog(m_AddingObjectDialog, "Gemini produced empty data.", "Generation Error", JOptionPane.ERROR_MESSAGE);
										resetUI();
									}
								}
							});
						}
						catch( final Exception ex )
						{
							SwingUtilities.invokeLater(new Runnable()
							{
								public void run()
								{
									ex.printStackTrace();
									JOptionPane.showMessageDialog(m_AddingObjectDialog, "Error generating object:\n" + ex.getMessage(), "API Error", JOptionPane.ERROR_MESSAGE);
									resetUI();
								}
							});
						}
					}
				});
				
				thread.start();
			}
		}

		private void resetUI()
		{
			m_AddingObjectDialog.setCursor(Cursor.getDefaultCursor());
		}

		private void finalizeObjectAdding( Object3D s )
		{
			if( s != null )
			{
				String shapeName = m_AddingObjectDialog.getObjectNameEntry();

				if( !shapeName.isEmpty() )
					s.setName(shapeName);
				else if ( m_AddingObjectDialog.isChoice3Selected() )
					s.setName("Generated_" + System.currentTimeMillis());

				Appearance a = s.getAppearance();
				a.setOutlineColor(m_AddingObjectDialog.getOutlineColor());
				a.setOutlineWidth(m_AddingObjectDialog.getOutlineWidth());
				s.setAppearance(a);

				s.setFaceColor(m_AddingObjectDialog.getObjectColor());

				s.translate(m_AddingObjectDialog.getCoords());
				m_Model.addObject(s);
			}
		}
	}

	public class AddingObjectDialog_ObjectOutlineColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_AddingObjectDialog, "Choose an outline color", m_AddingObjectDialog.getOutlineColor());

			if( c != null )
				m_AddingObjectDialog.setOutlineColor(c);
		}
	}

	public class AddingObjectDialog_ObjectColorHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			Color c   = JColorChooser.showDialog(m_AddingObjectDialog, "Choose object color", m_AddingObjectDialog.getObjectColor());

			if( c != null )
				m_AddingObjectDialog.setObjectColor(c);
		}
	}

	public class AddingObjectDialog_LoadFileHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			JButton b = (JButton)e.getSource();
			JFileChooser chooser           = new JFileChooser(new File("data"));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Objects files", "ply", "obj");

			chooser.setFileFilter(filter);
			if( chooser.showOpenDialog(m_AddingObjectDialog) == JFileChooser.APPROVE_OPTION )
				m_AddingObjectDialog.setFileName(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	public class AddingObjectDialog_CancelHandler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_AddingObjectDialog.dispose();
		}
	}

	public class AddingObjectDialog_Choice1Handler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_AddingObjectDialog.setChoice1();
		}
	}

	public class AddingObjectDialog_Choice2Handler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_AddingObjectDialog.setChoice2();
		}
	}

	public class AddingObjectDialog_Choice3Handler implements ActionListener
	{
		public void actionPerformed( ActionEvent e )
		{
			m_AddingObjectDialog.setChoice3();
		}
	}
}
