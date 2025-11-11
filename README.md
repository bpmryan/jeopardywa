# README

I've given up on using JavaFX for frontend code, so now I'm switching it over to HTML5, CSS, and JS for frontend code. 
I'm using Java for backend code and connect the frontend to it via Spring Boot. 
MySql will still be where I'm storing my data into the database.

Previous attempts are the repos JeopardyApp and JeopardyProject (now dead projects)

## Github branch workflow
working --> merging --> testing --> main

- working: Where I write new code/ideas
- merging: Where I merge in code from working branch (that works)
- testing: Where I merge in code that should work when deployed
- main: Code that completely works and is ready to be deployed 

## Features 

- Creator 
    - Should be able to 
        - Create questions and answers
        - Add images 
        - Login and logout 
        - Save their work in their account

<br>

- Host/ Creator
    - Should have access to
        - A list of which questions have been answered 
        - Amount of points per team 
            - Leader board
            - Give an option to post the scores
            - Add the points to each team 
                - Have a way to manually add points and automatically give points to the correct team
    - Should be able to control everything 
    - List of questions each team has answered 

<br> 

- Player
    - Should be able to see
        - Question screen and board
        - Only see the answer after the host allows them to see it 
        - Which questions have been answered 
            - Red line crossed through
            - Button cannot be clicked 
            - Have a way to reset if misclicked
                - Double click to bring list of options to reset

<br>

- Question and Answer templates 
    - 2 slides (now templates)
        - Question
        - Answer 
        <!-- - Use 2 tags
            - Apparently can be used to for each button  -->
    - Should have a link back to the question board (button)
    - Should have the answer pop up after team answers it correctly or dead question
        - Button to move to answer
            - should be linked to the answer template
        - Return button if misclicked
            - should be linked to the question board
    - Add questions (txt), color background, color of the text, and pictures
        - Color background and text should be the same for each category
        - There should be an option for the user to change the color for both of them
        - User should be able to edit those 3 things

<br>

- Question board
    - There should a way the adjust the amount of categories for jeopardy
        - Multiple choice section or drop box
        - Options are from 5-8 categories 
        - Use switch-case syntax
    - Buttons for each question 
    - There should be a question that opens to the final jeopardy question

<br>

- Final Jeopardy Screen
    - Should have the 
        - Topic 
        - Question
        - Answer 
    - Buttons to move back and forth should be there 

<br>

- Other notes
    - Should have a method to save progress
        - Accounts?
    - Host vs Player screen 
    - Vietnamese library should be available in this program 
    - Answered questions should have a slashed though the numbers and a faded button

<br>

- Bonus / Future ideas:
    - Animated scene that gives a check mark if answered correctly or X if incorrect
        - Family feud animation 
        - Host will have access to the option to give the check mark or X
    - Host can split screen answer and question
    - Have a list of questions that each team answered correctly
    - Have a way for the user to download their game as a file to play without the need for internet 
    - Admin role to monitor what is being added to each jeopardy game.

<br> 

## Queries & Actions to Manipulate the Data
<ul>
    <li> Scores for each team</li>
        <ul> 
            <li> Database that saves the amount of points per team in each game</li>
        </ul>
 </ul>
 <ul>
    <li> Number of categories and questions per category for the game</li>
        <ul> 
            <li> Database that stores the user's input on category and question</li>
            <li> Use to create the questionBoard</li>
        </ul>
 </ul>
    <ul>
    <li> Questions and answers the user created in the templates</li>
        <ul> 
            <li> txt file that saves each one</li>

## Client and Server
- Client (frontend) is resources folder
- Server (backend) is java folder