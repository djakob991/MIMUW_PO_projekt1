package prety;

public class Minimalistic extends Greedy {
	
	public Minimalistic(int[] lengths, int[] prices, int[] cuts) {
		super(lengths, prices, cuts);
	}
	
	@Override
	protected int chooseRod(int cut_length) {
		int beg = 0;
		int end = lengths.length - 1;
		int mid = 0;
		
		while(beg < end) {
			mid = (beg + end)/2;
			
			
			if(lengths[mid] < cut_length) {
				beg = mid + 1;
			} else {
				end = mid;
			}
		}
		
		return (beg + end)/2;
	}
}
