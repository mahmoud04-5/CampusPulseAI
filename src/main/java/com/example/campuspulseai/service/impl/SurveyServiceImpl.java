package com.example.campuspulseai.service.Impl;

import com.example.campuspulseai.common.util.AuthUtils;
import com.example.campuspulseai.domain.dto.SurveyQuestionDTO;
import com.example.campuspulseai.service.ISurveyService;
import com.example.campuspulseai.southbound.entity.SurveyQuestion;
import com.example.campuspulseai.southbound.entity.User;
import com.example.campuspulseai.southbound.entity.SurveyUserAnswers;
import com.example.campuspulseai.southbound.mapper.SurveyQuestionMapper;
import com.example.campuspulseai.southbound.repository.IQuestionChoicesRepository;
import com.example.campuspulseai.southbound.repository.ISurveyQuestionRepository;
import com.example.campuspulseai.southbound.repository.ISurveyUserAnswersRepository;
import com.example.campuspulseai.southbound.repository.IUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final AuthUtils authUtils;
    private final SurveyQuestionMapper surveyQuestionMapper;



    @Override
    @Transactional
    public void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses) {
        User user = authUtils.getAuthenticatedUser();
        SurveyUserAnswers userAnswers = buildSurveyUserAnswers(user, surveyResponses);
        saveSurveyResponse(userAnswers);
    }

    @Override
    public List<SurveyQuestionDTO> getAllSurveyQuestions() {
        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findAll();
        return surveyQuestionMapper.toDtoList(surveyQuestions); // MapStruct handles the list
    }

    // ---------- Helper methods ----------


    private SurveyUserAnswers buildSurveyUserAnswers(User user, List<SurveyQuestionDTO> surveyResponses) {
        if (surveyResponses == null || surveyResponses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Survey responses cannot be null or empty");
        }

        SurveyUserAnswers userAnswers = surveyUserAnswersRepository.findByUserId(user.getId())
                .orElse(new SurveyUserAnswers());
        userAnswers.setUser(user);

        Map<String, Object> questionAnswers = Optional.ofNullable(userAnswers.getQuestionAnswers())
                .orElse(new HashMap<>());

        for (SurveyQuestionDTO response : surveyResponses) {
            validateSurveyResponse(response);
            questionAnswers.put(String.valueOf(response.getQuestionId()), response.getSelectedChoicesIds());
        }

        userAnswers.setQuestionAnswers(questionAnswers);
        return userAnswers;
    }

    private void validateSurveyResponse(SurveyQuestionDTO response) {
        if (response.getSelectedChoicesIds() == null || response.getSelectedChoicesIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Selected choices for question " + response.getQuestionId() + " cannot be null or empty");
        }

        surveyQuestionRepository.findById(response.getQuestionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Question " + response.getQuestionId() + " not found"));

        response.getSelectedChoicesIds().forEach(choiceId ->
                questionChoicesRepository.findById(choiceId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                                "Choice " + choiceId + " not found"))
        );
    }

    @Transactional
    protected void saveSurveyResponse(SurveyUserAnswers userAnswers) {
        surveyUserAnswersRepository.save(userAnswers);
    }

}
