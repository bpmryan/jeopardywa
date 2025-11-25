/*
* Functions to present game
*/

document.addEventListener("DOMContentLoaded", async () => {
    const params = new URLSearchParams(window.location.search);
    const gameId = params.get("gameId");
    await loadBoard(gameId);
});

async function loadBoard(gameId) {
    const response = await fetch(`/api/game/play/${gameId}`);
    const data = await response.json();

    const board = document.getElementById("board");
    board.innerHTML = "";
    board.classList.add("gridBoard");
    board.style.gridTemplateColumns = `repeat(${data.categories.length}, 1fr)`;

    // Category headers
    data.categories.forEach(cat => {
        board.insertAdjacentHTML("beforeend",
            `<div class="categoryHeader">${cat.categoryName}</div>`
        );
    });

    // Generate rows by point value
    const maxRows = Math.max(...data.categories.map(c => c.qna.length));

    for (let row = 0; row < maxRows; row++) {
        for (let col = 0; col < data.categories.length; col++) {
            const q = data.categories[col].qna[row];
            if (!q) {
                board.insertAdjacentHTML("beforeend", `<div class="emptyCell"></div>`);
                continue;
            }

            board.insertAdjacentHTML("beforeend", `
                <div class="qnaCell" data-question='${JSON.stringify(q)}'>
                    ${q.pointValue}
                </div>
            `);
        }
    }

    // Click handler to reveal Q/A
    document.querySelectorAll(".qnaCell").forEach(cell => {
        cell.addEventListener("click", () => openModal(JSON.parse(cell.dataset.question)));
    });
}

function openModal(qna) {
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
}

document.getElementById("closeModal").addEventListener("click", () => {
    document.getElementById("modal").classList.add("hidden");
});
