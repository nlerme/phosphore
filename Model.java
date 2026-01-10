import java.util.*;


@SuppressWarnings("deprecation")
public class Model extends Observable
{
	private Vector<Object3D> m_Objects;
	private int m_CurrentObjectIndex;


	public Model()
	{
		m_Objects = new Vector<Object3D>();
	}

	public int getNumberOfObjects()
	{
		return m_Objects.size();
	}

	public int addObject( Object3D s )
	{
		m_Objects.add(s);
		setChanged();
		notifyObservers();
		return getNumberOfObjects() - 1;
	}

	public Object3D getObject( int index )
	{
		if( isValidIndex(index) )
			return m_Objects.get(index);

		return null;
	}

	public void setObject( int index, Object3D s )
	{
		if( isValidIndex(index) )
		{
			m_Objects.set(index, s);
			setChanged();
			notifyObservers();
		}
	}

	private boolean isValidIndex( int index )
	{
		int size = m_Objects.size();
		return size > 0 && index >= 0 && index < size;
	}

	public void removeObject( int index )
	{
		if( isValidIndex(index) )
		{
			m_Objects.remove(index);
			setChanged();
			notifyObservers();
		}
	}

	public void removeAllObjects()
	{
		m_Objects.clear();
		setChanged();
		notifyObservers();
	}

	public Iterator<Object3D> getIterator()
	{
		return m_Objects.listIterator();
	}
}
