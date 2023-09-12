-- create quiz table
CREATE TABLE IF NOT EXISTS QUIZ (
    id serial PRIMARY KEY,
    question text,
    answer boolean,
    author varchar(20)
)