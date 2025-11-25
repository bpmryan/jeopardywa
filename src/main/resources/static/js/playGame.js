/*
 * Functions to present game
 */

document.addEventListener("DOMContentLoaded", async () => {
  const params = new URLSearchParams(window.location.search);
  const gameId = params.get("gameId");
  // check to see if gameId doesn't exist
  if (!gameId) {
    document.getElementById("board").innerText = "No gameId provided";
    return;
  }
  await loadBoard(gameId);
});

async function loadBoard(gameId) {
  const response = await fetch(`/api/game/play/${gameId}`);
  if (!response.ok) {
    console.error("Failed to load play data", await response.text());
    return;
  }
  const data = await response.json();
  document.getElementById("gameTitle").textContent =
    data.gameName || "Jeopardy";

  const board = document.getElementById("board");
  board.innerHTML = "";
  board.style.gridTemplateColumns = `repeat(${data.categories.length}, 1fr)`;

  // Category headers
  data.categories.forEach((cat) => {
    const h = document.createElement("div");
    h.className = "categoryHeader";
    h.textContent = cat.categoryName;
    board.appendChild(h);
  });

  // Generate rows by point value
  const maxRows = Math.max(...data.categories.map((c) => c.qna.length));

  for (let row = 0; row < maxRows; row++) {
    for (let col = 0; col < data.categories.length; col++) {
      const q = data.categories[col].qna[row];
      if (!q) {
        const empty = document.createElement("div");
        empty.className = "emptyCell";
        board.appendChild(empty);
        continue;
      }

      const cell = document.createElement("div");
      cell.className = "qnaCell";
      cell.dataset.qna = JSON.stringify(q);
      cell.textContent = q.pointValue;
      cell.addEventListener("click", () => openModal(q, cell));
      board.insertAdjacentHTML(cell);
    }
  }
}

function openModal(q, cell) {
  const modal = document.getElementById("modal");
  const content = document.getElementById("modalContent");

  content.innerHTML = `
        <h2>${qna.pointValue} Points</h2>
        <p><strong>Question:</strong> ${qna.question}</p>
        ${qna.questionImageUrl ? `<img src="${qna.questionImageUrl}" />` : ""}
        <br><br>
        <p><strong>Answer:</strong> ${qna.answer}</p>
        ${qna.answerImageUrl ? `<img src="${qna.answerImageUrl}" />` : ""}
    `;

  modal.classList.remove("hidden");

  document.getElementById("showAnswerBtn").onclick = () => {
    document.getElementById("answerBox").style.display = "block";
  };

// Mark cell/question box as disabled when answered 
  if (cell) {
    cell.classList.add("answered");
    cell.removeEventListener("click", () => openModal(q, cell));
  }
}

document.getElementById("closeModal").addEventListener("click", () => {
  document.getElementById("modal").classList.add("hidden");
});
