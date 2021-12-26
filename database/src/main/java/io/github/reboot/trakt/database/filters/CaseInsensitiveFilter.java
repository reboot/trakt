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
package io.github.reboot.trakt.database.filters;

import static org.dizitart.no2.exceptions.ErrorCodes.FE_REGEX_NO_STRING_VALUE;
import static org.dizitart.no2.exceptions.ErrorCodes.IE_TEXT_FILTER_FIELD_NOT_INDEXED;
import static org.dizitart.no2.exceptions.ErrorMessage.errorMessage;
import static org.dizitart.no2.util.DocumentUtils.getFieldValue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.exceptions.FilterException;
import org.dizitart.no2.exceptions.IndexingException;
import org.dizitart.no2.filters.BaseFilter;
import org.dizitart.no2.store.NitriteMap;

public class CaseInsensitiveFilter extends BaseFilter {

    private String field;

    private String value;

    CaseInsensitiveFilter(String field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Set<NitriteId> apply(NitriteMap<NitriteId, Document> documentMap) {
        if (!nitriteService.hasIndex(field)
                || nitriteService.isIndexing(field)) {
            throw new IndexingException(errorMessage(field + " is not indexed",
                    IE_TEXT_FILTER_FIELD_NOT_INDEXED));
        }

        Set<NitriteId> ids = nitriteService.findTextWithIndex(field, value);
        return matchedSet(documentMap, ids);
    }

    private Set<NitriteId> matchedSet(NitriteMap<NitriteId, Document> documentMap, Set<NitriteId> ids) {
        Set<NitriteId> nitriteIdSet = new LinkedHashSet<>();

        for (NitriteId id : ids) {
            Document document = documentMap.get(id);
            Object fieldValue = getFieldValue(document, field);
            if (fieldValue == null) {
                continue;
            }
            if (!(fieldValue instanceof String)) {
                throw new FilterException(errorMessage(
                        field + " does not contain string value.",
                        FE_REGEX_NO_STRING_VALUE));
            }
            String string = (String) fieldValue;
            if (!string.equalsIgnoreCase(value)) {
                continue;
            }
            nitriteIdSet.add(id);
        }
        return nitriteIdSet;
    }

    @Override
    public String toString() {
        return "CaseInsensitiveFilter(field=" + field + ", value=" + value + ")";
    }

}
