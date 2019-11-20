/**
 * Module definition for the AI interface
 */
module ttt_ai_interface {
	exports ttt.api;
	
	// Needed for static method that finds implementations
	uses ttt.api.ttt_ai;
	
	// Needs classes from these modules
	requires transitive ttt_commons;
}