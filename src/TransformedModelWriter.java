import java.io.File;
import java.util.ArrayList;

public class TransformedModelWriter {

	private String driverName;
	private double[][] matrix;
	private ArrayList<String> writePieces;
	
	public TransformedModelWriter(ArrayList<String> wP, double[][] m, String dName) {
		driverName = dName;
		matrix = m;
		writePieces = wP;
	}
	
	public void write() {
		String folderName = driverName.substring(0, driverName.indexOf("."));
		File currentDirectory = new File("");
		System.out.println(currentDirectory.getAbsolutePath());
		File newDirectory = new File(currentDirectory.getAbsolutePath() + "\\" + folderName);
		System.out.println(newDirectory.getAbsolutePath());
		if(!newDirectory.exists()) {
			System.out.println("Making directory " + newDirectory.getAbsolutePath());
			newDirectory.mkdir();
		}
	}

}
