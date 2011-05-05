package org.onesun.atomator.delegates;

public interface Delegate {
	DelegateObject run(DelegateObject object);
	
	boolean isEnabled();
	void setEnabled(boolean enabled);
}
