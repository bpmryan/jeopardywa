use jeopardywebapp;

select * from UserInfo;
select * from Game;
select * from JeopardyCategory;
select * from QnAInfo;
select * from QuestionImage;
select * from AnswerImage;
select * from Dashboard;
select * from CurrentGame;
select * from Teams;
select * from TeamsCorrectQnA;

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