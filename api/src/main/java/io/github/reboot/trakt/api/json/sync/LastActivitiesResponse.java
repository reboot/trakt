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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastActivitiesResponse {

    private Date all;

    private Movies movies;

    private Episodes episodes;

    private Shows shows;

    private Seasons seasons;

    private Comments comments;

    private Lists lists;

    public Date getAll() {
        return all;
    }

    public void setAll(Date all) {
        this.all = all;
    }

    public Movies getMovies() {
        return movies;
    }

    public void setMovies(Movies movies) {
        this.movies = movies;
    }

    public Episodes getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Episodes episodes) {
        this.episodes = episodes;
    }

    public Shows getShows() {
        return shows;
    }

    public void setShows(Shows shows) {
        this.shows = shows;
    }

    public Seasons getSeasons() {
        return seasons;
    }

    public void setSeasons(Seasons seasons) {
        this.seasons = seasons;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public Lists getLists() {
        return lists;
    }

    public void setLists(Lists lists) {
        this.lists = lists;
    }

    public static class Movies {

        private Date watchedAt;

        private Date collectedAt;

        private Date ratedAt;

        private Date watchlistedAt;

        private Date commentedAt;

        private Date pausedAt;

        private Date hiddenAt;

        @JsonProperty("watched_at")
        public Date getWatchedAt() {
            return watchedAt;
        }

        public void setWatchedAt(Date watchedAt) {
            this.watchedAt = watchedAt;
        }

        @JsonProperty("collected_at")
        public Date getCollectedAt() {
            return collectedAt;
        }

        public void setCollectedAt(Date collectedAt) {
            this.collectedAt = collectedAt;
        }

        @JsonProperty("rated_at")
        public Date getRatedAt() {
            return ratedAt;
        }

        public void setRatedAt(Date ratedAt) {
            this.ratedAt = ratedAt;
        }

        @JsonProperty("watchlisted_at")
        public Date getWatchlistedAt() {
            return watchlistedAt;
        }

        public void setWatchlistedAt(Date watchlistedAt) {
            this.watchlistedAt = watchlistedAt;
        }

        @JsonProperty("commented_at")
        public Date getCommentedAt() {
            return commentedAt;
        }

        public void setCommentedAt(Date commentedAt) {
            this.commentedAt = commentedAt;
        }

        @JsonProperty("paused_at")
        public Date getPausedAt() {
            return pausedAt;
        }

        public void setPausedAt(Date pausedAt) {
            this.pausedAt = pausedAt;
        }

        @JsonProperty("hidden_at")
        public Date getHiddenAt() {
            return hiddenAt;
        }

        public void setHiddenAt(Date hiddenAt) {
            this.hiddenAt = hiddenAt;
        }

    }

    public static class Episodes {

        private Date watchedAt;

        private Date collectedAt;

        private Date ratedAt;

        private Date watchlistedAt;

        private Date commentedAt;

        private Date pausedAt;

        @JsonProperty("watched_at")
        public Date getWatchedAt() {
            return watchedAt;
        }

        public void setWatchedAt(Date watchedAt) {
            this.watchedAt = watchedAt;
        }

        @JsonProperty("collected_at")
        public Date getCollectedAt() {
            return collectedAt;
        }

        public void setCollectedAt(Date collectedAt) {
            this.collectedAt = collectedAt;
        }

        @JsonProperty("rated_at")
        public Date getRatedAt() {
            return ratedAt;
        }

        public void setRatedAt(Date ratedAt) {
            this.ratedAt = ratedAt;
        }

        @JsonProperty("watchlisted_at")
        public Date getWatchlistedAt() {
            return watchlistedAt;
        }

        public void setWatchlistedAt(Date watchlistedAt) {
            this.watchlistedAt = watchlistedAt;
        }

        @JsonProperty("commented_at")
        public Date getCommentedAt() {
            return commentedAt;
        }

        public void setCommentedAt(Date commentedAt) {
            this.commentedAt = commentedAt;
        }

        @JsonProperty("paused_at")
        public Date getPausedAt() {
            return pausedAt;
        }

        public void setPausedAt(Date pausedAt) {
            this.pausedAt = pausedAt;
        }

    }

    public static class Shows {

        private Date ratedAt;

        private Date watchlistedAt;

        private Date commentedAt;

        private Date hiddenAt;

        @JsonProperty("rated_at")
        public Date getRatedAt() {
            return ratedAt;
        }

        public void setRatedAt(Date ratedAt) {
            this.ratedAt = ratedAt;
        }

        @JsonProperty("watchlisted_at")
        public Date getWatchlistedAt() {
            return watchlistedAt;
        }

        public void setWatchlistedAt(Date watchlistedAt) {
            this.watchlistedAt = watchlistedAt;
        }

        @JsonProperty("commented_at")
        public Date getCommentedAt() {
            return commentedAt;
        }

        public void setCommentedAt(Date commentedAt) {
            this.commentedAt = commentedAt;
        }

        @JsonProperty("hidden_at")
        public Date getHiddenAt() {
            return hiddenAt;
        }

        public void setHiddenAt(Date hiddenAt) {
            this.hiddenAt = hiddenAt;
        }

    }

    public static class Seasons {

        private Date ratedAt;

        private Date watchlistedAt;

        private Date commentedAt;

        private Date hiddenAt;

        @JsonProperty("rated_at")
        public Date getRatedAt() {
            return ratedAt;
        }

        public void setRatedAt(Date ratedAt) {
            this.ratedAt = ratedAt;
        }

        @JsonProperty("watchlisted_at")
        public Date getWatchlistedAt() {
            return watchlistedAt;
        }

        public void setWatchlistedAt(Date watchlistedAt) {
            this.watchlistedAt = watchlistedAt;
        }

        @JsonProperty("commented_at")
        public Date getCommentedAt() {
            return commentedAt;
        }

        public void setCommentedAt(Date commentedAt) {
            this.commentedAt = commentedAt;
        }

        @JsonProperty("hidden_at")
        public Date getHiddenAt() {
            return hiddenAt;
        }

        public void setHiddenAt(Date hiddenAt) {
            this.hiddenAt = hiddenAt;
        }

    }

    public static class Comments {

        private Date likedAt;

        @JsonProperty("liked_at")
        public Date getLikedAt() {
            return likedAt;
        }

        public void setLikedAt(Date likedAt) {
            this.likedAt = likedAt;
        }

    }

    public static class Lists {

        private Date likedAt;

        private Date updatedAt;

        private Date commentedAt;

        @JsonProperty("liked_at")
        public Date getLikedAt() {
            return likedAt;
        }

        public void setLikedAt(Date likedAt) {
            this.likedAt = likedAt;
        }

        @JsonProperty("updated_at")
        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        @JsonProperty("commented_at")
        public Date getCommentedAt() {
            return commentedAt;
        }

        public void setCommentedAt(Date commentedAt) {
            this.commentedAt = commentedAt;
        }

    }

}
