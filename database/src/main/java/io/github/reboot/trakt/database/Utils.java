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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dizitart.no2.Index;
import org.dizitart.no2.IndexOptions;
import org.dizitart.no2.PersistentCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    static void updateIndices(PersistentCollection<?> persistentCollection, Map<String, IndexOptions> indices) {
        Set<String> fields = new HashSet<>(indices.keySet());
        for (Index index : persistentCollection.listIndices()) {
            logger.debug("Found index: {}", index);

            String field = index.getField();

            boolean dropIndex = false;
            if (indices.containsKey(field)) {
                IndexOptions indexOptions = indices.get(field);

                dropIndex |= index.getIndexType() != indexOptions.getIndexType();
            } else {
                dropIndex = true;
            }

            if (dropIndex) {
                logger.info("Dropping index on field {}", field);
                persistentCollection.dropIndex(field);
            } else {
                // persistentCollection.rebuildIndex(field, false);

                fields.remove(field);
            }
        }
        for (String field : fields) {
            IndexOptions indexOptions = indices.get(field);

            logger.info("Creating index on field {} with options {}", field, indexOptions);
            persistentCollection.createIndex(field, indexOptions);
        }
    }

}
