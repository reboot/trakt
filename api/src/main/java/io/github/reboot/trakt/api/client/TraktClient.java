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

import io.github.reboot.trakt.api.json.SearchResponse;
import io.github.reboot.trakt.api.json.SearchResult;

import java.util.Collection;

/**
 * A client for the Trakt API.
 */
public class TraktClient {

    /**
     * The client support.
     */
    private final TraktClientSupport support;

    /**
     * The client component for the OAuth API.
     */
    private final TraktClientOAuth oauth;

    /**
     * The client component for the users API.
     */
    private final TraktClientUsers users;

    /**
     * The client component for the sync API.
     */
    private final TraktClientSync sync;

    /**
     * Constructs a Trakt client with a client id and client secret.
     *
     * @param clientId The client id.
     * @param clientSecret The client secret.
     */
    public TraktClient(String clientId, String clientSecret) {
        support = new TraktClientSupport(clientId, clientSecret);
        oauth = new TraktClientOAuth(support);
        users = new TraktClientUsers(support);
        sync = new TraktClientSync(support);
    }

    /**
     * Sets the access token.
     *
     * @param accessToken The access token.
     */
    public void setAccessToken(String accessToken) {
        support.setAccessToken(accessToken);
    }

    /**
     * Clears the access token.
     */
    public void clearAccessToken() {
        support.clearAccessToken();
    }

    /**
     * Access the client component for the OAuth API.
     *
     * @return The client component for the OAuth API.
     */
    public TraktClientOAuth oauth() {
        return oauth;
    }

    /**
     * Access the client component for the users API.
     *
     * @returns The client component for the users API.
     */
    public TraktClientUsers users() {
        return users;
    }

    /**
     * Access the client component for the sync API.
     *
     * @return The client component for the sync API.
     */
    public TraktClientSync sync() {
        return sync;
    }

    /**
     * Performs a search for media objects with the specified types.
     *
     * @param types The media object types to return.
     * @param parameters Additional request parameters for the request.
     * @return The search result.
     * @throws TraktClientException If an error occurs during the request.
     */
    public Paginated<SearchResponse> search(Collection<SearchResult.Type> types, RequestParameter... parameters) throws TraktClientException {
        TraktClientCall call = new TraktClientCall("/search/");
        boolean firstType = true;
        for (SearchResult.Type type : types) {
            if (!firstType) {
                call.append(",");
            } else {
                firstType = false;
            }
            switch (type) {
            case MOVIE:
                call.append("movie");
                break;
            case SHOW:
                call.append("show");
                break;
            case EPISODE:
                call.append("episode");
                break;
            default:
                throw new AssertionError(type);
            }
        }
        call.appendRequestParameters(parameters);
        return support.doAuthenticatedRequest(call, new PaginatedResponseTransformer<>(support, SearchResponse.class), null);
    }

}
