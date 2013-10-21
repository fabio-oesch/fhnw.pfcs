package janfaessler.rollwiederstand;

import javax.media.opengl.*;

import java.awt.*;
import java.awt.event.*;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.*;


public class Rollwiederstand  extends JFrame implements KeyListener, GLEventListener {
	

	public static void main(String[] args) {
		new Rollwiederstand();
	}
	
	public Rollwiederstand() // Konstruktor
	{
		this.setName("Federpendel");
		this.setTitle("Federpendel");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);
		
		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		this.add(canvas);
		this.setVisible(true);
	}

	
	void zeichneAchsen(GL2 gl) {
		gl.glBegin(gl.GL_LINES);
		gl.glVertex2d(-10,   0);
		gl.glVertex2d( 10,   0);
		gl.glVertex2d(  0, -10);
		gl.glVertex2d(  0,  10);
		gl.glEnd();
	}
	
	void zeichneStrecke(GL2 gl, double s) {
		gl.glBegin(gl.GL_LINES);
		gl.glVertex2d(  0, 0);
		gl.glVertex2d(  s, 0);
		gl.glEnd();
	}

	void zeichneKreis(GL2 gl, double r, double x, double y) {
		int nPkte = 40; 
		double dt = 2.0 * Math.PI / nPkte;
		gl.glBegin(gl.GL_POLYGON);
		for (int i = 0; i < nPkte; i++)
			gl.glVertex2d(x + r * Math.cos(i * dt), 
						  y + r * Math.sin(i * dt));
		gl.glEnd();
	}

	// --------- OpenGL-Events -----------------------

	public void init(GLAutoDrawable drawable) {
		GL gl0 = drawable.getGL(); // OpenGL-Objekt
		GL2 gl = gl0.getGL2();
		gl.glClearColor(0.0f, 0.0f, 1.0f, 1.0f); // erasing color
	}

	public void display(GLAutoDrawable drawable) {
		GL gl0 = drawable.getGL();
		GL2 gl = gl0.getGL2();
		gl.glClear(gl.GL_COLOR_BUFFER_BIT); 
		gl.glMatrixMode(gl.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glColor3d(0.5, 0.5, 0.5);
		zeichneAchsen(gl);
		gl.glColor3d(1, 1, 0);
		
		
		gl.glTranslated(0, 4, 0);
		double s = 0.2;
		for (int i = 0;  i < 20; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
		}
		double phi = 0, dphi = 0.1;
		for (int i = 0;  i < 30; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
			phi += dphi;
		}
		for (int i = 0;  i < 30; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
		}
		for (int i = 0;  i < 30; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
			phi -= dphi;
		}
		
		gl.glTranslated(1, 0, 0);
		gl.glRotated(-30, 0, 0, 1);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl0 = drawable.getGL();
		GL2 gl = gl0.getGL2();
		gl.glViewport(0, 0, width, height);
		double aspect = (double) height / width;
		double left = -15, right = 15;
		double bottom = aspect * left, top = aspect * right;
		double near = -100, far = 100;
		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(left, right, bottom, top, near, far);
	}
	
	public void dispose(GLAutoDrawable drawable) {}
	
	@Override public void keyPressed(KeyEvent e) { }
	@Override public void keyTyped(KeyEvent e) { }
	@Override public void keyReleased(KeyEvent e) { }
}
