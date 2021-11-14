package the.max.lottery.random;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {

	private NavigableMap<Double, E> _map = new TreeMap<Double, E>();
	private Random _random = new Random();
	private Double _total = 0.0D;
	
	public void add(Double db, E e) {
		if (db <= 0.0) {
			return;
		}
		_total += db;
		_map.put(_total, e);
	}
	
	public E next() {
		return _map.ceilingEntry(_random.nextDouble() * _total).getValue();
	}
	
}
