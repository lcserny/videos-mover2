package net.cserny.videosMover.model;

import javafx.scene.control.Alert;

public class Message
{
    private Alert.AlertType alertType;
    private String content;
    private String title;

    public Message(Alert.AlertType alertType, String content, String title) {
        this.alertType = alertType;
        this.content = content;
        this.title = title;
    }

    public Alert.AlertType getAlertType() {
        return alertType;
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

        if (alertType != that.alertType) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return title != null ? title.equals(that.title) : that.title == null;
    }

    @Override
    public int hashCode() {
        int result = alertType != null ? alertType.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
