# faststring

This project is build in the context of my master thesis. The goal is to create a system which is able to optimize the  performance of String operations in a compiled Java application or library. 

I'll try to keep the you up to date and dirstribute news in my blog at http://wondee.info (which is down at the moment)

## Project's structure

```
- faststring
	- core 			<- the optimized String types
	- benchmarks 	<- benchmarks of the core types compared to the original ones
	- analysis		<- analysis of bytecode with WALA 
	- analysis-test 	<- test classes for the use in the unit tests of the analysis project
```

To build the benchmark module you need to build the benchmark tool yabt (http://github.com/wondee/yabt) first and have the right version in your maven repo.

Additionally you'll need to build the WALA core and utils package (https://github.com/wala/WALA) on which the analysis component is based 
