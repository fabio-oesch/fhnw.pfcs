package janfaessler.assignment_3;

import javax.media.opengl.GL2;
import javax.vecmath.Point2d;

public class Car {
	
	private final GL2 gl;							   
    
    private double size = 10;
    private double weelSize = size/10;
    
    private Point2d position;
    private double angle = 0;
    private double weelAngle = 0;
    private double speed = 0;
    private double rad = 0;
    
    private double w;
    private double h;
    private double ax;
    private double ay;
    private double ww;
    private double wh;
	
	public Car(GL2 gl, Point2d start, double size, double angle) {
		this.gl = gl;
		this.position = new Point2d(start.x, start.y);
		this.size = size;
		this.weelSize = size/10;
		this.angle = angle;
		
		// calculate size
		this.w = size/2;
		this.h = size/2/2;
		
		// calculate axis
		this.ax = 2*w/3;
		this.ay = h+h/3;
		
		// calculate weels
		this.ww = weelSize;
		this.wh = weelSize/2;
		
		draw();
	}
	
	public void update() {
		if (speed > 0) {
			double dt = DrawUtils.getTimeStep();
			Point2d v = new Point2d(0,0);
			
			double T = 2 * Math.PI * rad / speed;
			double w = 2 * Math.PI / T;
			
			if (!equal(0, rad)) {
				v.x = -w *  rad * Math.sin(Math.toRadians(w * dt));
				v.y = w * rad * Math.cos(Math.toRadians(w * dt));
			} else {
				v.x = speed;
				v.y = 0;
			}
			
			position.x += v.x * dt;
			position.y += v.y * dt;
			
			System.out.println("rad: "+rad + "\t w: " + w + "\t T: "+T+"\t speed: "+speed+"\t v.x: "+v.x+"\t v.y: "+v.y+"\t pos.x: "+position.x+"\t pos.y: "+position.y);
		}
		draw();
	}
	private static boolean equal(double a, double b) {
		return Math.abs(a - b) < 0.000000001;
	}
	
	public final double getWeelSize() { return weelSize; }
	public final void setWeelSize(double weelSize) { this.weelSize = weelSize; }

	public final double getRad() { return rad; }
	public final void setRad(double rad) { this.rad = rad; }
	
	public final double getSpeed() { return speed; }
	public final void setSpeed(double speed) { this.speed = speed; }

	private void draw() {
		gl.glPushMatrix();
		
		// translate and rotate
		gl.glTranslated(position.x, position.y, 0);
		gl.glRotated(angle, 0, 0, 1);
		
		// draw body
		DrawUtils.drawRect(gl, new Point2d(-w, h), new Point2d(w, -h));
		
		// draw axis
		DrawUtils.drawLine(gl, new Point2d(-ax, ay), new Point2d(-ax, -ay));
		DrawUtils.drawLine(gl, new Point2d(ax, ay),  new Point2d(ax, -ay));
		
		// draw weels
		drawWeel(new Point2d(-ax,  ay+weelSize/2),           0);
		drawWeel(new Point2d(-ax, -ay-weelSize/2),           0);
		drawWeel(new Point2d(ax,   ay+weelSize/2),   weelAngle);
		drawWeel(new Point2d(ax,  -ay-weelSize/2),   weelAngle);
		
		gl.glPopMatrix();
	}
	
	private void drawWeel(Point2d pos, double angle) {
		gl.glPushMatrix();
		
		// translate and rotate
		gl.glTranslated(pos.x, pos.y, 0);
		gl.glRotated(angle, 0, 0, 1);
		
		// draw weel
		DrawUtils.drawRect(gl, new Point2d(-ww, wh), new Point2d(ww, -wh));
		
		gl.glPopMatrix();
	}

}
