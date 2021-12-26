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
package io.github.reboot.trakt.database;

import static io.github.reboot.trakt.database.Utils.updateIndices;
import static java.util.Collections.unmodifiableMap;
import static org.dizitart.no2.Document.createDocument;
import static org.dizitart.no2.FindOptions.sort;
import static org.dizitart.no2.IndexOptions.indexOptions;
import io.github.reboot.trakt.api.json.sync.HistoryItem;

import java.util.HashMap;
import java.util.Map;

import org.dizitart.no2.Document;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.IndexOptions;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.SortOrder;
import org.dizitart.no2.event.ChangeListener;
import org.dizitart.no2.filters.Filters;
import org.dizitart.no2.objects.ObjectFilter;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;

public class TraktDatabase {

    private static final Map<String, IndexOptions> METADATA_INDICES;
    static {
        Map<String, IndexOptions> metadataIndices = new HashMap<>();
        metadataIndices.put("key", indexOptions(IndexType.Unique));
        METADATA_INDICES = unmodifiableMap(metadataIndices);
    }

    private static final Map<String, IndexOptions> HISTORY_ITEM_INDICES;
    static {
        Map<String, IndexOptions> historyItemIndices = new HashMap<>();
        historyItemIndices.put("id", indexOptions(IndexType.Unique));
        historyItemIndices.put("movie.title", indexOptions(IndexType.NonUnique));
        historyItemIndices.put("show.title", indexOptions(IndexType.NonUnique));
        historyItemIndices.put("episode.title", indexOptions(IndexType.Fulltext));
        historyItemIndices.put("movie.ids.trakt", indexOptions(IndexType.NonUnique));
        historyItemIndices.put("show.ids.trakt", indexOptions(IndexType.NonUnique));
        historyItemIndices.put("episode.ids.trakt", indexOptions(IndexType.NonUnique));
        HISTORY_ITEM_INDICES = unmodifiableMap(historyItemIndices);
    }

    private final NitriteCollection metadataCollection;

    private final ObjectRepository<HistoryItem> historyItemRepository;

    public TraktDatabase(Nitrite database) {
        database.getCollection("io.github.reboot.trakt.api.json.HistoryItem").drop();
        database.commit();

        metadataCollection = database.getCollection("metadata");
        updateIndices(metadataCollection, METADATA_INDICES);
        historyItemRepository = database.getRepository(HistoryItem.class);
        updateIndices(historyItemRepository, HISTORY_ITEM_INDICES);
    }

    public void register(Class<?> itemClass, ChangeListener changeListener) {
        if (HistoryItem.class.equals(itemClass)) {
            historyItemRepository.register(changeListener);
        }
    }

    public void deregister(Class<?> itemClass, ChangeListener changeListener) {
        if (HistoryItem.class.equals(itemClass)) {
            historyItemRepository.deregister(changeListener);
        }
    }

    public void close() {
        metadataCollection.close();
        historyItemRepository.close();
    }

    public void setMetadata(String key, Object value) {
        Document document = metadataCollection
                .find(Filters.eq("key", key))
                .firstOrDefault();
        if (document != null) {
            document.put("value", value);
            metadataCollection.update(document);
        } else {
            document = createDocument("key", key);
            document.put("value", value);
            metadataCollection.insert(document);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getMetadata(String key) {
        Document document = metadataCollection
                .find(Filters.eq("key", key))
                .firstOrDefault();
        if (document == null) {
            return null;
        }
        return (T) document.get("value");
    }

    public void updateHistoryItem(HistoryItem historyItem) {
        historyItemRepository.update(ObjectFilters.eq("id", historyItem.getId()), historyItem, true);
    }

    public Iterable<HistoryItem> findHistoryItems(ObjectFilter filter) {
        return findHistoryItems(filter, sort("watched_at", SortOrder.Descending));
    }

    public Iterable<HistoryItem> findHistoryItems(ObjectFilter filter, FindOptions findOptions) {
        return historyItemRepository.find(filter, findOptions);
    }

}
