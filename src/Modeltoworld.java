public class Modeltoworld {

	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("ERROR: no driver file listed.");
			return;
		}
		DriverReader dreader = new DriverReader(args[0]);
		dreader.readFile();
		System.out.println(dreader.toString());
		dreader.readModels();
		dreader.printModels();
	}
}