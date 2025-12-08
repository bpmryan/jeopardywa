-- update_qna.sql
-- Back up before running. This file contains idempotent UPDATE statements
-- that update rows by qnaId and example category updates.

START TRANSACTION;

-- Example: attempted update you tried (fixed column name to `pointValue`)
UPDATE QnAInfo
SET pointValue = 100,
    questionText = 'What is the largest ocean?',
    answerText = 'Pacific Ocean'
WHERE qnaId = 'f9092890-b0e6-4599-ac70-6a317620e410';

-- UPDATEs for the rows inserted in your session
UPDATE QnAInfo
SET pointValue = 100,
    questionText = 'What animal is known as the King of the Jungle?',
    answerText = 'Lion'
WHERE qnaId = '398151ca-06ec-4b07-b760-fda12980a515';

UPDATE QnAInfo
SET pointValue = 200,
    questionText = 'What is the fastest land animal?',
    answerText = 'Cheetah'
WHERE qnaId = '5096086b-e042-40fa-b973-403d5bbb583a';

UPDATE QnAInfo
SET pointValue = 100,
    questionText = 'What food is known as the "fruit of the gods"?',
    answerText = 'Fig'
WHERE qnaId = '7d1d21af-9374-453b-b04b-29fd813f29c6';

-- Example bulk update by categoryId (adjust categoryId as needed)
-- UPDATE QnAInfo
-- SET pointValue = 100
-- WHERE categoryId = '6973ebee-4748-40d0-9271-3c7bfc0eeb7a';

COMMIT;

-- Quick verification selects (run after executing updates)
SELECT * FROM QnAInfo
WHERE qnaId IN (
  'f9092890-b0e6-4599-ac70-6a317620e410',
  '398151ca-06ec-4b07-b760-fda12980a515',
  '5096086b-e042-40fa-b973-403d5bbb583a',
  '7d1d21af-9374-453b-b04b-29fd813f29c6'
);
