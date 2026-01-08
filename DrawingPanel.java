import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.geom.*;


@SuppressWarnings("deprecation")
public class DrawingPanel extends JPanel implements Observer
{
	private MainWindow m_MainWindow;
	private Projection m_Projection;
	private Camera m_Camera;
	private Viewport m_Viewport;
	private int m_CurrentShapeIndex;
	private boolean m_DisplayNormals;
	private Color m_NormalsColor;
	private Defines.DrawingMode m_DrawingMode;
	private boolean m_InitFocusDone = false;
	private boolean m_DisplayAABB   = false;


	public DrawingPanel( MainWindow mw, Color backgroundColor )
	{
		super();
		setBackground(backgroundColor);

		m_NormalsColor      = Defines.DefaultNormalsColor;
		m_DisplayNormals    = Defines.DisplayNormals;
		m_DrawingMode       = Defines.DefaultDrawingMode;
		m_Projection        = Defines.DefaultPerspectiveProjection;
		m_Camera            = new Camera();
		m_Viewport          = new Viewport();
		m_MainWindow        = mw;
		m_CurrentShapeIndex = -1;
		
		setupCameraControls();
	}

	private void setupCameraControls()
	{
		CameraMouseController cmc = new CameraMouseController();

		addMouseListener(cmc);
		addMouseMotionListener(cmc);
		addMouseWheelListener(cmc);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Defines.KEY_FOCUS, 0), "focus");
		getActionMap().put("focus", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				performFocus();
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Defines.KEY_TOGGLE_AABB, 0), "toggleAABB");
		getActionMap().put("toggleAABB", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				m_DisplayAABB = !m_DisplayAABB;
				repaint();
			}
		});
	}

	private void performFocus()
	{
		Model model = m_MainWindow.getModel();

		if( model.getNumberOfShapes() == 0 )
			return;

		int selectedIndex   = m_MainWindow.getShapesList().getSelectedIndex();
		Shape3D targetShape = null;

		if( selectedIndex>=0 )
			targetShape = model.getShape(selectedIndex);
		else if( model.getNumberOfShapes()>0 )
			targetShape = model.getShape(0);

		if( targetShape != null )
		{
			Point3D center = targetShape.getCenter();
			double radius  = targetShape.getBoundingRadius();

			if( radius<=Utils.EPSILON )
				radius = Defines.DefaultFocusRadius;

			m_Camera.lookAt(center.x, center.y, center.z, radius);

			repaint();
		}
	}

	private class CameraMouseController extends MouseAdapter
	{
		private int lastX, lastY;

		public void mousePressed( MouseEvent e )
		{
			lastX = e.getX();
			lastY = e.getY();
		}

		public void mouseDragged( MouseEvent e )
		{
			int dx = e.getX() - lastX;
			int dy = e.getY() - lastY;
			
			if( SwingUtilities.isLeftMouseButton(e) )
			{
				if( e.isControlDown() )
					m_Camera.addPan(dx, dy);
				else
					m_Camera.addOrbit(dx, dy);

				repaint();
			}

			lastX = e.getX();
			lastY = e.getY();
		}

		public void mouseWheelMoved( MouseWheelEvent e )
		{
			double zoomFactor = Defines.MouseZoomSpeed;

			if( e.isShiftDown() )
				zoomFactor = Defines.MouseZoomFactorFine;

			m_Camera.addZoom(e.getWheelRotation()*zoomFactor);

			repaint();
		}
	}

	public void reshape( Dimension screenDimensions )
	{
		m_Projection.reshape(screenDimensions);
		m_Viewport.reshape(screenDimensions);
	}

	public void paintComponent( Graphics g )
	{
		super.paintComponent(g);

		Graphics2D g2d             = (Graphics2D)g;
		Dimension screenDimensions = getSize();

		if (screenDimensions.width <= 0 || screenDimensions.height <= 0) return;

		reshape(screenDimensions);

		Model model                = m_MainWindow.getModel();
		Iterator<Shape3D> itShapes = model.getIterator();
		int currentShapeIndex      = m_MainWindow.getShapesList().getSelectedIndex();
		int index                  = 0;

		boolean doFill = (m_DrawingMode == Defines.DrawingMode.FILL || m_DrawingMode == Defines.DrawingMode.WIREFRAME_FILL);
		boolean doWire = (m_DrawingMode == Defines.DrawingMode.WIREFRAME || m_DrawingMode == Defines.DrawingMode.WIREFRAME_FILL);

		Vector<Double> frontier    = new Vector<Double>();
		Vector<Point2D> pointsList = new Vector<Point2D>();

		if( doFill )
		{
			double[][] buffer1 = new double[screenDimensions.height][screenDimensions.width];
			BufferedImage img = new BufferedImage(screenDimensions.width, screenDimensions.height, BufferedImage.TYPE_INT_ARGB);

			if( model.getNumberOfShapes() != 0 )
			{
				for( int y=0; y<screenDimensions.height; y++ )
				{
					Arrays.fill(buffer1[y], -Double.MAX_VALUE);
				}
			}

			Vector3D lightWorld = m_Camera.transformViewToWorld(Defines.LightDirection);
			itShapes            = model.getIterator(); 
			
			while( itShapes.hasNext() )
			{
				Shape3D s                = itShapes.next();
				Geometry geo             = s.getGeometry();
				Iterator<Facet> itFacets = geo.getFacetsListIterator();

				while( itFacets.hasNext() )
				{
					Facet f = itFacets.next();
					Point3D a3d = geo.getPoint(f.v1);
					Point3D b3d = geo.getPoint(f.v2);
					Point3D c3d = geo.getPoint(f.v3);
					
					Vector3D nA = geo.getVertexNormal(f.v1);
					Vector3D nB = geo.getVertexNormal(f.v2);
					Vector3D nC = geo.getVertexNormal(f.v3);

					if( a3d == null || b3d == null || c3d == null )
						continue;

					Point3D pA = m_Projection.transform(a3d, m_Camera);
					Point3D pB = m_Projection.transform(b3d, m_Camera);
					Point3D pC = m_Projection.transform(c3d, m_Camera);

					Point2D a2d = m_Viewport.toScreen(pA);
					Point2D b2d = m_Viewport.toScreen(pB);
					Point2D c2d = m_Viewport.toScreen(pC);

					int y, y_min, y_max;

					pointsList.clear();
					pointsList.add(a2d);
					pointsList.add(b2d);
					pointsList.add(c2d);
					pointsList.add(pointsList.get(0));

					y_min = y_max = (int)pointsList.get(0).y;
					int nbPoints1 = pointsList.size();

					for( int i=1; i<(nbPoints1 - 1); i++ )
					{
						y = (int)pointsList.get(i).y;
						if( y < y_min ) y_min = y;
						if( y > y_max )	y_max = y;
					}
					
					if( y_min < 0 ) y_min = 0;
					if( y_max >= screenDimensions.height ) y_max = screenDimensions.height - 1;

					for( y=y_min; y<=y_max; y++ )
					{
						frontier.clear();
						nbPoints1 = pointsList.size();

						for( int i=0; i<(nbPoints1 - 1); i++ )
						{
							Point2D p1 = pointsList.get(i);
							Point2D p2 = pointsList.get(i + 1);

							if( (p1.y <= y && p2.y > y) || (p2.y <= y && p1.y > y) )
							{
								double x = Utils.intersection(p1, p2, y);
								frontier.add(Double.valueOf(x));
							}
						}

						Collections.sort(frontier);
						int nbPoints2 = frontier.size();

						for( int i=0; i<nbPoints2; i+=2 )
						{
							int xmin = frontier.get(i).intValue();
							int xmax = frontier.get(i + 1).intValue();

							if( xmin < 0 ) xmin = 0;
							if( xmax >= screenDimensions.width ) xmax = screenDimensions.width - 1;

							for( int x=xmin; x<=xmax; x++ )
							{
								double denom = (b2d.y-c2d.y)*(a2d.x-c2d.x) + (c2d.x-b2d.x)*(a2d.y-c2d.y);

								if( Math.abs(denom)<Utils.EPSILON )
									continue;

								double w1 = ((b2d.y-c2d.y)*(x-c2d.x)+(c2d.x-b2d.x)*(y-c2d.y)) / denom;
								double w2 = ((c2d.y-a2d.y)*(x-c2d.x)+(a2d.x-c2d.x)*(y-c2d.y)) / denom;
								double w3 = 1.0-w1-w2;

								double z = w1*pA.z + w2*pB.z + w3*pC.z;

								if( z > buffer1[y][x] )
								{
									buffer1[y][x] = z;
									
									double nx = nA.x*w1 + nB.x*w2 + nC.x*w3;
									double ny = nA.y*w1 + nB.y*w2 + nC.y*w3;
									double nz = nA.z*w1 + nB.z*w2 + nC.z*w3;
									
									double norm = Math.sqrt(nx*nx + ny*ny + nz*nz);
									if (norm > Utils.EPSILON) {
										double inv = 1.0 / norm;
										nx *= inv; ny *= inv; nz *= inv;
									}
									
									double dotProduct = nx * lightWorld.x + ny * lightWorld.y + nz * lightWorld.z;
									double intensity  = Math.max(0.0, dotProduct);
									double finalFactor = Defines.LightAmbientFactor + (Defines.LightDiffuseFactor * intensity);
									
									Color c = Utils.applyLighting(f.color, finalFactor);
									img.setRGB(x, y, c.getRGB());
								}
							}
						}
					}
				}
			}

			g2d.drawImage(img, 0, 0, null);
		}

		if( doWire )
		{
			itShapes = model.getIterator();
			
			while( itShapes.hasNext() )
			{
				Shape3D s                = itShapes.next();
				Geometry geo             = s.getGeometry();
				Appearance a             = s.getAppearance();
				Iterator<Facet> itFacets = geo.getFacetsListIterator();

				g2d.setColor(a.getOutlineColor());
				g2d.setStroke(new BasicStroke(a.getOutlineWidth()));

				while( itFacets.hasNext() )
				{
					Facet f = itFacets.next();
					Point3D a3d = geo.getPoint(f.v1);
					Point3D b3d = geo.getPoint(f.v2);
					Point3D c3d = geo.getPoint(f.v3);

					if( a3d == null || b3d == null || c3d == null )
						continue;

					Point2D a2d = m_Viewport.toScreen(m_Projection.transform(a3d, m_Camera));
					Point2D b2d = m_Viewport.toScreen(m_Projection.transform(b3d, m_Camera));
					Point2D c2d = m_Viewport.toScreen(m_Projection.transform(c3d, m_Camera));

					Utils.drawLine(g2d, a2d, b2d);
					Utils.drawLine(g2d, a2d, c2d);
					Utils.drawLine(g2d, b2d, c2d);
				}
				
				if( m_DisplayNormals )
				{
					g2d.setColor(m_NormalsColor);
					g2d.setStroke(new BasicStroke(1));
					
					for( int i=0; i<geo.getNumberOfPoints(); i++ )
					{
						Point3D p         = geo.getPoint(i);
						Vector3D n        = geo.getVertexNormal(i);
						
						Point2D p2d_start = m_Viewport.toScreen(m_Projection.transform(p, m_Camera));
						Vector3D p_plus_n = Vector3D.add(new Vector3D(p), Vector3D.multiplyByScalar(n, 0.5));
						Point2D p2d_end   = m_Viewport.toScreen(m_Projection.transform(p_plus_n.toPoint3D(), m_Camera));
						
						Utils.drawLine(g2d, p2d_start, p2d_end);
					}
				}
			}
		}

		if( m_DisplayAABB )
		{
			itShapes = model.getIterator();
			index    = 0;

			while( itShapes.hasNext() )
			{
				Shape3D s     = itShapes.next();
				Color aabbCol = null;

				if( index == currentShapeIndex )
					aabbCol = Defines.AABBColorSelection;
				else
					aabbCol = Defines.AABBColorDefault;

				if( s != null )
					s.getAABB().draw(g2d, m_Viewport, m_Projection, m_Camera, aabbCol);

				index++;
			}
		}
	}

	public synchronized void update( Graphics g )
	{
		paint(g);
	}

	public void update( Observable o, Object arg )
	{
		if( !m_InitFocusDone && m_MainWindow.getModel().getNumberOfShapes()>0 )
		{
			performFocus();
			m_InitFocusDone = true;
		}

		repaint();
	}

	public void setDrawingMode( Defines.DrawingMode drawingMode )
	{
		m_DrawingMode = drawingMode;
	}

	public Defines.DrawingMode getDrawingMode()
	{
		return m_DrawingMode;
	}

	public void setProjection( Projection p )
	{
		m_Projection = p;
	}

	public Projection getProjection()
	{
		return m_Projection;
	}

	public boolean getDisplayNormalsState()
	{
		return m_DisplayNormals;
	}

	public void setDisplayNormalsState( boolean displayNormals )
	{
		m_DisplayNormals = displayNormals;
	}

	public Color getNormalsColor()
	{
		return m_NormalsColor;
	}

	public void setNormalsColor( Color normalsColor )
	{
		m_NormalsColor = normalsColor;
	}
}
