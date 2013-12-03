# faststring

This project is a is build in the context of my master thesis. The goal is to create a system which is able to optimize performance of the String operations in a compiled Java application or library. 

I'll try to keep the you up to date and dirstribute news in my blog at http://wondee.info.

## Project's structure

```
- faststring
	- core 			<- the optimized String types
	- benchmarks 	<- benchmarks of the core types compared to the original ones
```

To build the benchmark module you need to build the benchmark tool yabt (http://github.com/wondee/yabt) first and have the right version in your maven repo.
