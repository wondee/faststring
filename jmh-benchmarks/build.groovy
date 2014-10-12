import static Constants.*

class Constants {
    static final BASE_FOLDER = "../xalan"
}

def modes = ['opt', 'normal']

modes.each{ createBenchmark(it) }
modes.each{ runBenchmark(it) }

runProcess(["Rscript", "$BASE_FOLDER/printPlots.R", "$BASE_FOLDER/benchmark_normal.json", "$BASE_FOLDER/benchmark_opt.json"])

def ant = new AntBuilder()

ant.copy(file:"Rplots.pdf", tofile:"$BASE_FOLDER/boxplots.pdf")
ant.delete(file:"Rplots.pdf")


def createBenchmark(opt) {
	def cmd = ["mvn", "clean", "package", "-DskipTests"]

	if (opt != 'normal') {
		cmd << "-Dsuffix=-$opt"
	}

	runProcess(cmd)

	new AntBuilder().copy(file:'target/benchmarks.jar', tofile:"$BASE_FOLDER/benchmark_${opt}.jar")
}

def runBenchmark(opt) {
	jarName = "$BASE_FOLDER/benchmark_${opt}.jar"
	outName = jarName.replace(".jar", ".json");

	runProcess(["java", "-jar", "-noverify",  "${jarName}", "--rff",  "${outName}", "--rf", "JSON", "-f", "10"])
}

def runProcess(cmd) {
	
	def process = (cmd as String[]).execute()
	process.in.eachLine { line ->
		println line
	}

	def returnValue = process.waitFor()

	if (returnValue != 0) {
		throw new RuntimeException("process ended with $returnValue")
	}
	
}


