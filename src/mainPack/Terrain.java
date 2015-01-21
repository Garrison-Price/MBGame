/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mainPack;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author Dasty
 */
public class Terrain 
{
    private File directory = new File(".");
    private String workingDir;
    private BufferedImage heightMapTexture;
    private int width, height;
    private double[][] heightMap;
    
    public Terrain()
    {
        workingDir = directory.getAbsolutePath().substring(0, directory.getAbsolutePath().indexOf("\\lib"));
        
        try
        {
            heightMapTexture = ImageIO.read(new File(workingDir+"\\res\\grayscale.jpg"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        width = heightMapTexture.getWidth();
        height = heightMapTexture.getHeight();
        
        heightMap = new double[width][height];
        
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                heightMap[x][y] = heightMapTexture.getRGB(x, y)&0xff;
            }
        }
    }
    
    public void draw()
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(x == width - 1 && y == height - 1)
                {
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y);
                    glEnd();
                    
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y+1);
                    glEnd();
                    
                    glBegin(GL_LINES);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y+1);
                    glEnd();
                    
                    glBegin(GL_LINES);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y+1);
                    glEnd();
                }
                else if(x == width - 1)
                {
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y);
                    glEnd();
                    
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x,(float)heightMap[x][y+1],(float)y+1);
                    glEnd();
                    
                    glBegin(GL_LINES);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y+1],(float)y+1);
                    glEnd();
                }
                else if(y == height - 1)
                {
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y);
                    glEnd();
                    
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y+1);
                    glEnd();
                    
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y+1);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y+1);
                    glEnd();
                }
                else
                {
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x+1][y],(float)y);
                    glEnd();
                    
                    glBegin(GL_LINES);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x,(float)heightMap[x][y+1],(float)y+1);
                    glEnd();
                }
            }
        }
    }
    
    public void drawRect()
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(y%2 == 0 || x%2 == 0)
                    glColor3f(1f,1f,1f);
                else
                    glColor3f(0f,0f,0f);
                if(x == width - 1 && y == height - 1)
                {
                    glBegin(GL_QUADS);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y+1);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y+1);
                    glEnd();
                }
                else if(x == width - 1)
                {
                    glBegin(GL_QUADS);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x][y+1],(float)y+1);
                        glVertex3f((float)x,(float)heightMap[x][y+1],(float)y+1);
                    glEnd();
                }
                else if(y == height - 1)
                {
                    glBegin(GL_QUADS);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x+1][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x+1][y],(float)y+1);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y+1);
                    glEnd();
                }
                else
                {
                    glBegin(GL_QUADS);
                        glVertex3f((float)x,(float)heightMap[x][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x+1][y],(float)y);
                        glVertex3f((float)x+1,(float)heightMap[x+1][y+1],(float)y+1);
                        glVertex3f((float)x,(float)heightMap[x][y+1],(float)y+1);
                    glEnd();
                }
            }
        }
    }
}
