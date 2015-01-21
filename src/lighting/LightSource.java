package lighting;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.util.Arrays;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Dasty
 */
public class LightSource 
{
    private Vector3f absoluteLocation;
    private Vector3f relativeLocation;
    private float[] lightValues;
    private boolean isOn = true;
    
    public LightSource(Vector3f loc, float[] lv)
    {
        absoluteLocation = loc;
        System.out.println(Arrays.toString(lv));
        relativeLocation = new Vector3f(loc.x,loc.y,loc.z);
        lightValues = lv;
    }
    
    public void turnOn()
    {
        isOn = true;
    }
    
    public void turnOff()
    {
        isOn = false;
    }
    
    public void updatePosition(Matrix4f transMatrix)
    {
        Vector4f temp = new Vector4f(absoluteLocation.x, absoluteLocation.y, absoluteLocation.z, 1f);
        Vector4f out = new Vector4f();
        Matrix4f.transform(transMatrix, temp, out);
        relativeLocation.x = out.x;
        relativeLocation.y = out.y;
        relativeLocation.z = out.z;
    }
    
    public float[] getLightValues()
    {
        return lightValues;
    }
    
    public Vector3f getLocation()
    {
        return relativeLocation;
    }
}
