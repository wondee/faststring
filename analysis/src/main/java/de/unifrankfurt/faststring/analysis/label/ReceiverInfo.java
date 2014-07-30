package de.unifrankfurt.faststring.analysis.label;

import java.util.List;

import com.google.common.collect.ImmutableList;

@Deprecated
public class ReceiverInfo {

	private boolean defLabelable;
	
	private List<Integer> labelableParams;

	public static ReceiverInfo NOT_USABLE_AS_RECEIVER = new ReceiverInfo(false, ImmutableList.<Integer>of());
	
	public ReceiverInfo(boolean defLabelable, Iterable<Integer> labelableParams) {
		super();
		this.defLabelable = defLabelable;
		this.labelableParams = ImmutableList.copyOf(labelableParams);
	}
		
	
	public boolean isDefLabelable() {
		return defLabelable;
	}
	
	public List<Integer> getLabelableParams() {
		return labelableParams;
	}
}
