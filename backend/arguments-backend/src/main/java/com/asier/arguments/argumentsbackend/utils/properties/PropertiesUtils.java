package com.asier.arguments.argumentsbackend.utils.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import com.asier.arguments.argumentsbackend.utils.ResourceLocator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesUtils {
    @Getter
    private Properties props;

    private static HashMap<ResourceLocator, PropertiesUtils> properties;

    public static Properties getProperties(ResourceLocator locator){
        if (properties == null){
            properties = new HashMap<>();
        }
        if(!properties.containsKey(locator)){
            properties.put(locator, new PropertiesUtils(locator));
        }
        return properties.get(locator).getProps();
    }

    private PropertiesUtils(ResourceLocator locator) {
        this.props = new Properties();
        InputStream inputStream = getClass().getResourceAsStream(locator.getUri());
        
        if (inputStream == null) {
            log.error("Error loading properties: " + locator.getUri());
        }

        try {
            props.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            log.error("Error loading properties: " + locator.getUri(), e);
            throw new RuntimeException("resource");
        }
    }
}
