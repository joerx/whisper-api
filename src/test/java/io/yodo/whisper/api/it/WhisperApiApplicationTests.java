package io.yodo.whisper.api.it;

import io.yodo.whisper.api.WhisperApiApplication;
import io.yodo.whisper.api.test.support.TestTokenHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = WhisperApiApplication.class)
class WhisperApiApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestTokenHelper tokenHelper;

    @Test
    void testCanGetShouts() throws Exception{
        String token = tokenHelper.requestToken();
        mvc.perform(get("/shouts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
