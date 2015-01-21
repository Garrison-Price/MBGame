package mainPack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Model;
import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix4f;
import shaders.Shaders;
import camera.Camera;
import java.awt.Color;
import lighting.Lighting;
import models.OBJLoader;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 *
 * @author Dasty
 */
public class Game 
{
    private boolean fullScreen = false;
    private boolean running = false;
    
    private int fps;
    private long lastFPS;
    private long lastFrame;
    
    private Terrain terrain;
    private Lighting lights;
    private Camera camera;
    private Shaders shaders;
    private ArrayList<Model> models = new ArrayList<Model>();
    
    private Matrix4f transMatrix;
    
    public Game()
    {
        getDelta();
        lastFPS = getTime();
        initGL();
        camera = new Camera(1000, new Vector3f(0f,0f,0f), new Vector3f(0f,0f,0f));
        lights = new Lighting();
        //lights.createLight(Color.WHITE, 1f, new Vector3f(0f,5f,5f));
        lights.createLight(Color.BLUE, .5f, new Vector3f(-.5f,1f,-.5f));
        
        shaders = new Shaders(lights);
        try {
            models.add(OBJLoader.loadModel(new File("C:\\Users\\Dasty\\Documents\\NetBeansProjects\\MBGame\\res\\bunny.obj")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        transMatrix = new Matrix4f();
        transMatrix.translate(camera.startPosition);
        transMatrix.rotate(camera.rotation.x, new Vector3f(1f,0f,0f));
        transMatrix.rotate(camera.rotation.y, new Vector3f(0f,1f,0f));
        transMatrix.rotate(camera.rotation.z, new Vector3f(0f,0f,1f));
        start();
    }
    
    public final void initGL()
    {
        try 
        {
            if (fullScreen) 
            {
                Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            } 
            else 
            {
                Display.setResizable(true);
                Display.setDisplayMode(new DisplayMode(800, 600));
            }
            Display.setTitle("MB Test");
            Display.create();

            glViewport(0, 0, Display.getWidth(), Display.getHeight());
            glLoadIdentity();

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glShadeModel(GL_SMOOTH);              // Enable Smooth Shading
            glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    // Black Background
            glClearDepth(1.0f);                      // Depth Buffer Setup
            glEnable(GL_DEPTH_TEST);              // Enables Depth Testing
            glDepthFunc(GL_LEQUAL);               // The Type Of Depth Testing To Do
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ARRAY_BUFFER_BINDING);
            glEnable(GL_CULL_FACE);
            
            glCullFace(GL_BACK);
            
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            
            glMaterialf(GL_FRONT, GL_SHININESS, 128.0f);
            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
            
            
        } 
        catch (LWJGLException ex) 
        {
            System.err.println("Display initialization failed.");
            System.exit(1);
        }
    }
    
    public final void start()
    {
        if(!running)
            running = true;

        for(Model m : models)
        {
            m.setUp();
        }
        while(running)
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            
            transMatrix = new Matrix4f();
            
            camera.moveToPosition(transMatrix);
            lights.updateLightPositions(transMatrix);
            shaders.Apply();
            
            
            //draw here
            for(int x = 0; x < 8; x++)
            {
                for(int y = 0; y < 8; y++)
                {
                    models.get(0).draw();
                    glTranslatef(0.3f, 0f, 0f);
                }
                glTranslatef(-0.3f*8, 0f, .3f);
            }
            
            
            camera.Update();
            
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                if (!Mouse.isGrabbed() || Display.isFullscreen()) {
                    running = false;
                } else {
                    Mouse.setGrabbed(false);
                }
            }

            updateFPS();
            Display.update();
            if (Display.isCloseRequested()) {
                running = false;
            }
        }
        shaders.Destroy();
    }
    
    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }

    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            System.out.println("FPS: " + fps);
            
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    public static void main(String args[])
    {
        //System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "/natives/");
        new Game();
    }
}
