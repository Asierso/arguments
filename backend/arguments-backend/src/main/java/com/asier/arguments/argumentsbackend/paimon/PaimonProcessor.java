package com.asier.arguments.argumentsbackend.paimon;

import com.asier.arguments.argumentsbackend.utils.LlamaConnectionUtils;
import com.asier.arguments.argumentsbackend.utils.ResourceLocator;
import com.asier.arguments.argumentsbackend.utils.properties.PropertiesUtils;
import com.asier.arguments.argumentsbackend.utils.velocity.VelocityTemplate;
import com.asier.arguments.argumentsbackend.utils.velocity.VelocityUtils;
import com.asierso.llamaapi.LlamaConnection;
import com.asierso.llamaapi.builder.LlamaPromptsBuilder;
import com.asierso.llamaapi.builder.LlamaSettingsBuilder;
import com.asierso.llamaapi.handlers.LlamaConnectionException;
import com.asierso.llamaapi.handlers.LlamaSettings;
import com.asierso.llamaapi.models.LlamaResponse;
import com.asierso.llamaapi.models.requests.LlamaRequest;
import com.asierso.llamaapi.models.requests.LlamaStreamRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Slf4j
public class PaimonProcessor {
    private final LlamaSettings settings;
    private final LlamaConnection connection;
    private final Properties props = PropertiesUtils.getProperties(ResourceLocator.ARGUMENTS);

    @Autowired
    private VelocityUtils velocity;

    public PaimonProcessor(){
        settings = new LlamaSettingsBuilder()
                .useModel(props.getProperty("paimon.model"))
                .build();
        connection = LlamaConnectionUtils.getConnection();
    }

    public void processAsPrompt(VelocityTemplate template, PaimonFetchCallback callback){
        String prompt = velocity.applyTemplate(template);

        LlamaStreamRequest req = new LlamaPromptsBuilder(settings)
                .appendPrompt(prompt)
                .withStream(false)
                .build();
        try {
            //Run callback with llama response
            callback.run(connection.fetch(req).getResponse());
        }catch (LlamaConnectionException e){
            log.error("Error at paimon: " + e.getMessage());
        }
    }

}
