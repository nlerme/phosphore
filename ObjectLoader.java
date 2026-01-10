import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class ObjectLoader
{
	public static Object3D load( String fileName )
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

	public static Object3D loadFromString( String objData )
	{
		return parseOBJ(new BufferedReader(new StringReader(objData)));
	}

	private static Object3D loadFromPLY( String fileName )
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

			if( line == null || tok.countTokens() != 3 || !tok.nextToken().equals("format") )
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

				if( tok.countTokens() == 3 && tok.nextToken().equals("element") )
				{
					String s1 = tok.nextToken();
					if( s1.equals("vertex") ) numberOfVertices = Integer.parseInt(tok.nextToken());
					else if( s1.equals("face") ) numberOfFacets = Integer.parseInt(tok.nextToken());
				}
			}

			if( !endHeader ) return null;

			Object3D s   = new Object3D();
			Geometry geo = s.getGeometry();

			while( numberOfVertices > 0 && (line = i.readLine()) != null )
			{
				tok = new StringTokenizer(line);

				if( tok.countTokens() == 3 )
					geo.addPoint(new Point3D(Double.parseDouble(tok.nextToken()), Double.parseDouble(tok.nextToken()), Double.parseDouble(tok.nextToken())));

				--numberOfVertices;
			}

			while( numberOfFacets > 0 && (line = i.readLine()) != null )
			{
				tok = new StringTokenizer(line);

				if( tok.countTokens() == 4 )
				{
					tok.nextToken();
					geo.addFacet(new Facet(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken())));
				}

				--numberOfFacets;
			}

			if( geo.getNumberOfPoints() == 0 || geo.getNumberOfFacets() == 0 )
				return null;

			s.setGeometry(geo);
			s.processNormals();
			return s;
		}
		catch( Exception e ) { return null; }
	}

	private static Object3D loadFromOBJ( String fileName )
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			Object3D s = parseOBJ(reader);
			reader.close();
			return s;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return null;
		}
	}

	private static Object3D parseOBJ( BufferedReader reader )
	{
		try
		{
			Object3D s   = new Object3D();
			Geometry geo = s.getGeometry();
			String line;

			while( (line = reader.readLine()) != null )
			{
				line = line.trim();
				if( line.startsWith("#") || line.isEmpty() ) continue;

				StringTokenizer tok = new StringTokenizer(line);
				if( !tok.hasMoreTokens() ) continue;

				String type = tok.nextToken();

				if( type.equals("v") )
				{
					try
					{
						double x = Double.parseDouble(tok.nextToken());
						double y = Double.parseDouble(tok.nextToken());
						double z = Double.parseDouble(tok.nextToken());
						geo.addPoint(new Point3D(x, y, z));
					}
					catch( Exception ex ){}
				}
				else if( type.equals("f") )
				{
					Vector<Integer> indices = new Vector<Integer>();
					boolean faceValid = true;
					int totalPoints = geo.getNumberOfPoints();

					while( tok.hasMoreTokens() )
					{
						String token = tok.nextToken();
						int slashPos = token.indexOf('/');

						if( slashPos != -1 )
							token = token.substring(0, slashPos);

						try
						{
							int idx = Integer.parseInt(token) - 1;
							
							if (idx < 0 || idx >= totalPoints)
							{
								faceValid = false;
								break; 
							}

							indices.add(idx);
						}
						catch (NumberFormatException nfe)
						{
							faceValid = false;
							break;
						}
					}

					if( faceValid && indices.size() >= 3 )
					{
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
