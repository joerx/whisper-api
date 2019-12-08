package io.yodo.whisper.api.it;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletResponse;

abstract class JsonUtil {

    static String toJson(Object o) throws Exception {
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return om.writeValueAsString(o);
    }

    static <T> T fromJson(String content, Class<T> type) throws Exception{
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return om.readValue(content, type);
    }

    static <T> T fromResponse(MockHttpServletResponse res, Class<T> type) throws Exception{
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return om.readValue(res.getContentAsString(), type);
    }
}
