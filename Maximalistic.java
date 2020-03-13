package prety;

public class Maximalistic extends Greedy {
	
	public Maximalistic(int[] lengths, int[] prices, int[] cuts) {
		super(lengths, prices, cuts);
	}
	
	@Override
	protected int chooseRod(int cut_length) {
		return lengths.length - 1;
	}
}
