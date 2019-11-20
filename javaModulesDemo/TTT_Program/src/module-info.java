/**
 * Module definition for main program
 */
module ttt_program {
	// Needs classes from these modules
	requires ttt_ai_interface;
	// requires ttt_commons; // not actually needed, although Eclipse keeps adding it, because inherited transitively from AI_Interface

	// Specifically will use an implementation of this interface.
	// Note that we do not say specifically which class we will use.
	// We will choose the one to use at run-time
	uses ttt.api.ttt_ai;
	
	// Requires the following JavaFX modules
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.controls;
	
	// Allow JavaFX to call our start method, etc.
	exports ttt.program to javafx.graphics;
}