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
import io.github.reboot.trakt.api.json.oauth.TokenRequest;
import io.github.reboot.trakt.api.json.oauth.TokenRequest.GrantType;
import io.github.reboot.trakt.api.json.oauth.TokenResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The client component for the OAuth API.
 */
public class TraktClientOAuth {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(TraktClientOAuth.class);

    /**
     * The client support.
     */
    private final TraktClientSupport support;

    /**
     * The client component for the OAuth device API.
     */
    private final TraktClientOAuthDevice device;

    /**
     * Constructs a client component for the OAuth API.
     *
     * @param support The client support.
     */
    TraktClientOAuth(TraktClientSupport support) {
        this.support = support;

        device = new TraktClientOAuthDevice(support);
    }

    /**
     * Access the client component for the OAuth device API.
     *
     * @return The client component for the OAuth device API.
     */
    public TraktClientOAuthDevice device() {
        return device;
    }

    /**
     * Performs a token request. The grant type specifies if a new OAuth token
     * is requested or if an existing token should be renewed using the refresh
     * token.
     *
     * @param grantType The grant type for the request.
     * @param value The value put into the request field needed for the grant
     *        type. For the grant type {@link GrantType#AUTHORIZATION_CODE
     *        AUTHORIZATION_CODE} this has to be the OAuth code. For the grant
     *        type {@link GrantType#REFRESH_TOKEN REFRESH_TOKEN} this has to be
     *        the refresh token,
     * @param redirectURI The redirect URI.
     * @return The token response.
     * @throws TraktClientException If an error occurs during the request.
     */
    public TokenResponse token(GrantType grantType, String value, String redirectURI) throws TraktClientException {
        TokenRequest request = new TokenRequest();
        switch (grantType) {
        case AUTHORIZATION_CODE:
            request.setCode(value);
            break;
        case REFRESH_TOKEN:
            request.setRefreshToken(value);
            break;
        }
        request.setClientId(support.getClientId());
        request.setClientSecret(support.getClientSecret());
        request.setRedirectURI(redirectURI);
        request.setGrantType(grantType);
        ResponseTransformer<TokenResponse> responseTransformer = new ResponseTransformer<TokenResponse>() {

            /**
             * The delegate used to transform the token response.
             */
            private final ResponseTransformer<TokenResponse> delegate = new BasicResponseTransformer<TokenResponse>(support, TokenResponse.class);

            @Override
            public TokenResponse transform(HttpURLConnection connection) throws IOException, TraktClientException {
                int responseCode = connection.getResponseCode();
                logger.debug("Response code: {}", responseCode);
                switch (responseCode) {
                case 200:
                    break;
                case 401:
                    throw new UnauthorizedException("OAuth must be provided");
                default:
                    throw new RequestFailedException(
                            format("Request failed with unexpected HTTP response code {0}: {1}", responseCode, connection.getResponseMessage()));
                }

                return delegate.transform(connection);
            }

        };
        return support.doAuthRequest("/oauth/token", responseTransformer, request);
    }

}
