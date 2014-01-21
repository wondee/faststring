package de.unifrankfurt.faststring.analysis;

import static org.objectweb.asm.Opcodes.ASM5;

import java.io.IOException;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.BasicVerifier;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

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
		Analyzer a = new Analyzer(new SimpleVerifier());
		try {
			a.analyze(owner, mn);
			
			Frame[] frames = a.getFrames();
			for (Frame frame : frames) {

			}
			AbstractInsnNode[] insns = mn.instructions.toArray();
			for (int i = 0; i < frames.length; i++) {
//				if (frames[i] == null && !(insns[i] instanceof LabelNode)) {
//					mn.instructions.remove(insns[i]);
//				}
//				System.out.println(insns[i].toString());
				
				if (frames[i] != null) {
					for(int j = 0; j < frames[i].getLocals(); j++) {
						BasicValue local = (BasicValue) frames[i].getLocal(j);
//						System.out.println(local.getClass());
						System.out.println(local.getType());
					}
				} else {
					System.out.println("frame is null");
				}
				
				
			}
		} catch (AnalyzerException ignored) {
		}
//		mn.accept(next);
	}
	
	public static void main(String[] args) throws IOException {
		ClassReader reader = new ClassReader(
				"de.unifrankfurt.faststring.analysis.MyTestClass");
		
		reader.accept(new ClassVisitor(ASM5) {
			private String owner;
			@Override
			public void visit(int version, int access, String name, String signature,
					String superName, String[] interfaces) {
				owner = name;
				System.out.println(name);
				
			}
			
			@Override
			public MethodVisitor visitMethod(int access, String name,
					String desc, String signature, String[] exceptions) {
				System.out.printf("%d, %s, %s, %s, %s\n", access, name, desc, signature, Arrays.toString(exceptions));
				return new RemoveDeadCodeAdapter(owner, access, name, desc, null);
			}
			
		}, 0);
	}
}
