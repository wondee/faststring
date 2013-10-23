# faststring

This project is a is build in the context of my master thesis. The goal is to create a system which is able to optimize performance of the String datatype in Java. 

I'll try to keep the news up to date and dirstribute news in my blog at http://wondee.github.io.

## Project's structure

```
- fastsring
	- core 			<- the optimized String types
	- benchmarks 	<- benchmarks of the core types compared to the original ones
	- yabt			<- benchmark tool to run the benchmarks
```

## Status 23.10.2013

The first step was to build optimized types for the String manipulation operations:

- concat (the '+' operator)
- substring
- replace (for chars and regex pattern based replacements)

To check if a optimized operation is really faster than the original one, a benchmark should be created to check this requirement. After struggling with caliper (http://code.google.com/p/caliper/), which is really a nice and powerful framework. But it failed when I tried to measure a single operation that could not be nested within a loop. So currently I'm working on a benchmark tool which is able to do so. An intial commit follows as I have a working one. I called it yabt.

So stay tuned...

