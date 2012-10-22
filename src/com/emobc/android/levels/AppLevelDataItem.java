/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AppLevelDataItem.java
* eMobc Android Framework
*
* eMobc is free software: you can redistribute it and/or modify
* it under the terms of the Affero GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* eMobc is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the Affero GNU General Public License
* along with eMobc. If not, see <http://www.gnu.org/licenses/>.
*
*/
package com.emobc.android.levels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.emobc.android.NextLevel;
import com.emobc.android.SearchResult;
import com.emobc.android.SimpleSearchResult;


/**
 * AppLevelDataItem superclass.
 * Contains the basic descriptions for each AppLevelDataItem 
 * @author Jorge E. Villaverde
 * @version 0.1
 * @since 0.1
 */
public abstract class AppLevelDataItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8684371733952075864L;
	private String id;
	private String headerImageFile;
	private String headerText;
	private String geoReferencia;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public String getHeaderImageFile() {
		return headerImageFile;
	}

	public void setHeaderImageFile(String headerImageFile) {
		this.headerImageFile = headerImageFile;
	}

	public void setGeoReferencia(String geoReferencia) {
		this.geoReferencia = geoReferencia;
	}

	public String getGeoReferencia() {
		return geoReferencia;
	}

	public List<SearchResult> getAllImages(final String levelId){
		List<SearchResult> ret = new ArrayList<SearchResult>();
		return ret;
	}

	public List<SearchResult> findWidthText(String text, final String levelId) {
		List<SearchResult> ret = new ArrayList<SearchResult>();
		if(headerText != null && headerText.contains(text)){
			NextLevel nextLevel = new NextLevel(levelId, id);
			ret.add(new SimpleSearchResult(headerText, headerText, nextLevel));
		}
		return ret;
	}

	public List<SearchResult> findAllGeoref(String levelId) {
		List<SearchResult> ret = new ArrayList<SearchResult>();
		if(geoReferencia != null){
			NextLevel nextLevel = new NextLevel(levelId, id);
			ret.add(new SimpleSearchResult(headerText, geoReferencia, nextLevel));
		}
		return ret;
	}
}