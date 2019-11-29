package io.yodo.whisper.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yodo.whisper.api.entity.ErrorResponse;
import io.yodo.whisper.api.error.ApiResponseError;
import io.yodo.whisper.api.error.ApiTransportError;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Fetch implements Closeable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CloseableHttpClient httpClient;

    private final String baseUrl;

    public Fetch(String baseUrl) {
        this.baseUrl = baseUrl;
        httpClient = HttpClients.createDefault();
    }

    public RequestBuilder get(String path) {
        return this.new RequestBuilder(new HttpGet(uri(path)));
    }

    public RequestBuilder post(String path) {
        return this.new RequestBuilder(new HttpPost(uri(path)));
    }

    public RequestBuilder put(String path) {
        return this.new RequestBuilder(new HttpPut(uri(path)));
    }

    private URI uri(String path) {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl);
            uriBuilder.setPath(path);
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new ApiTransportError(e);
        }
    }

    public void close() throws IOException {
        log.debug("Shutting down http client");
        httpClient.close();
    }

    public class RequestBuilder {

        final HttpUriRequest req;

        private final ObjectMapper mapper = new ObjectMapper();

        private RequestBuilder(HttpUriRequest req) {
            this.req = req;
        }

        public RequestBuilder auth(String token) {
            req.setHeader("Authorization", "Bearer " + token);
            return this;
        }

        public RequestBuilder body(Object entity) {
            if (!(req instanceof HttpEntityEnclosingRequest)) {
                throw new UnsupportedOperationException("Can only set body on " + HttpEntityEnclosingRequest.class);
            }

            HttpEntityEnclosingRequest er = (HttpEntityEnclosingRequest) req;
            er.setEntity(mapEntity(entity));
            er.setHeader("Content-type", "application/json");

            return this;
        }

        private StringEntity mapEntity(Object entity) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return new StringEntity(mapper.writeValueAsString(entity));
            } catch(Exception e) {
                throw new ApiTransportError(e);
            }
        }

        public <T> T getResponse(Class<T> responseType) {
            log.debug(req.getMethod() + " " + req.getURI().toString());

            try (CloseableHttpResponse res = httpClient.execute(req)) {
                log.debug(res.getStatusLine() + " " + res.getStatusLine().getReasonPhrase());

                int statusCode = res.getStatusLine().getStatusCode();
                if (statusCode >= 400) {
                    BufferedHttpEntity buf = new BufferedHttpEntity(res.getEntity());
                    ErrorResponse er = mapper.readValue(buf.getContent(), ErrorResponse.class);
                    throw new ApiResponseError(statusCode, er.getMessage());
                }

                return mapper.readValue(res.getEntity().getContent(), responseType);
            } catch (Exception e) {
                throw new ApiTransportError(e);
            }
        }
    }
}
