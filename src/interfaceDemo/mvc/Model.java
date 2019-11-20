package interfaceDemo.mvc;

import interfaceDemo.commons.Comm;
import javafx.beans.property.SimpleBooleanProperty;

public class Model implements Comm {
	private boolean weAreSelected;
	private SimpleBooleanProperty theyAreSelected = new SimpleBooleanProperty();
	
	private Comm opponent;
	
	// Incoming actions from the opponent
	@Override
	public void select() {
		theyAreSelected.set(true);
	}
	@Override
	public void deselect() {
		theyAreSelected.set(false);
	}

	// Whatever kind of opponent we have, it supports the Comm interface
	public void setOpponent(Comm opponent) {
		this.opponent = opponent;
	}	

	// User actions need to be sent to the opponent, using methods
	// defined in the Comm interface
	public void setWeAreSelected(boolean weAreSelected) {
		this.weAreSelected = weAreSelected;
		if (weAreSelected) {
			opponent.select();
		} else {
			opponent.deselect();
		}
	}
	
	public boolean isWeAreSelected() {
		return weAreSelected;
	}
	public SimpleBooleanProperty getTheyAreSelected() {
		return theyAreSelected;
	}
	public void setTheyAreSelected(SimpleBooleanProperty theyAreSelected) {
		this.theyAreSelected = theyAreSelected;
	}
	public boolean isTheyAreSelected() {
		return theyAreSelected.get();
	}
	
	
}
