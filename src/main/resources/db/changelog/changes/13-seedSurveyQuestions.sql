-- liquibase formatted sql

-- changeset abdelrahman:insert-survey-questions
INSERT INTO survey_questions (question, allow_Mulltiple_Answers)
VALUES ('What categories of events are you most interested in?', TRUE),
       ('What type of event format do you prefer?', FALSE),
       ('What time of day do you usually attend events?', FALSE),
       ('Do you prefer networking/social events or learning/knowledge-sharing events?', FALSE),
       ('What size of events do you feel most comfortable with?', FALSE);

-- changeset abdelrahman:insert-question-choices
-- Question 1: Categories
INSERT INTO questions_choices (question_id, choice)
VALUES (1, 'Music'),
       (1, 'Technology'),
       (1, 'Sports'),
       (1, 'Art'),
       (1, 'Business'),
       (1, 'Health & Wellness');

-- Question 2: Formats
INSERT INTO questions_choices (question_id, choice)
VALUES (2, 'Workshop'),
       (2, 'Conference'),
       (2, 'Concert'),
       (2, 'Meetup');

-- Question 3: Time of Day
INSERT INTO questions_choices (question_id, choice)
VALUES (3, 'Morning'),
       (3, 'Afternoon'),
       (3, 'Evening');

-- Question 4: Purpose
INSERT INTO questions_choices (question_id, choice)
VALUES (4, 'Networking / Socializing'),
       (4, 'Learning / Knowledge');

-- Question 5: Event Size
INSERT INTO questions_choices (question_id, choice)
VALUES (5, 'Small (up to 50 people)'),
       (5, 'Medium (50-200 people)'),
       (5, 'Large (200+ people)');
