package com.asier.arguments.argumentsbackend.utils;

import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import com.asierso.llamaapi.LlamaConnection;

import java.util.Properties;

public class LlamaConnectionUtils {
    public static LlamaConnection getConnection() {
        Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);
        StringBuilder uri = new StringBuilder().append(props.getProperty("llama.uri.protocol"))
                .append("://")
                .append(props.getProperty("llama.uri.address"))
                .append(":")
                .append(props.getProperty("llama.uri.port"));
        return new LlamaConnection(uri.toString());
    }
}
