package de.unifrankfurt.faststring.analysis.wala;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Maps.filterValues;
import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.Set;

public class UseRegister {

	private Map<Integer, Boolean> useMap = newHashMap();
	
	public boolean add(int index, boolean usedLater) {
		if (useMap.containsKey(index)) {
			if (useMap.get(index)) {
				return false;
			} else {
				useMap.put(index, usedLater);
			}
				
		} else {
			useMap.put(index, usedLater);
		}
		
		return true;
	}

	public Set<Integer> getCandidates() {
		return filterValues(useMap, equalTo(false)).keySet();
	}
	
}
