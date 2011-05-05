package org.onesun.sfs.client;

import java.util.List;

import org.onesun.sfs.shared.DataModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Element;

public class AggregatedView extends Composite {
	private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	
	private ListBox channelListBox = new ListBox();
	
	public AggregatedView() {
		splitPanel.setSplitPosition("75%");
		splitPanel.setSize("100%", "100%");
	
		// setup left panel
		leftPanel.setSize("100%", "100%");

		// setup right panel
		channelListBox.setSize("100%", "100%");
		channelListBox.setVisibleItemCount(20);

		rightPanel.setSize("100%", "100%");
		rightPanel.add(channelListBox);
		channelListBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final int selectedIndex = channelListBox.getSelectedIndex();
				if(selectedIndex >= 0){
					updateLeftPanel(DataModel.getList(channelListBox.getItemText(selectedIndex)));
				}
			}
		});

		// setup the main panel
		splitPanel.setLeftWidget(leftPanel);
		splitPanel.setRightWidget(rightPanel);

		initWidget(splitPanel);
	}
	
	public void clearLeftPanel(){
		leftPanel.clear();
	}
	
	public void addToLeftPanel(Element element) {
		try {
			leftPanel.add(new AbderaEntryRenderer(element));
		} catch (Exception e) {
		}
	}

	public void updateChannel(String key, int value){
		final int size = channelListBox.getItemCount();
		boolean found = false;
		
		int index = 0;
		for(; index < size; index++){
			String text = channelListBox.getItemText(index);
			
			if(text.startsWith(key)){
				found = true;
				break;
			}
		}
		
		final String text = key + " : (" + value + ")";
		if(found == false){
			channelListBox.addItem(text);
		}
		else {
			channelListBox.setItemText(index, text);	
		}
	}
	
	public void updateAll() {
		for(String key : DataModel.getKeyset()){
			List<Element> elements = DataModel.getList(key);
			
			if(elements != null){
				updateChannel(key, elements.size());
			}
		}
	}
	
	public void updateLeftPanel(List<Element> list){
		if(list != null && list.size() > 0){
			clearLeftPanel();
			
			for(Element element : list){
				addToLeftPanel(element);
			}
		}
	}
}
