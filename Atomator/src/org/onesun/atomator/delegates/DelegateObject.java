package org.onesun.atomator.delegates;

import java.util.List;

import org.apache.abdera.model.Entry;
import org.onesun.atomator.adaptors.Adaptor;

public class DelegateObject {
	public DelegateObject(Adaptor adaptor, List<Entry> entries) {
		super();
		
		this.adaptor = adaptor;
		this.entries = entries;
	}
	
	public Adaptor getAdaptor() {
		return adaptor;
	}
	public void setAdaptor(Adaptor adaptor) {
		this.adaptor = adaptor;
	}
	public List<Entry> getEntries() {
		return entries;
	}
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	private Adaptor adaptor;
	private List<Entry> entries = null;
}
