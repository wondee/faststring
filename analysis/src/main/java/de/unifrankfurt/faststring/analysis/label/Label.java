package de.unifrankfurt.faststring.analysis.label;

import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_SUBSTRING;
import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_SUBSTRING_DEFAULT_START;

import java.util.Arrays;
import java.util.List;

import com.ibm.wala.types.MethodReference;

public enum Label {

	SUBSTRING {
		@Override
		public List<MethodReference> methods() {
			return Arrays.asList(METHOD_SUBSTRING, METHOD_SUBSTRING_DEFAULT_START);
		}
	};
	
	
	public abstract List<MethodReference> methods();
}
