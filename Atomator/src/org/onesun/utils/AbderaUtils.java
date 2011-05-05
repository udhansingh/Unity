package org.onesun.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;

import com.cliqset.abdera.ext.activity.ActivityExtensionFactory;

public class AbderaUtils {
	private static Abdera abdera = null;
	public static Abdera getAbdera() {
		return abdera;
	}

	private static Factory abderaFactory = null;

	public static Factory getAbderaFactory() {
		return abderaFactory;
	}

	public static void setAbderaFactory(Factory abderaFactory) {
		AbderaUtils.abderaFactory = abderaFactory;
	}

	public AbderaUtils(){
		abdera = new Abdera();
		abderaFactory = abdera.getFactory();

		abderaFactory.registerExtension(new ActivityExtensionFactory());
	}

	public static Element addElement(String qname, String prefix, String tag, String value, Entry entry) {
		return entry.addSimpleExtension(qname, tag, prefix, value);
	}

	public static String chopQName(String text, String qname){
		String tokens[] = text.split(qname);

		if(tokens.length == 1){
			return tokens[0];
		}
		else if(tokens.length > 1){
			return tokens[1];
		}
		else {
			return null;
		}
	}

	public static Element getElement(String qname, String tag, Entry entry){
		List<Element> elements = entry.getExtensions(qname);

		for(Element element : elements){
			QName qName = element.getQName();

			if(qName.getLocalPart().compareTo(tag) == 0){
				return element;
			}
		}

		return null;
	}

	public static String getValue(String qname, String tag, Entry entry){
		return getElement(qname, tag, entry).getText();
	}

	public static Entry toAbderaEntry(String entryText) throws ParseException {
		Parser abderaParser = abdera.getParser();

		Reader reader = new StringReader(entryText);
		Document<Entry> document = null;

		document = abderaParser.parse(reader);
		Entry entry = document.getRoot();

		try {
			StringWriter writer = new StringWriter();
			entry.writeTo(writer);
		} catch (IOException e) {
		}

		return entry;
	}

	public static Feed createFeed(List<Entry> entries){
		if(entries != null && entries.size() > 0){
			Feed feed = abderaFactory.newFeed();
			
			for(Entry entry : entries){
				feed.addEntry(entry);
			}
			
			return feed;
		}else {
			return null;
		}
	}
}
