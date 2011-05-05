package org.onesun.sfs.client;

import java.util.Date;

import org.onesun.sfs.shared.Constants;
import org.onesun.sfs.shared.XXMLUtils;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Element;

public class AbderaEntryRenderer extends Composite {
	private VerticalPanel rootPanel = new VerticalPanel();
	private HorizontalPanel headerPanel = new HorizontalPanel();
	private HorizontalPanel docPanel = new HorizontalPanel();
	
	private HTML lblTitle;
	private Label lblChannel;
	private Label lblUpdated;
	private HTML lblSummary;
	private HTML lblAuthor;
	
	private Image icon;
	
	private Element element = null; 
	
	public AbderaEntryRenderer(Element entryElement) throws Exception {
		this.element = entryElement;
		
		initWidget(rootPanel);

		DeferredCommand.addCommand(new Command() {
			@Override
			public void execute() {
				if(element != null){
					initUXPanel();
				}
			}
		});
	}
	
	private void initUXPanel(){
		String text = null; 
		
		// Get Channel
		text = XXMLUtils.getQNameChoppedValue(element, "endpoint", Constants.QNAME); 
		if(text != null) {
			lblChannel = new HTML(text);
		}
		
		// Get title
		text = XXMLUtils.getValue(element, "title");
		if(text != null) {
			lblTitle = new HTML((text));
		}
		
		// Get published
		text = XXMLUtils.getValue(element, "published");
		if(text != null){
			try{
				// 2010-10-24T21:30:01+00:00
				Date date = DateTimeFormat.getFormat("yyyy-MM-ddTHH:mm:ss+00:00").parse(text);
				text = DateTimeFormat.getFullDateTimeFormat().format(date);
			}catch(Exception e1){
				// 2010-10-24T14:10:00.000Z
				try {
					Date date = DateTimeFormat.getFormat("yyyy-MM-ddTHH:mm:ss.000Z").parse(text);
					text = DateTimeFormat.getFullDateTimeFormat().format(date);
				}catch(Exception e2){
					
				}
			}
			
			lblUpdated = new Label(text);
		}
		
		// Get Author
		text = XXMLUtils.getValue(element, "author");
		if(text != null){
			lblAuthor = new HTML((text));
		}
		
		// Get Summary
		text = XXMLUtils.getValue(element, "summary");
		if(text != null){
			lblSummary = new HTML((text));
		}
		
		
		// Populate header items
		if(lblChannel != null)	{
			headerPanel.add(lblChannel);
			lblChannel.setStyleName("FeedRendererChannel");
		}
		if(lblTitle != null)	{
			headerPanel.add(lblTitle);
			lblTitle.setStyleName("FeedRendererTitle");
		}
		if(lblUpdated != null)	{
			headerPanel.add(lblUpdated);
			lblUpdated.setStyleName("FeedRendererUpdated");
		}
		if(lblAuthor != null)		{
			headerPanel.add(lblAuthor);
			lblAuthor.setStyleName("FeedRendererAuthor");
		}
		
		// Populate document items 
		if(lblSummary != null){
			docPanel.add(lblSummary);
			lblSummary.setStyleName("FeedRendererSummary");
		}
		
		if(icon != null)	{
			docPanel.add(icon);
			icon.setStyleName("FeedRendererIcon");
		}
		
		// add the root panel
		rootPanel.add(headerPanel);		
		rootPanel.add(docPanel);
		rootPanel.setStyleName("FeedRenderer");
	}
}