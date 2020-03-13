package prety;

public class Economic extends Eco {
	
	public Economic(int[] lengths, int[] prices, int[] cuts) {
		super(lengths, prices, cuts);
	}
	
	@Override
	protected int findBestRod(long v) {
		long bestPrice = Long.MAX_VALUE;
		int bestId = -1;
	
		if(lengths[lengths.length - 1] < v) {
			
			return bestId;
		}
		
		int i = lengths.length - 1;
		
		while(i >= 0 && lengths[i] >= v) {
			if(prices[i] < bestPrice) {
				bestPrice = prices[i];
				bestId = i;
			}
			i--;
		}
		
		return bestId;
	}
	
	@Override
	protected long bestRodValue(int id, long aktSum) {
		if(id == -1) {
			return Long.MAX_VALUE;
		}
		
		return prices[id];
	}
}
