import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TransformedModelWriter {

	private String driverName;
	private double[][] matrix;
	private ArrayList<String> writePieces;
	private String modelName;
	
	public TransformedModelWriter(ArrayList<String> wP, double[][] m, String dName, String mName) {
		driverName = dName;
		matrix = m;
		writePieces = wP;
		modelName = mName;
	}
	
	public void write() {
		String folderName = driverName.substring(0, driverName.indexOf("."));
		File currentDirectory = new File("");
		File newDirectory = new File(currentDirectory.getAbsolutePath() + "/" + folderName);
		if(!newDirectory.exists()) {
			System.out.println("Making directory " + newDirectory.getAbsolutePath());
			newDirectory.mkdir();
		}
		String newFileName = findValidName(newDirectory);
		File newFile = new File(newDirectory.getAbsolutePath() + "/" + newFileName);
		try {
			PrintWriter writer = new PrintWriter(newFile, "UTF-8");
			int count = 0;
			for(String s : writePieces) {
				if(isInt(s)) {
					String vectorLine = "v ";
					for(int i = 0; i < 3; i++) {
						vectorLine += String.format("%.7f", matrix[count][i]);
						vectorLine += " ";
					}
					vectorLine = vectorLine.substring(0, vectorLine.length());
					writer.println(vectorLine);
					count++;
				}
				else {
					writer.println(s);
				}
			}
			writer.close();
		} catch(IOException e) {
			System.out.println("ERROR: Could not write to file " + newFileName);
		}
		System.out.println(newFile.getAbsolutePath());
	}
	
	private String findValidName(File driverDirectory) {
		String extension = ".ppm";
		String prefix = modelName.substring(0, modelName.indexOf(".")) + "_mw";
		int numDuplicates = 0;
		String[] files = driverDirectory.list();
		if(files.length < 1) {
			prefix += "00";
			prefix += extension;
		}
		else {
			for(String s : files) {
				if(s.contains(prefix)) {
					numDuplicates++;
				}
			}
			if(numDuplicates < 10) {
				prefix += "0";
				prefix += numDuplicates;
			}
			else {
				prefix += numDuplicates;
			}
			prefix += extension;
		}
		return prefix;
	}
	
	private boolean isInt(String piece) {
		try {
			int tempNum = Integer.parseInt(piece);
			tempNum = tempNum++;
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}
