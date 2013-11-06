package de.unifrankfurt.faststrings.core;

import java.util.Stack;

public class StringTreeBuilder {

	private Node root;

	private Node rightMostNode;
//	private Node leftMostNode;

	private int length = 0;

	public StringTreeBuilder() {
		root = null;
	}

	public StringTreeBuilder(SubstringString string) {
		append(string);
		length += string.length();
	}

	public StringTreeBuilder append(SubstringString string) {
		Node newNode = new Node(string);
		if (root == null) {
			root = newNode;
//			leftMostNode = newNode;
		} else {
			rightMostNode.right = newNode;
			newNode.parent = rightMostNode;
			balanceTree();
		}
		rightMostNode = newNode;

		return this;
	}

	private void balanceTree() {
		// TODO Auto-generated method stub

	}

	public String toString() {
		StringBuilder builder = new StringBuilder(length);

		Stack<Node> stack = new Stack<>();
		stack.push(root);

		while (!stack.isEmpty()) {
			Node current = stack.pop();

			if (current.right != null) {
				stack.push(current.right);
			}

			if (current.left == null) {
				builder.append(current.data);
			} else {
				stack.push(current);
				stack.push(current.left);
			}
		}



		return builder.toString();
	}

	class Node {
		SubstringString data;

		Node parent;

		Node left;
		Node right;

		Node(SubstringString data) {
			this.data = data;
		}
	}
}
