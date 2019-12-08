package io.yodo.whisper.api.http;

import java.io.InputStream;

public class HttpResponseEntity {

    private int statusCode;

    private InputStream content;

    public HttpResponseEntity(int statusCode, InputStream content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }
}
