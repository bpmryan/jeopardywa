use jeopardywebapp;

create table userInfo (
    fName char(225) not null,
    lName char(225) not null,
    username char(30) not null,
    email char(30) not null,
    password char (10)) not null,
    -- userId example: U01234
    -- U is always a part if id
    userId char(6) not null,
    primary key (userId)
);

create table jeopardyCategory (
    -- gameId example: G01234
    -- G is always a part of id 
    gameId char(6) not null,
    category char(100) not null, 
    -- categoryId example: C01234
    -- C is always a part of id
    categoryId char(6) not null, 
    foreign key (categoryId, gameId)
);

create table QnAInfo (
    userId char(6) not null,
    gameId char(6) not null,
    categoryId char(6) not null,
    question char(500) not null,
    answer char(500) not null,
    -- qnaId example: Q01234
    -- Q is always a part of id
    qnaId char(6) not null,
    foreign key (qnaId),
    primary key (userId)
        references userInfo(userId),     
    foreign key (categoryId, gameId)
        references jeopardyCategory(categoryId, gameId)
);

create table image (
    userId char(6) not null,
    categoryId char(6) not null,
    qnaId char(6) not null, 
    image char(500) not null,
    imagePosition char(10) not null, 
    -- imageId example: I01234
    -- I is always a part of id
    imageId char(6) not null,
    foreign key (imageId),
    primary key (userId)
        references userInfo,     
    foreign key (categoryId, gameId)
        references jeopardyCategory(categoryId, gameId)
    foreign key (qnaId)
        references QnAInfo(qnaId)
);

create table currentGame (
    gameId
)
