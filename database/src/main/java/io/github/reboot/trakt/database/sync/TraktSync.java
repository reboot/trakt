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
package io.github.reboot.trakt.database.sync;

import static org.dizitart.no2.util.Iterables.firstOrDefault;
import io.github.reboot.trakt.api.client.Paginated;
import io.github.reboot.trakt.api.client.PaginationParameter;
import io.github.reboot.trakt.api.client.TraktClient;
import io.github.reboot.trakt.api.client.TraktClientException;
import io.github.reboot.trakt.api.json.sync.HistoryItem;
import io.github.reboot.trakt.api.json.sync.HistoryItem.Type;
import io.github.reboot.trakt.api.json.sync.HistoryResponse;
import io.github.reboot.trakt.api.json.sync.LastActivitiesResponse;
import io.github.reboot.trakt.database.TraktDatabase;

import java.util.Date;
import java.util.Objects;

import org.dizitart.no2.FindOptions;
import org.dizitart.no2.SortOrder;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraktSync {

    private final Logger logger = LoggerFactory.getLogger(TraktSync.class);

    private final TraktClient traktClient;

    private final TraktDatabase traktDatabase;

    public TraktSync(TraktClient traktClient, TraktDatabase traktDatabase) {
        this.traktClient = traktClient;
        this.traktDatabase = traktDatabase;
    }

    public void syncHistory() {
        Date episodesWatchedAt = traktDatabase.getMetadata("episodes.watchedAt");
        Date moviesWatchedAt = traktDatabase.getMetadata("movies.watchedAt");

        try {
            LastActivitiesResponse lastActivities = traktClient.sync().lastActivities();
            Date episodesWatchedAtLast = lastActivities.getEpisodes().getWatchedAt();
            Date moviesWatchedAtLast = lastActivities.getMovies().getWatchedAt();

            if (!Objects.equals(episodesWatchedAt, episodesWatchedAtLast)) {
                syncHistory(Type.EPISODE);

                traktDatabase.setMetadata("episodes.watchedAt", episodesWatchedAtLast);
            }
            if (!Objects.equals(moviesWatchedAt, moviesWatchedAtLast)) {
                syncHistory(Type.MOVIE);

                traktDatabase.setMetadata("movies.watchedAt", moviesWatchedAtLast);
            }
        } catch (TraktClientException e) {
            logger.error("Error fetching history", e);
        }
    }

    public void syncHistory(Type type) throws TraktClientException {
        Date startAt = getLatestWatchedAt(type);

        int page = 0;
        Paginated<HistoryResponse> history;
        do {
            page++;

            PaginationParameter paginationParameter;
            if (startAt != null) {
                paginationParameter = new PaginationParameter(page, null);
            } else {
                paginationParameter = new PaginationParameter(page, 1000);
            }

            logger.info("Fetching new {} history items starting from {}, page {}", type, startAt, page);
            history = traktClient.sync().history(type, startAt, null, paginationParameter);
            if (history.getPage() != page) {
                throw new RuntimeException("Got wrong page, requested " + page + " and got " + history.getPage());
            }
            for (HistoryItem historyItem : history.getResponse()) {
                logger.info("Adding history item {}", historyItem.getId());
                traktDatabase.updateHistoryItem(historyItem);
            }
        } while (page < history.getPageCount());
    }

    Date getLatestWatchedAt(Type type) {
        ObjectFilter filter = ObjectFilters.eq("type", type);
        FindOptions findOptions = FindOptions
                .sort("watched_at", SortOrder.Descending)
                .thenLimit(0, 1);
        HistoryItem historyItem = firstOrDefault(traktDatabase.findHistoryItems(filter, findOptions));
        if (historyItem == null) {
            return null;
        }
        return historyItem.getWatchedAt();
    }

}
