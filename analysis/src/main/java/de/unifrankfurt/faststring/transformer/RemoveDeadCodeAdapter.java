package de.unifrankfurt.faststring.transformer;

import static org.objectweb.asm.Opcodes.ASM5;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;

public class RemoveDeadCodeAdapter extends MethodVisitor {
	String owner;
	MethodVisitor next;

	public RemoveDeadCodeAdapter(String owner, int access, String name,
			String desc, MethodVisitor mv) {
		super(ASM5, new MethodNode(access, name, desc, null, null));
		this.owner = owner;
		next = mv;
	}

	@Override
	public void visitEnd() {
		MethodNode mn = (MethodNode) mv;
		Analyzer a = new Analyzer(
				new BasicInterpreter());
		try {
			a.analyze(owner, mn);
			Frame[] frames = a.getFrames();
			AbstractInsnNode[] insns = mn.instructions.toArray();
			for (int i = 0; i < frames.length; ++i) {
				if (frames[i] == null && !(insns[i] instanceof LabelNode)) {
					mn.instructions.remove(insns[i]);
				}
			}
		} catch (AnalyzerException ignored) {
		}
		mn.accept(next);
	}
}
