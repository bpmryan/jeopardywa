/* 
 * Functions that: 
 *  dynamically generate the cards of each game the player has made
 *  Edit button allows the user to continue to edit their saved game
 *  Play button allows the user to present their jeopardy
 *  Trash button delete whole game
 *  Possibily adding the function to dynamically generate grid here 
*/  

// still needs userId 
const userId = localStorage.getItem("userId");
if(!userId){
    alert("Error: No user Id found. Please log in again");
}

// pipes in gameCard as a string into the dashboard
const gameCardString =    
    `<div class="card">
            <label class="card-label"></label>
            <div class="cardOptions">
                <button class="editBtn">Edit</button>
                <button class="playBtn">Play</button>
                <button class="deleteBtn">🗑 Delete</button>
            </div>

        </div>`;

const gameContainer = document.querySelector("gameScene");
gameContainer.innerHTML = gameCardString; //replace loading with gameCardString

// load all games and display them in the dashboard
async function loadUserGames() {
    try {
        // finds userId and the games associated with that user
        const res = await fetch('/api/game/user/${userId}');
        const games = await res.json();

        // if no games exist for the user, then they get the default message
        if (games.length === 0) {
            gameContainer.innerHTML = `<p>You have no saved games yet.</p>`;
            return;
        }

        gameContainer.innerHTML = games.map(buildGameCard).join("");

    } catch {
        console.error("Error loading game(s): ", err);
        gameContainer.innerHTML = "<p>Error loading game(s).</p>"
    }
} 

// Event delegation for edit, play, and delete buttons
document.addEventListener("click", async (e) => {
    const card = e.target.closest(".card");
    if (!card) return;

    const gameId = card.dataset.gameId;

    // Edit function
    // Sends user to the game they want to edit
    if (e.target.classList.contains(".editBtn")) {
        window.location.href = `../gameCreate/gameContent.html?gameId=${gameId}`;
    }

    // Play function
    // TODO: Don't have a gamePlay folder just yet
    if (e.target.classList.contains("playBtn")) {
        window.location.href = `../gamePlay/gameBoard.html?gameId=${gameId}`;
    }

    // Delete function
    if (e.target.classList.contains("deleteBtn")) {
        const confirmDelete = confirm("Are you sure you want to delete this game? This cannot be undone.");

        if (!confirmDelete) return;

        const deleted = await fetch(`/api/game/${gameId}`, {method: "DELETE"});
        if (deleted.ok) {
            card.remove;
        }
    }
})

loadUserGames();