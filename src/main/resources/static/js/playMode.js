document.addEventListener("DOMContentLoaded", async () => {
  const gameId = new URLSearchParams(location.search).get("gameId");
  const res = await fetch(`/api/game/play/${gameId}`);
  const game = await res.json();

  document.getElementById("gameTitle").textContent = game.gameName;

  const board = document.getElementById("board");
  board.innerHTML = "";

  for (const cat of game.categories) {
    const col = document.createElement("div");
    col.className = "categoryColumn";

    const header = document.createElement("div");
    header.className = "categoryHeader";
    header.textContent = cat.categoryName;
    col.append(header);

    cat.qna.forEach(q => {
      const cell = document.createElement("div");
      cell.className = "cell";
      cell.textContent = q.ptValue;

      cell.addEventListener("click", () => showQuestion(q));
      col.append(cell);
    });

    board.append(col);
  }
});

/* Simple modal questions */
function showQuestion(q) {
  const modal = document.getElementById("modal");
  modal.style.display = "block";

  document.getElementById("modalQuestion").textContent = q.question;
  document.getElementById("modalAnswer").textContent = "";
  document.getElementById("revealBtn").onclick = () =>
    document.getElementById("modalAnswer").textContent = q.answer;

  document.getElementById("closeBtn").onclick = () =>
    (modal.style.display = "none");
}