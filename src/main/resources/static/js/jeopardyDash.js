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

document.addEventListener("DOMContentLoaded", async () => {
  const params = new URLSearchParams(window.location.search);
  const gameId = params.get("gameId");

  if (gameId) {
    console.log("Loading game:", gameId);
    await loadExistingGame(gameId);
  }
});

// Function that loads existing game wehn editing
async function loadExistingGame(gameId) {
  const response = await fetch(`/api/game/full/${gameId}`);
  const data = await response.json();

  const main = document.getElementById("mainContainer");
  main.innerHTML = ""; // Clear existing

  for (const category of data.categories) {
    const categoryHTML = await loadPartial("../gameCreate/CategoryItem.html");
    main.insertAdjacentHTML("beforeend", categoryHTML);

    const categoryCard = main.lastElementChild;

    // Fill category fields
    categoryCard.querySelector(".categoryName").value = category.categoryName;
    categoryCard.querySelector("#bgkColor").value = category.bkgColor;
    categoryCard.querySelector("#textColor").value = category.textColor;
    categoryCard.dataset.categoryId = category.categoryId;

    const qnaContainer = categoryCard.querySelector(".qnaContainer");

    for (const q of category.qna) {
      const qnaHTML = await loadPartial("../gameCreate/QnAItems.html");
      qnaContainer.insertAdjacentHTML("beforeend", qnaHTML);

      const qnaCard = qnaContainer.lastElementChild;

      qnaCard.querySelector("#ptValue").value = q.pointValue;
      qnaCard.querySelector("#questionText").value = q.question;
      qnaCard.querySelector("#answerText").value = q.answer;

      // Question image
      qnaCard.querySelector(".questionImageUrl").value =
        q.questionImageUrl ?? "";
      qnaCard.querySelector(".questionImagePosition").value =
        q.questionImagePosition ?? "";
      qnaCard.querySelector(".questionImageScale").value =
        q.questionImageScale ?? "";

      // Answer image
      qnaCard.querySelector(".answerImageUrl").value = q.answerImageUrl ?? "";
      qnaCard.querySelector(".answerImagePosition").value =
        q.answerImagePosition ?? "";
      qnaCard.querySelector(".answerImageScale").value =
        q.answerImageScale ?? "";
    }

    renumberQnA(categoryCard);
  }
}

// still needs userId
const userId = localStorage.getItem("userId");
if (!userId) {
  alert("Error: No user Id found. Please log in again");
  // TODO: uncomment once you have a functioning userAuthen
  // window.location.href = "../index.html";
}

// load all games and display them in the dashboard
async function loadUserGames() {
  const gameScene = document.querySelector("gameScene");
  gameScene.innerHTML = "loading...";
  try {
    // finds userId and the games associated with that user
    const response = await fetch(`/api/game/user/${userId}`);
    if (!response.ok) throw new Error("Failed to load games");

    const games = await res.json();

    // if no games exist for the user, then they get the default message
    if (games.length === 0) {
      gameScene.innerHTML = "<p>No games created yet.</p>";
      return;
    }
    gameContainer.innerHTML = games.map(buildGameCard).join("");
  } catch (err) {
    console.error(err);
    gameContainer.innerHTML = "<p>Error loading game(s).</p>";
  }
}

// pipes in gameCard as a string into the dashboard
function createGameCard(game) {
  const div = document.createElement("div");
  div.classList.add("card");

  div.innerHTML = `
        <label class="card-label">${game.gameName || "Untitled Game"}</label>

        <div class="cardOptions">
            <button class="editBtn" data-id="${game.gameId}">Edit</button>
            <button class="playBtn" data-id="${game.gameId}">Play</button>
            <button class="trashBtn" data-id="${game.gameId}">Trash</button>
        </div>
    `;

  return div;
}

// Event delegation for edit, play, and delete buttons
document.addEventListener("click", async (e) => {
  const btn = e.target;

  // Edit function
  // Sends user to the game they want to edit
  if (btn.classList.contains("editBtn")) {
    const id = btn.getAttribute("data-id");
    localStorage.setItem("editGameId", id); // store which game to edit
    window.location.href = "../gameCreate/gameContent.html";
  }

  // Play function
  // TODO: Don't have a gamePlay folder just yet
  if (e.target.classList.contains("playBtn")) {
    const id = btn.getAttribute("data-id");
    localStorage.setItem("playGameId", id);
    window.location.href = "../playgame/play.html";
  }

  // Delete function
  if (e.target.classList.contains("deleteBtn")) {
    const id = btn.getAttribute("data-id");

    const confirmDelete = confirm("Delete this game permanently?");
    if (!confirmDelete) return;

    try {
      const response = await fetch(`/api/game/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) throw new Error("Failed to delete game");

      loadGames(); // refresh dashboard
    } catch (err) {
      console.error(err);
      alert("Error deleting game.");
    }
  }
});

loadGames();
