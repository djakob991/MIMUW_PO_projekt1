package prety;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collections;

/*
 * Abstrakcyjna klasa strategii zachlannej.
 * Metoda chooseRod jest zaimplementowana w podklasach
 * odpowiednio dla strategii maksym. i minim.
 */

abstract class Greedy extends Strategy {
	
	public Greedy(int[] lengths, int[] prices, int[] cuts) {
		super(lengths, prices, cuts);
	}
	
	abstract protected int chooseRod(int cut_length);
	
	@Override
	public void solve() {
		
		long totalPrice = 0;
		long totalLeft = 0;
		ArrayList<Pret> rodList = new ArrayList<Pret>();
		
		int P = cuts.length;
		
		TreeMap<Integer, Integer> cutsMap = new TreeMap<>(Collections.reverseOrder());
		
		for(int i=0; i<P; i++) {
			Integer count = cutsMap.get(cuts[i]);
			
			if(count == null) {
				cutsMap.put(cuts[i], 1);
			} else {
				cutsMap.put(cuts[i], count + 1);
			}
		}
		
		while(!cutsMap.isEmpty()) {
			int biggestCut = cutsMap.firstKey();
			
			cutsMap.put(biggestCut, cutsMap.get(biggestCut) - 1);
			if(cutsMap.get(biggestCut) == 0) {
				cutsMap.remove(biggestCut);
			}
			
			int chooseId = chooseRod(biggestCut);
			totalPrice += prices[chooseId];
			
			Pret newRod = new Pret(lengths[chooseId], prices[chooseId]);
			newRod = newRod.cut(biggestCut);
			
			while(true) {
				Integer longestFit = cutsMap.ceilingKey(newRod.rest());
				if(longestFit == null) {
					break;
				}
				
				int possible = newRod.rest() / longestFit;
				int count = cutsMap.get(longestFit);
				int howMany = Math.min(possible, count);
				
				for(int i=0; i<howMany; i++) {
					newRod = newRod.cut(longestFit);
				}
				
				cutsMap.put(longestFit, count - howMany);
				if(cutsMap.get(longestFit) == 0) {
					cutsMap.remove(longestFit);
				}
			}
			
			totalLeft += newRod.rest();
			rodList.add(newRod);
		}
		
		System.out.println(totalPrice);
		System.out.println(totalLeft);
		
		for(int i=0; i<rodList.size(); i++) {
			System.out.println(rodList.get(i));
		}
		
	}
}
