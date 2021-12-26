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

import io.github.reboot.trakt.api.json.sync.HistoryItem;
import io.github.reboot.trakt.api.json.sync.HistoryResponse;
import io.github.reboot.trakt.api.json.sync.LastActivitiesResponse;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TraktClientSync {

    private TraktClientSupport support;

    TraktClientSync(TraktClientSupport support) {
        this.support = support;
    }

    public LastActivitiesResponse lastActivities() throws TraktClientException {
        TraktClientCall call = new TraktClientCall("/sync/last_activities");
        return support.doAuthenticatedRequest(call, new BasicResponseTransformer<>(support, LastActivitiesResponse.class), null);
    }

    public Paginated<HistoryResponse> history(RequestParameter... parameters) throws TraktClientException {
        return history(null, null, null, null, parameters);
    }

    public Paginated<HistoryResponse> history(HistoryItem.Type type, Date startAt, Date endAt, RequestParameter... parameters) throws TraktClientException {
        return history(type, null, startAt, endAt, parameters);
    }

    public Paginated<HistoryResponse> history(HistoryItem.Type type, Long id, Date startAt, Date endAt, RequestParameter... parameters)
            throws TraktClientException {
        ObjectMapper objectMapper = support.getObjectMapper();

        TraktClientCall call = new TraktClientCall("/sync/history");
        if (type != null) {
            String typeValue;
            switch (type) {
            case EPISODE:
                typeValue = "episodes";
                break;
            case MOVIE:
                typeValue = "movies";
                break;
            default:
                throw new AssertionError(type);
            }
            call.append("/");
            call.append(typeValue);
            if (id != null) {
                call.append("/");
                call.append(Long.toString(id));
            }
        }
        if (startAt != null) {
            String value = objectMapper.getDateFormat().format(startAt);
            call.appendQueryParameter("start_at", value);
        }
        if (endAt != null) {
            String value = objectMapper.getDateFormat().format(endAt);
            call.appendQueryParameter("end_at", value);
        }
        call.appendRequestParameters(parameters);
        return support.doAuthenticatedRequest(call, new PaginatedResponseTransformer<>(support, HistoryResponse.class), null);
    }

}
