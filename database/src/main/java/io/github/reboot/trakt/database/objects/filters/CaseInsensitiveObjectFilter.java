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
package io.github.reboot.trakt.database.objects.filters;

import static io.github.reboot.trakt.database.filters.Filters.eqIgnoreCase;
import static org.dizitart.no2.util.ValidationUtils.validateSearchTerm;

import java.util.Set;

import org.dizitart.no2.Document;
import org.dizitart.no2.Filter;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.filters.BaseObjectFilter;
import org.dizitart.no2.store.NitriteMap;

public class CaseInsensitiveObjectFilter extends BaseObjectFilter {

    private String field;

    private String value;

    CaseInsensitiveObjectFilter(String field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Set<NitriteId> apply(NitriteMap<NitriteId, Document> documentMap) {
        validateSearchTerm(nitriteMapper, field, value);
        Filter filter = eqIgnoreCase(field, value);
        filter.setNitriteService(nitriteService);
        return filter.apply(documentMap);
    }

    @Override
    public String toString() {
        return "CaseInsensitiveObjectFilter(field=" + field + ", value=" + value + ")";
    }

}
