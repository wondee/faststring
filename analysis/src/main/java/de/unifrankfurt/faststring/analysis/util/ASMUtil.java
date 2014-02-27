package de.unifrankfurt.faststring.analysis.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public final class ASMUtil {
	private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer); 
	
    private ASMUtil() {
		// not intended for instantiation
	}
    
    
    public static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString().trim();
    }
    

	public static void printStack(Frame frame) {
		if (frame != null) {
			int stackSize = frame.getStackSize();
			if (stackSize == 0) {
				System.out.println("stack is empty");
			} else {
				System.out.println("current stack:");
				for (int i = 0; i < stackSize; i++) {
					System.out.println(i + ": " + frame.getStack(i).toString());
				}
			}
		} else {
			System.out.println("frame is null");
		}
	}
}

