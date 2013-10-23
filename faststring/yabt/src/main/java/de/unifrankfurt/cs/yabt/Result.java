package de.unifrankfurt.cs.yabt;

import java.util.Map;

import com.google.common.collect.Maps;

public class Result {

	private Map<String, double[][]> resultMap;
	private int measurementRuns;
	private int runs;

	Result(int runs, int measurementRuns) {
		resultMap = Maps.newHashMap();
		this.runs = runs;
		this.measurementRuns = measurementRuns;
	}

	void set(int run, int measurement, String name, double value) {
		if (!resultMap.containsKey(name)) {
			initializeMap(name);
		}

		resultMap.get(name)[run][measurement] = value;
	}

	private void initializeMap(String name) {
		resultMap.put(name, new double[runs][measurementRuns]);

	}

}
