CREATE DATABASE IF NOT EXISTS jeopardywebapp;
USE jeopardywebapp;

DELETE FROM UserInfo
WHERE fName IN ('Alice', 'Brian', 'Carla', 'David', 'Emma');
select * from UserInfo;

-- password is password123!
INSERT INTO UserInfo (userId, fName, lName, username, email, passwordHash) VALUES
('a1c2e3f4-1234-4321-abcd-001122334455', 'Alice',  'Johnson', 'alicej', 'alice@example.com',
 '$2a$10$b5fY7eHdbCMptG5Vn9Yw0e0pWn00ZAZt0.puWZP1H35ROqEX3p6pS'),

('b2d3f4e5-2345-5432-bcde-112233445566', 'Brian', 'Stevens', 'brianst', 'brian@example.com',
 '$2a$10$b5fY7eHdbCMptG5Vn9Yw0e0pWn00ZAZt0.puWZP1H35ROqEX3p6pS'),

('c3e4f5d6-3456-6543-cdef-223344556677', 'Carla', 'Ramirez', 'carlar', 'carla@example.com',
 '$2a$10$b5fY7eHdbCMptG5Vn9Yw0e0pWn00ZAZt0.puWZP1H35ROqEX3p6pS'),

('d4f5e6c7-4567-7654-def0-334455667788', 'David', 'Nguyen', 'dnguyen', 'david@example.com',
 '$2a$10$b5fY7eHdbCMptG5Vn9Yw0e0pWn00ZAZt0.puWZP1H35ROqEX3p6pS'),

('e5f6c7d8-5678-8765-ef01-445566778899', 'Emma', 'Peterson', 'emmap', 'emma@example.com',
 '$2a$10$b5fY7eHdbCMptG5Vn9Yw0e0pWn00ZAZt0.puWZP1H35ROqEX3p6pS');

INSERT INTO Game (gameId, userId, gameName) VALUES
('11111111-1111-1111-1111-111111111111', 'a1c2e3f4-1234-4321-abcd-001122334455', 'General Knowledge Trivia'),
('22222222-2222-2222-2222-222222222222', 'b2d3f4e5-2345-5432-bcde-112233445566', 'Science Challenge'),
('33333333-3333-3333-3333-333333333333', 'c3e4f5d6-3456-6543-cdef-223344556677', 'History Master Quiz'),
('44444444-4444-4444-4444-444444444444', 'd4f5e6c7-4567-7654-def0-334455667788', 'Movie & TV Trivia'),
('55555555-5555-5555-5555-555555555555', 'e5f6c7d8-5678-8765-ef01-445566778899', 'Sports Trivia');

INSERT INTO JeopardyCategory (categoryId, gameId, categoryName, bkgColor, textColor) VALUES
-- Game 1: General Knowledge
('cat-g1-1', '11111111-1111-1111-1111-111111111111', 'Geography', '#003f5c', '#ffffff'),
('cat-g1-2', '11111111-1111-1111-1111-111111111111', 'Animals', '#58508d', '#ffffff'),
('cat-g1-3', '11111111-1111-1111-1111-111111111111', 'Food', '#bc5090', '#ffffff'),

-- Game 2: Science
('cat-g2-1', '22222222-2222-2222-2222-222222222222', 'Biology', '#003f5c', '#ffffff'),
('cat-g2-2', '22222222-2222-2222-2222-222222222222', 'Chemistry', '#58508d', '#ffffff'),
('cat-g2-3', '22222222-2222-2222-2222-222222222222', 'Physics', '#bc5090', '#ffffff'),

-- Game 3: History
('cat-g3-1', '33333333-3333-3333-3333-333333333333', 'Ancient', '#003f5c', '#ffffff'),
('cat-g3-2', '33333333-3333-3333-3333-333333333333', 'Medieval', '#58508d', '#ffffff'),
('cat-g3-3', '33333333-3333-3333-3333-333333333333', 'Modern', '#bc5090', '#ffffff'),

