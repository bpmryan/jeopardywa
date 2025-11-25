/*
 * Functions that:
 *  dynamically generate the cards of each game the player has made
 *  Edit button allows the user to continue to edit their saved game
 *  Play button allows the user to present their jeopardy
 *  Delete button delete whole game
 *  Possibily adding the function to dynamically generate grid here
 *
 * Collects: gameId, gameName, userId
 * TODO: create an attribute in game table for gameName
 */

const userId = localStorage.getItem("userId");
if (!userId) {
  window.location.href = "/index.html";
}
// TODO: uncomment once you have a functioning userAuthen
// window.location.href = "../index.html";

const gameContainer = document.querySelector("gameScene");

// document.addEventListener("DOMContentLoaded", async () => {
//   const params = new URLSearchParams(window.location.search);
//   const gameId = params.get("gameId");

//   if (gameId) {
//     console.log("Loading game:", gameId);
//     await loadExistingGame(gameId);
//   }
// });

// Function that loads existing game wehn editing
async function loadExistingGame(gameId) {
  const response = await fetch(`/api/game/full/${gameId}`);
  if (!response.ok) {
    gameContainer.innerHTML = "<p>Error loading</p>";
    return;
  }
  const games = await response.json();
  if (!game.length) {
    gameContainer.innerHTML = "<p>No games </p>";
    return;
  }

  gameContainer.innerHTML = games
    .map(
      (g) => `
    <div class="card" data-id="${g.gameid}">
        <label class="card-label">${game.gameName || "Untitled Game"}</label>
        <div class="cardOptions">
            <button class="editBtn" data-id="${game.gameId}">Edit</button>
            <button class="playBtn" data-id="${game.gameId}">Play</button>
            <button class="trashBtn" data-id="${game.gameId}">Trash</button>
        </div>
    </div>
    `
    )
    .join("");
}

// Event delegation for edit, play, and delete buttons
document.addEventListener("click", async (e) => {
  const btn = e.target;
  const id = btn.dataset.id;
  if (!id) return;

  // Edit function
  // Sends user to the game they want to edit
  if (btn.classList.contains("editBtn")) {
    window.location.href = `/gameCreate/gameContent.html?gameId=${id}`;
  }

  // Play function
  // TODO: Don't have a gamePlay folder just yet
  if (btn.classList.contains("playBtn")) {
    window.location.href = `/playgame/playGame.html?gameId=${id}`;
  }

  // Delete function
  if (btn.classList.contains("trashBtn")) {
    const ok = confirm("Delete game permanently?");
    if (!ok) return;
    const res = await fetch(`/api/game/${id}`, { method: "DELETE" });
    if (res.ok) loadGames();
    else alert("Delete failed");
  }
});

loadGames();
