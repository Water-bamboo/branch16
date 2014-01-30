===============
=== READ ME ===
===============

This algorithm is only for 4*4 puzz.

A* Search ('asm' or 'aso' depending on heuristic)

Number Out of Place ('aso')
Manhattan Distance ('asm')

Compilation:
	javac ProblemSolver.java
	
After Optimazation, it works fun, don't need -Xmx1024 for JVM..

Execution:
	Example usage for aso first:
		java -Xmx1024m ProblemSolver asm 15 12 6 3 4 2 5 7 11 10 9 0 1 8 13 14
		java -Xmx1024m ProblemSolver asm 0 1 6 3 4 2 5 7 8 9 10 11 12 13 14 15
	Example usage for asm first:
		java ProblemSolver asm 15 12 6 3 4 2 5 7 11 10 9 0 1 8 13 14
		java ProblemSolver asm 0 1 6 3 4 2 5 7 8 9 10 11 12 13 14 15
