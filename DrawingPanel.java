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
	private int m_CurrentObjectIndex;
	private boolean m_DisplayNormals;
	private Color m_NormalsColor;
	private Defines.DrawingMode m_DrawingMode;
	private Defines.ShadingMode m_ShadingMode;
	private boolean m_InitFocusDone = false;
	private boolean m_DisplayAABB   = true;


	public DrawingPanel( MainWindow mw, Color backgroundColor )
	{
		super();
		setBackground(backgroundColor);
		
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		m_NormalsColor       = Defines.DefaultNormalsColor;
		m_DisplayNormals     = Defines.DisplayNormals;
		m_DrawingMode        = Defines.DefaultDrawingMode;
		m_ShadingMode        = Defines.DefaultShadingMode;
		m_Projection         = Defines.DefaultPerspectiveProjection;
		m_Camera             = new Camera();
		m_Viewport           = new Viewport();
		m_MainWindow         = mw;
		m_CurrentObjectIndex = -1;
		
		setupCameraControls();
		setupObjectControls();
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
				toggleAABB();
			}
		});

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Defines.KEY_TOGGLE_NORMALS, 0), "toggleNormals");
		getActionMap().put("toggleNormals", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				m_DisplayNormals = !m_DisplayNormals;
				repaint();
			}
		});
	}

	private void setupObjectControls()
	{
		int condition = JComponent.WHEN_FOCUSED;

		// Move Left (X-)
		getInputMap(condition).put(KeyStroke.getKeyStroke(Defines.KEY_MOVE_LEFT, 0), "moveLeft");
		getActionMap().put("moveLeft", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				moveSelectedObject(-Defines.ObjectMoveSpeed, 0, 0);
			}
		});

		// Move Right (X+)
		getInputMap(condition).put(KeyStroke.getKeyStroke(Defines.KEY_MOVE_RIGHT, 0), "moveRight");
		getActionMap().put("moveRight", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				moveSelectedObject(Defines.ObjectMoveSpeed, 0, 0);
			}
		});

		// Move Up (Z-)
		getInputMap(condition).put(KeyStroke.getKeyStroke(Defines.KEY_MOVE_UP, 0), "moveUp");
		getActionMap().put("moveUp", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				moveSelectedObject(0, 0, -Defines.ObjectMoveSpeed);
			}
		});

		// Move Down (Z+)
		getInputMap(condition).put(KeyStroke.getKeyStroke(Defines.KEY_MOVE_DOWN, 0), "moveDown");
		getActionMap().put("moveDown", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				moveSelectedObject(0, 0, Defines.ObjectMoveSpeed);
			}
		});

		// Move Higher (Y+)
		getInputMap(condition).put(KeyStroke.getKeyStroke(Defines.KEY_MOVE_HIGHER, 0), "moveHigher");
		getActionMap().put("moveHigher", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				moveSelectedObject(0, Defines.ObjectMoveSpeed, 0);
			}
		});

		// Move Lower (Y-)
		getInputMap(condition).put(KeyStroke.getKeyStroke(Defines.KEY_MOVE_LOWER, 0), "moveLower");
		getActionMap().put("moveLower", new AbstractAction()
		{
			public void actionPerformed( ActionEvent e )
			{
				moveSelectedObject(0, -Defines.ObjectMoveSpeed, 0);
			}
		});
	}

	private void moveSelectedObject( double dx, double dy, double dz )
	{
		int index = m_MainWindow.getObjectsList().getSelectedIndex();

		if( index >= 0 )
		{
			Object3D s = m_MainWindow.getModel().getObject(index);

			if( s != null )
			{
				s.translate(new Vector3D(dx, dy, dz));
				repaint();
			}
		}
	}

	public void performFocus()
	{
		Model model = m_MainWindow.getModel();

		if( model.getNumberOfObjects() == 0 )
			return;

		int selectedIndex   = m_MainWindow.getObjectsList().getSelectedIndex();
		Object3D targetObject = null;

		if( selectedIndex>=0 )
			targetObject = model.getObject(selectedIndex);
		else if( model.getNumberOfObjects()>0 )
			targetObject = model.getObject(0);

		if( targetObject != null )
		{
			Point3D center = targetObject.getCenter();
			double radius  = targetObject.getBoundingRadius();

			if( radius<=Utils.EPSILON )
				radius = Defines.DefaultFocusRadius;

			m_Camera.lookAt(center.x, center.y, center.z, radius);

			repaint();
		}
	}
	
	public void toggleAABB()
	{
		m_DisplayAABB = !m_DisplayAABB;
		repaint();
	}

	private class CameraMouseController extends MouseAdapter
	{
		private int lastX, lastY;

		public void mousePressed( MouseEvent e )
		{
			lastX = e.getX();
			lastY = e.getY();
			requestFocusInWindow();
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
	
	private void drawAxes(Graphics2D g2d)
	{
		double size = Defines.AxesSize;
		Point3D origin = new Point3D(0, 0, 0);
		Point3D xAxis  = new Point3D(size, 0, 0);
		Point3D yAxis  = new Point3D(0, size, 0);
		Point3D zAxis  = new Point3D(0, 0, size);
		
		Point2D pOrigin = m_Viewport.toScreen(m_Projection.transform(origin, m_Camera));
		Point2D pX      = m_Viewport.toScreen(m_Projection.transform(xAxis, m_Camera));
		Point2D pY      = m_Viewport.toScreen(m_Projection.transform(yAxis, m_Camera));
		Point2D pZ      = m_Viewport.toScreen(m_Projection.transform(zAxis, m_Camera));
		
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(2.0f));

		// Axis x
		g2d.setColor(Color.RED);
		Utils.drawLine(g2d, pOrigin, pX);
		g2d.drawString("X", (int)pX.x, (int)pX.y);

		// Axis Y
		g2d.setColor(Color.GREEN);
		Utils.drawLine(g2d, pOrigin, pY);
		g2d.drawString("Y", (int)pY.x, (int)pY.y);

		// Axis Z
		g2d.setColor(Color.BLUE);
		Utils.drawLine(g2d, pOrigin, pZ);
		g2d.drawString("Z", (int)pZ.x, (int)pZ.y);
		
		g2d.setStroke(oldStroke);
	}
	
	private double computeIntensity(Vector3D normal, Vector3D lightDir)
	{
		double dotProduct = normal.x * lightDir.x + normal.y * lightDir.y + normal.z * lightDir.z;
		double intensity  = Math.abs(dotProduct); 
		return Defines.LightAmbientFactor + (Defines.LightDiffuseFactor * intensity);
	}

	public void paintComponent( Graphics g )
	{
		super.paintComponent(g);

		Graphics2D g2d             = (Graphics2D)g;
		Dimension screenDimensions = getSize();

		if (screenDimensions.width <= 0 || screenDimensions.height <= 0) return;

		reshape(screenDimensions);
		
		Model model                  = m_MainWindow.getModel();
		Iterator<Object3D> itObjects = model.getIterator();
		int currentObjectIndex       = m_MainWindow.getObjectsList().getSelectedIndex();
		int index                    = 0;

		boolean doFill = (m_DrawingMode == Defines.DrawingMode.FILL || m_DrawingMode == Defines.DrawingMode.WIREFRAME_FILL);
		boolean doWire = (m_DrawingMode == Defines.DrawingMode.WIREFRAME || m_DrawingMode == Defines.DrawingMode.WIREFRAME_FILL);

		if( doWire && !doFill )
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		else
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Vector<Double> frontier    = new Vector<Double>();
		Vector<Point2D> pointsList = new Vector<Point2D>();

		if( doFill )
		{
			double[][] buffer1 = new double[screenDimensions.height][screenDimensions.width];
			BufferedImage img  = new BufferedImage(screenDimensions.width, screenDimensions.height, BufferedImage.TYPE_INT_ARGB);

			if( model.getNumberOfObjects() != 0 )
				for( int y=0; y<screenDimensions.height; y++ )
					Arrays.fill(buffer1[y], -Double.MAX_VALUE);

			Vector3D lightWorld = m_Camera.transformViewToWorld(Defines.LightDirection);
			itObjects           = model.getIterator(); 
			
			while( itObjects.hasNext() )
			{
				Object3D s                = itObjects.next();
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

					// Pre-calculation for FLAT and GOURAUD shading
					Color flatColor = null;
					Color cA = null, cB = null, cC = null;
					
					if( m_ShadingMode == Defines.ShadingMode.FLAT )
					{
						double intensity = computeIntensity(f.normal, lightWorld);
						flatColor = Utils.applyLighting(f.color, intensity);
					}
					else if( m_ShadingMode == Defines.ShadingMode.GOURAUD )
					{
						double iA = computeIntensity(nA, lightWorld);
						double iB = computeIntensity(nB, lightWorld);
						double iC = computeIntensity(nC, lightWorld);
						
						cA = Utils.applyLighting(f.color, iA);
						cB = Utils.applyLighting(f.color, iB);
						cC = Utils.applyLighting(f.color, iC);
					}

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

								if( z>buffer1[y][x] )
								{
									buffer1[y][x] = z;
									
									//--- Shading ---
									if( m_ShadingMode == Defines.ShadingMode.FLAT )
									{
										if( flatColor != null )
											img.setRGB(x, y, flatColor.getRGB());
									}
									else if( m_ShadingMode == Defines.ShadingMode.GOURAUD )
									{
										int red   = (int)(cA.getRed() * w1 + cB.getRed() * w2 + cC.getRed() * w3);
										int green = (int)(cA.getGreen() * w1 + cB.getGreen() * w2 + cC.getGreen() * w3);
										int blue  = (int)(cA.getBlue() * w1 + cB.getBlue() * w2 + cC.getBlue() * w3);
										
										red   = Math.min(255, Math.max(0, red));
										green = Math.min(255, Math.max(0, green));
										blue  = Math.min(255, Math.max(0, blue));
										
										img.setRGB(x, y, new Color(red, green, blue).getRGB());
									}
									else // PHONG
									{
										double nx = nA.x*w1 + nB.x*w2 + nC.x*w3;
										double ny = nA.y*w1 + nB.y*w2 + nC.y*w3;
										double nz = nA.z*w1 + nB.z*w2 + nC.z*w3;
										
										double norm = Math.sqrt(nx*nx + ny*ny + nz*nz);

										if( norm>Utils.EPSILON )
										{
											double inv = 1.0 / norm;
											nx *= inv; ny *= inv; nz *= inv;
										}
										
										double dotProduct = nx * lightWorld.x + ny * lightWorld.y + nz * lightWorld.z;
										double intensity  = Math.abs(dotProduct);
										double finalFactor = Defines.LightAmbientFactor + (Defines.LightDiffuseFactor * intensity);
										
										Color c = Utils.applyLighting(f.color, finalFactor);
										img.setRGB(x, y, c.getRGB());
									}
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
			itObjects = model.getIterator();
			
			while( itObjects.hasNext() )
			{
				Object3D s               = itObjects.next();
				Geometry geo             = s.getGeometry();
				Appearance a             = s.getAppearance();
				Iterator<Facet> itFacets = geo.getFacetsListIterator();
				int nbPoints             = geo.getNumberOfPoints();
				Point2D[] projectedPoints = new Point2D[nbPoints];

				g2d.setColor(a.getOutlineColor());
				g2d.setStroke(new BasicStroke(a.getOutlineWidth()));

				// Optimization: Project all vertices once
				for( int i=0; i<nbPoints; i++ )
				{
					Point3D p3d = geo.getPoint(i);
					if( p3d != null )
					{
						projectedPoints[i] = m_Viewport.toScreen(m_Projection.transform(p3d, m_Camera));
					}
				}

				Path2D wirePath = new Path2D.Float();

				while( itFacets.hasNext() )
				{
					Facet f = itFacets.next();
					
					Point2D a2d = projectedPoints[f.v1];
					Point2D b2d = projectedPoints[f.v2];
					Point2D c2d = projectedPoints[f.v3];

					if( a2d == null || b2d == null || c2d == null )
						continue;

					wirePath.moveTo(a2d.x, a2d.y);
					wirePath.lineTo(b2d.x, b2d.y);
					wirePath.lineTo(c2d.x, c2d.y);
					wirePath.closePath();
				}
				g2d.draw(wirePath);
				
				if( m_DisplayNormals )
				{
					g2d.setColor(m_NormalsColor);
					g2d.setStroke(new BasicStroke(1));
					Path2D normalsPath = new Path2D.Float();
					
					double objRadius   = s.getBoundingRadius();
					double normalScale = (objRadius > Utils.EPSILON) ? objRadius * 0.1 : 0.5;

					for( int i=0; i<nbPoints; i++ )
					{
						Point3D p         = geo.getPoint(i);
						Vector3D n        = geo.getVertexNormal(i);
						
						Point2D p2d_start = projectedPoints[i]; 
						
						if (p2d_start == null) continue;

						Vector3D p_plus_n = Vector3D.add(new Vector3D(p), Vector3D.multiplyByScalar(n, normalScale));
						Point2D p2d_end   = m_Viewport.toScreen(m_Projection.transform(p_plus_n.toPoint3D(), m_Camera));
						
						normalsPath.moveTo(p2d_start.x, p2d_start.y);
						normalsPath.lineTo(p2d_end.x, p2d_end.y);
					}
					g2d.draw(normalsPath);
				}
			}
		}

		if( m_DisplayAABB )
		{
			itObjects = model.getIterator();
			index    = 0;

			while( itObjects.hasNext() )
			{
				Object3D s     = itObjects.next();
				Color aabbCol = null;

				if( index == currentObjectIndex )
					aabbCol = Defines.AABBColorSelection;
				else
					aabbCol = Defines.AABBColorDefault;

				if( s != null )
					s.getAABB().draw(g2d, m_Viewport, m_Projection, m_Camera, aabbCol);

				index++;
			}
		}
		
		drawAxes(g2d);
	}

	public synchronized void update( Graphics g )
	{
		paint(g);
	}

	public void update( Observable o, Object arg )
	{
		if( !m_InitFocusDone && m_MainWindow.getModel().getNumberOfObjects()>0 )
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
	
	public void setShadingMode( Defines.ShadingMode mode )
	{
		m_ShadingMode = mode;
	}
	
	public Defines.ShadingMode getShadingMode()
	{
		return m_ShadingMode;
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
