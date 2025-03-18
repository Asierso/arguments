package com.asier.arguments.arguments_backend.ollama;

import com.asier.arguments.argumentsbackend.utils.LlamaConnectionUtils;
import com.asierso.llamaapi.LlamaConnection;
import com.asierso.llamaapi.builder.LlamaPromptsBuilder;
import com.asierso.llamaapi.builder.LlamaSettingsBuilder;
import com.asierso.llamaapi.handlers.LlamaConnectionException;
import com.asierso.llamaapi.handlers.LlamaSettings;
import com.asierso.llamaapi.models.LlamaResponse;
import com.asierso.llamaapi.models.requests.LlamaStreamRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class LlamaBasicPrompts {
    @Test
    public void simplePrompt(){
        LlamaConnection conn = LlamaConnectionUtils.getConnection();
        LlamaSettings settings = new LlamaSettingsBuilder().useModel("llama3.2:1b").build();
        LlamaStreamRequest lls = new LlamaPromptsBuilder(settings).appendPrompt("Capital de españa").build();

        try {
            LlamaResponse resp = conn.fetch(lls);
            System.out.println(resp.getMessage());
        } catch (LlamaConnectionException e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}
