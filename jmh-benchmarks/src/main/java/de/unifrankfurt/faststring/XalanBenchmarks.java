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

	private static XNumber xnumber = new XNumber(12345.67890);

	private static XPathFactory factory = XPathFactory.newInstance();
	private static XPath xpath = factory.newXPath();

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void checkAttribQNameBenchmark() {
    	BasisLibrary.checkAttribQName("abcbcbcbcbcbcbcbcbc:cbcbcbcbcbcbcbcbcbcbcb:cbcbcbcbbcbcbcbcb");
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void instantiateURIBenchmark() throws MalformedURIException {
    	new URI("http://www.example.org:4711/this/is/a/path.html?x=123&y=Hallo+Welt");
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void xNumberToString() {
    	xnumber.str();
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void compileXPath() throws XPathExpressionException {
        xpath.compile("//test/a[abc='xyz']");
    }
}
