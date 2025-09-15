insert into authors(full_name)
values ('Author_1');

insert into genres(name)
values ('Genre_1');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1);

insert into book_comments(text, book_id)
values ('comment_text_1_1', 1);
