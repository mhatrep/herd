/*
* Copyright 2015 herd contributors
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
package org.finra.herd.tools.retention.exporter;

import org.finra.herd.model.api.xml.BusinessObjectDataSearchRequest;
import org.finra.herd.model.api.xml.BusinessObjectDataSearchResult;
import org.finra.herd.model.api.xml.BusinessObjectDefinition;
import org.finra.herd.sdk.api.BusinessObjectDataApi;
import org.finra.herd.sdk.api.BusinessObjectDefinitionApi;
import org.finra.herd.sdk.invoker.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import org.finra.herd.tools.common.databridge.DataBridgeWebClient;

import java.net.URISyntaxException;

/**
 * This class encapsulates web client functionality required to communicate with the registration server.
 */
@Component
class RetentionExpirationExporterWebClient extends DataBridgeWebClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RetentionExpirationExporterWebClient.class);

    /**
     * Retrieves business object definition from the herd registration server.
     *
     * @param namespace the namespace of the business object definition
     * @param businessObjectDefinitionName the name of the business object definition
     *
     * @return the business object definition
     * @throws ApiException if an Api exception was encountered
     */
    BusinessObjectDefinition getBusinessObjectDefinition(String namespace, String businessObjectDefinitionName) throws ApiException, URISyntaxException {

        LOGGER.info("Retrieving business object definition information from the registration server...");
        BusinessObjectDefinitionApi businessObjectDefinitionApi = new BusinessObjectDefinitionApi(createApiClient(regServerAccessParamsDto));

        org.finra.herd.sdk.model.BusinessObjectDefinition sdkResponse = businessObjectDefinitionApi.businessObjectDefinitionGetBusinessObjectDefinition(namespace, businessObjectDefinitionName, false);
        BusinessObjectDefinition businessObjectDefinition = new BusinessObjectDefinition();
        BeanUtils.copyProperties(sdkResponse, businessObjectDefinition);

        LOGGER.info("Successfully retrieved business object definition from the registration server.");

       return businessObjectDefinition;
    }

    /**
     * Retrieves business object definition from the herd registration server.
     *
     * @param businessObjectDataSearchRequest the business object definition search request
     * @param pageNum the page number for the result to contain
     *
     * @return the business object definition
     * @throws ApiException if an Api exception was encountered
     */
    BusinessObjectDataSearchResult searchBusinessObjectData(BusinessObjectDataSearchRequest businessObjectDataSearchRequest, Integer pageNum)
            throws ApiException, URISyntaxException {
        LOGGER.info("Sending business object data search request to the registration server...");

        BusinessObjectDataApi businessObjectDataApi = new BusinessObjectDataApi(createApiClient(regServerAccessParamsDto));
        org.finra.herd.sdk.model.BusinessObjectDataSearchRequest sdkRequest = new org.finra.herd.sdk.model.BusinessObjectDataSearchRequest();
        BeanUtils.copyProperties(businessObjectDataSearchRequest, sdkRequest);
        org.finra.herd.sdk.model.BusinessObjectDataSearchResult sdkResponse = businessObjectDataApi.businessObjectDataSearchBusinessObjectData(sdkRequest, pageNum, null);

        LOGGER.info("Successfully received search business object data response from the registration server.");
        return convertType(sdkResponse, BusinessObjectDataSearchResult.class);
    }
}
