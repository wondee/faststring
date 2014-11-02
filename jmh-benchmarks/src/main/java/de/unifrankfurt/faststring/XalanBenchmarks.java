package de.unifrankfurt.faststring;

import java.util.concurrent.TimeUnit;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xml.utils.URI;
import org.apache.xml.utils.URI.MalformedURIException;
import org.apache.xpath.objects.XNumber;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

public class XalanBenchmarks {

	private static XNumber xnumberPos = new XNumber(12.34);
	private static XNumber xnumberNeg = new XNumber(-12.34);
	private static XNumber xnumberInt = new XNumber(12);
	private static XNumber xnumberE = new XNumber(0.12e-5);

	private static XPathFactory factory = XPathFactory.newInstance();
	private static XPath xpath = factory.newXPath();

	private static String EXPR = "ex:addFunc(2, 3) + $xyz";

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void checkAttribQNameBenchmark() {
    	BasisLibrary.checkAttribQName("xmlns:redirect");
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void instantiateURIBenchmark() throws MalformedURIException {
    	new URI("http://xml.apache.org/xalan-j/apidocs/javax/xml/transform/package-summary.html");
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void xNumberToStringPositive() {
    	xnumberPos.str();
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void xNumberToStringNegative() {
    	xnumberNeg.str();
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void xNumberToStringInteger() {
    	xnumberInt.str();
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void xNumberToStringExponent() {
    	xnumberE.str();
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void compileXPath() throws XPathExpressionException {
        xpath.compile(EXPR);
    }
}
