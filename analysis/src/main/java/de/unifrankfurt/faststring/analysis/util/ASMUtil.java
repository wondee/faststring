package de.unifrankfurt.faststring.analysis.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public final class ASMUtil {
	private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer); 
	
    public static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }
}
