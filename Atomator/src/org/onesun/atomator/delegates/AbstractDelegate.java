package org.onesun.atomator.delegates;

public abstract class AbstractDelegate implements Delegate {
	protected boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