-- Game 4: Movies & TV
('cat-g4-1', '44444444-4444-4444-4444-444444444444', 'Actors', '#003f5c', '#ffffff'),
('cat-g4-2', '44444444-4444-4444-4444-444444444444', 'Movies', '#58508d', '#ffffff'),
('cat-g4-3', '44444444-4444-4444-4444-444444444444', 'TV Shows', '#bc5090', '#ffffff'),

-- Game 5: Sports
('cat-g5-1', '55555555-5555-5555-5555-555555555555', 'Football', '#003f5c', '#ffffff'),
('cat-g5-2', '55555555-5555-5555-5555-555555555555', 'Basketball', '#58508d', '#ffffff'),
('cat-g5-3', '55555555-5555-5555-5555-555555555555', 'Olympics', '#bc5090', '#ffffff');

UPDATE QnAInfo
SET ptValue = 100,
    questionText = 'What is the largest ocean?',
    answerText = 'Pacific Ocean'
WHERE qnaId = 'f9092890-b0e6-4599-ac70-6a317620e410';



UPDATE QnAInfo
SET ptValue = 200,
    questionText = 'What is the fastest land animal?',
    answerText = 'Cheetah'
WHERE qnaId = '5096086b-e042-40fa-b973-403d5bbb583a';

UPDATE QnAInfo
SET ptValue = 100,
    questionText = 'What animal is known as the King of the Jungle?',
    answerText = 'Lion'
WHERE qnaId = '398151ca-06ec-4b07-b760-fda12980a515';

UPDATE QnAInfo
SET ptValue = 100,
    questionText = 'What food is known as the "fruit of the gods"?',
    answerText = 'Fig'
WHERE qnaId = '7d1d21af-9374-453b-b04b-29fd813f29c6';


INSERT INTO Dashboard (dashboardId, userId, gameId) VALUES
('dash-1', 'a1c2e3f4-1234-4321-abcd-001122334455', '11111111-1111-1111-1111-111111111111'),
('dash-2', 'b2d3f4e5-2345-5432-bcde-112233445566', '22222222-2222-2222-2222-222222222222'),
('dash-3', 'c3e4f5d6-3456-6543-cdef-223344556677', '33333333-3333-3333-3333-333333333333'),
('dash-4', 'd4f5e6c7-4567-7654-def0-334455667788', '44444444-4444-4444-4444-444444444444'),
('dash-5', 'e5f6c7d8-5678-8765-ef01-445566778899', '55555555-5555-5555-5555-555555555555');


select * from Game;
select * from JeopardyCategory;
select * from QnAInfo;


-- todo: update all tables 

