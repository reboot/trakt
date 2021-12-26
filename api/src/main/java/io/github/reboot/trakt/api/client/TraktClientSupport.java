/**
 * Copyright (C) 2018 Christoph Hohmann <reboot@gmx.ch>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.reboot.trakt.api.client;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A client component for shared functionality used by the different components
 * of the API.
 */
class TraktClientSupport {

    /**
     * The base URL of the Trakt API.
     */
    private static final String API_URL_BASE = "https://api.trakt.tv";

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(TraktClientSupport.class);

    /**
     * The client id.
     */
    private final String clientId;

    /**
     * The client secret.
     */
    private final String clientSecret;

    /**
     * The object mapper.
     */
    private ObjectMapper objectMapper;

    /**
     * The access token.
     */
    private String accessToken;

    /**
     * Constructs a client support with a client id and client secret.
     *
     * @param clientId The client id.
     * @param clientSecret The client secret.
     */
    TraktClientSupport(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        objectMapper = new ObjectMapper()
                .disable(FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(WRITE_DATES_AS_TIMESTAMPS)
                .enable(INDENT_OUTPUT);
    }

    /**
     * Gets the client id.
     *
     * @return The client id.
     */
    String getClientId() {
        return clientId;
    }

    /**
     * Gets the client secret.
     *
     * @return The client secret.
     */
    String getClientSecret() {
        return clientSecret;
    }

    /**
     * Sets the access token.
     *
     * @param accessToken The access token.
     */
    void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Clears the access token.
     */
    void clearAccessToken() {
        this.accessToken = null;
    }

    /**
     * Gets the object mapper.
     *
     * @return The object mapper.
     */
    ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Perform a request that does not need authentication.
     *
     * @param call The call with path and parameters.
     * @param transformer The response transformer for the request.
     * @param request An optional request object for POST requests.
     * @return The response created by the response transformer.
     * @throws TraktClientException If an error occurs during the request.
     */
    <T> T doUnauthenticatedRequest(TraktClientCall call, ResponseTransformer<T> transformer, Object request) throws TraktClientException {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");

        return doRequest(call, headers, transformer, request);
    }

    /**
     * Perform a request that does need authentication. All authenticated
     * requests will be processed by the {@link DefaultResponseCodeTransformer}
     * in addition to the provided transformer to handle HTTP error codes.
     *
     * @param call The call with path and parameters.
     * @param transformer The response transformer for the request.
     * @param request An optional request object for POST requests.
     * @return The response created by the response transformer.
     * @throws TraktClientException If an error occurs during the request.
     */
    <T> T doAuthenticatedRequest(TraktClientCall call, ResponseTransformer<T> transformer, Object request) throws TraktClientException {
        if (StringUtils.isBlank(accessToken)) {
            throw new NoAccessTokenException("No access token available to access API method");
        }

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("trakt-api-version", "2");
        headers.put("trakt-api-key", clientId);

        return doRequest(call, headers, new DefaultResponseCodeTransformer<T>(transformer), request);
    }

    /**
     * Perform an API request.
     *
     * @param call The call with path and parameters.
     * @param headers The HTTP headers that should be added to the request.
     * @param transformer The response transformer for the request.
     * @param request An optional request object for POST requests.
     * @return The response created by the response transformer.
     * @throws TraktClientException If an error occurs during the request.
     */
    private <T> T doRequest(TraktClientCall call, Map<String, String> headers, ResponseTransformer<T> transformer, Object request) throws TraktClientException {
        URL url;
        try {
            url = new URL(API_URL_BASE + call.toFile());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        logger.debug("Request URL: {}", url);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.addRequestProperty(header.getKey(), header.getValue());
            }

            if (request != null) {
                connection.setDoOutput(true);
                try (OutputStream outputStream = createOutputStream(connection)) {
                    objectMapper.writeValue(outputStream, request);
                }
            }

            T result = transformer.transform(connection);

            return result;
        } catch (IOException e) {
            throw new RequestFailedException("Requested failed due to an unexpected error", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Creates an output stream for the HTTP connection. If debug logging is
     * enabled for the client then the raw request will be logged.
     *
     * @param connection The HTTP connection.
     * @return The output stream for the HTTP connection.
     * @throws IOException If an error occurs opening the output stream.
     */
    private OutputStream createOutputStream(HttpURLConnection connection) throws IOException {
        OutputStream outputStream;
        outputStream = connection.getOutputStream();
        if (logger.isDebugEnabled()) {
            ByteArrayOutputStream branch = new ByteArrayOutputStream() {

                @Override
                public void close() throws IOException {
                    if (size() == 0) {
                        return;
                    }
                    String request = new String(toByteArray());
                    request = request.replace(clientId, "<hidden>");
                    request = request.replace(clientSecret, "<hidden>");
                    logger.debug("Request: {}", request);
                    reset();
                }

            };
            outputStream = new TeeOutputStream(outputStream, branch);
        }
        return outputStream;
    }

}
