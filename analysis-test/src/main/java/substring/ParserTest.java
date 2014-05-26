package substring;

public class ParserTest {

	public static void main(String[] args) {
		new ParserTest().parse("abababababab");
	}
	
	public void parse(String line) {
		
		for (int i = 0; i < 10; i+=2) {
			String substring = line.substring(i , 2);
			
		}
		
	}
	
}
