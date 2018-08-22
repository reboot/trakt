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

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * A response transformer that extracts the pagination information from the HTTP
 * connection, parses the actual response using a delegate response transformer
 * and then wraps it into a {@link Paginated} object with the extracted
 * pagination information.
 *
 * @param <T> The response type.
 */
class PaginatedResponseTransformer<T> implements ResponseTransformer<Paginated<T>> {

    /**
     * The delegate response transformer.
     */
    private final ResponseTransformer<T> delegate;

    /**
     * Constructs a paginated response transformer with a delegate response
     * transformer.
     *
     * @param delegate The delegate.
     */
    PaginatedResponseTransformer(ResponseTransformer<T> delegate) {
        this.delegate = delegate;
    }

    /**
     * Constructs a paginated response transformer with a
     * {@link BasicResponseTransformer} for the specified response type as the
     * delegate response transformer.
     *
     * @param support The client support.
     * @param responseType The response type.
     */
    PaginatedResponseTransformer(TraktClientSupport support, Class<T> responseType) {
        this(new BasicResponseTransformer<>(support, responseType));
    }

    @Override
    public Paginated<T> transform(HttpURLConnection connection) throws IOException, TraktClientException {
        T response = delegate.transform(connection);
        if (response == null) {
            return null;
        }

        int page = connection.getHeaderFieldInt("X-Pagination-Page", -1);
        int limit = connection.getHeaderFieldInt("X-Pagination-Limit", -1);
        int pageCount = connection.getHeaderFieldInt("X-Pagination-Page-Count", -1);
        int itemCount = connection.getHeaderFieldInt("X-Pagination-Item-Count", -1);

        return new Paginated<T>(response, page, limit, pageCount, itemCount);
    }

}
