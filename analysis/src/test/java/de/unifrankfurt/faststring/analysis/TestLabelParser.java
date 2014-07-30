package de.unifrankfurt.faststring.analysis;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;

import org.junit.Test;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;

public class TestLabelParser {

	@Test
	public void testParserFromFile() throws Exception {
		
		 TypeLabel label = new TypeLabelConfigParser().parseFile("src/test/resources/testLabel.json");
		
		 assertTrue (label.getOriginalType() == String.class);
		 assertTrue (label.getOptimizedType() == HashMap.class);
		 
		 assertFalse (label.compatibleWith(label));
	}
	
	
	@Test
	public void testParserFromClasspath() throws Exception {
		
		 TypeLabel label = new TypeLabelConfigParser().parseFile("/testLabel.json");
		
		 assertTrue (label.getOriginalType() == String.class);
		 assertTrue (label.getOptimizedType() == HashMap.class);
		 
	}
	
	@Test
	public void testEmptyFileFromClasspath() throws Exception {
		
		 TypeLabel label = new TypeLabelConfigParser().parseFile("/testLabelEmpty.json");
		
		 assertTrue (label.getOriginalType() == String.class);
		 assertTrue (label.getOptimizedType() == HashMap.class);
		 
	}
}
