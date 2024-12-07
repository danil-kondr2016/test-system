package ru.danilakondr.testsystem.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.List;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="response_type", visible = true)
@JsonSubTypes({
          @JsonSubTypes.Type(value= Response.SessionKeyResponse.class, name="SESSION_KEY")
        , @JsonSubTypes.Type(value= Response.Error.class, name="ERROR")
        , @JsonSubTypes.Type(value= Response.Description.class, name="DESCRIPTION")
        , @JsonSubTypes.Type(value= Response.DescriptionList.class, name="LIST")
})
public abstract class Response {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error extends Response {
        private String reason;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class SessionKeyResponse extends Response {
        private String sessionKey;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class Description extends Response {
        ru.danilakondr.testsystem.protocol.Description description;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class DescriptionList extends Response {
        List<ru.danilakondr.testsystem.protocol.Description> descriptions;
    }
}
