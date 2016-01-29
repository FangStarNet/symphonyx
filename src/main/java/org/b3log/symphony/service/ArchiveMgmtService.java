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

import javax.inject.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.repository.ArchiveRepository;
import org.json.JSONObject;

/**
 * Archive management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Jan 29, 2016
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
}
