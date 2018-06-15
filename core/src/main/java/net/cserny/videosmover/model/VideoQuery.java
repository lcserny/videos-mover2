package net.cserny.videosmover.model;

import java.util.Objects;

public class VideoQuery {

    private String name;
    private Integer year;
    private String language;

    private VideoQuery(Builder builder) {
        this.name = builder.name;
        this.year = builder.year;
        this.language = builder.language;
    }

    public static Builder newInstance() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoQuery that = (VideoQuery) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(year, that.year) &&
                Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, year, language);
    }

    public static class Builder {
        private String name;
        private Integer year;
        private String language;

        public Builder withName(String name) {
            if (name != null && !name.isEmpty()) {
                this.name = name;
            }
            return this;
        }

        public Builder withYear(Integer year) {
            if (year != null) {
                this.year = year;
            }
            return this;
        }

        public Builder withLanguage(String language) {
            if (language != null && !language.isEmpty()) {
                this.language = language;
            }
            return this;
        }

        public VideoQuery build() {
            return new VideoQuery(this);
        }
    }
}
