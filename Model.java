import java.util.*;


@SuppressWarnings("deprecation")
public class Model extends Observable
{
	private Vector<Shape3D> m_Shapes;
	private int m_CurrentShapeIndex;


	public Model()
	{
		m_Shapes = new Vector<Shape3D>();
	}

	public int getNumberOfShapes()
	{
		return m_Shapes.size();
	}

	public int addShape( Shape3D s )
	{
		m_Shapes.add(s);
		setChanged();
		notifyObservers();
		return getNumberOfShapes() - 1;
	}

	public Shape3D getShape( int index )
	{
		if( isValidIndex(index) )
			return m_Shapes.get(index);

		return null;
	}

	public void setShape( int index, Shape3D s )
	{
		if( isValidIndex(index) )
		{
			m_Shapes.set(index, s);
			setChanged();
			notifyObservers();
		}
	}

	private boolean isValidIndex( int index )
	{
		int size = m_Shapes.size();
		return size > 0 && index >= 0 && index < size;
	}

	public void removeShape( int index )
	{
		if( isValidIndex(index) )
		{
			m_Shapes.remove(index);
			setChanged();
			notifyObservers();
		}
	}

	public void removeAllShapes()
	{
		m_Shapes.clear();
		setChanged();
		notifyObservers();
	}

	public Iterator<Shape3D> getIterator()
	{
		return m_Shapes.listIterator();
	}
}
