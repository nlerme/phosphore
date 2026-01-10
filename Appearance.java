import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


@SuppressWarnings("deprecation")
public class Appearance
{
	private float m_OutlineWidth;
	private Color m_OutlineColor;
	private Color m_FaceColor;


	public Appearance()
	{
		this(1, Color.black, Defines.DefaultFaceColor);
	}

	public Appearance( float outlineWidth, Color outlineColor, Color faceColor )
	{
		m_OutlineWidth = outlineWidth;
		m_OutlineColor = outlineColor;
		m_FaceColor    = faceColor;
	}

	public Color getOutlineColor()
	{
		return m_OutlineColor;
	}

	public void setOutlineColor( Color outlineColor )
	{
		m_OutlineColor = outlineColor;
	}

	public float getOutlineWidth()
	{
		return m_OutlineWidth;
	}

	public void setOutlineWidth( float outlineWidth )
	{
		m_OutlineWidth = outlineWidth;
	}
	
	public Color getFaceColor()
	{
		return m_FaceColor;
	}

	public void setFaceColor( Color faceColor )
	{
		m_FaceColor = faceColor;
	}
}
