package net.cserny.videosMover.service.validator;

import net.cserny.videosMover.model.Video;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by leonardo on 12.09.2017.
 */
@Service
public class RemovePathRestriction implements RemovalRestriction
{
    @Value("#{'${restricted.remove.paths}'.split(',')}")
    private List<String> restrictedFolders;

    @Override
    public boolean isRestricted(Video video) {
        boolean restricted = false;
        for (String restrictedFolder : restrictedFolders) {
            if (video.getInput().getParent().getFileName().toString().equals(restrictedFolder)) {
                restricted = true;
                break;
            }
        }
        return restricted;
    }
}
