package de.unifrankfurt.faststring.yabt;

import java.util.Map;

import com.google.common.collect.Maps;

public class Result {

	private Map<String, double[][]> resultMap;
	private int runs;

	Result(int runs) {
		resultMap = Maps.newHashMap();
		this.runs = runs;
	}

	void set(int run, int measurement, String name, double value) {
		if (!resultMap.containsKey(name)) {
			initializeMap(name);
		}

		resultMap.get(name)[run][measurement] = value;
	}

	private void initializeMap(String name) {
		resultMap.put(name, new double[runs][]);

	}

	public void set(int i, String name, double[] results) {
		resultMap.get(name)[i] = results;

	}

}
