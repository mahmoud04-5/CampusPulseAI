package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.common.exception.ResourceNotFoundException;
import com.example.campuspulseai.common.util.IAuthUtils;
import com.example.campuspulseai.domain.dto.SuggestedEventParts;
import com.example.campuspulseai.service.IAiService;
import com.example.campuspulseai.service.IEventRecommendationService;
import com.example.campuspulseai.southbound.Enum.Category;
import com.example.campuspulseai.southbound.entity.*;
import com.example.campuspulseai.southbound.mapper.EventMapper;
import com.example.campuspulseai.southbound.repository.IEventRepository;
import com.example.campuspulseai.southbound.repository.IQuestionChoicesRepository;
import com.example.campuspulseai.southbound.repository.ISurveyQuestionRepository;
import com.example.campuspulseai.southbound.repository.ISurveyUserAnswersRepository;
import com.example.campuspulseai.southbound.specification.IEventSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRecommendationServiceImpl implements IEventRecommendationService {

    private final IAuthUtils authUtils;
    private final ISurveyUserAnswersRepository surveyUserAnswersRepository;
    private final ISurveyQuestionRepository surveyQuestionRepository;
    private final IQuestionChoicesRepository questionChoicesRepository;
    private final IAiService aiService;
    private final IEventRepository eventRepository;
    private final IEventSpecifications eventSpecifications;
    private final EventMapper eventMapper;

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

    public List<SuggestedOrganizerEvent> getSuggestedOrganizerEvents() {
        List<SurveyUserAnswers> allUserAnswers = surveyUserAnswersRepository.findAll();
        List<SurveyQuestion> questions = surveyQuestionRepository.findAll();
        List<QuestionChoices> choices = questionChoicesRepository.findAll();

        String prompt = buildEventRecommendationPromptForOrganizers(
                questions,
                choices,
                allUserAnswers,
                Arrays.stream(Category.values()).toList());

        String response = aiService.chat(prompt);

        return splitSuggestedEventsForCreationResponse(response);
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

    private String buildEventRecommendationPromptForOrganizers(List<SurveyQuestion> questions,
                                                               List<QuestionChoices> choices,
                                                               List<SurveyUserAnswers> userAnswers,
                                                               List<Category> categories) {
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("You are an event recommendation assistant.\n");
        promptBuilder.append("The goal is to generate 3 creative event ideas that best match the user's survey answers.\n");
        promptBuilder.append("Each event idea must contain:\n");
        promptBuilder.append("- a short engaging TITLE\n");
        promptBuilder.append("- a concise DESCRIPTION (1–2 sentences)\n");
        promptBuilder.append("- the most relevant CATEGORY (chosen strictly from the provided enum categories)\n\n");

        // Append survey questions & choices
        promptBuilder.append("### Survey Questions & Choices:\n");
        for (SurveyQuestion q : questions) {
            promptBuilder.append("{")
                    .append("\"id\": ").append(q.getId()).append(", ")
                    .append("\"question\": \"").append(q.getQuestionText()).append("\"")
                    .append(", \"choices\": [");
            choices.stream()
                    .filter(c -> c.getQuestion().getId().equals(q.getId()))
                    .forEach(c -> promptBuilder.append("{\"id\": ").append(c.getId())
                            .append(", \"choice\": \"").append(c.getChoice()).append("\"}, "));
            if (promptBuilder.charAt(promptBuilder.length() - 2) == ',') {
                promptBuilder.delete(promptBuilder.length() - 2, promptBuilder.length()); // remove trailing comma
            }
            promptBuilder.append("]}\n");
        }

        // Append user answers
        promptBuilder.append("\n### User Answers:\n[");
        for (SurveyUserAnswers ans : userAnswers) {
            promptBuilder.append(ans.getQuestionAnswers()).append(", ");
        }
        if (promptBuilder.charAt(promptBuilder.length() - 2) == ',') {
            promptBuilder.delete(promptBuilder.length() - 2, promptBuilder.length()); // remove trailing comma
        }
        promptBuilder.append("]\n\n");

        // Append enum categories
        promptBuilder.append("### Event Categories (enum values):\n[");
        for (Category c : categories) {
            promptBuilder.append("\"").append(c.getCategory()).append("\", ");
        }
        if (promptBuilder.charAt(promptBuilder.length() - 2) == ',') {
            promptBuilder.delete(promptBuilder.length() - 2, promptBuilder.length());
        }
        promptBuilder.append("]\n\n");

        // Append task
        promptBuilder.append("### Task:\n")
                .append("Based on the survey answers, suggest 3 new event ideas.\n")
                .append("Return the result STRICTLY in the format:\n")
                .append("title&description&category@title&description&category@title&description&category\n")
                .append("DO NOT forget the format separate the 3 suggested event objects with '&' and separate the suggested events with '@'\n")
                .append("do not include At('@') or ampersands('&') in the title, description, or category.\n")
                .append("Do not include explanations or any extra text.\n");

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
                    .append("\"question\": \"").append(q.getQuestionText()).append("\"")
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
