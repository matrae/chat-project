/**
 * Module definition for AI implementations
 */
module ttt_ai_minimax {
	// Needs classes from this module
	requires ttt_ai_interface;
	
	// Provides an implementation of this interface in the named class
	// Each module can only provide each service once! 
	// Alternate implementations must be in different modules!
	provides ttt.api.ttt_ai with ttt.ai.minimax.MinimaxPlayer;
}