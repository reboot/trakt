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
import io.github.reboot.trakt.api.json.oauth.device.CodeRequest;
import io.github.reboot.trakt.api.json.oauth.device.CodeResponse;
import io.github.reboot.trakt.api.json.oauth.device.TokenRequest;
import io.github.reboot.trakt.api.json.oauth.device.TokenResponse;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The client component for the OAuth client API.
 */
public class TraktClientOAuthDevice {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(TraktClientOAuthDevice.class);

    /**
     * The client support.
     */
    private final TraktClientSupport support;

    /**
     * Constructs a client component for the OAuth device API.
     *
     * @param support The client support.
     */
    TraktClientOAuthDevice(TraktClientSupport support) {
        this.support = support;
    }

    /**
     * Performs a device code request to start a device authentication process.
     *
     * @return The code response.
     * @throws TraktClientException If an error occurs during the request.
     */
    public CodeResponse code() throws TraktClientException {
        CodeRequest request = new CodeRequest();
        request.setClientId(support.getClientId());
        return support.doAuthRequest("/oauth/device/code", new BasicResponseTransformer<CodeResponse>(support, CodeResponse.class), request);
    }

    /**
     * Performs a device token request to retrieve the OAuth token for a device
     * authentication process.
     *
     * @param code The code from {@link CodeResponse#getDeviceCode()} returned
     *        by {@link #code()}.
     * @return The device token response or {@code null} if the user still needs
     *         to authenticate the access.
     * @throws TraktClientException If an error occurs during the request.
     */
    public TokenResponse token(String code) throws TraktClientException {
        TokenRequest request = new TokenRequest();
        request.setCode(code);
        request.setClientId(support.getClientId());
        request.setClientSecret(support.getClientSecret());
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
                case 400:
                    return null;
                case 404:
                    throw new TokenRequestFailedException("Invalid device code");
                case 409:
                    throw new TokenRequestFailedException("User already approved this code");
                case 410:
                    throw new TokenRequestFailedException("The tokens have expired, restart the process");
                case 418:
                    throw new TokenRequestFailedException("User explicitly denied this code");
                case 429:
                    throw new TokenRequestFailedException("Your app is polling too quickly");
                default:
                    throw new RequestFailedException(
                            format("Request failed with unexpected HTTP response code {0}: {1}", responseCode, connection.getResponseMessage()));
                }

                return delegate.transform(connection);
            }

        };
        return support.doAuthRequest("/oauth/device/token", responseTransformer, request);
    }

}
