package shaders;

import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import lighting.Lighting;
import org.lwjgl.util.vector.Vector3f;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Arrays;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author Dasty
 */
public class Shaders 
{
    private int shaderProgram;
    private int vertexShader;
    private int fragmentShader;
    private int lightPositionsUniform;
    private int lightValuesUniform;
    private int numLightsUniform;
    StringBuilder vertexShaderSource;
    StringBuilder fragmentShaderSource;
    
    private File directory = new File(".");
    private String workingDir;
    
    private Lighting lights;
    
    public Shaders(Lighting l)
    {
        lights = l;
        
        workingDir = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().indexOf("\\lib"));
        
        shaderProgram = glCreateProgram();
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        
        vertexShaderSource = new StringBuilder();
        fragmentShaderSource = new StringBuilder();
        
        try  
        {
            BufferedReader reader = new BufferedReader(new FileReader(workingDir+"\\src\\shaders\\shader.vert"));
            String line;
            while((line = reader.readLine()) != null)
            {
                vertexShaderSource.append(line);
            }
            reader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(workingDir+"\\src\\shaders\\shader.frag"));
            String line;
            while((line = reader.readLine()) != null)
            {
                fragmentShaderSource.append(line);
            }
            reader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        
        if(glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
            System.out.println("Bad Vertex Shader"+glGetShaderInfoLog(vertexShader, 65536));
        
        
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        System.out.println(glGetShaderInfoLog(fragmentShader, 65536));
        if(glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)
            System.out.println("Bad Fragment Shader"+glGetShaderInfoLog(fragmentShader, 65536));
        

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);
        
        lightPositionsUniform = glGetUniformLocation(shaderProgram, "lightPositions");
        lightValuesUniform = glGetUniformLocation(shaderProgram, "lightValues");
        numLightsUniform = glGetUniformLocation(shaderProgram, "numLights");
    }
    
    public void Apply()
    {
        glUseProgram(shaderProgram);
        glUniform1i(numLightsUniform,lights.getLights().size());
        float[] lightPositions = new float[lights.getLights().size()*3];
        for(int i = 0; i < lights.getLights().size(); i++)
        {
            lightPositions[i*3] = lights.getLights().get(i).getLocation().x;
            lightPositions[(i*3)+1] = lights.getLights().get(i).getLocation().y;
            lightPositions[(i*3)+2] = lights.getLights().get(i).getLocation().z;
        }
        //System.out.println(Arrays.toString(lightPositions));
        glUniform3(lightPositionsUniform, asFloatBuffer(lightPositions));
        
        float[] lightValues = new float[lights.getLights().size()*3];
        for(int i = 0; i < lights.getLights().size(); i++)
        {
            lightValues[i*3] = lights.getLights().get(i).getLightValues()[0];
            lightValues[(i*3)+1] = lights.getLights().get(i).getLightValues()[1];
            lightValues[(i*3)+2] = lights.getLights().get(i).getLightValues()[2];
        }
        glUniform3(lightValuesUniform, asFloatBuffer(lightValues));
    }
    
    public void Destroy()
    {
        glDeleteProgram(shaderProgram);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }
    
    private FloatBuffer asFloatBuffer(float[] values)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
        buffer.put(values);
        buffer.flip();
        return buffer;
    }
}
