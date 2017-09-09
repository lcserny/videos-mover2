package net.cserny.videosMover2.dto;

import java.nio.file.Path;

/**
 * Created by leonardo on 09.09.2017.
 */
public class SimpleFile extends AbstractSimpleFile
{
    private SimpleFile(Builder builder) {
        this.path = builder.path;
        this.size = builder.size;
        this.type = builder.type;
    }

    public static class Builder
    {
        private final Path path;
        private long size;
        private String type;

        public Builder(Path path) {
            this.path = path;
        }

        public Builder withSize(long size) {
            this.size = size;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public SimpleFile build() {
            return new SimpleFile(this);
        }
    }
}
