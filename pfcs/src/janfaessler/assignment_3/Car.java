package janfaessler.assignment_3;

import java.awt.Color;

import javax.media.opengl.GL2;
import javax.vecmath.Point2d;

public class Car {
	
	private final GL2 graphics;							   
    
	private Color carColor = Color.RED;
	
    private double sizeCar = 10;
    
    private Point2d position;
    private double speed;
    
    private double angleWheel = 0;
    private double angleCar = 0;
    private double angleAlpha = 0;
    private double angleBeta = 0;
        
    private double carLenght;  
    private double carWidth;
    private double axisOffsetX;
    private double axisLenght;
    private double wheelLenght;
    private double wheelWidth;
	
    public Car(GL2 gl, Point2d start, Color color) { this(gl, start, 10, 0, color); }
    public Car(GL2 gl, Point2d start, double size) { this(gl, start, size, 0, Color.BLACK); }
    public Car(GL2 gl, Point2d start, double size, double angle) { this(gl, start, size, angle, Color.BLACK); }
    public Car(GL2 gl, Point2d start, double size, Color color) { this(gl, start, size, 0, color); }
	public Car(GL2 gl, Point2d start, double size, double angle, Color color) {
		graphics = gl;
		position = new Point2d(start.x, start.y);
		sizeCar = size;
		angleCar = angle;
		carColor = color;
		
		// calculate size
		carLenght = size/2;   // the half of the car length for less calculations
		carWidth = carLenght/2;  // the half of the car width for less calculations
		
		// calculate axis
		axisOffsetX = size/3;
		axisLenght = carWidth + carWidth/3; // the half of the axis width for less calculations
		
		// calculate wheels
		wheelLenght = sizeCar/10;
		wheelWidth = wheelLenght/2;
		
		draw();
	}
	
	public void update() {
		
		double dt = DrawUtils.getTimeStep();

		if (angleWheel == 0) {
			
			// drive straight on
			position.x += speed * Math.cos(Math.toRadians(angleCar)) * dt;
			position.y += speed * Math.sin(Math.toRadians(angleCar)) * dt;
			
		} else {
			
			double d = 2 * axisOffsetX;  // Abstand Achsen
			double b = axisLenght;       // Halbe Breite
			double r;                    // Radius
			
			if (angleWheel > 0) {
				
				// turn left
				r = b + d / Math.tan(Math.toRadians(angleWheel));
				angleAlpha = angleWheel;
				angleBeta  = Math.toDegrees(Math.tan(d / (r - b)));
				
			} else {
				
				// turn right
				r = -b + d / Math.tan(Math.toRadians(angleWheel));
				angleAlpha = Math.toDegrees(Math.tan(d / (r - b)));
				angleBeta = angleWheel;
				
			}
			
			double deltaPhi = speed * dt / r;
			
			position.x += r * (Math.sin(Math.toRadians(angleCar) + deltaPhi) - Math.sin(Math.toRadians(angleCar)));
			position.y -= r * (Math.cos(Math.toRadians(angleCar) + deltaPhi) - Math.cos(Math.toRadians(angleCar)));
			
			angleCar += Math.toDegrees(deltaPhi);
		}

		draw();
	}
	
	public final Color getColor() { return carColor; }
	public final void setColor(Color color) { this.carColor = color; } 
	
	public final Point2d getPosition() { return position; }
	public final void setPosition(Point2d location) { position = location; }

	public final double getWheelAngle() { return angleWheel; }
	public final void setWheelAngle(double angle) { this.angleWheel = angle; }
	
	public final double getAngle() { return angleCar; }
	public final void setAngle(double angle) { this.angleCar = angle; }
	
	public final double getSpeed() { return speed; }
	public final void setSpeed(double speed) { this.speed = speed; }

	private void draw() {
		graphics.glPushMatrix();
		graphics.glColor3d(carColor.getRed()/255, carColor.getGreen()/255, carColor.getBlue()/255);
		
		// translate and rotate
		graphics.glTranslated(position.x, position.y, 0);
		graphics.glRotated(angleCar, 0, 0, 1);
		
		// draw body
		DrawUtils.drawRect(graphics, new Point2d(-carLenght, carWidth), new Point2d(carLenght, -carWidth), true);
		
		// draw axis
		DrawUtils.drawLine(graphics, new Point2d(-axisOffsetX, axisLenght), new Point2d(-axisOffsetX, -axisLenght));
		DrawUtils.drawLine(graphics, new Point2d(axisOffsetX, axisLenght),  new Point2d(axisOffsetX, -axisLenght));
		
		// draw weels
		drawWeel(new Point2d(-axisOffsetX,  axisLenght+wheelWidth),           0);
		drawWeel(new Point2d(-axisOffsetX, -axisLenght-wheelWidth),           0);
		drawWeel(new Point2d(axisOffsetX,   axisLenght+wheelWidth),   angleAlpha);
		drawWeel(new Point2d(axisOffsetX,  -axisLenght-wheelWidth),   angleBeta);
		
		graphics.glPopMatrix();
	}
	
	private void drawWeel(Point2d pos, double angle) {
		graphics.glPushMatrix();
		
		// translate and rotate
		graphics.glTranslated(pos.x, pos.y, 0);
		graphics.glRotated(angle, 0, 0, 1);
		
		// draw weel
		DrawUtils.drawRect(graphics, new Point2d(-wheelLenght, wheelWidth), new Point2d(wheelLenght, -wheelWidth), true);
		
		graphics.glPopMatrix();
	}

}
