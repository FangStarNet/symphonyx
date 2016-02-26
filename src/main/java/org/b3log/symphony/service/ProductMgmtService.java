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
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.repository.ProductRepository;
import org.json.JSONObject;

/**
 * Product management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Feb 24, 2016
 * @since 1.4.0
 */
@Service
public class ProductMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ProductMgmtService.class);

    /**
     * Product repository.
     */
    @Inject
    private ProductRepository productRepository;

    /**
     * Adds the specified product.
     *
     * @param product the specified product
     * @return product id
     * @throws ServiceException service exception
     */
    @Transactional
    public String addProduct(final JSONObject product) throws ServiceException {
        try {
            return productRepository.add(product);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Adds product failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Updates the specified product.
     *
     * @param product the specified product
     * @throws ServiceException service exception
     */
    @Transactional
    public void updateProduct(final JSONObject product) throws ServiceException {
        try {
            final String productId = product.optString(Keys.OBJECT_ID);

            productRepository.update(productId, product);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Updates product failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Removes the specified product.
     *
     * @param productId the specified productId
     * @throws ServiceException service exception
     */
    @Transactional
    public void removeOrder(final String productId) throws ServiceException {
        try {
            productRepository.remove(productId);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Removes product failed", e);

            throw new ServiceException(e);
        }
    }
}
