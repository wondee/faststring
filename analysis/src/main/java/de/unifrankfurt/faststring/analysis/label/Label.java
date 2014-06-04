package de.unifrankfurt.faststring.analysis.label;

import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_STRING_VALUE_OF;
import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_SUBSTRING;
import static de.unifrankfurt.faststring.analysis.util.IRUtil.METHOD_SUBSTRING_DEFAULT_START;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.ibm.wala.types.MethodReference;

public enum Label {

	
	
	SUBSTRING {
		@Override
		public List<MethodReference> methods() {
			return Arrays.asList(METHOD_SUBSTRING, METHOD_SUBSTRING_DEFAULT_START);
		}

		@Override
		public boolean canBeUsedAsParamFor(MethodReference method, int index) {
			// TODO check if its possible for stringbuilder
			return false;
		}

		@Override
		public boolean canBeUsedAsReceiverFor(MethodReference calledMethod) {
			return compatibleMethods().contains(calledMethod);
		}
		
		private Set<MethodReference> compatibleMethods() {
			Set<MethodReference> set = Sets.newHashSet(methods());
			// TODO maybe wrong only if it is substituted with a toString() call
			set.add(METHOD_STRING_VALUE_OF);
			return set;
		}

		@Override
		public boolean canBeDefinedAsResultOf(MethodReference method) {
			return methods().contains(method);
		}
	};
	
	
	public abstract List<MethodReference> methods();

	public abstract boolean canBeUsedAsParamFor(MethodReference method, int index);

	public abstract boolean canBeUsedAsReceiverFor(MethodReference method);

	public abstract boolean canBeDefinedAsResultOf(MethodReference method);
}
