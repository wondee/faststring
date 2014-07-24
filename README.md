# faststring

This project is build in the context of my master thesis. The goal is to create a system which is able to optimize a given bytecode by choosing different implementation types. This is done so far for String and StringBuilder types, but the system is be able to transform a program with given rules by changing a given type A with any other B. 

I'll try to keep the you up to date and dirstribute news in my blog at http://wondee.info (which is down at the moment)

## Project's structure

```
- faststring
	- core 				<- the optimized String types with its approriate TypeLabels
	- benchmarks 		<- benchmarks of the core types compared to the original ones
	- analysis			<- analysis and transformation (the real magic happens here) 
	- analysis-test 	<- integration tests for the analysis
	- test-classes		<- test classes for the use in the tests of the analysis project
	- documenation 		<- all the documentation stuff
```

To build the benchmark module you need to build the benchmark tool yabt (http://github.com/wondee/yabt).

For the analysis component you'll need my version of the WALA core, utils and shrike package (https://github.com/wondee/WALA). 

Because both libraries are not in maven central you'll need to build them in you local environment. 
