package uebung5;

public class Vector2 {
	public double x;
	public double y;

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Vector2 clone() {
		return new Vector2(x, y);
	}

	public Vector2 norm() {
		double abs = Math.sqrt(x * x + y * y);
		return new Vector2(x / abs, y / abs);
	}

	@Override
	public String toString() {
		return "Vec3[x: " + x + " y: " + y + "]";
	}
}
