package de.unifrankfurt.faststring.core;

public class StringListBuilder {

	private Element head;
	private Element last;

	private int length = 0;

	public StringListBuilder() {

	}

	public StringListBuilder append(SubstringString string) {
		Element newElem = new Element(string);
		length += string.length();

		if (head == null) {
			head = newElem;
		} else {
			last.next = newElem;
		}

		last = newElem;

		return this;
	}


	public String toString() {
		StringBuilder builder = new StringBuilder(length);
		Element current = head;
		while(current != null) {
			builder.append(current.data);
			current = current.next;
		}

		return builder.toString();
	}

	class Element {
		SubstringString data;

		Element next;

		public Element(SubstringString data) {
			this.data = data;
		}
	}
}
