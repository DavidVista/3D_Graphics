package geom;

import java.lang.Math;

public class Point3D {
    public float x;
    public float y;
    public float z;

    public static int MAX_X;
    public static int MAX_Y;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(float polarAngle, float azimuthAngle) {
        this((float) (Math.sin(azimuthAngle) * Math.cos(polarAngle)), (float) (Math.sin(azimuthAngle) * Math.sin(polarAngle)), (float) Math.cos(azimuthAngle));
    }

    public float distance(Point3D v) {
        return (float) Math.sqrt((this.x - v.x) * (this.x - v.x) + (this.y - v.y) * (this.y - v.y) + (this.z - v.z) * (this.z - v.z));
    }

    public int getX() {
        return Math.abs(this.x) <= MAX_X ? (int) Math.floor(this.x) : ((this.x >= 0) ? 1 : -1) * MAX_X;
    }

    public int getY() {
        return Math.abs(this.y) <= MAX_Y ? (int) Math.floor(this.y) : ((this.y >= 0) ? 1 : -1) * MAX_Y;
    }

}


