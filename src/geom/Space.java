package geom;

import java.util.Arrays;

public class Space {
    public static final float ZERO_APPROX = 0.001f;
    private float polarAngle = 0f;
    private float azimuthAngle = 0f;
    private Vector3D directionVector = new Vector3D(1, 1, 1);
    private Plane displayPlane;
    private Vector3D e1;
    private Vector3D e2;
    private Matrix transitionMatrix;
    private Matrix inverseTransition;
    private Vector3D shift;

    public void movePolar(float diff) {
        if (this.polarAngle + diff < -Math.PI / 2) {
            this.polarAngle = (float) -Math.PI / 2;
        } else if (this.polarAngle + diff > Math.PI / 2) {
            this.polarAngle = (float) Math.PI / 2;
        } else {
            this.polarAngle += diff;
        }
        setDirection(new Vector3D(this.polarAngle, this.azimuthAngle));
    }

    public void moveAzimuth(float diff) {
        if (this.azimuthAngle + diff < -Math.PI) {
            this.azimuthAngle = (float) Math.PI;
        } else if (this.azimuthAngle + diff > Math.PI) {
            this.azimuthAngle = (float) -Math.PI;
        } else {
            this.azimuthAngle += diff;
        }
        setDirection(new Vector3D(this.polarAngle, this.azimuthAngle));
    }

    public Vector3D getDirection() {
        return this.directionVector;
    }

    public void setDirection(Vector3D vec) {
        this.directionVector.setVector(vec);
        this.displayPlane = new Plane(directionVector, directionVector);
        this.e1 = xAxisVec(this.directionVector);
        this.e2 = yAxisVec(this.directionVector);
        this.transitionMatrix = new Matrix(
                Arrays.asList(
                        Arrays.asList(this.e1.x, this.e2.x, -this.directionVector.x),
                        Arrays.asList(this.e1.y, this.e2.y, -this.directionVector.y),
                        Arrays.asList(this.e1.z, this.e2.z, -this.directionVector.z)
                )
        );
        this.inverseTransition = this.transitionMatrix.inverse();
        this.shift = new Vector3D(-this.directionVector.x, -this.directionVector.y, -this.directionVector.z);
    }

    public Space(float polarAngle, float azimuthAngle) {
        this.polarAngle = polarAngle;
        this.azimuthAngle = azimuthAngle;
        setDirection(new Vector3D(polarAngle, azimuthAngle));
    }

    public Space(float x, float y, float z) {
        this.azimuthAngle = (float) Math.acos(z);
        this.polarAngle = (float) Math.atan2(y, x);
        setDirection(new Vector3D(this.polarAngle, this.azimuthAngle));
    }

    public Plane getDisplayPlane() {
        return this.displayPlane;
    }

    private Vector3D xAxisVec(Vector3D d) {
        if (d.y == 0) {
            if (d.x == 0) {
                return new Vector3D(1, 0, d.z);
            } else {
                float temp = (1 - d.x * d.x - d.z * d.z) / d.x;
                return new Vector3D((1 - d.z * d.z) / d.x, -(float) Math.sqrt(1 - temp * temp), d.z);
            }
        } else if (d.y > 0) {
            if (d.x == 0) {
                float temp = (1 - d.y * d.y - d.z * d.z) / d.y;
                return new Vector3D((float) Math.sqrt(1 - temp * temp), (1 - d.z * d.z) / d.y, d.z);
            } else {
                float k = d.y - (1 - d.z * d.z) / d.y;
                float t = (float) Math.sqrt(1 / (d.x * d.x + k * k));
                return new Vector3D(d.x + d.x * t, d.y + k * t, d.z);
            }
        } else {
            if (d.x == 0) {
                float temp = (1 - d.y * d.y - d.z * d.z) / d.y;
                return new Vector3D(-(float) Math.sqrt(1 - temp * temp), (1 - d.z * d.z) / d.y, d.z);
            } else {
                float k = d.y - (1 - d.z * d.z) / d.y;
                float t = -(float) Math.sqrt(1 / (d.x * d.x + k * k));
                return new Vector3D(d.x + d.x * t, d.y + k * t, d.z);
            }
        }
    }

    private Vector3D yAxisVec(Vector3D d) {
        if (d.z == 0) {
            return new Vector3D(d.x, d.y, 1);
        } else {
            if (d.x == 0 && d.y == 0) {
                return new Vector3D(0, -1, d.z);
            } else {
                float h = d.z - 1 / d.z;
                float temp = (float) Math.sqrt(1 / (d.x * d.x + d.y * d.y + h * h));
                float t = (d.z > 0) ? temp : -temp;
                return new Vector3D(d.x * (1 - t), d.y * (1 - t), d.z - h * t);
            }
        }
    }

    public float scaleCoefficient(float distance) {
        if (distance <= 1) {
            return 0;
        }
        return 10 / distance;
    }

    public Vector3D pointProjection(Point3D originalPoint) {
        Point3D projectPoint = displayPlane.projectPoint(originalPoint);
        Vector3D p = new Vector3D(projectPoint);
        p.add(shift);
        return inverseTransition.multiply(p);
    }

}
