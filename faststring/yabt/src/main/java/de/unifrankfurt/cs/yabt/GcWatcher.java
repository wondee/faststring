package de.unifrankfurt.cs.yabt;

import java.lang.ref.WeakReference;

class GcWatcher {

	@SuppressWarnings("unused")
	private WeakReference<GcNotifier> weakRef;
	private GcListener listener;
	
	GcWatcher() {
		reset();
	}

	void registerListener(GcListener listener) {
		this.listener = listener;
	}
	
	private synchronized void reset() {
		weakRef = new WeakReference<GcNotifier>(new GcNotifier());
	}
	
	class GcNotifier {
		
		@Override
		protected void finalize() throws Throwable {
			if (listener != null) {
				listener.gcRun();
			}
			reset();
		}
	}
}
