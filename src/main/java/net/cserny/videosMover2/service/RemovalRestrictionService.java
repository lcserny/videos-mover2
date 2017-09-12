package net.cserny.videosMover2.service;

import java.util.List;

/**
 * Created by leonardo on 12.09.2017.
 */
public interface RemovalRestrictionService
{
    List<String> getRestrictedFolders();

    void addRestriction(String restriction);
}
