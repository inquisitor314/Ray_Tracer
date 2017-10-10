import java.util.ArrayList;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class FaceModel {

	private ArrayList<Double> rotationAxis;
	private ArrayList<Double> translation;
	private ArrayList<String> writePieces;
	private String fileName;
	private double theta;
	private double scale;
	private RealMatrix pointsMatrix;
	private TransformedModelWriter writer;
	private String driverName;
	
	public FaceModel(ArrayList<Double> rA, ArrayList<Double> t, ArrayList<String> wP, ArrayList<Double[]> v, String fN, int th, double s, String dName) {
		rotationAxis = rA;
		translation = t;
		writePieces = wP;
		fileName = fN;
		theta = Math.toRadians(th);
		scale = s;
		pointsMatrix = makeMatrix(v);
		driverName = dName;
		transform();
	}
	
	public String toString() {
		String name = "Model: " + fileName + "\n";
		for(double[] v : pointsMatrix.getData()) {
			name += "[  ";
			for(double d : v) {
				name += d + "  ";
			}
			name += "]\n";
		}
		return name;
	}

	private void transform() {
		makeAxisUnitLength(rotationAxis);
		RealMatrix rotationMatrix = makeRotationMatrix();
		RealMatrix scaleMatrix = makeScaleMatrix();
		RealMatrix translationMatrix = makeTranslateMatrix();
		RealMatrix mmMatrix = translationMatrix.multiply(scaleMatrix);
		mmMatrix = mmMatrix.multiply(rotationMatrix);
		pointsMatrix = pointsMatrix.transpose();
		pointsMatrix = mmMatrix.multiply(pointsMatrix);
		pointsMatrix = pointsMatrix.transpose();
		writer = new TransformedModelWriter(writePieces, pointsMatrix.getData(), driverName, fileName);
		writer.write();
	}
	
	private void makeAxisUnitLength(ArrayList<Double> axis) {
		double magnitude = Math.sqrt(Math.pow(axis.get(0), 2) + Math.pow(axis.get(1), 2) + Math.pow(axis.get(2), 2));
		for(int i = 0; i < 3; i++) {
			axis.set(i, (axis.get(i) / magnitude));
		}
	}
	
	private RealMatrix makeMatrix(ArrayList<Double[]> vectorList) {
		double[][] matrixData = new double[vectorList.size()][4];
		for(int i = 0; i < vectorList.size(); i++) {
			for(int j = 0; j < 4; j++) {
				if(j == 3) {
					matrixData[i][j] = 1.0;
				}
				else {
					matrixData[i][j] = vectorList.get(i)[j];
				}
			}
		}
		return MatrixUtils.createRealMatrix(matrixData);
	}
	
	private boolean rotateZAxis() {
		if(!(Math.abs(rotationAxis.get(0)) < .001)) {
			return false;
		}
		if(!(Math.abs(rotationAxis.get(1)) < .001)) {
			return false;
		}
		if(!(Math.abs(rotationAxis.get(2) - 1) < .001)) {
			return false;
		}
		return true;
	}
	
	
	private RealMatrix makeScaleMatrix() {
		double[][] data = {{scale, 0.0, 0.0, 0.0},{0.0, scale, 0.0, 0.0},{0.0, 0.0, scale, 0.0},{0.0, 0.0, 0.0, 1.0}};
		return MatrixUtils.createRealMatrix(data);
	}
	
	private RealMatrix makeTranslateMatrix() {
		double[][] data = {{1.0, 0.0, 0.0, translation.get(0)},{0.0, 1.0, 0.0, translation.get(1)},{0.0, 0.0, 1.0, translation.get(2)},{0.0, 0.0, 0.0, 1.0}};
		return MatrixUtils.createRealMatrix(data);
	}
	
	private RealMatrix makeRotationMatrix() {
		double[][] data = {{Math.cos(theta), -Math.sin(theta), 0, 0},{Math.sin(theta), Math.cos(theta), 0, 0},{0, 0, 1, 0},{0, 0, 0, 1}};
		RealMatrix rzThetaMatrix = MatrixUtils.createRealMatrix(data);
		if(rotateZAxis()) {
			return rzThetaMatrix;
		}
		else {
			ArrayList<Double> mAxis = new ArrayList<Double>();
			for(Double d : rotationAxis) {
				mAxis.add(d);
			}
			int smallestTerm = findSmallestTerm(rotationAxis);
			mAxis.set(smallestTerm, 1.0);
			makeAxisUnitLength(mAxis);
			ArrayList<Double> uAxis = crossProduct(rotationAxis, mAxis);
			makeAxisUnitLength(uAxis);
			ArrayList<Double> vAxis = crossProduct(rotationAxis, uAxis);
			double[][] data1 = {{uAxis.get(0), uAxis.get(1), uAxis.get(2), 0},{vAxis.get(0), vAxis.get(1), vAxis.get(2), 0},{rotationAxis.get(0), rotationAxis.get(1), rotationAxis.get(2), 0},{0, 0, 0, 1}};
			RealMatrix rwMatrix = MatrixUtils.createRealMatrix(data1);
			RealMatrix resultMatrix = rwMatrix.transpose().multiply(rzThetaMatrix);
			return resultMatrix.multiply(rwMatrix);
		}
	}
	
	private int findSmallestTerm(ArrayList<Double> axis) {
		int smallest = 0;
		Double term = axis.get(0);
		for(int i = 1; i < 3; i++) {
			if(Math.abs(axis.get(i)) < term) {
				term = Math.abs(axis.get(i));
				smallest = i;
			}
		}
		return smallest;
	}
	
	private ArrayList<Double> crossProduct(ArrayList<Double> a, ArrayList<Double> b) {
		ArrayList<Double> crossVector = new ArrayList<Double>();
		crossVector.add(a.get(1) * b.get(2) - a.get(2) * b.get(1));
		crossVector.add(a.get(2) * b.get(0) - a.get(0) * b.get(2));
		crossVector.add(a.get(0) * b.get(1) - a.get(1) * b.get(0));
		return crossVector;
	}
}
