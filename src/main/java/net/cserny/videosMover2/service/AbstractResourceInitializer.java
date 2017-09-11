package net.cserny.videosMover2.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractResourceInitializer
{
    protected List<String> fillListFromResource(String resource) {
        List<String> list = new ArrayList<>();
        try (BufferedReader txtReader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(resource)))) {
            String line;
            while ((line = txtReader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
