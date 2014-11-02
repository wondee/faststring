# faststring

This project is build in the context of my master thesis. The goal is to create a system which is able to optimize a given bytecode by choosing different implementation types. This is done so far for String and StringBuilder types, but the system is be able to transform a program with given rules by changing a given type A with any other B.

I'll try to keep the you up to date and dirstribute news in my blog at http://wondee.info (which is down at the moment)

## Project's structure

```
- faststring
	- core 				<- the optimized String types with its approriate TypeLabels
	- jmh-benchmarks 	<- benchmarks of the optimized xalan functions
	- analysis			<- analysis and transformation (the real magic happens here)
	- analysis-test 	<- integration tests for the analysis
	- test-classes		<- test classes for the use in the tests of the analysis project
	- documenation 		<- all the documentation stuff (presentation and thesis; in german)
```

For the analysis component you'll need my version of the WALA core, utils and shrike package (https://github.com/wondee/WALA).

Because the library is not in maven central you'll need to build them in you local environment.

To get a first touch of the API have a look at the unit tests located in the analysis-test project.

To optimize a given project have a look at the core projects pom. It declares the de.unifrankfurt.faststring.Runner class
as main class and in turn holds the optimization types. build it with mvn package and you can run the resulting jar with
dependencies.

To run the benchmarks have a look at the build.groovy script in the jmh-benchmarks project.
