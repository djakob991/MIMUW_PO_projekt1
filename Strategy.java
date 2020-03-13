package prety;

/*
 * Abstrakcyjna klasa strategii. Jej podklasy za pomocą
 * metody solve() realizują konkretną strategię.
 */
abstract class Strategy {
	
	protected final int[] lengths;
	protected final int[] prices;
	protected final int[] cuts;
	
	public Strategy(int[] lengths, int[] prices, int[] cuts) {
		this.lengths = lengths;
		this.prices = prices;
		this.cuts = cuts;
	}
	
	abstract public void solve();
}
