package ru.danilakondr.testsystem.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import ru.danilakondr.testsystem.info.Report;

import java.util.List;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="response_type", visible = true)
@JsonSubTypes({
          @JsonSubTypes.Type(value= Response.SessionKey.class, name="SESSION_KEY")
        , @JsonSubTypes.Type(value= Response.Error.class, name="ERROR")
        , @JsonSubTypes.Type(value= Response.Description.class, name="DESCRIPTION")
        , @JsonSubTypes.Type(value= Response.DescriptionList.class, name="LIST")
        , @JsonSubTypes.Type(value= Response.Participant.class, name="PARTICIPANT")
        , @JsonSubTypes.Type(value= Response.ReportAll.class, name="REPORT_ALL")
        , @JsonSubTypes.Type(value= Response.SystemInfo.class, name="SYSTEM_INFO")
})
public abstract class Response {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error extends Response {
        public static final Error RESOURCE_NOT_FOUND = new Response.Error("RESOURCE_NOT_FOUND");
        public static final Error PERMISSION_DENIED = new Response.Error("PERMISSION_DENIED");
        public static final Error INVALID_CREDENTIALS = new Response.Error("INVALID_CREDENTIALS");
        public static final Error INVALID_REQUEST = new Response.Error("INVALID_REQUEST");
        public static final Error USER_ALREADY_EXISTS = new Response.Error("USER_ALREADY_EXISTS");
        private String reason;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class SessionKey extends Response {
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
        List<? extends ru.danilakondr.testsystem.protocol.Description> descriptions;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class Participant extends Response {
        String sessionKey;
        List<ru.danilakondr.testsystem.protocol.Description.Question> questions;
    }

    @AllArgsConstructor
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ReportAll extends Response {
        List<Report> reports;
    }

    @AllArgsConstructor
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class SystemInfo extends Response {
        ru.danilakondr.testsystem.info.SystemInfo systemInfo;
    }
}
