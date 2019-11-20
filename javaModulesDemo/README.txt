This is a small demo of the module system introduced in Java 9
(first LTS version: Java 11).

The five directories here are intended to be independent projects.
In Eclipse, create a new Java Project "on top of" each of the
subdirectories. Each of these projects contains a "module-info.java"
defining its role in the overall system of five modules.

In a nutshell:

- TTT_Program contains the main program.

- TTT_Commons contains classes used by multiple modules

- TTT_AI_Interface defines an interface for the AI modules.
  Modules meeting this interface are found _at_run_time_ by
  searching the module path.
  
- TTT_AI_Minimax and TTT_AI_Random are the two modules that
  should be found and displayed as options in the program. 

Refer to the lecture slides for more information.