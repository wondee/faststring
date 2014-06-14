package de.unifrankfurt.faststring.analysis.prototype;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.ShrikeCTMethod;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.collections.Pair;

import de.unifrankfurt.faststring.analysis.TargetApplication;
import de.unifrankfurt.faststring.analysis.util.StringUtil;
import de.unifrankfurt.faststring.analysis.util.TestUtilities;

public class WalaTest {


	
	public static void main(String[] args) throws IOException, WalaException,
			CancelException, InvalidClassFileException {

		TargetApplication targetApplication = TestUtilities.loadTestClasses();
		
		
		for (IClass clazz : targetApplication.getApplicationClasses()) {

			System.out.println("-- Class: " + clazz.getName());
			for (IMethod m : clazz.getDeclaredMethods()) {
				if (m instanceof IBytecodeMethod) {
//					ShrikeCFG scfg = (ShrikeCFG) new ShrikeIRFactory().makeCFG((IBytecodeMethod) m, Everywhere.EVERYWHERE);
					System.out.println("method: " + m.getSignature());
					if (m instanceof ShrikeCTMethod) {
						
						ShrikeCTMethod bm = (ShrikeCTMethod) m;
						
						int maxLocals = bm.getMaxLocals();
						
						
						Multimap<Pair<String, Integer>, Integer> varIndexMap = HashMultimap.create();
						
						System.out.println("max locals: " + maxLocals);
						for (int bcIndex = 0; bcIndex < bm.getInstructions().length; bcIndex++) {
							for (int localIndex = 0; localIndex < maxLocals; localIndex++) {
								String name = bm.getLocalVariableName(bcIndex, localIndex);
//								System.out.printf("%d %d %sn", bcIndex, localIndex, name);
								
								Pair<String, Integer> pair = Pair.make(name, bcIndex);
								varIndexMap.put(pair, localIndex);
								
							}
						}
						
						
						
						System.out.println(StringUtil.toStringMap(Multimaps.asMap(varIndexMap)));
					}
					/*
					IR ir = targetApplication.findIRForMethod(m);
					
					SSAInstruction[] instructions = ir.getInstructions();
					for (int i = 0; i < instructions.length; i++) {
						SSAInstruction instruction = instructions[i];
						
						System.out.printf("instruction: %d %s: \n", i, instruction);
						
						if (instruction != null) {
							
							
							List<Integer> list = IRUtil.getUsesList(instruction);
							
							int def = instruction.getDef();
							if (def > 0) {
								list.add(def);
							}
							
							for (Integer v : list) {
								System.out.printf("  v%d %s\n", v, Arrays.toString(ir.getLocalNames(i, v)));
								
							}
						}
						System.out.println();
					}
					*/
					
//					System.out.println(scfg);
				} else {
					throw new IllegalArgumentException("given method is no IBytecodeMethod");
				}
				
				
			}

		}
	}
	
}
