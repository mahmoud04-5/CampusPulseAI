package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.domain.DTO.SurveyQuestionDTO;
import com.example.campuspulseai.service.ISurveyService;
import com.example.campuspulseai.southBound.entity.QuestionChoices;
import com.example.campuspulseai.southBound.entity.SurveyQuestion;
import com.example.campuspulseai.southBound.entity.User;
import com.example.campuspulseai.southBound.entity.SurveyUserAnswers;
import com.example.campuspulseai.southBound.repository.IQuestionChoicesRepository;
import com.example.campuspulseai.southBound.repository.ISurveyQuestionRepository;
import com.example.campuspulseai.southBound.repository.ISurveyUserAnswersRepository;
import com.example.campuspulseai.southBound.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SurveyServiceImpl implements ISurveyService {
    private final IUserRepository userRepository;
    private final ISurveyQuestionRepository surveyQuestionRepository;
    private final ISurveyUserAnswersRepository surveyUserAnswersRepository;
    private final IQuestionChoicesRepository questionChoicesRepository;


    @Override
    @Transactional
    public void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses) {
        //User user = getAuthenticatedUser();
        User user = getDummyUser();
        SurveyUserAnswers userAnswers = prepareSurveyResponse(user, surveyResponses);
        saveSurveyResponse(userAnswers);
    }

    private SurveyUserAnswers prepareSurveyResponse(User user, List<SurveyQuestionDTO> surveyResponses) {
        if (surveyResponses == null || surveyResponses.isEmpty()) {
            throw new IllegalArgumentException("Survey responses cannot be null or empty");
        }

        // Check for existing response and update if needed
        SurveyUserAnswers userAnswers = surveyUserAnswersRepository.findByUserId(user.getId())
                .orElse(new SurveyUserAnswers());
        userAnswers.setUser(user);

        Map<String, Object> questionAnswers = Optional.ofNullable(userAnswers.getQuestionAnswers())
                .orElse(new HashMap<>());
        for (SurveyQuestionDTO response : surveyResponses) {
            if (response.getSelectedChoicesIds() == null || response.getSelectedChoicesIds().isEmpty()) {
                throw new IllegalArgumentException("Selected choices for question " + response.getQuestionId() + " cannot be null or empty");
            }
            // Validate question and choices existence
            surveyQuestionRepository.findById(response.getQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("Question " + response.getQuestionId() + " not found"));
            response.getSelectedChoicesIds().forEach(choiceId ->
                    questionChoicesRepository.findById(choiceId)
                            .orElseThrow(() -> new IllegalArgumentException("Choice " + choiceId + " not found"))
            );
            questionAnswers.put(String.valueOf(response.getQuestionId()), response.getSelectedChoicesIds());
        }
        userAnswers.setQuestionAnswers(questionAnswers);

        return userAnswers;
    }

    @Transactional
    protected void saveSurveyResponse(SurveyUserAnswers userAnswers) {
        surveyUserAnswersRepository.save(userAnswers);
    }

    @Override
    public boolean isSurveyCompleted() {
        User user = getAuthenticatedUser();
        return surveyUserAnswersRepository.findByUserId(user.getId()).isPresent();
    }


    @Override
    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }


    public User getDummyUser() {
        return userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Dummy user not found"));
    }

    @Override
    public List<SurveyQuestionDTO> getAllSurveyQuestions() {
        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findAll();
        return surveyQuestions.stream()
                .map(surveyQuestion -> {
                    List<Long> choiceIds = surveyQuestion.getChoices() != null
                            ? surveyQuestion.getChoices().stream()
                            .map(QuestionChoices::getId)
                            .toList()
                            : List.of();
                    return new SurveyQuestionDTO(
                            surveyQuestion.getId(),
                            surveyQuestion.getQuestionText(),
                            choiceIds
                    );
                })
                .toList();
    }
}