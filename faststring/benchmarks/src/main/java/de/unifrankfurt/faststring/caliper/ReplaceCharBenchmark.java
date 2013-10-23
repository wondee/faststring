
package de.unifrankfurt.faststring.caliper;


import java.util.ArrayList;
import java.util.List;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Benchmark;
import com.google.caliper.runner.CaliperMain;

import de.unifrankfurt.faststrings.core.ReplaceCharString;

public final class ReplaceCharBenchmark {

	final String ORIG = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";

	List<Character> chars = new ArrayList<>(26);

	@BeforeExperiment
	public void init() {
		for (int i = 0; i < 26; i++) {
			chars.add(ORIG.charAt(i));
		}
	}

	@Benchmark
	public String replaceOpt(int count) {
		ReplaceCharString result_ = new ReplaceCharString(ORIG);
		for (int i = 0; i < count; i++) {
			char oldChar = chars.get(i % 26);

			result_.replace(oldChar, '0');
		}

		return result_.toString();
	}


	@Benchmark
	public String replaceNormal(int count) {
		String result = String.valueOf(ORIG);
		for (int i = 0; i < count; i++) {
			char oldChar = chars.get(i % 26);

			result.replace(oldChar, '0');
		}

		return result;
	}


	public static void main(String[] args) {
		CaliperMain.main(ReplaceCharBenchmark.class, new String[] { "-i", "runtime", "-r", "ReplaceChar" });
	}
}
