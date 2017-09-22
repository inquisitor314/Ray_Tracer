import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DriverReader {

	String driverName;
	ArrayList<ModelReader> models;
	
	public DriverReader(String driverName) {
		this.driverName = driverName;
		models = new ArrayList<ModelReader>();
	}
	
	public boolean readFile() {
		int numObjects = 0;
		File file = new File(driverName);
		try {
			Scanner driver = new Scanner(file);
			while(driver.hasNextLine()) {
				String line = driver.nextLine();
				if(!isComment(line)) {
					String[] linePieces = line.split(" ");
					if(validModelInfo(linePieces, numObjects)) {
						models.add(new ModelReader(linePieces));
					}
				}
				numObjects++;
			}
			driver.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Driver not found.");
			return false;
		}
		return true;
	}
	
	private boolean validModelInfo(String[] linePieces, int numObjects) {
		if(linePieces.length != 10) {
			System.out.println("ERROR on line " + numObjects +": object definition isn't the correct length.");
			return false;
		}
		if(!linePieces[0].equals("model")) {
			System.out.println("ERROR on line " + numObjects +": model isn't first word of object definition.");
			return false;
		}
		for(int i = 1; i < 4; i++) {
			if(!isDouble(linePieces[i])) {
				System.out.println("ERROR on line " + numObjects +": rotation axis coordinate " + i + " isn't a double.");
				return false;
			}
		}
		if(!isInteger(linePieces[4])) {
			System.out.println("ERROR on line " + numObjects +": theta isn't an integer.");
			return false;
		}
		if(!isDouble(linePieces[5])) {
			System.out.println("ERROR on line " + numObjects +": scaling factor isn't a double.");
			return false;
		}
		for(int j = 6; j < 9; j++) {
			if(!isDouble(linePieces[j])) {
				System.out.println("ERROR on line " + numObjects +": M2W translation axis coordinate " + (j-5) + " isn't a double.");
				return false;
			}		
		}
		if(!linePieces[9].contains(".obj")) {
			System.out.println("ERROR on line " + numObjects +": .obj file is not indicated by object definition.");
			return false;
		}
		return true;
	}
	
	private boolean isDouble(String piece) {
		try {
			double tempNum = Double.parseDouble(piece);
			tempNum = tempNum++;
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isInteger(String piece) {
		try {
			int tempNum = Integer.parseInt(piece);
			tempNum = tempNum++;
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isComment(String line) {
		if(line.contains("#")) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		String modelInfo = "Models from " + driverName + "\n";
		for(ModelReader m : models) {
			modelInfo += m.toString();
		}
		return modelInfo;
	}
}