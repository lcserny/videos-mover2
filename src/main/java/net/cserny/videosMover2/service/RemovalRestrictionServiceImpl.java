package net.cserny.videosMover2.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by leonardo on 12.09.2017.
 */
@Service
public class RemovalRestrictionServiceImpl extends AbstractResourceInitializer implements RemovalRestrictionService
{
    public static final String RESOURCE_REMOVE_RESTRICTIONS = "remove_restrictions.cfg";

    private List<String> restrictedFolders;

    public RemovalRestrictionServiceImpl() {
        this.restrictedFolders = fillListFromResource(RESOURCE_REMOVE_RESTRICTIONS);
    }

    @Override
    public List<String> getRestrictedFolders() {
        return restrictedFolders;
    }

    @Override
    public void addRestriction(String restriction) {
        restrictedFolders.add(restriction);
    }
}
