package de.unifrankfurt.faststring.analysis.wala;

import static com.google.common.base.Predicates.equalTo;

import static com.google.common.collect.Maps.filterValues;
import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.Set;

/**
 * container for value numbers of references that are or are not used after their
 * first use.
 * 
 * @author markus
 *
 */
public class UseRegister {

	private Map<Integer, Boolean> useMap = newHashMap();
	
	/**
	 * adds the index to the register if it does not already exist. 
	 * If it not exists it is only added if the currently contained {@code usedLater} 
	 * flag is {@code false} (it was defined as not used later before)
	 * 
	 * @param index the index to be stored
	 * @param usedLater flag if this index is used later
	 * 
	 * @return if the value was stored to the register
	 */
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

	/**
	 * @return all value numbers that are not used later
	 */
	public Set<Integer> getCandidates() {
		return filterValues(useMap, equalTo(false)).keySet();
	}
	
}
