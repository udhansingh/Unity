/* 
Copyright 2010 Udaya Kumar (Udy)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.onesun.atomator.adaptors;

import java.io.InputStream;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;
import org.onesun.atomator.channels.Channel;
import org.onesun.utils.AbderaUtils;
import org.onesun.utils.StreamUtils;

public class TwitterAdaptor extends AbstractAdaptor {
	public TwitterAdaptor() {
		super();
	}

	public TwitterAdaptor(Channel channel){
		super(channel);
	}

	@Override
	protected Feed parseEntries(final String input) {
		Abdera abdera = AbderaUtils.getAbdera();
		Parser parser = abdera.getParser();
		
		InputStream is = StreamUtils.toInputStream(input);
		
		Document<Feed> document = null;
		Feed feed = null;
		
		if(is != null) document = parser.parse(is);
		if(document != null) {
			try {
				feed = document.getRoot();
			}
			catch(Exception e){
			}
		}
		
		if(feed != null && feed.getEntries().size() > 0){
			return feed;
		}else {
			return null;
		}
	}
}
