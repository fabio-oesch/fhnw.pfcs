package janfaessler.assignment_3;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

public class RacingGame extends JFrame implements GLEventListener, KeyListener {

	private static final long serialVersionUID = -8790324402153794190L;

	private final int viewportWidth = 50;
	
	private final double speedSteps = 1.0;
	private final double angleSteps = 1.5;
	
	private Course course;
	private Car car;
	
	private boolean running = false;
	private double speed;

	public static void main(String[] args) { new RacingGame(); }
	
	public RacingGame() {
		this.setName("RacingGame");
		this.setTitle("RacingGame");
        this.setSize(800, 600);  
        this.addKeyListener(this); 
        
        GLCanvas canvas = new GLCanvas(); 
        canvas.addGLEventListener(this); 
        canvas.addKeyListener(this); 
        this.add(canvas); 
        FPSAnimator anim = new FPSAnimator(canvas, 60, true); 
        anim.start(); 
 
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true); 
	}

    @Override
    public void init(GLAutoDrawable drawable) { 
        GL gl0 = drawable.getGL(); 
        GL2 gl = gl0.getGL2(); 
        gl.glClearColor(0.25f, 0.25f, 1.0f, 1.0f);
        
        course = new RoundCourse(gl);
        car = new Car(gl, course.getStartPosition(), 2.5, Color.RED);
    } 

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl0 = drawable.getGL(); 
        GL2 gl = gl0.getGL2(); 
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT); 
        gl.glMatrixMode(GL2.GL_MODELVIEW); 
        gl.glColor3d(0, 0, 0);

        course.draw();
        car.update();
	}


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        double aspect = (double) height / width;
        double left = -viewportWidth;
        double right = viewportWidth;
        double bottom = left * aspect;
        double top = right * aspect;
        double near = -100, far = 100;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(left, right, bottom, top, near, far);
    }
    
    @Override
	public void dispose(GLAutoDrawable arg0) { }
	
	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (car.getWheelAngle() < 25)
				car.setWheelAngle(car.getWheelAngle() + angleSteps);
			System.out.println("angle: "+car.getWheelAngle());
			break;
		case KeyEvent.VK_RIGHT:
			if (car.getWheelAngle() > -25)
				car.setWheelAngle(car.getWheelAngle() - angleSteps);
			System.out.println("angle: "+ car.getWheelAngle());
			break;
		case KeyEvent.VK_UP:
			if (car.getSpeed() < 20)
				car.setSpeed(car.getSpeed() + speedSteps);
			System.out.println("speed: "+car.getSpeed());
			break;
		case KeyEvent.VK_DOWN:
			if (car.getSpeed() > -20)
				car.setSpeed(car.getSpeed() - speedSteps);
			System.out.println("speed: "+car.getSpeed());
			break;
		case KeyEvent.VK_S:
			if (running) {
				running = false;
				speed = car.getSpeed();
				car.setSpeed(0);
			} else {
				running = true;
				car.setSpeed(speed);
			}
			break;
		case KeyEvent.VK_R: 
			car.setSpeed(0);
			car.setAngle(0);
			car.setWheelAngle(0);
			car.setPosition(course.getStartPosition());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { }

}