/*
 * gameCreate.js
 * Purpose: Frontend editor script for building or editing a Jeopardy game.
 * Responsibilities:
 *  - Dynamically create category and QnA cards
 *  - Serialize DOM to the GameDTO structure and POST to `/api/game/saveAll`
 *  - Load an existing game into the editor for modification
 */

// Prof Reed example:
// const question = document.createElement('div');
// question.className('question');
// document.getElementById('questionContainer').appendChild(question);

// Utility to fetch partial html
async function loadPartial(path) {
  const res = await fetch(path);
  if (!res.ok) throw new Error("Failed to load " + path);
  return await res.text();
}

// Insert a new category card (returns element)
async function createCategoryCard(initial = {}) {
  const html = await loadPartial("/gameCreate/CategoryItem.html");
  const wrap = document.createElement("div");
  wrap.innerHTML = html.trim();
  const card = wrap.firstElementChild; // .categoryCard
  // populate initial values if provided
  if (initial.categoryId) card.dataset.categoryId = initial.categoryId;
  if (initial.categoryName)
    card.querySelector(".categoryName").value = initial.categoryName;
  if (initial.bkgColor)
    card.querySelector(".categoryBkgColor").value = initial.bkgColor;
  if (initial.textColor)
    card.querySelector(".categoryTextColor").value = initial.textColor;

  return card;
}

// Insert a QnA card (returns element)
async function createQnACard(initial = {}) {
  const html = await loadPartial("/gameCreate/QnAItems.html");
  const wrap = document.createElement("div");
  wrap.innerHTML = html.trim();
  const card = wrap.firstElementChild;

  if (initial.qnaId) card.dataset.qnaId = initial.qnaId;
  if (initial.ptValue !== undefined)
    card.querySelector(".ptValue").value = initial.ptValue;
  if (initial.questionText)
    card.querySelector(".questionText").value = initial.questionText;
  if (initial.answerText)
    card.querySelector(".answerText").value = initial.answerText;

  // update range displays:
  // question image range
  if (initial.questionImageUrl)
    card.querySelector(".questionImageUrl").value = initial.questionImageUrl;
  // answer image range
  if (initial.answerImageUrl)
    card.querySelector(".answerImageUrl").value = initial.answerImageUrl;

  return card;
}

// Add category button handler
document.getElementById("addCategory").addEventListener("click", async () => {
  document.getElementById("mainContainer").append(await createCategoryCard());
});

// Delegated click listener for category-level buttons
document.addEventListener("click", async (e) => {
  const t = e.target;

  // Add QnA inside category
  if (t.classList.contains("addQnABtn")) {
    const cat = t.closest(".categoryCard");
    const area = cat.querySelector(".qnaContainer");
    area.append(await createQnACard());
    renumber(cat);
  }

  // Delete QnA
  if (t.classList.contains("deleteQnABtn")) {
    const cat = t.closest(".categoryCard");
    t.closest(".qnaCard").remove();
    renumber(cat);
  }

  // Delete category
  if (t.classList.contains("deleteCategoryBtn")) {
    if (confirm("Delete category?")) t.closest(".categoryCard").remove();
  }

  // Collapse category
  if (t.classList.contains("collapseCategory")) {
    const card = t.closest(".categoryCard");
    card.classList.toggle("collapsed");
    const qnaContainer = card.querySelector(".qnaContainer");
    const settings = card.querySelector(".categorySettings");
    if (qnaContainer)
      qnaContainer.style.display =
        qnaContainer.style.display === "none" ? "block" : "none";
    if (settings)
      settings.style.display =
        settings.style.display === "none" ? "flex" : "none";
    return;
  }
});

// Renumber QnA titles inside a category
function renumber(catCard) {
  catCard.querySelectorAll(".qnaCard").forEach((q, i) => {
    q.querySelector(".qnaTitle").textContent = `Question ${i + 1}`;
  });
}

// Build the canonical GameDTO from DOM and POST to backend
async function saveAll() {
  // Build DTO
  const dto = {
    userId: localStorage.getItem("userId"),
    gameId: window.gameEditingId || null,
    gameName: document.getElementById("gameName").value,
    categories: []
  };

  document.querySelectorAll(".categoryCard").forEach(cat => {
    const c = {
      categoryId: cat.dataset.categoryId || null,
      categoryName: cat.querySelector(".categoryName").value,
      bkgColor: cat.querySelector(".categoryBkgColor").value,
      textColor: cat.querySelector(".categoryTextColor").value,
      qna: []
    };

    cat.querySelectorAll(".qnaCard").forEach(q => {
      c.qna.push({
        qnaId: q.dataset.qnaId || null,
        ptValue: Number(q.querySelector(".ptValue").value || 0),
        questionText: q.querySelector(".questionText").value,
        answerText: q.querySelector(".answerText").value,
        questionImage: {
          url: q.querySelector(".questionImageUrl").value
        },
        answerImage: {
          url: q.querySelector(".answerImageUrl").value
        }
      });
    });

    dto.categories.push(c);
  });

  // POST to backend/dto
  // json body
   const res = await fetch("/api/game/saveAll", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(dto)
  });

  const savedId = (await res.text()).trim();
  window.gameEditingId = savedId;

  history.replaceState(null, "", `?gameId=${savedId}`);
  await reloadFromServer(savedId);

  alert("Saved!");
}

// reload after save 
async function reloadFromServer(gameId) {
  const res = await fetch(`/api/game/full/${gameId}`);
  const dto = await res.json();

  document.getElementById("gameName").value = dto.gameName;

  const container = document.getElementById("mainContainer");
  container.innerHTML = "";

  for (const catDTO of dto.categories) {
    const card = await createCategoryCard(catDTO);
    const qArea = card.querySelector(".qnaContainer");

    for (const qdto of catDTO.qna) {
      const qCard = await createQnACard({
        qnaId: qdto.qnaId,
        ptValue: qdto.ptValue,
        questionText: qdto.questionText,
        answerText: qdto.answerText,
        questionImageUrl: qdto.questionImageUrl,
        answerImageUrl: qdto.answerImageUrl
      });
      qArea.append(qCard);
    }

    container.append(card);
    renumber(card);
  }
}

// inital load 
document.addEventListener("DOMContentLoaded", async () => {
  const gid = new URLSearchParams(location.search).get("gameId");
  if (gid) {
    window.gameEditingId = gid;
    await reloadFromServer(gid);
  }
});

document.getElementById("saveGameBtn").addEventListener("click", saveAll);