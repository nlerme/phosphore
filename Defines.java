import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


final public class Defines
{
	// General defines
	public static String SoftwareName    = "Phosphore";
	public static String SoftwareVersion = "1.0.0";

	// Drawing defines
	public static float DefaultOutlineWidth          = 1.0f;
	public static Color DefaultOutlineColor          = Color.black;
	public static Color DefaultBackgroundColor       = Color.lightGray;
	public static Color DefaultFaceColor             = Color.blue;
	public static String DefaultShapeName            = "default";
	public static boolean DisplayNormals             = false;
	public static Color DefaultNormalsColor          = Color.blue;
	public static Color DefaultOutlineColorSelection = Color.red;
	public static DrawingMode DefaultDrawingMode     = DrawingMode.WIREFRAME;
	public static Color AABBColorDefault             = Color.green;
	public static Color AABBColorSelection           = Color.red;
	public static enum DrawingMode
	{
		WIREFRAME,
		FILL,
		WIREFRAME_FILL
	}

	// Lighting parameters
	public static Vector3D LightDirection   = Vector3D.normalize(new Vector3D(1.0,1.0,1.0)); 
	public static double LightAmbientFactor = 0.4;
	public static double LightDiffuseFactor = 0.6;

	// Camera parameters
	public static double CameraDefaultDistance = 10.0;
	public static double CameraMinDistance     = 0.1;
	public static double FocusRadiusMultiplier = 3.0;
	public static double DefaultFocusRadius    = 2.0;
	public static double DefaultFovY           = 60.0;
	
	// Controls
	public static int KEY_FOCUS       = KeyEvent.VK_F;
	public static int KEY_TOGGLE_AABB = KeyEvent.VK_B;
	
	// Mouse Control Sensitivity
	public static double MousePanSensitivity = 0.001; 
	public static double MouseZoomFactorFine = 0.2;   
	public static double MouseZoomSpeed      = 1.0;   

	// Projection defines
	public static Projection.Type DefaultProjectionType                = Projection.Type.PERSPECTIVE;
	public static double DefaultDMin                                   = 5.0;
	public static double DefaultDMax                                   = 3.0;
	public static double DefaultCameraDistance                         = 7.0; 
	public static PerspectiveProjection DefaultPerspectiveProjection   = new PerspectiveProjection(DefaultDMin, DefaultDMax, DefaultCameraDistance, DefaultFovY);
	public static OrthographicProjection DefaultOrthographicProjection = new OrthographicProjection(DefaultCameraDistance);
}
