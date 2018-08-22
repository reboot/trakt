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

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A response transformer that checks the HTTP response code of an HTTP
 * connection and throws client exceptions for known error codes und a general
 * exception for unknown codes. If the status code does not indicate an error
 * the connection is passed to a delegate transformer for further processing.
 *
 * @param <T> The response type, if no error occurs.
 */
class DefaultResponseCodeTransformer<T> implements ResponseTransformer<T> {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(DefaultResponseCodeTransformer.class);

    /**
     * The delegate response transformer.
     */
    private final ResponseTransformer<T> delegate;

    /**
     * Constructs a default response code transformer with a delegate response
     * transformer.
     *
     * @param delegate The delegate response transformer.
     */
    DefaultResponseCodeTransformer(ResponseTransformer<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T transform(HttpURLConnection connection) throws IOException, TraktClientException {
        int responseCode = connection.getResponseCode();
        logger.debug("Response code: {}", responseCode);
        switch (responseCode) {
        case 200:
        case 201:
        case 204:
            break;
        case 401:
            throw new UnauthorizedException("OAuth must be provided");
        case 404:
            return null;
        case 409:
            throw new ConflictException("Resource already created");
        case 503:
        case 504:
            throw new ServerOverloadedException("Service Unavailable - server overloaded", 30);
        case 520:
        case 521:
        case 522:
            throw new ServiceUnavailableException("Service Unavailable - Cloudflare error");
        default:
            throw new RequestFailedException(
                    format("Request failed with unexpected HTTP response code {0}: {1}", responseCode, connection.getResponseMessage()));
        }

        return delegate.transform(connection);
    }

}
