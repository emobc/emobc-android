/*
 * Copyright © 2011-2012 Neurowork S.L.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emobc.android;

/**
 * Simple Implementation of <tt>SearchResult</tt>
 * @author Jorge E. Villaverde
 * @see SearchResult
 * @version 0.1
 * @since 0.1
 */
public class SimpleSearchResult implements SearchResult {
	private final String name;
	private final String text;
	private final NextLevel nextLevel;
	
	public SimpleSearchResult(String name, String text, NextLevel nextLevel) {
		super();
		this.name = name;
		this.text = text;
		this.nextLevel = nextLevel;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getText() {
		return text;
	}

	@Override
	public NextLevel getNextLevel() {
		return nextLevel;
	}
}