package prety;
import java.util.Scanner;

public class MainClass {
	
	private static int C;
	private static int P;
	
	private static int[] lengths;
	private static int[] prices;
	private static int[] cuts;
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		C = Integer.parseInt(scan.nextLine());
		lengths = new int[C];
		prices = new int[C];
		
		for(int i=0; i<C; i++) {
			String data = scan.nextLine();
			Scanner scanLine = new Scanner(data);
			
			lengths[i] = scanLine.nextInt();
			prices[i] = scanLine.nextInt();
			
			scanLine.close();
		}
		
		P = Integer.parseInt(scan.nextLine());
		cuts = new int[P];
		
		String data = scan.nextLine();
		Scanner scanLine = new Scanner(data);
		
		for(int i=0; i<P; i++) {
			
			cuts[i] = scanLine.nextInt();
		}
		scanLine.close();
		
		String str = scan.nextLine();
		scan.close();
		
		Strategy solver;
		
		if(str.equals("minimalistyczna")) {
			solver = new Minimalistic(lengths, prices, cuts);
			solver.solve();
		}
		
		if(str.equals("maksymalistyczna")) {
			solver = new Maximalistic(lengths, prices, cuts);
			solver.solve();
		}
		
		if(str.equals("ekonomiczna")) {
			solver = new Economic(lengths, prices, cuts);
			solver.solve();
		}
		
		if(str.equals("ekologiczna")) {
			solver = new Ecologic(lengths, prices, cuts);
			solver.solve();
		}
	}
}
