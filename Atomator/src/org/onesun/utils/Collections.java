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
package org.onesun.utils;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Collections {
	private List<?> list;
	private Set<?> set;
	private Map<?, ?> map;
	private Properties properties;
	
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public Set<?> getSet() {
		return set;
	}
	public void setSet(Set<?> set) {
		this.set = set;
	}
	public Map<?, ?> getMap() {
		return map;
	}
	public void setMap(Map<?, ?> map) {
		this.map = map;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}