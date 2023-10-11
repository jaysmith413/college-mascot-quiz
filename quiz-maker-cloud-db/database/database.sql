BEGIN TRANSACTION;

DROP TABLE IF EXISTS leaderboard;

CREATE TABLE leaderboard(
    player_id serial PRIMARY KEY,
    player_name varchar(50) NOT NULL,
    score int NOT NULL,
    date date NOT NULL
);

COMMIT;

BEGIN TRANSACTION;

DROP TABLE IF EXISTS answer;

CREATE TABLE answer(
    answer_id serial PRIMARY KEY,
    answer_text varchar(50) NOT NULL
);

INSERT INTO answer
(answer_text)
VALUES ('Bears'),('Orange'),('Gators'),('Seminoles'),('Tigers'),('Razorbacks'),('Hurricanes'),('Bulldogs'),
('Elephants'),('Crimson Tide'),('Dolphins'),('Rattlers'),('Cowboys'),('Texans'),('Longhorns'),('Mavericks'),
('Geese'),('Ducks'),('Chickens'),('Peacocks'),('Fighting Irish'),('Colts'),('Bengals'),('Sooners'),('Red'),
('Wildcats'),('Red Raiders'),('Eagles'),('Cornhuskers'),('Hawks'),('Cyclones'),('Hawkeyes'),('Bruins'),('Islanders'),
('Bearcats'),('Mean Green'),('Panthers'),('Bobcats'),('Titans'),('Blazers'),('Rams');


COMMIT;

BEGIN TRANSACTION;

DROP TABLE IF EXISTS question;

CREATE TABLE question(
    question_id serial PRIMARY KEY,
    question_text varchar(100) NOT NULL,
    answer_id int NOT NULL,
	CONSTRAINT fk_answer_id FOREIGN KEY (answer_id)
		REFERENCES answer(answer_id)
);

INSERT INTO question
(question_text, answer_id)
VALUES
('Florida State:', 4), ('Arkansas:', 6), ('Alabama:', 10), ('Texas:', 15), ('Oregon:', 18), ('Notre Dame:', 21),
('Florida:', 3), ('Oklahoma:', 24), ('Texas Tech:', 27), ('Nebraska:', 29), ('Iowa:', 32), ('UCLA:', 33),
('Cincinnati:', 35), ('North Texas:', 36), ('Texas State:', 38), ('Alabama Birmingham:', 40),
('Florida A&M:', 12), ('Central Arkansas:', 1);

COMMIT;