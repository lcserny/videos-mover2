package net.cserny.videosmover.javafx.provider;

import javafx.scene.control.Alert;
import net.cserny.videosmover.core.constants.MessageType;

import java.util.HashMap;
import java.util.Map;

public class StaticMessageAlertTypeConverter {

    private static Map<MessageType, Alert.AlertType> messageTypeAlertTypeMap;

    static {
        messageTypeAlertTypeMap = initMessageTypeAlertTypeMap();
    }

    private static Map<MessageType, Alert.AlertType> initMessageTypeAlertTypeMap() {
        Map<MessageType, Alert.AlertType> map = new HashMap<>();
        map.put(MessageType.ERROR, Alert.AlertType.ERROR);
        map.put(MessageType.INFO, Alert.AlertType.INFORMATION);
        map.put(MessageType.WARNING, Alert.AlertType.WARNING);
        return map;
    }

    public static Alert.AlertType convert(MessageType messageType) {
        return messageTypeAlertTypeMap.get(messageType);
    }
}
