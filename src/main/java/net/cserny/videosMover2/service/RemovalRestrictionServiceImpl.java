package net.cserny.videosMover2.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by leonardo on 12.09.2017.
 */
@Service
public class RemovalRestrictionServiceImpl implements RemovalRestrictionService
{
    @Value("#{'${restricted.remove.paths}'.split(',')}")
    private List<String> restrictedFolders;

    @Override
    public List<String> getRestrictedFolders() {
        return restrictedFolders;
    }

    @Override
    public void addRestriction(String restriction) {
        restrictedFolders.add(restriction);
    }
}
