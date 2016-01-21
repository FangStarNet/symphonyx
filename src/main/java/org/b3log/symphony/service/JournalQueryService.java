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
package org.b3log.symphony.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang.time.DateUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.PropertyFilter;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.repository.ArticleRepository;
import org.json.JSONObject;

/**
 * Journal query service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Jan 21, 2016
 * @since 1.4.0
 */
@Service
public class JournalQueryService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(JournalQueryService.class.getName());

    /**
     * Article repository.
     */
    @Inject
    private ArticleRepository articleRepository;

    /**
     * Section generated today?
     *
     * @return {@code true} if section generated, returns {@code false} otherwise
     */
    public synchronized boolean hasSectionToday() {
        try {
            final Query query = new Query().addSort(Keys.OBJECT_ID, SortDirection.DESCENDING).
                    setCurrentPageNum(1).setPageSize(1);

            query.setFilter(new PropertyFilter(Article.ARTICLE_TYPE, FilterOperator.EQUAL,
                    Article.ARTICLE_TYPE_C_JOURNAL_SECTION));

            final JSONObject result = articleRepository.get(query);
            final List<JSONObject> journals = CollectionUtils.<JSONObject>jsonArrayToList(result.optJSONArray(Keys.RESULTS));

            if (journals.isEmpty()) {
                return false;
            }

            final JSONObject maybeToday = journals.get(0);
            final long created = maybeToday.optLong(Article.ARTICLE_CREATE_TIME);

            return DateUtils.isSameDay(new Date(created), new Date());
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Check section generated failed", e);

            return false;
        }
    }

    /**
     * Chapter generated this week?
     *
     * @return {@code true} if chapter generated, returns {@code false} otherwise
     */
    public synchronized boolean hasChapterWeek() {
        try {
            final Query query = new Query().addSort(Keys.OBJECT_ID, SortDirection.DESCENDING).
                    setCurrentPageNum(1).setPageSize(1);

            query.setFilter(new PropertyFilter(Article.ARTICLE_TYPE, FilterOperator.EQUAL,
                    Article.ARTICLE_TYPE_C_JOURNAL_CHAPTER));

            final JSONObject result = articleRepository.get(query);
            final List<JSONObject> journals = CollectionUtils.<JSONObject>jsonArrayToList(result.optJSONArray(Keys.RESULTS));

            if (journals.isEmpty()) {
                return false;
            }

            final JSONObject maybeToday = journals.get(0);
            final long created = maybeToday.optLong(Article.ARTICLE_CREATE_TIME);

            return isSameWeek(new Date(created), new Date());
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Check chapter generated failed", e);

            return false;
        }
    }

    private static boolean isSameWeek(final Date date1, final Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }

        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }
}
