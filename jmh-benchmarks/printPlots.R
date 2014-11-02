library(rjson)
library(sm)
library(vioplot)

args <- commandArgs(trailingOnly = TRUE)

input <- fromJSON(file=args[1])
input_opt <- fromJSON(file=args[2])


lines <- c()

for (i in 1:length(input)) {
	data <- data.frame( 
		normal = c(input[[i]]$primaryMetric$rawData, recursive=TRUE), 
		optimized = c(input_opt[[i]]$primaryMetric$rawData, recursive=TRUE)
	)
	name <- tail(strsplit(input[[i]]$benchmark, "\\.")[[1]], n=1)
	boxplot(data, ylab ="Time spend in a method call (ns/op)")
	title(name)
	vioplot(data$normal, data$optimized, names=c("normal", "optimized"), col="lightblue")
	title(name)

	title(name)

	lines <- c(lines, 
				name,
				paste("n\t", input[[i]]$primaryMetric$score, 
						"\t", input[[i]]$primaryMetric$scoreError, 
						"\t", median(data$normal)),
				paste("o\t", input_opt[[i]]$primaryMetric$score, 
						"\t", input_opt[[i]]$primaryMetric$scoreError,
						"\t", median(data$optimized)),
				""
			   )
}

outFile <- file("measurement_out")
writeLines(lines, outFile)	
close(outFile)

