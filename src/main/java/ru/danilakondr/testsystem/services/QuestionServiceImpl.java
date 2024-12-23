package ru.danilakondr.testsystem.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danilakondr.testsystem.dao.AnswerVariantDAO;
import ru.danilakondr.testsystem.dao.QuestionDAO;
import ru.danilakondr.testsystem.data.AnswerVariant;
import ru.danilakondr.testsystem.data.Question;
import ru.danilakondr.testsystem.data.Test;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private AnswerVariantDAO variantDAO;

    @Override
    @Transactional
    public Question create(Test test, Question.Type type, String text, List<AnswerVariant> variants) {
        Question question = new Question();
        question.setType(type);
        question.setText(text);
        question.setTest(test);
        question.setVariants(variants);
        variants.forEach(x -> x.setQuestion(question));

        long newOrder = questionDAO.streamByTestOrderByOrderAsc(test)
                .mapToLong(Question::getOrder)
                .reduce((a, b) -> (Long.compareUnsigned(a, b) > 0) ? a : b)
                .orElse(0)
                + 1;
        question.setOrder(newOrder);

        questionDAO.save(question);
        return question;
    }

    @Override
    @Transactional
    public Question get(long questionId) {
        return questionDAO.getReferenceById(questionId);
    }

    @Override
    @Transactional
    public void update(Question question, Question.Type type, String text, List<AnswerVariant> variants) {
        question.setText(question.getText());
        question.setType(question.getType());

        variants.forEach(variant -> {
            AnswerVariant v1 = variantDAO.getReferenceById(variant.getId());
            v1.setText(variant.getText());
            v1.setCorrect(variant.isCorrect());
        });
    }

    @Override
    @Transactional
    public void remove(Question question) {
        questionDAO.delete(question);
    }
}
