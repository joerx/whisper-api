package io.yodo.whisper.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Shout {

    @JsonProperty("id")
    private int id;

    @JsonProperty("message")
    private String message;

    @JsonProperty("username")
    private String username;

    @JsonProperty("timestamp")
    private Date timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shout shout = (Shout) o;
        return id == shout.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Shout{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
