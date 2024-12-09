package ru.danilakondr.testsystem.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="description_type", visible = true)
@JsonSubTypes({
          @JsonSubTypes.Type(value=Description.Test.class, name="TEST")
        , @JsonSubTypes.Type(value=Description.Question.class, name="QUESTION")
        , @JsonSubTypes.Type(value=Description.AnswerVariant.class, name="VARIANT")
        , @JsonSubTypes.Type(value=Description.TestSession.class, name="TEST_SESSION")
})
public abstract class Description {
    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class AnswerVariant extends Description {
        @JsonSerialize(using= ToStringSerializer.class)
        long id;
        String text;
        boolean correct;

        public AnswerVariant(ru.danilakondr.testsystem.data.AnswerVariant variant) {
            this.id = variant.getId();
            this.text = variant.getText();
            this.correct = variant.isCorrect();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class Question extends Description {
        @JsonSerialize(using= ToStringSerializer.class)
        long id;
        ru.danilakondr.testsystem.data.Question.Type type;
        String text;
        List<AnswerVariant> variants;

        public Question(ru.danilakondr.testsystem.data.Question question) {
            this.id = question.getId();
            this.type = question.getType();
            this.text = question.getText();
            this.variants = question.getVariants().stream().map(AnswerVariant::new).toList();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class Test extends Description {
        @JsonSerialize(using= ToStringSerializer.class)
        long id;
        String name;
        List<Question> questions;

        public Test(ru.danilakondr.testsystem.data.Test test) {
            this.id = test.getId();
            this.name = test.getName();
            this.questions = test.getQuestions().stream().map(Question::new).toList();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    public static class TestSession extends Description {
        UUID id;
        long testId;
        ru.danilakondr.testsystem.data.TestSession.State state;
        LocalDateTime begin, end;

        public TestSession(ru.danilakondr.testsystem.data.TestSession session)  {
            this.id = session.getId();
            this.testId = session.getTest().getId();
            this.state = session.getTestSessionState();
            this.begin = session.getBegin();
            this.end = session.getEnd();
        }
    }
}
