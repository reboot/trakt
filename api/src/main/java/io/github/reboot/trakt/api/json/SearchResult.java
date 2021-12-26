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
package io.github.reboot.trakt.api.json;

import io.github.reboot.trakt.api.json.common.Episode;
import io.github.reboot.trakt.api.json.common.Movie;
import io.github.reboot.trakt.api.json.common.Show;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {

    private Type type;

    private double score;

    private Movie movie;

    private Show show;

    private Episode episode;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public enum Type {

        @JsonProperty("movie")
        MOVIE,

        @JsonProperty("show")
        SHOW,

        @JsonProperty("episode")
        EPISODE,

        // @JsonProperty("person")
        // PERSON,
        //
        // @JsonProperty("list")
        // LIST,

    }

}
