package de.unifrankfurt.faststring.analysis.classes;

public class ConnectTestClass {

	/*
< Application, Lde/unifrankfurt/faststring/analysis/classes/ConnectTestClass, simple()Ljava/lang/String; >
CFG:
BB0[-1..-2]
    -> BB1
BB1[0..4]
    -> BB2
    -> BB3
BB2[5..7]
    -> BB3
BB3[-1..-2]
Instructions:
BB0
BB1
4   v6 = invokevirtual < Application, Ljava/lang/String, substring(I)Ljava/lang/String; > v3:#abcdef,v4:#3 @5 exception:v5(line 29) {3=[a]}
BB2
7   return v3:#abcdef                        (line 31) {3=[a]}
BB3


	 */
	
	public String simple() {
		String a = "abcdef";
		
		a.substring(3);
		
		return a;
	}
	/*

< Application, Lde/unifrankfurt/faststring/analysis/classes/ConnectTestClass, ifTest(Z)Ljava/lang/String; >
CFG:
BB0[-1..-2]
    -> BB1
BB1[0..6]
    -> BB4
    -> BB2
BB2[7..9]
    -> BB3
    -> BB13
BB3[10..11]
    -> BB12
BB4[12..14]
    -> BB5
    -> BB13
BB5[15..16]
    -> BB6
    -> BB13
BB6[17..19]
    -> BB7
    -> BB13
BB7[20..20]
    -> BB8
    -> BB13
BB8[21..24]
    -> BB9
    -> BB13
BB9[25..25]
    -> BB10
    -> BB13
BB10[26..26]
    -> BB11
    -> BB13
BB11[27..27]
    -> BB12
BB12[28..29]
    -> BB13
BB13[-1..-2]
Instructions:
BB0
BB1
6   conditional branch(eq) v2,v6:#0          (line 38) {2=[is]}
BB2
9   v22 = invokevirtual < Application, Ljava/lang/String, substring(I)Ljava/lang/String; > v4:#axyz,v7:#4 @12 exception:v21(line 39) {4=[a]}
BB3
11   goto                                    (line 40)
BB4
14   v9 = invokevirtual < Application, Ljava/lang/String, substring(I)Ljava/lang/String; > v5:#bcd,v7:#4 @22 exception:v8(line 41) {5=[b]}
BB5
16   v10 = new <Application,Ljava/lang/StringBuilder>@27(line 43)
BB6
19   v12 = invokestatic < Application, Ljava/lang/String, valueOf(Ljava/lang/Object;)Ljava/lang/String; > v9 @33 exception:v11(line 43) {9=[c]}
BB7
20   invokespecial < Application, Ljava/lang/StringBuilder, <init>(Ljava/lang/String;)V > v10,v12 @36 exception:v13(line 43)
BB8
24   v16 = invokevirtual < Application, Ljava/lang/String, substring(II)Ljava/lang/String; > v5:#bcd,v14:#1,v7:#4 @42 exception:v15(line 43) {5=[b]}
BB9
25   v18 = invokevirtual < Application, Ljava/lang/StringBuilder, append(Ljava/lang/String;)Ljava/lang/StringBuilder; > v10,v16 @45 exception:v17(line 43)
BB10
26   v20 = invokevirtual < Application, Ljava/lang/StringBuilder, toString()Ljava/lang/String; > v18 @48 exception:v19(line 43)
BB11
BB12
           v23 = phi  v22,v20
29   return v23                              (line 46) {23=[c]}
BB13


	 */
	public String ifTest(boolean is) {
		String a = "axyz";
		String b = "bcd";
		String c;
		if (is) {
			c = a.substring(4);
		} else {
			c = b.substring(4);
			
			c = c + b.substring(1, 4);
		}
		
		return c;
	}
	
	/*
	
< Application, Lde/unifrankfurt/faststring/analysis/classes/ConnectTestClass, loopTest()V >
CFG:
BB0[-1..-2]
    -> BB1
BB1[0..4]
    -> BB4
BB2[5..8]
    -> BB3
    -> BB7
BB3[9..13]
    -> BB4
BB4[14..16]
    -> BB5
    -> BB7
BB5[17..17]
    -> BB2
    -> BB6
BB6[18..18]
    -> BB7
BB7[-1..-2]
Instructions:
BB0
BB1
4   goto                                     (line 91)
BB2
8   v9 = invokevirtual < Application, Ljava/lang/String, substring(II)Ljava/lang/String; > v12,v4:#0,v7:#6 @12 exception:v8(line 92) {12=[a]}
BB3
12   v11 = binaryop(add) v13 , v10:#1        (line 91) {13=[i]}
BB4
           v12 = phi  v3:#abd,v9
           v13 = phi  v4:#0,v11
16   v6 = invokevirtual < Application, Ljava/lang/String, length()I > v12 @21 exception:v5(line 91) {12=[a]}
BB5
17   conditional branch(lt) v13,v6           (line 91) {13=[i]}
BB6
18   return                                  (line 94)
BB7	
	
	
	 */
	public void loopTest() {
		String a = "abd";
		
		for (int i = 0; i < a.length(); i++) {
			a = a.substring(0, 6);
		}
	}
	
}
