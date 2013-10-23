package de.unifrankfurt.faststring.perifidix;

import static de.unifrankfurt.faststring.perifidix.Constants.*;

import java.awt.PageAttributes.OriginType;

import org.perfidix.annotation.Bench;

import de.unifrankfurt.faststrings.core.ReplaceCharString;

public class ReplaceCharBenchmark {

	private String result;
	private ReplaceCharString result_;
	@SuppressWarnings("unused")
	private String finalString;
	
	
	public void init() {
		result = ORIG;
		result_ = new ReplaceCharString(ORIG);
	}
	
	@Bench(runs=100, beforeEachRun="init")
	public void replaceCharNormal() {
		finalString = result.replace('a', '0');
	}
	
	@Bench(runs=100, beforeEachRun="init")
	public void replaceCharOpt() {
		finalString = result_.replace('a', '0').toString();
	}
}
