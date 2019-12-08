package io.yodo.whisper.api.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.yodo.whisper.api.error.ApiClientError;
import io.yodo.whisper.api.error.ApiGatewayError;
import io.yodo.whisper.api.error.ApiTransportError;
import io.yodo.whisper.commons.web.error.ErrorResponse;
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
import java.io.InputStream;
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

    public RequestBuilder delete(String path) {
        return this.new RequestBuilder(new HttpDelete(uri(path)));
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

            String payload = encodeEntity(entity);
            log.debug("Sending data " + payload);

            HttpEntityEnclosingRequest er = (HttpEntityEnclosingRequest) req;
            er.setEntity(wrapEntity(payload));
            er.setHeader("Content-type", "application/json");

            return this;
        }

        private String encodeEntity(Object payload) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(payload);
            } catch(Exception e) {
                throw new ApiTransportError(e);
            }
        }

        private StringEntity wrapEntity(String payload) {
            try {
                return new StringEntity(payload);
            } catch(Exception e) {
                throw new ApiTransportError(e);
            }
        }

        private void handleErrorResponse(int statusCode, InputStream content) {
            ErrorResponse er;

            try {
                er = mapper.readValue(content, ErrorResponse.class);
            } catch (IOException io) {
                throw new ApiTransportError(io);
            }

            if (statusCode >= 500) {
                throw new ApiGatewayError(statusCode, er.getMessage());
            } else if (statusCode >= 400) {
                throw new ApiClientError(statusCode, er.getMessage());
            }
        }

        private HttpResponseEntity doHttp() {
            log.debug(req.getMethod() + " " + req.getURI().toString());

            int statusCode;
            InputStream content;

            try (CloseableHttpResponse res = httpClient.execute(req)) {
                log.debug(res.getStatusLine() + " " + res.getStatusLine().getReasonPhrase());
                statusCode = res.getStatusLine().getStatusCode();
                content = new BufferedHttpEntity(res.getEntity()).getContent();
            } catch (IOException e) {
                throw new ApiTransportError(e);
            }

            if (statusCode >= 400) {
                handleErrorResponse(statusCode, content);
            }
            return new HttpResponseEntity(statusCode, content);
        }

        public <T> T getResponse(Class<T> responseType) {
            try {
                return mapper.readValue(doHttp().getContent(), responseType);
            } catch (IOException e) {
                throw new ApiTransportError(e);
            }
        }

        public HttpResponseEntity execute() {
            return doHttp();
        }
    }
}
