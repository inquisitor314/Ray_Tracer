import java.util.ArrayList;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Model {

	private ArrayList<Double> rotationAxis;
	private ArrayList<Double> translation;
	private ArrayList<String> writePieces;
	private String fileName;
	private int theta;
	private double scale;
	private RealMatrix matrix;
	private TransformedModelWriter writer;
	private String driverName;
	
	public Model(ArrayList<Double> rA, ArrayList<Double> t, ArrayList<String> wP, ArrayList<Double[]> v, String fN, int th, double s, String dName) {
		rotationAxis = rA;
		translation = t;
		writePieces = wP;
		fileName = fN;
		theta = th;
		scale = s;
		matrix = makeMatrix(v);
		driverName = dName;
		writer = new TransformedModelWriter(writePieces, matrix.getData(), driverName, fileName);
		calculate();
	}
	
	public String toString() {
		String name = "Model: " + fileName + "\n";
		for(double[] v : matrix.getData()) {
			name += "[  ";
			for(double d : v) {
				name += d + "  ";
			}
			name += "]\n";
		}
		return name;
	}

	private void calculate() {
		makeRotationAxisUnitLength();
		writer.write();
	}
	private void makeRotationAxisUnitLength() {
		double magnitude = Math.sqrt(Math.pow(rotationAxis.get(0), 2) + Math.pow(rotationAxis.get(1), 2) + Math.pow(rotationAxis.get(2), 2));
//		System.out.println("Magnitude before normalizing: " + magnitude);
		for(int i = 0; i < 3; i++) {
			rotationAxis.set(i, (rotationAxis.get(i) / magnitude));
		}
//		Test if rotation axis is unit length
//		for(double d : rotationAxis) {
//			System.out.println(d);
//		}
//		double newMag = Math.sqrt(Math.pow(rotationAxis.get(0), 2) + Math.pow(rotationAxis.get(1), 2) + Math.pow(rotationAxis.get(2), 2));
//		System.out.println("Magnitude after normalizing: " + newMag);
	}
	
	private RealMatrix makeMatrix(ArrayList<Double[]> vectorList) {
		double[][] matrixData = new double[vectorList.size()][3];
		for(int i = 0; i < vectorList.size(); i++) {
			for(int j = 0; j < 3; j++) {
				matrixData[i][j] = vectorList.get(i)[j];
			}
		}
		return MatrixUtils.createRealMatrix(matrixData);
	}
}
