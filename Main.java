import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;


@SuppressWarnings("deprecation")
public class Main
{
	public static void main( String[] args )
	{
		Dimension screen  = Toolkit.getDefaultToolkit().getScreenSize();
		Model m           = new Model();
		MainWindow window = new MainWindow(Defines.SoftwareName + " - " + Defines.SoftwareVersion, screen, m);
		MainController c  = new MainController(m, window);

		window.setLocationRelativeTo(window.getParent());
		window.setVisible(true);
	}
}
