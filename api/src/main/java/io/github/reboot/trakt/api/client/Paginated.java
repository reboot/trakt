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
 * A wrapper for paginated responses. The wrapper contains the response for the
 * current page and information about the current page, item per page, total
 * number of pages, and total number of items.
 *
 * @param <T> The response type.
 */
public class Paginated<T> {

    /**
     * The response for the current page.
     */
    private final T response;

    /**
     * Current page.
     */
    private final int page;

    /**
     * Items per page.
     */
    private final int limit;

    /**
     * Total number of pages.
     */
    private final int pageCount;

    /**
     * Total number of items.
     */
    private final int itemCount;

    /**
     * Constructs a paginated response.
     *
     * @param response The response for the current page.
     * @param page The current page.
     * @param limit The items per page.
     * @param pageCount The total number of pages.
     * @param itemCount The total number of items.
     */
    Paginated(T response, int page, int limit, int pageCount, int itemCount) {
        this.response = response;
        this.page = page;
        this.limit = limit;
        this.pageCount = pageCount;
        this.itemCount = itemCount;
    }

    /**
     * Gets the response for the current page.
     *
     * @return The response.
     */
    public T getResponse() {
        return response;
    }

    /**
     * The current page.
     *
     * @return The current page.
     */
    public int getPage() {
        return page;
    }

    /**
     * Gets the items per page.
     *
     * @return The items per page.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the total number of pages.
     *
     * @return The total number of pages.
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Gets the total number of items.
     *
     * @return The total number of items.
     */
    public int getItemCount() {
        return itemCount;
    }

}
