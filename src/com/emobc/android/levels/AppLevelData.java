/**
* Copyright 2012 Neurowork Consulting S.L.
*
* This file is part of eMobc.
*
* AppLevelData.java
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

import java.util.List;

import com.emobc.android.NextLevel;
import com.emobc.android.SearchResult;


/**
 * Application Level Data. 
 * <p>
 * An Application Level Data is a secondary Level in the eMobc Framework.
 * Each application has an <tt>app.xml</tt> file which defines the primary
 * levels. Each of these levels has one or more secondary levels associated.
 * </p>
 * @author Jorge E. Villaverde
 * @see AppLevel
 * @version 0.1
 * @since 0.1
 */
public interface AppLevelData {
	void addItem(AppLevelDataItem item);
	
	/**
	 * Returns a list with all AppLevelDataItem
	 * @return
	 */
	List<AppLevelDataItem> getItems();
	
	/**
	 * Find in a map a LevelDataItem.
	 * @param id
	 * @return
	 */
	AppLevelDataItem findById(String id);
	
	/**
	 * Find number of nextLevel
	 * @param nextLevel
	 * @return
	 */
	AppLevelDataItem findByNextLevel(NextLevel nextLevel);
	
	/**
	 * Index again the map
	 */
	void reIndex();

	/**
	 * Returns all images of search result list
	 * @param levelId
	 * @return
	 */
	List<SearchResult> getAllImages(final String levelId);

	/**
	 * Returns a list with SearchResults
	 * @param text
	 * @param levelId
	 * @return
	 */
	List<SearchResult> findWidthText(final String text, final String levelId);

	/**
	 * Find all geo-referrals points
	 * @param id
	 * @return
	 */
	List<SearchResult> findAllGeoref(String id);
}