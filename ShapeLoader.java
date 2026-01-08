import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


public class ShapeLoader
{
	public static Shape3D load( String fileName )
	{
		int pos = fileName.lastIndexOf(".");

		if( (pos >= 0) && (pos != fileName.length() - 1) )
		{
			String ext = fileName.substring(pos + 1).toLowerCase();

			if( ext.equals("ply") )
				return loadFromPLY(fileName);
			else if( ext.equals("obj") )
				return loadFromOBJ(fileName);
		}

		return null;
	}

	private static Shape3D loadFromPLY( String fileName )
	{
		try
		{
			BufferedReader i = new BufferedReader(new FileReader(new File(fileName)));
			String line;
			StringTokenizer tok;

			line = i.readLine();

			if( line == null || !line.equals("ply") )
				return null;

			line = i.readLine();
			tok  = new StringTokenizer(line);

			if( line == null || tok.countTokens() != 3 || !tok.nextToken().equals("format") || !tok.nextToken().equals("ascii") || !tok.nextToken().equals("1.0") )
				return null;

			boolean endHeader    = false;
			int numberOfVertices = 0;
			int numberOfFacets   = 0;


			while( (line = i.readLine()) != null && !endHeader )
			{
				if( line.equals("end_header") )
				{
					endHeader = true;
					break;
				}

				tok = new StringTokenizer(line);

				if( tok.countTokens() == 3 )
				{
					if( tok.nextToken().equals("element") )
					{
						String s1 = tok.nextToken();

						if( s1.equals("vertex") )
							numberOfVertices = Integer.parseInt(tok.nextToken());
						else if( s1.equals("face") )
							numberOfFacets = Integer.parseInt(tok.nextToken());
					}
				}
			}

			if( !endHeader )
				return null;

			Shape3D s    = new Shape3D();
			Geometry geo = s.getGeometry();

			while( numberOfVertices > 0 && (line = i.readLine()) != null )
			{
				tok = new StringTokenizer(line);

				if( tok.countTokens() == 3 )
				{
					double x = Double.parseDouble(tok.nextToken());
					double y = Double.parseDouble(tok.nextToken());
					double z = Double.parseDouble(tok.nextToken());
					geo.addPoint(new Point3D(x, y, z));
				}

				--numberOfVertices;
			}

			while( numberOfFacets > 0 && (line = i.readLine()) != null )
			{
				tok = new StringTokenizer(line);

				if( tok.countTokens() == 4 )
				{
					tok.nextToken();
					int v1 = Integer.parseInt(tok.nextToken());
					int v2 = Integer.parseInt(tok.nextToken());
					int v3 = Integer.parseInt(tok.nextToken());
					geo.addFacet(new Facet(v1, v2, v3));
				}

				--numberOfFacets;
			}

			if( geo.getNumberOfPoints() == 0 || geo.getNumberOfFacets() == 0 )
				return null;

			s.setGeometry(geo);
			s.processNormals();

			return s;
		}
		catch( Exception e )
		{
			return null;
		}
	}

	private static Shape3D loadFromOBJ( String fileName )
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			Shape3D s             = new Shape3D();
			Geometry geo          = s.getGeometry();
			String line;

			while( (line = reader.readLine()) != null )
			{
				line = line.trim();

				if( line.startsWith("#") || line.isEmpty() ) continue;

				StringTokenizer tok = new StringTokenizer(line);

				if( !tok.hasMoreTokens() )
					continue;

				String type = tok.nextToken();

				if( type.equals("v") ) // Vertex
				{
					double x = Double.parseDouble(tok.nextToken());
					double y = Double.parseDouble(tok.nextToken());
					double z = Double.parseDouble(tok.nextToken());
					geo.addPoint(new Point3D(x, y, z));
				}
				else if( type.equals("f") ) // Face
				{
					Vector<Integer> indices = new Vector<Integer>();

					while( tok.hasMoreTokens() )
					{
						String token = tok.nextToken();
						int slashPos = token.indexOf('/');

						if( slashPos != -1 )
							token = token.substring(0, slashPos);
						
						// OBJ indices are 1-based, we need 0-based
						indices.add(Integer.parseInt(token) - 1);
					}

					if( indices.size() >= 3 )
					{
						// Basic triangulation (fan) for polygon
						int v0 = indices.get(0);

						for( int i=1; i<indices.size()-1; i++ )
						{
							int v1 = indices.get(i);
							int v2 = indices.get(i+1);
							geo.addFacet(new Facet(v0, v1, v2));
						}
					}
				}
			}
			reader.close();

			if( geo.getNumberOfPoints() == 0 || geo.getNumberOfFacets() == 0 )
				return null;

			s.setGeometry(geo);
			s.processNormals();

			return s;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return null;
		}
	}
}
