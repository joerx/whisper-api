package io.yodo.whisper.api.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yodo.whisper.api.WhisperApiApplication;
import io.yodo.whisper.api.http.Fetch;
import io.yodo.whisper.api.test.support.TestTokenHelper;
import io.yodo.whisper.commons.entity.ShoutDTO;
import io.yodo.whisper.commons.entity.ShoutPageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;
import java.sql.Statement;

import static io.yodo.whisper.api.it.JsonUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = {WhisperApiApplication.class, ApiTestDataSourceConfig.class})
@ActiveProfiles("it")
class ShoutApiTests {

    private static final Logger log = LoggerFactory.getLogger(ShoutApiTests.class);

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestTokenHelper tokenHelper;

    @Autowired
    @Qualifier("backendDataSource")
    private DataSource backendDS;

    @Autowired
    @Qualifier("backendClient")
    private Fetch backendClient;

    ShoutPageDTO getShouts() throws Exception {
        ObjectMapper om = new ObjectMapper();
        String token = tokenHelper.getToken();

        MvcResult res  = mvc.perform(get("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        return om.readValue(res.getResponse().getContentAsString(), ShoutPageDTO.class);
    }

    @BeforeEach
    void prepareDataSources() throws Exception {
        log.debug("Truncating shout table");

        // truncate shouts table
        Statement m = backendDS.getConnection().createStatement();
        m.execute("truncate table shout");

        // seed shouts via backend API
        String token = tokenHelper.getToken();
        for(int i = 0; i < 3; i++) {
            backendClient.post("/shouts")
                    .body(new ShoutDTO("hello whisper " + i, "user" + i))
                    .auth(token).execute();
        }
    }

    @Test
    void testCanGetShouts() throws Exception{
        String token = tokenHelper.getToken();
        mvc.perform(get("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNonExistingShoutsWillRespondNotFound() throws Exception{
        String token = tokenHelper.getToken();

        mvc.perform(get("/shouts/100000000")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCannotGetShoutsIfNotAuthorized() throws Exception{
        mvc.perform(get("/shouts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCanCreateShout() throws Exception{
        String message = "Hello whisper!";
        String username = "donald-duck";
        String data = toJson(new ShoutDTO(message, username));

        MvcResult res = mvc.perform(post("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenHelper.getToken())
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        int id = fromResponse(res.getResponse(), ShoutDTO.class).getId();
        mvc.perform(get("/shouts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenHelper.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void testCannotCreateShoutWithoutMessage() throws Exception{
        String username = "donald-duck";
        String data = toJson(new ShoutDTO(null, username));

        mvc.perform(post("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenHelper.getToken())
                .content(data))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCanUpdateShout() throws Exception{
        ShoutDTO s = getShouts().getItems().get(0);
        s.setMessage("foo bar baz");

        mvc.perform(put("/shouts/" + s.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenHelper.getToken())
                .content(toJson(s)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(s.getMessage()))
                .andExpect(jsonPath("$.username").value(s.getUsername()))
                .andExpect(jsonPath("$.id").value(s.getId()));
    }

    @Test
    void testCanGetShout() throws Exception{
        ShoutDTO s = getShouts().getItems().get(0);

        mvc.perform(get("/shouts/" + s.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenHelper.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(s.getMessage()))
                .andExpect(jsonPath("$.username").value(s.getUsername()))
                .andExpect(jsonPath("$.id").value(s.getId()));
    }

    @Test
    void testCanDeleteShout() throws Exception{
        ShoutDTO s = getShouts().getItems().get(0);

        mvc.perform(delete("/shouts/" + s.getId())
                .header("Authorization", "Bearer " + tokenHelper.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(s.getId()));

        mvc.perform(get("/shouts/" + s.getId())
                .header("Authorization", "Bearer " + tokenHelper.getToken()))
                .andExpect(status().isNotFound());
    }
}
