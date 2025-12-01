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
async function fetchGamesByUser(userId) {
  const res = await fetch(`/api/game/user/${encodeURIComponent(userId)}`);
  if (!res.ok) throw new Error(await res.text());
  return await res.json();
}
// TODO: uncomment once you have a functioning userAuthen
// window.location.href = "../index.html";

function createGameCard(game) {
  const el = document.createElement('div');
  el.className = 'gameCard';
  el.innerHTML = `
    <div class="cardHeader"><strong>${game.gameName || 'Untitled'}</strong></div>
    <div class="cardBody">
      <div>Game ID: ${game.gameId}</div>
      <div class="cardOptions">
        <button class="editBtn" data-gameid="${game.gameId}">Edit</button>
        <button class="playBtn" data-gameid="${game.gameId}">Play</button>
        <button class="deleteBtn" data-gameid="${game.gameId}">Delete</button>
      </div>
    </div>
  `;
  return el;
}

// loads all games based of the gameId's linked to the userId
async function loadDashboard() {
  const userId = localStorage.getItem('userId');
  if (!userId) {
    document.querySelector('gameScene').innerHTML = '<p>Please log in.</p>';
    return;
  }

  const container = document.querySelector('gameScene');
  container.innerHTML = '<p>Loading...</p>';
  try {
    const games = await fetchGamesByUser(userId);
    if (games.length === 0) {
      container.innerHTML = '<p>No games yet. Create one!</p>';
      return;
    }
    container.innerHTML = '';
    games.forEach(g => container.appendChild(createGameCard(g)));
  } catch (err) {
    container.innerHTML = `<p>Error: ${err.message}</p>`;
  }
}

// Event delegation for edit, play, and delete buttons
document.addEventListener('click', async (e) => {
  const t = e.target;
  if (t.classList.contains('editBtn')) {
    const id = t.dataset.gameid;
    window.location.href = `/gameCreate/gameContent.html?gameId=${encodeURIComponent(id)}`;
  }
  if (t.classList.contains('playBtn')) {
    const id = t.dataset.gameid;
    window.location.href = `/play/playMode.html?gameId=${encodeURIComponent(id)}`;
  }
  if (t.classList.contains('deleteBtn')) {
    const ok = confirm('Delete this game permanently?');
    if (!ok) return;
    const id = t.dataset.gameid;
    const res = await fetch(`/api/game/${encodeURIComponent(id)}`, { method: 'DELETE' });
    if (!res.ok) { alert('Delete failed'); return; }
    // remove card visually
    t.closest('.gameCard').remove();
  }
});

document.addEventListener('DOMContentLoaded', loadDashboard);
