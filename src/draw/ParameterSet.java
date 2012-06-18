package draw;

import java.util.HashMap;
import java.util.Map;

public class ParameterSet {
	
	private Map<String, Double> doubleValues;
	
	public ParameterSet() {
		this.doubleValues = new HashMap<String, Double>();
	}
	
	public void set(String key, double value) {
		this.doubleValues.put(key, value);
	}

	public double get(String key) {
		if (doubleValues.containsKey(key)) {
			return doubleValues.get(key);
		} else {
			return 1.0;
		}
	}
	
}
