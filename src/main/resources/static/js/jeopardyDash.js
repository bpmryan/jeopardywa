/* 
 * Functions that: 
 *  dynamically generate the cards of each game the player has made
 *  Edit button allows the user to continue to edit their saved game
 *  Play button allows the user to present their jeopardy
 *  Trash button delete whole game
 *  Possibily adding the function to dynamically generate grid here 
*/  

const gameCardString =    
    `<div class="card">
            <label class="card-label"></label>
            <div class="cardOptions">
                <button class="">Edit</button>
                <button class="">Play</button>
                <button>Trash</button>
            </div>

        </div>`;

const gameContainer = document.querySelector("gameScene");
gameContainer.innerHTML = gameCardString; //replace loading with gameCardString
