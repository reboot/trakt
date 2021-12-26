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
package io.github.reboot.trakt.api.json.users;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsResponse {

    private User user;

    private Account account;

    private Connections connections;

    private SharingText sharingText;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

    @JsonProperty("sharing_text")
    public SharingText getSharingText() {
        return sharingText;
    }

    public void setSharingText(SharingText sharingText) {
        this.sharingText = sharingText;
    }

    public static class User {

        private String username;

        private boolean _private;

        private String name;

        private boolean vip;

        private boolean vipEp;

        private Ids ids;

        private Date joinedAt;

        private String location;

        private String about;

        private String gender;

        private Integer age;

        private Images images;

        private boolean vipOg;

        private int vipYears;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isPrivate() {
            return _private;
        }

        public void setPrivate(boolean _private) {
            this._private = _private;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isVip() {
            return vip;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        @JsonProperty("vip_ep")
        public boolean isVipEp() {
            return vipEp;
        }

        public void setVipEp(boolean vipEp) {
            this.vipEp = vipEp;
        }

        public Ids getIds() {
            return ids;
        }

        public void setIds(Ids ids) {
            this.ids = ids;
        }

        @JsonProperty("joined_at")
        public Date getJoinedAt() {
            return joinedAt;
        }

        public void setJoinedAt(Date joinedAt) {
            this.joinedAt = joinedAt;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        @JsonProperty("vip_og")
        public boolean isVipOg() {
            return vipOg;
        }

        public void setVipOg(boolean vipOg) {
            this.vipOg = vipOg;
        }

        @JsonProperty("vip_years")
        public int getVipYears() {
            return vipYears;
        }

        public void setVipYears(int vipYears) {
            this.vipYears = vipYears;
        }

        public static class Ids {

            private String slug;

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

        }

        public static class Images {

            private Avatar avatar;

            public Avatar getAvatar() {
                return avatar;
            }

            public void setAvatar(Avatar avatar) {
                this.avatar = avatar;
            }

            public static class Avatar {

                private String full;

                public String getFull() {
                    return full;
                }

                public void setFull(String full) {
                    this.full = full;
                }

            }

        }

    }

    public static class Account {

        private String timezone;

        private boolean time24hr;

        private String coverImage;

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        @JsonProperty("time_24hr")
        public boolean isTime24hr() {
            return time24hr;
        }

        public void setTime24hr(boolean time24hr) {
            this.time24hr = time24hr;
        }

        @JsonProperty("cover_image")
        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

    }

    public static class Connections {

        private boolean facebook;

        private boolean twitter;

        private boolean google;

        private boolean tumblr;

        private boolean medium;

        private boolean slack;

        public boolean isFacebook() {
            return facebook;
        }

        public void setFacebook(boolean facebook) {
            this.facebook = facebook;
        }

        public boolean isTwitter() {
            return twitter;
        }

        public void setTwitter(boolean twitter) {
            this.twitter = twitter;
        }

        public boolean isGoogle() {
            return google;
        }

        public void setGoogle(boolean google) {
            this.google = google;
        }

        public boolean isTumblr() {
            return tumblr;
        }

        public void setTumblr(boolean tumblr) {
            this.tumblr = tumblr;
        }

        public boolean isMedium() {
            return medium;
        }

        public void setMedium(boolean medium) {
            this.medium = medium;
        }

        public boolean isSlack() {
            return slack;
        }

        public void setSlack(boolean slack) {
            this.slack = slack;
        }

    }

    public static class SharingText {

        private String watching;

        private String watched;

        public String getWatching() {
            return watching;
        }

        public void setWatching(String watching) {
            this.watching = watching;
        }

        public String getWatched() {
            return watched;
        }

        public void setWatched(String watched) {
            this.watched = watched;
        }

    }

}
