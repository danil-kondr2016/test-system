package ru.danilakondr.testsystem.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="response_type", visible = true)
@JsonSubTypes({
          @JsonSubTypes.Type(value=JwtResponse.class, name="JWT")
        , @JsonSubTypes.Type(value= ErrorResponse.class, name="ERROR")
})
public abstract class Response {
}
