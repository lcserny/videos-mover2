package net.cserny.videosmover.core.model;

import net.cserny.videosmover.core.constants.MessageType;

public class Message {

    private final MessageType messageType;
    private final String content;
    private final String title;

    public Message(MessageType messageType, String content, String title) {
        this.messageType = messageType;
        this.content = content;
        this.title = title;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message that = (Message) o;

        if (messageType != that.messageType) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return title != null ? title.equals(that.title) : that.title == null;
    }

    @Override
    public int hashCode() {
        int result = messageType != null ? messageType.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
