package ru.danilakondr.testsystem.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="response_type", visible = true)
@JsonSubTypes({
          @JsonSubTypes.Type(value= SessionKeyResponse.class, name="SESSION_KEY")
        , @JsonSubTypes.Type(value= ErrorResponse.class, name="ERROR")
        , @JsonSubTypes.Type(value= TestDescriptionResponse.class, name="TEST_DESCR")
})
public abstract class Response {
}
