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
package io.github.reboot.trakt.api.json.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaObject {

    private Ids ids;

    public Ids getIds() {
        return ids;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }

    public static class Ids {

        private String trakt;

        private String slug;

        private String imdb;

        private String tmdb;

        public String getTrakt() {
            return trakt;
        }

        public void setTrakt(String trakt) {
            this.trakt = trakt;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        @JsonProperty("imdb")
        public String getIMDB() {
            return imdb;
        }

        public void setIMDB(String imdb) {
            this.imdb = imdb;
        }

        @JsonProperty("tmdb")
        public String getTMDB() {
            return tmdb;
        }

        public void setTMDB(String tmdb) {
            this.tmdb = tmdb;
        }

    }

}