CREATE TABLE IF NOT EXISTS UserInfo (
  userId CHAR(6) PRIMARY KEY,
  fName VARCHAR(225) NOT NULL,
  lName VARCHAR(225) NOT NULL,
  username VARCHAR(60) UNIQUE NOT NULL,
  email VARCHAR(100),
  password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Game (
  gameId CHAR(6) PRIMARY KEY,
  userId CHAR(6) NOT NULL,
  gameName VARCHAR(255),
  FOREIGN KEY (userId) REFERENCES UserInfo(userId) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS JeopardyCategory (
  categoryId CHAR(6) PRIMARY KEY,
  gameId CHAR(6) NOT NULL,
  categoryName VARCHAR(100) NOT NULL,
  bkgColor VARCHAR(20),
  textColor VARCHAR(20),
  FOREIGN KEY (gameId) REFERENCES Game(gameId) ON DELETE CASCADE
);

-- condensed question data and answer data into 1 table
CREATE TABLE IF NOT EXISTS QnAInfo (
  qnaId CHAR(6) PRIMARY KEY,
  categoryId CHAR(6) NOT NULL,
  pointValue INT NOT NULL,
  questionText VARCHAR(2000),
  answerText VARCHAR(2000),
  questionImageUrl VARCHAR(1000),
  questionImagePosition VARCHAR(50),
  questionImageScale VARCHAR(50),
  answerImageUrl VARCHAR(1000),
  answerImagePosition VARCHAR(50),
  answerImageScale VARCHAR(50),
  FOREIGN KEY (categoryId) REFERENCES JeopardyCategory(categoryId) ON DELETE CASCADE
);


select * from UserInfo;
select * from Game;
select * from JeopardyCategory;
select * from QnAInfo;
select * from Dashboard;
-- select * from CurrentGame;
-- select * from Teams;
-- select * from TeamsCorrectQnA;


-- create table UserInfo (
--     -- userId example: U01234
--     -- U is always a part if id
--     userId char(6) primary key,
--     fName varchar(225) not null,
--     lName varchar(225) not null,
--     username varchar(30) not null,
--     email varchar(30) not null,
--     password varchar (10) not null
-- );

-- create table Game (
--     -- gameId example: G01234
--     -- G is always a part of id 
--     gameId char(6) primary key,
--     userId char(6) not null,
--     foreign key (userId) 
--         references UserInfo(userId)
-- );

-- create table JeopardyCategory (
--     -- categoryId example: C01234
--     -- C is always a part of id
--     categoryId char(6) primary key,
--     gameId char(6) not null,
--     categoryName varchar(100) not null,
--     foreign key (gameId)
--         references Game(gameId)
-- );


-- alter table JeopardyCategory
-- add bgkColor char(6) not null,
-- add textColor char(6) not null;

-- create table QnAInfo (
--     -- qnaId example: Q01234
--     -- Q is always a part of id
--     qnaId char(6) primary key,
--     categoryId char(6) not null,
--     -- questionId example: QT1234
--     questionId char(6) not null unique,
--     -- answerId example: AT1234
--     answerId char(6) not null unique,
--     questionText varchar(500) not null,
--     answerText varchar(500) not null,
--     -- should have id's for questions and answers separatly for images 
--     foreign key (categoryId) 
--         references JeopardyCategory(categoryId)
-- );

-- create table QuestionImage (
--     -- imageId example: QI1234
--     -- QI is always a part of id
--     questionImageId char(6) primary key,
--     questionId char(6) not null,
--     imageURL varchar(500) not null,
--     imagePosition varchar(20),
--     foreign key (questionId) 
--         references QnAInfo(questionId)
-- );

-- create table AnswerImage (
--     -- imageId example: AI1234
--     -- AI is always a part of id
--     answerImageId char(6) primary key,
--     answerId char(6) not null,
--     imageURL varchar(500) not null,
--     imagePosition varchar(20),
--     foreign key (answerId) 
--         references QnAInfo(answerId)
-- );

-- create table Dashboard (
--     -- might be missing some UserInfo
--     -- dashboardId example: D01234
--     dasboardId char(6) primary key,
--     userId char(6) not null,
--     gameId char(6) not null,
--     foreign key (userId)
--         references UserInfo,
--     foreign key (gameId) 
--         references Game(gameId)
-- );

-- create table CurrentGame (
--     -- currentGameId example: CG01234
--     -- CG is always a part of id
--     currentGameId char(7) primary key,
--     gameId char(6) not null,
--     foreign key (gameId) 
--         references Game(gameId)
-- );

-- create table Teams (
--     -- teamId example: T01234
--     teamId char(6) primary key,
--     currentGameId char(7) not null,
--     teamName varchar(50) not null,
--     points int default 0,
--     foreign key (currentGameId)
--         references CurrentGame(currentGameId)
-- );

-- create table TeamsCorrectQnA (
--     teamId char(6) not null,
--     qnaId char(6) not null,
--     primary key (teamId, qnaId),
--     foreign key (teamId) 
--         references Teams(teamId),
--     foreign key (qnaId) 
--         references QnAInfo(qnaId)
-- );