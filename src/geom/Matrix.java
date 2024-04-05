package geom;

import java.util.List;

public class Matrix {
    private int rows;
    private int columns;

    private float[][] matrix;

    public Matrix(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new ArithmeticException("Number of rows and columns must be positive!");
        }
        this.rows = rows;
        this.columns = columns;
        matrix = new float[rows][columns];
        this.fill(0);
    }

    public Matrix(List <List <Float>> list) {
        if (list.isEmpty()) {
            throw new ArithmeticException("Matrix cannot be empty!");
        }
        this.rows = list.size();
        this.columns = list.get(0).size();
        matrix = new float[rows][columns];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.matrix[i][j] = list.get(i).get(j);
            }
        }
    }

    private void fill(float k) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.matrix[i][j] = k;
            }
        }
    }

    public Vector3D multiply(Vector3D vec) {
        if (this.columns != 3 || this.rows != 3) {
            throw new ArithmeticException("Matrix-vector multiplication is not defined for this matrix!");
        }
        return new Vector3D(vec.x * this.matrix[0][0] + vec.y * this.matrix[0][1] + vec.z * this.matrix[0][2]
                , vec.x * this.matrix[1][0] + vec.y * this.matrix[1][1] + vec.z * this.matrix[1][2]
                , vec.x * this.matrix[2][0] + vec.y * this.matrix[2][1] + vec.z * this.matrix[2][2]);
    }

    public Matrix scale(float l) {
        Matrix result = new Matrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                result.matrix[i][j] = l * this.matrix[i][j];
            }
        }
        return result;
    }

    public float det() {
        if (this.rows == 1 && this.columns == 1) {
            return this.matrix[0][0];
        } else if (this.rows == 2 && this.columns == 2) {
            return this.matrix[0][0] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][0];
        } else if (this.rows == 3 && this.columns == 3) {
            float res = 0;
            for (int j = 0; j < 3; ++j) {
                if (j % 2 == 0) {
                    res += this.matrix[0][j] * minor(0, j);
                } else {
                    res -= this.matrix[0][j] * minor(0, j);
                }
            }
            return res;
        } else {
            throw new ArithmeticException("Only for n x n matrix, where 1 <= n <= 3");
        }
    }

    public float minor(int i, int j) {
        Matrix minorMatrix = new Matrix(this.rows-1, this.rows-1);
        int u = 0, v = 0;
        int nPrev = -1;
        for (int n = 0; n < this.rows; ++n) {
            for (int m = 0; m < this.rows; ++m) {
                if (n != i && m != j) {
                    if (nPrev == -1) {
                        nPrev = n;
                    } else if (n == nPrev) {
                        v++;
                    } else {
                        u++;
                        v = 0;
                        nPrev = n;
                    }
                    minorMatrix.matrix[u][v] = this.matrix[n][m];
                }
            }
        }

        return minorMatrix.det();
    }

    public Matrix transpose() {
        Matrix result = new Matrix(this.columns, this.rows);
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.columns; ++j) {
                result.matrix[j][i] = this.matrix[i][j];
            }
        }
        return result;
    }

    private Matrix adj() {
        Matrix result = new Matrix(this.rows, this.rows);
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.rows; ++j) {
                if ((i + j) % 2 == 0) {
                    result.matrix[i][j] = minor(i, j);
                } else {
                    result.matrix[i][j] = -minor(i, j);
                }
            }
        }
        return result;
    }

    public Matrix inverse() {
        float d = this.det();
        if (Math.abs(d) <= 0.001f) {
            throw new ArithmeticException("No inverse matrix!");
        }
        Matrix adj = adj();
        return adj.transpose().scale(1 / d);
    }

    public void display() {
        for (float[] row: this.matrix) {
            for (float elem: row) {
                System.out.print(elem + " ");
            }
            System.out.print("\n");
        }
    }
}
