/*
 * Functions to present/play game
 */

// reads a value from the url query string
// extracts gameId from the url string when frontend calls for gameId
// uses URLSearchParams to read the url and find gameId
function getParam(name) {
  return new URLSearchParams(window.location.search).get(name);
}

/*
* fetches the complete "play mode" version of the game from Spring Boot backend
* retrieves: catgeory names, pt values, questions, answers, images, order of tiles (when dynamically generating board)
*/
async function loadGameForPlay(gameId) {
  const res = await fetch(`/api/game/play/${encodeURIComponent(gameId)}`);
  if (!res.ok) throw new Error(await res.text());
  return await res.json();
}

/*
 * dynamically generates grid 
 * one column per category
 * one tile per pt value
 * category names on top
 * each tile stores qna and image
 * 
 * converts JSON object into html for user to view
 */
function buildBoard(dto) {
  const board = document.getElementById('boardContainer');
  board.innerHTML = '';

  // clears board
  const categories = dto.categories || [];
  const grid = document.createElement('div');
  grid.className = 'boardGrid';

  // creates a column for each category 
  categories.forEach(cat => {
    const col = document.createElement('div');
    col.className = 'boardColumn';

    const header = document.createElement('div');
    header.className = 'categoryHeader';
    header.textContent = cat.categoryName;
    col.appendChild(header);

    // adds the category header (top of column)
    // sort qna by pointValue ascending (or as provided)
    const qnas = (cat.qna || []).sort((a,b) => a.pointValue - b.pointValue);
    qnas.forEach(q => {
      // creates buttons for each tile
      const tile = document.createElement('button');
      tile.className = 'tile';
      tile.dataset.qnaId = q.qnaId;
      tile.dataset.categoryId = cat.categoryId;
      tile.dataset.question = q.question || '';
      tile.dataset.answer = q.answer || '';
      tile.dataset.questionImage = q.questionImageUrl || '';
      tile.textContent = q.pointValue;
      tile.addEventListener('click', onTileClick);
      col.appendChild(tile);
    });

    columns.appendChild(col);
  });

  board.appendChild(columns);
}

/*
 * Reads the question data (questionId and questionText)
 * displays the modal popup
 * shows the question
 * shows the question image  
 * hides answer until "Reveal answer" has been clicked
 * 
 * mainly makes the tile open and show the question popup
*/
function onTileClick(evt) {
  // gets the data for the clicked tile
  const tile = evt.currentTarget;
  
  // pulls data fromm attributes
  const q = tile.dataset.question;
  const a = tile.dataset.answer;
  const img = tile.dataset.questionImage;
  
  // shows the category name inside the modal
  document.getElementById('modalCategory').textContent = tile.closest('.boardColumn').querySelector('.categoryHeader').textContent;
  
  // shows question text
  document.getElementById('modalQuestion').textContent = q;

  // handles image
  const imgDiv = document.getElementById('modalImage');
  imgDiv.innerHTML = '';
  if (img) {
    const i = document.createElement('img');
    i.src = img;
    i.style.maxWidth = '100%';
    imgDiv.appendChild(i);
  }

  // handles hiding and showing modal
  document.getElementById('modalAnswer').textContent = a;
  document.getElementById('modalAnswer').classList.add('hidden');
  document.getElementById('modal').classList.remove('hidden');
}

// runs automatically when the page loads
/*
* gets gameId
* loads game from API
* sets the title
* builds the board
*/ 

// finds the gameId from the url query from the top function of this file
document.addEventListener('DOMContentLoaded', async () => {
  const gameId = getParam('gameId');

  // back btn returns to dashboard
  document.getElementById('backBtn').addEventListener('click', () => window.location.href = '/jeopardyDash/Dashboard.html');

  // fetch the game and draw the grid/board
  try {
    const dto = await loadGameForPlay(gameId);
    document.getElementById('gameTitle').textContent = dto.gameName || 'Jeopardy';
    buildBoard(dto);
  } catch (err) {
    document.getElementById('boardContainer').innerHTML = `<p>Error loading game: ${err.message}</p>`;
  }

  // reveals answer when clicked 
  // shows that it was hidden initially
  document.getElementById('revealAnswerBtn').addEventListener('click', () => {
    document.getElementById('modalAnswer').classList.remove('hidden');
  });
  // close/hides the modal popup
  document.getElementById('closeModalBtn').addEventListener('click', () => {
    document.getElementById('modal').classList.add('hidden');
  });
});
