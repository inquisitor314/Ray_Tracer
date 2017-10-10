import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ModelReader {

	private ArrayList<String> rawParts;
	private ArrayList<Double> rotationAxis;
	private ArrayList<Double> translation;
	private ArrayList<String> writePieces;
	private ArrayList<Double[]> vectors;
	private String fileName;
	private String driverName;
	private int theta;
	private int numVectors;
	private double scale;
	private FaceModel model;
	private String comment;
	
	public ModelReader(String[] linePieces, String dName) {
		driverName = dName;
		writePieces = new ArrayList<String>();
		rawParts = new ArrayList<String>();
		vectors = new ArrayList<Double[]>();
		numVectors = 0;
		for(String s : linePieces) {
			rawParts.add(s);
		}
		fileName = rawParts.get(9);
		theta = Integer.parseInt(rawParts.get(4));
		scale = Double.parseDouble(rawParts.get(5));
		rotationAxis = new ArrayList<Double>();
		rotationAxis.add(Double.parseDouble(rawParts.get(1)));
		rotationAxis.add(Double.parseDouble(rawParts.get(2)));
		rotationAxis.add(Double.parseDouble(rawParts.get(3)));
		translation = new ArrayList<Double>();
		translation.add(Double.parseDouble(rawParts.get(6)));
		translation.add(Double.parseDouble(rawParts.get(7)));
		translation.add(Double.parseDouble(rawParts.get(8)));
		comment = "# This is a modified version of " + fileName;
	}
	
	public boolean readObjectFile() {
		File file = new File(fileName);
		try {
			Scanner driver = new Scanner(file);
			while(driver.hasNextLine()) {
				String line = driver.nextLine();
				if(!isGarbage(line)) {
					if(isComment(line)) {
						writePieces.add(line);
					}
					else if(isFace(line)) {
						
					}
					else {
						writePieces.add("" + numVectors);
						numVectors++;
						String[] linePieces = line.split(" ");
						Double vector[] = new Double[3];
						vector[0] = Double.parseDouble(linePieces[1]);
						vector[1] = Double.parseDouble(linePieces[2]);
						vector[2] = Double.parseDouble(linePieces[3]);
						vectors.add(vector);
					}
				}
				else {
					//Make sure you're throwing out only bad lines
					//System.out.println("Throwing out line " + line);
				}
			}
			driver.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Driver not found.");
			return false;
		}
		addComment();
		makeModel();
		return true;
	}
	
	private void makeModel() {
		model = new FaceModel(rotationAxis, translation, writePieces, vectors, fileName, theta, scale, driverName);
	}
	
	public FaceModel getModel() {
		return model;
	}
	
	private boolean isFace(String line){
		if(!isGarbage(line) && line.charAt(0) == 'f'){
			return true;
		}
		return false;
	}
	
	private boolean isVector(String line) {
		if(!isGarbage(line) && line.charAt(0) == 'v') {
			return true;
		}
		return false;
	}
	
	private boolean isGarbage(String line) {
		if(line.contains("s off")) {
			return true;
		}
		if(line.charAt(0) == 'v' && line.charAt(1) == 'n') {
			return true;
		}
		return false;
	}
	
	public String toString() {

		String info = "Name: ";
			   info += fileName + "\n";
			   info += "Theta: ";
			   info += theta + "\n";
			   info += "Scale: ";
			   info += scale + "\n";
			   info += "Rotation Axis: ";
			   info += rotationAxis.toString() + "\n";
			   info += "Translation: ";
			   info += translation.toString() + "\n\n";
		return info;
	}
	
	public void printWritePieces() {
		for(String s : writePieces) {
			System.out.println(s);
		}
	}
	
	public void printModel() {
		System.out.println(model.toString());
	}
	
	private void addComment() {
		int lastComment = 0;
		for(String s : writePieces) {
			if(s.charAt(0) == '#') {
				lastComment++;
			}
			else {
				break;
			}
		}
		writePieces.add(lastComment, comment);
	}
	
	private boolean isComment(String line) {
		if(line.charAt(0) == '#') {
			return true;
		}
		return false;
	}
}
