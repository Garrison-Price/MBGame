package lighting;


import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import java.awt.Color;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author Dasty
 */
public class Lighting 
{
    public ArrayList<LightSource> lightSources;
    
    public Lighting()
    {
        lightSources = new ArrayList();
    }
    
    public void updateLightPositions(Matrix4f transMatrix)
    {
        for(int i = 0; i < lightSources.size(); i++)
        {
            lightSources.get(i).updatePosition(transMatrix);
        }
    }
    
    public void createLight(Color color, double intensity, Vector3f location)
    {
        LightSource ls = new LightSource(location, new float[]{color.getRed()/255f*(float)intensity, color.getGreen()/255f*(float)intensity, color.getBlue()/255f*(float)intensity, 1f});
        lightSources.add(ls);
    }
    
    public ArrayList<LightSource> getLights()
    {
        return lightSources;
    }
}
