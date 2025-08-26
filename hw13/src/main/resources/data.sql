insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into book_comments(text, book_id)
values ('comment_text_1_1', 1), ('comment_text_1_2', 1), ('comment_text_1_3', 1),
       ('comment_text_2_1', 2), ('comment_text_2_2', 2), ('comment_text_3_1', 3);

insert into users (username, password)
values
    ('user', '$2a$13$XW59Z6DCfyq8xomwlxjiC.DYBMpdLuot.wMne.V7kEE1ilvlmMmlu'),
    ('lib', '$2a$13$3EBATJ0h6jFjViJVrrT4gOrmbg8Nc00h0upLTRMkSV.a9ZWyDtaqG');

insert into authorities(authority)
values ('ROLE_BOOK_EDITOR'),
       ('ROLE_COMMENT_EDITOR');

insert into user_authority_link(user_id, authority_id)
values
    (1, 2), -- user - COMMENT_EDITOR
    (2, 1); -- lib - BOOK_EDITOR

INSERT INTO acl_sid (principal, sid)
VALUES
    (1, 'user'), -- 1
    (1, 'lib'), -- 2
    (0, 'ROLE_COMMENT_EDITOR'); -- 3

INSERT INTO acl_class (class)
VALUES ('ru.otus.hw.dto.CommentDto');

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
VALUES
    (1, 1, NULL, 1, 0), -- 1
    (1, 2, NULL, 1, 0), -- 2
    (1, 3, NULL, 1, 0), -- 3
    (1, 4, NULL, 2, 0), -- 4
    (1, 5, NULL, 2, 0), -- 5
    (1, 6, NULL, 2, 0); -- 6

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
VALUES
    (1, 1, 1, 1, 1, 1, 1),
    (1, 2, 1, 2, 1, 1, 1),
    (1, 3, 3, 1, 1, 1, 1),
    (2, 1, 1, 1, 1, 1, 1),
    (2, 2, 1, 2, 1, 1, 1),
    (2, 3, 3, 1, 1, 1, 1),
    (3, 1, 1, 1, 1, 1, 1),
    (3, 2, 1, 2, 1, 1, 1),
    (3, 3, 3, 1, 1, 1, 1),
    (4, 1, 2, 1, 1, 1, 1),
    (4, 2, 2, 2, 1, 1, 1),
    (4, 3, 3, 1, 1, 1, 1),
    (5, 1, 2, 1, 1, 1, 1),
    (5, 2, 2, 2, 1, 1, 1),
    (5, 3, 3, 1, 1, 1, 1),
    (6, 1, 2, 1, 1, 1, 1),
    (6, 2, 2, 2, 1, 1, 1),
    (6, 3, 3, 1, 1, 1, 1);
