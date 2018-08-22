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
 * The interface for response transformers.
 *
 * @param <T> The response type.
 */
interface ResponseTransformer<T> {

    /**
     * Transforms the HTTP connection into an response object.
     *
     * @param connection The HTTP connection.
     * @return The response object.
     * @throws IOException If any problems occur accessing the HTTP response
     *         from the connection.
     * @throws TraktClientException If the transformation fails for some reason.
     */
    T transform(HttpURLConnection connection) throws IOException, TraktClientException;

}
