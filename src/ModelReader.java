import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.math3.*;

public class ModelReader {

	ArrayList<String> rawParts;
	ArrayList<Double> rotationAxis;
	ArrayList<Double> translation;
	String fileName;
	int theta;
	double scale;
	Model model;
	
	public ModelReader(String[] linePieces) {
		rawParts = new ArrayList<String>();
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
	}
	
	public boolean readObjectFile() {
		File file = new File(fileName);
		try {
			Scanner driver = new Scanner(file);
			while(driver.hasNextLine()) {
				String line = driver.nextLine();
				if(line.contains("s off")) {
					break;
				}
				if(!isComment(line)) {
					String[] linePieces = line.split(" ");
					if(!linePieces[0].equals("vn")) {
						Double vector[] = new Double[3];
						vector[0] = Double.parseDouble(linePieces[1]);
						vector[1] = Double.parseDouble(linePieces[2]);
						vector[2] = Double.parseDouble(linePieces[3]);
					}
				}
			}
			driver.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Driver not found.");
			return false;
		}
		return true;
	}
	
	private boolean isComment(String line) {
		if(line.contains("#")) {
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
			   info += translation.toString() + "\n";
		return info;
	}
	
}
