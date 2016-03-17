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

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang.time.DateFormatUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.repository.CompositeFilterOperator;
import org.b3log.latke.repository.FilterOperator;
import org.b3log.latke.repository.PropertyFilter;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.symphony.model.Archive;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.repository.ArchiveRepository;
import org.b3log.symphony.repository.UserRepository;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Archive management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.1, Mar 17, 2016
 * @since 1.4.0
 */
@Service
public class ArchiveMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ArchiveMgmtService.class);

    /**
     * Archive repository.
     */
    @Inject
    private ArchiveRepository archiveRepository;

    /**
     * User repository.
     */
    @Inject
    private UserRepository userRepository;
    
    /**
     * Avatar query service.
     */
    @Inject
    private AvatarQueryService avatarQueryService;

    /**
     * Adds the specified archive.
     *
     * @param archive the specified archive
     * @throws ServiceException service exception
     */
    @Transactional
    public void addArchive(final JSONObject archive) throws ServiceException {
        try {
            archiveRepository.add(archive);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Adds archive failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Refreshes the teams in an archive specified by the given time.
     *
     * @param time the given time
     * @throws ServiceException service exception
     */
    @Transactional
    public void refreshTeams(final long time) throws ServiceException {
        try {
            boolean toAdd = false;
            JSONObject archive = archiveRepository.getArchive(time);
            if (null == archive) {
                archive = new JSONObject();
                archive.put(Archive.ARCHIVE_DATE, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd"));

                toAdd = true;
            }

            final JSONArray teams = new JSONArray();
            final String[] teamStrs = Symphonys.get("teams").split(",");
            for (final String teamStr : teamStrs) {
                final JSONObject team = new JSONObject();
                teams.put(team);
                team.put(Common.TEAM_NAME, teamStr);
                final JSONArray members = new JSONArray();
                team.put(User.USERS, members);

                final List<JSONObject> teamMembers = getTeamMembers(teamStr);
                for (final JSONObject teamMember : teamMembers) {
                    final String memberId = teamMember.optString(Keys.OBJECT_ID);

                    members.put(memberId);
                }
            }

            archive.put(Archive.ARCHIVE_TEAMS, teams.toString());

            if (toAdd) {
                archiveRepository.add(archive);
            } else {
                archiveRepository.update(archive.optString(Keys.OBJECT_ID), archive);
            }
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Refreshes archive failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Gets all members of a team specified by the given team name.
     *
     * @param teamName the given team name
     * @return all members
     */
    private List<JSONObject> getTeamMembers(final String teamName) {
        final Query query = new Query().setFilter(CompositeFilterOperator.and(
                new PropertyFilter(UserExt.USER_TEAM, FilterOperator.EQUAL, teamName),
                new PropertyFilter(UserExt.USER_STATUS, FilterOperator.EQUAL, UserExt.USER_STATUS_C_VALID)));

        try {
            final JSONObject result = userRepository.get(query);

            final JSONArray users = result.optJSONArray(Keys.RESULTS);
            for (int i = 0; i < users.length(); i++) {
                final JSONObject user = users.optJSONObject(i);
                avatarQueryService.fillUserAvatarURL(user);
            }

            return CollectionUtils.<JSONObject>jsonArrayToList(result.optJSONArray(Keys.RESULTS));
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Gets team members failed", e);

            return Collections.emptyList();
        }
    }
}
