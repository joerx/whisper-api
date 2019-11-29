package io.yodo.whisper.api.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yodo.whisper.api.WhisperApiApplication;
import io.yodo.whisper.api.entity.Shout;
import io.yodo.whisper.api.entity.ShoutPage;
import io.yodo.whisper.api.test.support.TestTokenHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = WhisperApiApplication.class)
class ShoutApiTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestTokenHelper tokenHelper;

    ShoutPage getShouts() throws Exception {
        ObjectMapper om = new ObjectMapper();
        String token = tokenHelper.getToken();

        MvcResult res  = mvc.perform(get("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andReturn();

        return om.readValue(res.getResponse().getContentAsString(), ShoutPage.class);
    }

    @Test
    void testCanGetShouts() throws Exception{
        String token = tokenHelper.getToken();

        mvc.perform(get("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
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
        String data = "{\"message\": \"" + message + "\", \"username\": \"" + username + "\"}";

        mvc.perform(post("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenHelper.getToken())
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void testCannotCreateShoutWithoutMessage() throws Exception{
        String username = "donald-duck";
        String data = "{\"message\": \"\", \"username\": \"" + username + "\"}";

        mvc.perform(post("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenHelper.getToken())
                .content(data))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCanUpdateShout() throws Exception{
        ObjectMapper om = new ObjectMapper();
        Shout s = getShouts().getItems().get(0);

        s.setMessage("foo bar baz");

        mvc.perform(put("/shouts/" + s.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenHelper.getToken())
                .content(om.writeValueAsString(s)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(s.getMessage()))
                .andExpect(jsonPath("$.username").value(s.getUsername()))
                .andExpect(jsonPath("$.id").value(s.getId()));
    }

    @Test
    void testCanGetShout() throws Exception{
        Shout s = getShouts().getItems().get(0);

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
        Shout s = getShouts().getItems().get(0);

        mvc.perform(delete("/shouts/" + s.getId())
                .header("Authorization", "Bearer " + tokenHelper.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(s.getId()));

        mvc.perform(get("/shouts/" + s.getId())
                .header("Authorization", "Bearer " + tokenHelper.getToken()))
                .andExpect(status().isNotFound());
    }
}
