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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.commons.io.input.TeeInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A response transformer that transforms the connections input stream into an
 * object using the {@link ObjectMapper} from the {@link TraktClientSupport}.
 *
 * @param <T> The response type.
 */
class BasicResponseTransformer<T> implements ResponseTransformer<T> {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(BasicResponseTransformer.class);

    /**
     * The client support.
     */
    private final TraktClientSupport support;

    /**
     * The class object for the response type.
     */
    private final Class<T> responseType;

    /**
     * Constructs a basic response transformer.
     *
     * @param support The client support.
     * @param responseType The class object for the result type.
     */
    BasicResponseTransformer(TraktClientSupport support, Class<T> responseType) {
        this.support = support;
        this.responseType = responseType;
    }

    @Override
    public T transform(HttpURLConnection connection) throws IOException {
        ObjectMapper objectMapper = support.getObjectMapper();

        try (InputStream inputStream = createInputStream(connection)) {
            return objectMapper.readValue(inputStream, responseType);
        }
    }

    /**
     * Creates an input stream for the HTTP connection. If debug logging is
     * enabled for the client then the raw response will be logged.
     *
     * @param connection The HTTP connection.
     * @return The input stream for the HTTP connection.
     * @throws IOException If an error occurs opening the input stream.
     */
    private InputStream createInputStream(HttpURLConnection connection) throws IOException {
        InputStream inputStream;
        inputStream = connection.getInputStream();
        if (logger.isDebugEnabled()) {
            ByteArrayOutputStream branch = new ByteArrayOutputStream() {

                @Override
                public void close() throws IOException {
                    if (size() == 0) {
                        return;
                    }
                    logger.debug("Response: {}", new String(toByteArray()));
                    reset();
                }

            };
            inputStream = new TeeInputStream(inputStream, branch, true);
        }
        return inputStream;
    }

}
