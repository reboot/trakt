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
package io.github.reboot.trakt.api.json.sync;

import io.github.reboot.trakt.api.json.common.Episode;
import io.github.reboot.trakt.api.json.common.Movie;
import io.github.reboot.trakt.api.json.common.Show;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryItem {

    private long id;

    private Date watchedAt;

    private Action action;

    private Type type;

    private Movie movie;

    private Episode episode;

    private Show show;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("watched_at")
    public Date getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(Date watchedAt) {
        this.watchedAt = watchedAt;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public enum Type {

        @JsonProperty("movie")
        MOVIE,

        @JsonProperty("show")
        SHOW,

        @JsonProperty("season")
        SEASON,

        @JsonProperty("episode")
        EPISODE,

    }

    public enum Action {

        @JsonProperty("scrobble")
        SCROBBLE,

        @JsonProperty("checkin")
        CHECKIN,

        @JsonProperty("watch")
        WATCH,

    }

}
