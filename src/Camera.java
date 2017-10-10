import java.util.ArrayList;

public class Camera {

	ArrayList<Double> eye;
	ArrayList<Double> look;
	ArrayList<Double> up;
	ArrayList<Double> bounds;
	ArrayList<Integer> resolution;
	double focalLength;
	
	public Camera() {
		eye = new ArrayList<Double>();
		look = new ArrayList<Double>();
		up = new ArrayList<Double>();
		bounds = new ArrayList<Double>();
		resolution = new ArrayList<Integer>();
	}
	
	public boolean setEye(String line) {
		String[] lineData = line.split(" ");
		for(String s : lineData) {
			if(!isDouble(s)) {
				return false;
			}
			else {
				eye.add(Double.parseDouble(s));
			}
		}
		return true;
	}
	
	public boolean setLook(String line) {
		String[] lineData = line.split(" ");
		for(String s : lineData) {
			if(!isDouble(s)) {
				return false;
			}
			else {
				look.add(Double.parseDouble(s));
			}
		}
		return true;
	}
	
	public boolean setUp(String line) {
		String[] lineData = line.split(" ");
		for(String s : lineData) {
			if(!isDouble(s)) {
				return false;
			}
			else {
				up.add(Double.parseDouble(s));
			}
		}
		return true;
	}
	
	public boolean setFLength(String line) {
		if(!isDouble(line)) {
			return false;
		}
		else {
			focalLength = Double.parseDouble(line);
			return true;
		}
	}
	
	public boolean setBounds(String line) {
		String[] lineData = line.split(" ");
		for(String s : lineData) {
			if(!isDouble(s)) {
				return false;
			}
			else {
				bounds.add(Double.parseDouble(s));
			}
		}
		return true;
	}
	
	public boolean setResolution(String line) {
		String[] lineData = line.split(" ");
		for(String s : lineData) {
			if(!isInteger(s)) {
				return false;
			}
			else {
				resolution.add(Integer.parseInt(s));
			}
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
	
	public String toString() {
		String cameraInfo = "Eye: " + eye.get(0) + ", " + eye.get(1) + ", " + eye.get(2) + "\n";
				cameraInfo += "Look: " + look.get(0) + ", " + look.get(1) + ", " + look.get(2) + "\n";
				cameraInfo += "Up: " + up.get(0) + ", " + up.get(1) + ", " + up.get(2) + "\n";
				cameraInfo += "Focal Length: " + focalLength + "\n";
				cameraInfo += "Bounds: " + bounds.get(0) + ", " + bounds.get(1) + ", " + bounds.get(2) + ", " + bounds.get(3) + "\n";
				cameraInfo += "Resolution: " + resolution.get(0) + ", " + resolution.get(1) + "\n";
		return cameraInfo;
	}
}
