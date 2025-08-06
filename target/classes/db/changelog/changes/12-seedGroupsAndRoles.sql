insert into roles
values (1, 'ROLE_STUDENT'),
       (2, 'ROLE_ORGANIZER');
insert into groups
values (1, 'GROUP_STUDENTS'),
       (2, 'GROUP_ORGANIZERS');
insert into group_roles
values (1, 1),
       (2, 1),
       (2, 2);