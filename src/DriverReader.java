import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DriverReader {

	String driverName;
	ArrayList<ModelReader> models;
	Camera camera;
	
	public DriverReader(String driverName) {
		this.driverName = driverName;
		models = new ArrayList<ModelReader>();
		camera = new Camera();
	}
	
	public boolean readFile() {
		int numObjects = 0;
		File file = new File(driverName);
		try {
			Scanner driver = new Scanner(file);
			while(driver.hasNextLine()) {
				String line = driver.nextLine();
				if(!isComment(line)) {
					if(isEye(line)) {
						if(!camera.setEye(line.substring(4))) {
							//System.out.println("Eye: " + line.substring(4));
							System.out.println("ERROR: Invalid eye definition.");
							driver.close();
							return false;
						}
					}
					else if(isLook(line)) {
						if(!camera.setLook(line.substring(5))) {
							//System.out.println("Look: " + line.substring(5));
							System.out.println("ERROR: Invalid look vector definition.");
							driver.close();
							return false;
						}
					}
					else if(isUp(line)) {
						if(!camera.setUp(line.substring(3))) {
							//System.out.println("Up Vector: " + line.substring(3));
							System.out.println("ERROR: Invalid up vector definition.");
							driver.close();
							return false;
						}
					}
					else if(isD(line)) {
						if(!camera.setFLength(line.substring(2))) {
							//System.out.println("Focal Length: " + line.substring(2));
							System.out.println("ERROR: Invalid focal length definition.");
							driver.close();
							return false;
						}
					}
					else if(isBounds(line)) {
						if(!camera.setBounds(line.substring(7))) {
							//System.out.println("Bounds: " + line.substring(7));
							System.out.println("ERROR: Invalid bounds definition.");
							driver.close();
							return false;
						}
					}
					else if(isResolution(line)) {
						if(!camera.setResolution(line.substring(4))) {
							//System.out.println("Resolution: " + line.substring(4));
							System.out.println("ERROR: Invalid resolution definition.");
							driver.close();
							return false;
						}
					}
					else {
						String[] linePieces = line.split(" ");
						if(validModelInfo(linePieces, numObjects)) {
							models.add(new ModelReader(linePieces, driverName));
							numObjects++;
						}
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
	
	private boolean isEye(String line) {
		if(line.charAt(0) == 'e') {
			return true;
		}
		return false;
	}
	
	private boolean isLook(String line) {
		if(line.charAt(0) == 'l') {
			return true;
		}
		return false;
	}
	
	private boolean isUp(String line) {
		if(line.charAt(0) == 'u') {
			return true;
		}
		return false;
	}
	
	private boolean isD(String line) {
		if(line.charAt(0) == 'd') {
			return true;
		}
		return false;
	}
	
	private boolean isBounds(String line) {
		if(line.charAt(0) == 'b') {
			return true;
		}
		return false;
	}
	
	private boolean isResolution(String line) {
		if(line.charAt(0) == 'r') {
			return true;
		}
		return false;
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
		if(line.charAt(0) == '#') {
			return true;
		}
		return false;
	}
	
	public String toString() {
		String cameraInfo = "Camera info: \n" + camera.toString() + "\n";
		String modelInfo = "Models from " + driverName + ":\n";
		for(ModelReader m : models) {
			modelInfo += m.toString();
		}
		return cameraInfo + modelInfo;
	}
	
	public void printModels() {
		for(ModelReader m : models) {
			m.printModel();
		}
	}
	
	public boolean readModels() {
		boolean allGood = true;
		for(ModelReader m : models) {
			if(!m.readObjectFile()) {
				allGood = false;
			}
		}
		return allGood;
	}
	
}