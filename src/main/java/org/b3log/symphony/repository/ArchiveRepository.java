/*
 * Copyright (c) 2012-2016, b3log.org & hacpai.com & fangstar.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.symphony.repository;

import org.apache.commons.lang.time.DateFormatUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.repository.AbstractRepository;
import org.b3log.latke.repository.CompositeFilterOperator;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.PropertyFilter;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.repository.annotation.Repository;
import org.b3log.symphony.model.Archive;
import org.b3log.symphony.util.Times;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Archive repository.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Jan 29, 2016
 * @since 1.4.0
 */
@Repository
public class ArchiveRepository extends AbstractRepository {

    /**
     * Gets the latest week archive with the specified time.
     *
     * @param time the specified time
     * @return archive, returns {@code null} if not found
     * @throws RepositoryException repository exception
     */
    public JSONObject getWeekArchive(final long time) throws RepositoryException {
        final long weekEndTime = Times.getWeekEndTime(time);
        final String startDate = DateFormatUtils.format(time, "yyyyMMdd");
        final String endDate = DateFormatUtils.format(weekEndTime, "yyyyMMdd");

        final Query query = new Query().setCurrentPageNum(1).setPageCount(1).
                addSort(Archive.ARCHIVE_DATE, SortDirection.DESCENDING).
                setFilter(CompositeFilterOperator.and(
                        new PropertyFilter(Archive.ARCHIVE_DATE, FilterOperator.GREATER_THAN_OR_EQUAL, startDate),
                        new PropertyFilter(Archive.ARCHIVE_DATE, FilterOperator.LESS_THAN_OR_EQUAL, endDate)
                ));
        final JSONObject result = get(query);
        final JSONArray data = result.optJSONArray(Keys.RESULTS);

        if (data.length() < 1) {
            return null;
        }

        return data.optJSONObject(0);
    }

    /**
     * Gets an archive with the specified time.
     *
     * @param time the specified time
     * @return archive, returns {@code null} if not found
     * @throws RepositoryException repository exception
     */
    public JSONObject getArchive(final long time) throws RepositoryException {
        final String archiveDate = DateFormatUtils.format(time, "yyyyMMdd");

        final Query query = new Query().setCurrentPageNum(1).setPageCount(1).
                setFilter(new PropertyFilter(Archive.ARCHIVE_DATE, FilterOperator.EQUAL, archiveDate));
        final JSONObject result = get(query);
        final JSONArray data = result.optJSONArray(Keys.RESULTS);

        if (data.length() < 1) {
            return null;
        }

        return data.optJSONObject(0);
    }

    /**
     * Public constructor.
     */
    public ArchiveRepository() {
        super(Archive.ARCHIVE);
    }
}
