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

// expects localStorage,userId set after login
const userId = localStorage.getItem("userId");

if (!userId) {
  // redirect to login
  // todo: comment out if unwanted
  console.warn('No userId in storage; dashboard may be empty.');
}
// TODO: uncomment once you have a functioning userAuthen
// window.location.href = "../index.html";

// Function that loads existing game wehn editing
async function loadGames() {
  const container = document.querySelector('#gamesContainer');
  container.innerHTML = 'Loading...';
  try {
    const res = await fetch(`/api/game/user/${encodeURIComponent(userId)}`);
    if (!res.ok) throw new Error(await res.text());
    const games = await res.json();
    if (!games.length) {
      container.innerHTML = '<p>No saved games.</p>';
      return;
    }
    container.innerHTML = '';
    games.forEach(g => {
      const el = document.createElement('div');
      el.className = 'gameCard';
      el.innerHTML = `
        <div class="gameCardHeader">
          <strong>${g.gameName || 'Untitled'}</strong>
          <div class="gameCardButtons">
            <button class="editBtn" data-gameid="${g.gameId}">Edit</button>
            <button class="playBtn" data-gameid="${g.gameId}">Play</button>
            <button class="delBtn" data-gameid="${g.gameId}">Delete</button>
          </div>
        </div>
        <div class="gameCardBody">
          <small>Game ID: ${g.gameId}</small>
        </div>
      `;
      container.appendChild(el);
    });
  } catch (err) {
    container.innerHTML = `<p>Error loading games: ${err.message}</p>`;
  }
}

const gameContainer = document.querySelector("gameScene");


// Event delegation for edit, play, and delete buttons
document.addEventListener("click", async (e) => {
  const btn = e.target;
  if (t.classList.contains('editBtn')) {
    const gameId = t.dataset.gameid;
    // navigate to gameCreate page with gameId query param (edit mode)
    window.location.href = `/gameCreate/gameContent.html?gameId=${encodeURIComponent(gameId)}`;
  }

  // Play function
  // TODO: Don't have a gamePlay folder just yet
  if (t.classList.contains('playBtn')) {
    const gameId = t.dataset.gameid;
    // navigate to play mode
    window.location.href = `/play/playMode.html?gameId=${encodeURIComponent(gameId)}`;
  }

  // Delete function
  if (t.classList.contains('delBtn')) {
    const gameId = t.dataset.gameid;
    const ok = confirm('Delete this game? This cannot be undone.');
    if (!ok) return;
    try {
      const res = await fetch(`/api/game/${encodeURIComponent(gameId)}`, { method: 'DELETE' });
      if (!res.ok) throw new Error(await res.text());
      alert('Deleted');
      loadGames();
    } catch (err) {
      alert('Delete failed: ' + err.message);
    }
  }
});

document.addEventListener('DOMContentLoaded', loadGames);
