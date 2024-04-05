package geom;

public class Vector3D extends Point3D {

    public Vector3D(float x, float y, float z) {
        super(x, y, z);
    }

    public Vector3D() {
        super(0, 0, 0);
    }

    public Vector3D(Point3D point3D) {
        super(point3D.x, point3D.y, point3D.z);
    }

    public Vector3D(float polarAngle, float azimuthAngle) {
        super(polarAngle, azimuthAngle);
    }

    public void scale(float l) {
        this.x *= l;
        this.y *= l;
        this.z *= l;
    }

    public void add(Vector3D v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    public boolean isUnit() {
        return this.x * this.x + this.y * this.y + this.z * this.z - 1 <= Space.ZERO_APPROX;
    }

    public void setVector(Vector3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }
}
