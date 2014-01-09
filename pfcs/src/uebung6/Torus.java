package uebung6;

import java.awt.Color;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Torus extends AbstractBullet {

	private static final double M = 1;
	private final double rBig, rLittle;

	public Torus(Vec3 pos, Vec3 angleSpeed, double v0, double a0, Vec2 size) {
		super(pos, angleSpeed, v0, a0);
		rBig = size.x;
		rLittle = size.y;

		super.color = Color.BLUE;
		super.setDullness(this.getDullness());
	}

	@Override
	public void draw(GL2 gl) {
		super.prepareDraw(gl);
		(new GLUT()).glutSolidTorus(rBig, rLittle, 16, 16);
		super.finishDraw(gl);
	}

	private double[] getDullness() {
		double I1 = M * rBig * rBig + 5.0 / 8.0 * M * rLittle * rLittle;
		double I2 = I1;
		double I3 = M * rBig * rBig + 3.0 / 4.0 * M * rLittle * rLittle;
		double[] res = { I1, I2, I3 };
		return res;
	}

}
