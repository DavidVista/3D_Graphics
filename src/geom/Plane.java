package geom;

public final class Plane {

    private Vector3D normalLine;
    private Point3D point;

    public Plane(Point3D point, Vector3D normalLine) {
        if (!normalLine.isUnit()) {
            throw new ArithmeticException("Normal line should be unit!");
        }
        this.normalLine = normalLine;
        this.point = point;
    }

    public float distanceToPoint(Point3D point) {
        return this.normalLine.x * (point.x - this.point.x) + this.normalLine.y * (point.y - this.point.y)
                + this.normalLine.z * (point.z - this.point.z);
    }

    public Point3D projectPoint(Point3D point) {
        float t = 1 - (this.point.x * point.x + this.point.y * point.y + this.point.z * point.z);
        float xp = point.x + t * this.point.x;
        float yp = point.y + t * this.point.y;
        float zp = point.z + t * this.point.z;
        return new Point3D(xp, yp, zp);
    }

}
