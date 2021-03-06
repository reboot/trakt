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

/**
 * Base exception class for all exceptions thrown by the Trakt client APIs.
 */
public abstract class TraktClientException extends Exception {

    /**
     * Default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a Trakt client exception with a message.
     *
     * @param message The message.
     */
    TraktClientException(String message) {
        super(message);
    }

    /**
     * Constructs a Trakt client exception with a message and an exception that
     * caused the failure.
     *
     * @param message The message.
     * @param cause The exception that caused the failure.
     */
    TraktClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
