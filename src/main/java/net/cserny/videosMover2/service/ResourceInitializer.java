package net.cserny.videosMover2.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceInitializer
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
