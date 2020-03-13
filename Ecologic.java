package prety;

public class Ecologic extends Eco {
	
	public Ecologic(int[] lengths, int[] prices, int[] cuts) {
		super(lengths, prices, cuts);
	}
	
	@Override
	protected int findBestRod(long v) {
		
		//System.out.println("longest: " + lengths[lengths.length - 1]);
		//System.out.println("act: " + v);
		
		if(lengths[lengths.length - 1] < v) {
			//System.out.println("xx");
			return -1;
		}
		
		int beg = 0;
		int end = lengths.length - 1;
		int mid = 0;
		
		while(beg < end) {
			mid = (beg + end)/2;
			
			
			if(lengths[mid] < v) {
				beg = mid + 1;
			} else {
				end = mid;
			}
		}
		
		return (beg + end)/2;
	}
	
	@Override
	protected long bestRodValue(int id, long aktSum) {
		if(id == -1) {
			return Long.MAX_VALUE;
		}
		
		return lengths[id] - (int)aktSum;
	}
}
