package de.unifrankfurt.faststring.analysis.classes;

public class LoadTestClass {

	private boolean textStorage;

	public void testLoad() {
		String a = "test";

		a.startsWith(a.substring(3));
	}

	public void testStore() {
		String a = "abc".substring(3);

		a.intern();
	}

	public void test2() {

		String a = "";

		a.startsWith(a);
	}

	public void dumpData(String fileName) {
		setDatabaseName(fileName.substring(0, fileName.length()
				- "test".length()));
		try {
			textStorage = isTextStorage(fileName, false);
			dumpData2(fileName, textStorage, fileName, 124);
		} catch (Exception e) {
			traceError("Can not parse file header", e);
			for (int i = 0; i < 256; i += 16) {
				textStorage = (i % 2) == 0;
				int offset = i / 2;
				String out = fileName + (textStorage ? ".txt." : ".") + offset
						+ "test";
				dumpData2(fileName, textStorage, out, offset);
			}
		}
	}

	private void traceError(String string, Exception e) {
		// TODO Auto-generated method stub

	}

	private void dumpData2(String fileName, boolean textStorage2,
			String fileName2, int i) {
		// TODO Auto-generated method stub

	}

	private boolean isTextStorage(String fileName, boolean b) {
		// TODO Auto-generated method stub
		return false;
	}

	private void setDatabaseName(String substring) {
		// TODO Auto-generated method stub

	}

}
