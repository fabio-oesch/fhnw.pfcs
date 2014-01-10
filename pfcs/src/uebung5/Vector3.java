package uebung5;

public class Vector3 {

	public double x;
	public double y;
	public double z;

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3 clone() {
		return new Vector3(x, y, z);
	}

	public Vector3 norm() {
		double abs = Math.sqrt(x * x + y * y + z * z);
		return new Vector3(x / abs, y / abs, z / abs);
	}

	@Override
	public String toString() {
		return "Vec3[x: " + x + " y: " + y + " z: " + z + "]";
	}
}
