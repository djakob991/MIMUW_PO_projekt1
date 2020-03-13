package prety;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.BitSet;

/*
 * Abstrakcyjna klasa strategii optymalizacyjnej.
 * funkcje findBestRod i bestRodValue zaimplementowane
 * w podklasach odpowiadają za minimalizowanie odpowiedniej wartosci:
 * ceny lub lacznej sumy odpadow.
 */

public abstract class Eco extends Strategy{
	
	public Eco(int[] lengths, int[] prices, int[] cuts) {
		super(lengths, prices, cuts);
	}
	
	abstract protected int findBestRod(long v);
	abstract protected long bestRodValue(int id, long actSum);
	
	/*
	 * Obiekty tej klasy pomocniczej przechowują informacje o danym
	 * podzbiorze odcinkow, reprezentowanym jako maska bitowa.
	 */
	
	private class SetData {
		
		private final int bestOneRodId;
		private BitSet originMask;
		private long bestScore;
		private BitSet lastProcessed;
		
		public int getBestOneRodId() {
			return bestOneRodId;
		}
		
		public BitSet getOrginMask() {
			return originMask;
		}
		
		public long getScore() {
			return bestScore;
		}
		
		public BitSet getLastProcessed() {
			return lastProcessed;
		}
		
		public void setOriginMask(BitSet newMask) {
			this.originMask = newMask;
		}
		
		public void setScore(long newScore) {
			this.bestScore = newScore;
		}
		
		public void setLastProcessed(BitSet newMask) {
			this.lastProcessed = newMask;
		}
		
		public SetData(BitSet mask, long actSum) {
			this.bestOneRodId = findBestRod(actSum);
			this.bestScore = bestRodValue(bestOneRodId, actSum);
			
			this.originMask = mask;
			this.lastProcessed = null;
		}
		
	}
	
	private static Map<BitSet, SetData> maskMap = new HashMap<BitSet, SetData>();
	private static ArrayList<Pret> rodList = new ArrayList<Pret>();
	
	/*
	 * Zwraca obiekt BitSet reprezentujący maskę bitową powstałą
	 * po dodaniu (w systemie binarnym) 1 do argumentu 'mask'.
	 */
	private static BitSet nextMask(BitSet mask) {
		BitSet newMask = (BitSet)mask.clone();
		
		int firstZero = newMask.nextClearBit(0);
		newMask.clear(0, firstZero);
		newMask.set(firstZero);
		
		return newMask;
	}
	
	/*
	 * Modyfikuje wartosc wskazanego obiektu BitSet na odpowiadającą
	 * odjęciu w systemie binarnym 1.
	 */
	private static void moveToPrevSubset(BitSet subset, BitSet mask) {
		int firstOne = subset.nextSetBit(0);
		subset.set(0, firstOne);
		subset.clear(firstOne);
		subset.and(mask);
	}
	
	/*
	 * Zwraca dopełnienie podzbioru 1 (subset) do podzbioru 2 (mask)
	 */
	private static BitSet getComplement(BitSet subset, BitSet mask) {
		BitSet complement = (BitSet)subset.clone();
		complement.flip(0, complement.size());
		complement.and(mask);
		
		return complement;
	}
	
	private void addRod(BitSet set) {
		SetData setData = maskMap.get(set);
		//System.out.println("dodaje pret dla zbioru: " + set);
		int rodToBuyId = setData.getBestOneRodId();
		Pret newRod = new Pret(lengths[rodToBuyId], prices[rodToBuyId]);
		
		for(int i=0; i<set.size(); i++) {
			if(set.get(i)) {
				newRod = newRod.cut(cuts[i]);
			}
		}
		
		rodList.add(newRod);
	}
	
	private void makeRodList(BitSet set) {
		SetData setData = maskMap.get(set);
		BitSet originMask = setData.getOrginMask();
		
		if(originMask.equals(set)) {
			addRod(set);
		} else {
			BitSet complement = getComplement(originMask, set);
			
			makeRodList(originMask);
			makeRodList(complement);
		}
	}
	
	
	@Override
	public void solve() {
		
		int P = cuts.length;
		long[] prefixSum = new long[P + 1];
		prefixSum[0] = 0;
		
		for(int i=1; i<=P; i++) {
			prefixSum[i] = prefixSum[i - 1] + cuts[i - 1];
		}
		
		BitSet fullMask = new BitSet(P);
		fullMask.flip(0, P);
		
		BitSet emptyMask = new BitSet(P);
		
		BitSet actMask = nextMask(emptyMask);
		
		long actSum = 0;
		
		while(true) {
			int zerosOnBeg = actMask.nextSetBit(0);
			actSum += cuts[zerosOnBeg] - prefixSum[zerosOnBeg];
			
			maskMap.put(actMask, new SetData(actMask, actSum));
			
			if(actMask.equals(fullMask)) {
				break;
			}
			
			actMask = nextMask(actMask);
		}
		
		actMask = nextMask(emptyMask);
		
		while(true) {
			SetData maskData = maskMap.get(actMask);
			
			BitSet subset = (BitSet)actMask.clone();
			moveToPrevSubset(subset, actMask);
			
			while(!subset.equals(emptyMask)) {
				SetData subsetData = maskMap.get(subset);
				
				BitSet lastProcessed = subsetData.getLastProcessed();
				if(lastProcessed != null && lastProcessed.equals(actMask)) {
					moveToPrevSubset(subset, actMask);
					continue;
				}
				
				BitSet complement = getComplement(subset, actMask);
				SetData complData = maskMap.get(complement);
				
				long potentialScore = subsetData.getScore() + complData.getScore();
				
				if(potentialScore < maskData.getScore()) {
					maskData.setScore(potentialScore);
					maskData.setOriginMask(complement);
				}
				
				subsetData.setLastProcessed(actMask);
				complData.setLastProcessed(actMask);
				
				moveToPrevSubset(subset, actMask);
			}
			
			if(actMask.equals(fullMask)) {
				break;
			}
			
			actMask = nextMask(actMask);
		}
		
		makeRodList(fullMask);
		
		long totalPrice = 0;
		long totalLeft = 0;
		
		for(int i=0; i<rodList.size(); i++) {
			totalPrice += rodList.get(i).getPrice();
			totalLeft += rodList.get(i).rest();
		}
		
		System.out.println(totalPrice);
		System.out.println(totalLeft);
		
		for(int i=0; i<rodList.size(); i++) {
			System.out.println(rodList.get(i));
		}
	}
}
