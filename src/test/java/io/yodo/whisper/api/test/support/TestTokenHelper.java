package io.yodo.whisper.api.test.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yodo.whisper.api.entity.TokenRequest;
import io.yodo.whisper.api.entity.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestTokenHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockMvc mvc;

    @Value("${test.auth.client-secret}")
    private String clientSecret;

    @Value("${test.auth.client-id}")
    private String clientId;

    public String getToken() throws Exception {
        log.debug("Requesting token for client with id " + clientId);

        ObjectMapper om = new ObjectMapper();
        TokenRequest tr = new TokenRequest(clientId, clientSecret);

        MvcResult res = mvc.perform(post("/token")
                .content(om.writeValueAsString(tr))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        TokenResponse token = om.readValue(res.getResponse().getContentAsString(), TokenResponse.class);

        return token.getToken();
    }
}
