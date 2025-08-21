package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.common.exception.ResourceNotFoundException;
import com.example.campuspulseai.common.util.IAuthUtils;
import com.example.campuspulseai.service.IEventRecommendationService;
import com.example.campuspulseai.southbound.entity.Event;
import com.example.campuspulseai.southbound.entity.QuestionChoices;
import com.example.campuspulseai.southbound.entity.SurveyQuestion;
import com.example.campuspulseai.southbound.entity.SurveyUserAnswers;
import com.example.campuspulseai.southbound.repository.IEventRepository;
import com.example.campuspulseai.southbound.repository.IQuestionChoicesRepository;
import com.example.campuspulseai.southbound.repository.ISurveyQuestionRepository;
import com.example.campuspulseai.southbound.repository.ISurveyUserAnswersRepository;
import com.example.campuspulseai.southbound.specification.IEventSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRecommendationServiceImpl implements IEventRecommendationService {

    private final IAuthUtils authUtils;
    private final ISurveyUserAnswersRepository surveyUserAnswersRepository;
    private final ISurveyQuestionRepository surveyQuestionRepository;
    private final IQuestionChoicesRepository questionChoicesRepository;
    private final AIService aiService;
    private final IEventRepository eventRepository;
    private final IEventSpecifications eventSpecifications;

    @Override
    public List<Long> getRecommendedEventIds(Long userId) {

        SurveyUserAnswers userAnswers = getSurveyUserAnswersfromDb(userId);

        List<SurveyQuestion> questions = surveyQuestionRepository.findAll();
        List<QuestionChoices> choices = questionChoicesRepository.findAll();

        List<Event> events = getEventsToSuggestFrom();

        String prompt = buildEventRecommendationPrompt(questions, choices, userAnswers, events);

        String response = aiService.chat(prompt);

        List<String> eventIds = List.of(response.split(","));
        return eventIds.stream()
                .map(String::trim)
                .map(Long::valueOf)
                .toList();

    }

    private String buildEventRecommendationPrompt(List<SurveyQuestion> questions, List<QuestionChoices> choices, SurveyUserAnswers userAnswers, List<Event> events) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("You are an event recommendation system.\n");
        promptBuilder.append("The goal is to recommend events that best match the user's survey answers.\n\n");

        appendSurveyQuestionsAndChoices(promptBuilder, questions, choices);

        appendUserAnswers(promptBuilder, userAnswers);

        appendEvents(promptBuilder, events);

        promptBuilder.append("### Task:\n")
                .append("Based on the survey answers, recommend 5 events that best fit this user.\n")
                .append("Return ONLY the event IDs as a comma-separated list, no explanations.\n");

        return promptBuilder.toString();
    }

    private void cleanTrailingComma(StringBuilder promptBuilder) {
        if (promptBuilder.charAt(promptBuilder.length() - 2) == ',') {
            promptBuilder.delete(promptBuilder.length() - 2, promptBuilder.length()); // remove trailing comma
        }
    }

    private void appendSurveyQuestionsAndChoices(StringBuilder promptBuilder, List<SurveyQuestion> questions, List<QuestionChoices> choices) {
        promptBuilder.append("### Survey Questions & Choices:\n");
        for (SurveyQuestion q : questions) {
            promptBuilder.append("{")
                    .append("\"id\": ").append(q.getId()).append(", ")
                    .append("\"question\": \"").append(q.getQuestion()).append("\"")
                    .append(", \"choices\": [");
            choices.stream()
                    .filter(c -> c.getQuestion().getId().equals(q.getId()))
                    .forEach(c -> promptBuilder.append("{\"id\": ").append(c.getId())
                            .append(", \"choice\": \"").append(c.getChoice()).append("\"}, "));
            cleanTrailingComma(promptBuilder);
            promptBuilder.append("]}\n");
        }
    }

    private void appendUserAnswers(StringBuilder promptBuilder, SurveyUserAnswers userAnswers) {
        promptBuilder.append("\n### User Answers:\n")
                .append(userAnswers.getQuestionAnswers()) // JSONB Map<String,Object>
                .append("\n\n");
    }

    private void appendEvents(StringBuilder promptBuilder, List<Event> events) {
        promptBuilder.append("### Events:\n[");
        for (Event e : events) {
            promptBuilder.append("{")
                    .append("\"id\": ").append(e.getId()).append(", ")
                    .append("\"title\": \"").append(e.getTitle()).append("\", ")
                    .append("\"description\": \"").append(e.getDescription()).append("\", ")
                    .append("\"category\": \"").append(e.getCategory()).append("\", ")
                    .append("\"date\": \"").append(e.getStartDate()).append("\", ")
                    .append("\"capacity\": ").append(e.getCapacity())
                    .append("}, ");
        }
        cleanTrailingComma(promptBuilder);
        promptBuilder.append("]\n\n");
    }

    private List<Event> getEventsToSuggestFrom() {
        Specification<Event> spec = eventSpecifications.isActive()
                .and(eventSpecifications.hasEventWithinGivenDays());
        List<Event> events = eventRepository.findAll(spec)
                .stream()
                .toList();
        if (events.isEmpty()) {
            throw new ResourceNotFoundException("No active events found within the next 90 days.");
        }
        return events;
    }

    private SurveyUserAnswers getSurveyUserAnswersfromDb(Long userId) {
        return surveyUserAnswersRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No survey answers found for user ID: " + userId));
    }
}
