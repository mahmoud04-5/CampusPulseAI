package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.domain.dto.SurveyQuestionDTO;
import com.example.campuspulseai.service.ISurveyService;
import com.example.campuspulseai.southbound.entity.SurveyUserAnswers;
import com.example.campuspulseai.southbound.entity.User;
import com.example.campuspulseai.southbound.repository.IQuestionChoicesRepository;
import com.example.campuspulseai.southbound.repository.ISurveyQuestionRepository;
import com.example.campuspulseai.southbound.repository.ISurveyUserAnswersRepository;
import com.example.campuspulseai.southbound.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SurveyServiceImpl implements ISurveyService {
    private final IUserRepository userRepository;
    private final ISurveyQuestionRepository surveyQuestionRepository;
    private final ISurveyUserAnswersRepository surveyUserAnswersRepository;
    private final IQuestionChoicesRepository questionChoicesRepository;

    @Override
    public List<SurveyQuestionDTO> getAllSurveyQuestions() {
        return List.of();
    }

    @Override
    public void submitSurveyResponse(List<SurveyQuestionDTO> surveyResponses) {
        User user = getAuthenticatedUser();
        surveyResponses.forEach(surveyQuestionDTO -> {
            if (surveyQuestionDTO.getChoices() == null || surveyQuestionDTO.getChoices().isEmpty()) {
                throw new IllegalArgumentException("Survey answer cannot be null or empty");
            } else {
                surveyQuestionDTO.getSelectedChoicesIds().forEach(choiceId -> {
                    SurveyUserAnswers surveyUserAnswers = new SurveyUserAnswers();
                    surveyUserAnswers.setUser(user); //sets the user field of the SurveyUserAnswers object to the authenticated User retrieved earlier.
                    //surveyUserAnswers.setSurveyQuestion(surveyQuestionRepository.findById(surveyQuestiondto.getQuestionId())
                    //  .orElseThrow(() -> new IllegalArgumentException("Survey question not found")));
                    //surveyUserAnswers.setChoice(questionChoicesRepository.findById(choiceId)
                    //.orElseThrow(() -> new IllegalArgumentException("Choice not found")));
                    surveyUserAnswersRepository.save(surveyUserAnswers
                    );
                });
            }
        });
    }

    @Override
    public boolean isSurveyCompleted() {
        User user = getAuthenticatedUser();
        return surveyUserAnswersRepository.existsById(user.getId());
    }


    @Override
    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
}