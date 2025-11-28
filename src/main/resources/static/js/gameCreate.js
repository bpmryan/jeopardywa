// const question = document.createElement('div');
// question.className('question');
// document.getElementById('questionContainer').appendChild(question);

// load html partail into a string
async function loadPartial(path) {
  const response = await fetch(path);
  if (!response.ok) throw new Error("Failed to load " + path);
  return await response.text();
}

// clear container generate categories and qna in the grid
// Get the first main container
// GameContent.html has 2 with the saem id; todo: pick first
const mainContainer = document.getElementById("#mainContainer");
// main.innerHTML = "";

// Helper: get current userId (expects login set localStorage.userId)
function getCurrentUserId() {
  return localStorage.getItem("userId") || null;
}

// Generate a short stable id (not for DB primary keys - server will create those)
function genLocalId(prefix = "L") {
  return prefix + Math.floor(Math.random() * 1000000);
}

// Insert one category card and return the DOM node
function insertCategoryFromHTML(html) {
  // create temp wrapper to pipe in the category card
  const wrapper = document.createElement("div");
  wrapper.innerHTML = html.trim();
  const category = wrapper.firstElementChild;
  if (!category) throw new Error("Category partial returned empty content");
  category.dataset._localId = genLocalId("cat");
  mainContainer.appendChild(category);
  return category;
}

// Insert one qna card into a category's qnaContainer, return qne node
function insertQnAIntoCategory(categoryCard, qnaHTML) {
  // create temp wrapper similarly to category card
  const wrapper = document.createElement("div");
  wrapper.innerHTML = html.trim();
  const qna = wrapper.firstElementChild;
  if (!qna) throw new Error("QnA partial returned empty content");
  qna.dataset._localId = genLocalId("qna");
  const qnaContainer = categoryCard.querySelector(".qnaContainer");
  if (!qnaContainer) {
    // fallback: append at end of category
    categoryCard.appendChild(qna);
  } else {
    qnaContainer.appendChild(qna);
  }
  // initialize sliders and text
  attachRangeHandlersTo(qna);
  return qna;
}

// Attach slider handlers for a single qnaCard
function attachRangeHandlersTo(qnaCard) {
  const rangeEls = qnaCard.querySelectorAll('input[type="range]');
  rangeEls.forEach((range) => {
    // detect nearby span to show value (use nextElementSibling or query by class)
    let display = range.nextElementSibling;
    // if next siblint is not the label span, attempt to find span with class near it
    if (!display || display.tagName !== "SPAN") {
      // try find sibling with specific class patterns
      const parent = range.parentElement || qnaCard;
      display =
        parent.querySelector(
          ".questionScaleValue, .answerScaleValue, questionScaleValue"
        ) || null;
    }
    if (display) display.textContent = range.value;
    range.addEventListener("input", () => {
      if (display) display.textContent = range.value;
    });
  });
}

// Adds a number to every question in each category
function renumberQnA(categoryCard) {
  const qnaNodes = categoryCard.querySelectorAll(".qnaCard");
  qnaNodes.forEach((card, index) => {
    const titleEl = card.querySelector(".qnaTitle");
    if (titleEl) titleEl.textContent = `Question ${index + 1}`;
  });
}

// Collapse toggle for a category
function toggleCollapseCategory(categoryCard) {
  categoryCard.classList.toggle("collapsed");
  const qnaContainer = categoryCard.querySelector(".qnaContainer");
  const settings = categoryCard.querySelector(".categorySettings");
  if (qnaContainer)
    qnaContainer.style.display =
      qnaContainer.style.display === "none" ? "block" : "none";
  if (settings)
    settings.style.display =
      settings.style.display === "none" ? "flex" : "none";
}

// Delete category safely
function deleteCategory(categoryCard) {
  categoryCard.remove();
}

// Delete single qna and re-number
function deleteQnA(qnaCard) {
  const categoryCard = qnaCard.closest(".categoryCard");
  qnaCard.remove();
  if (categoryCard) renumberQnA(categoryCard);
}

// event delegation
document.addEventListener("click", async (event) => {
  const target = event.target;

  // Add category button (top level)
  if (target.id === "addCategory" || target.classList.contains("addCategory")) {
    try {
      const html = await loadPartial("../gameCreate/CategoryItem.html");
      // insert only once per click
      insertCategoryFromHTML(html);
    } catch (err) {
      console.error("Failed to add category:", err);
      alert("Failed to load category templates.");
    }
    return;
  }

  // Add QnA inside a category
  if (target.classList.contains("addQnABtn")) {
    const categoryCard = target.closest(".categoryCard");
    if (!categoryCard) return;
    try {
      const qnaHTML = await loadPartial("../gameCreate/QnAItems.html");
      insertQnAIntoCategory(categoryCard, qnaHTML);
      renumberQnA(categoryCard);
    } catch (err) {
      console.error("Failed to add QnA", err);
      alert("Failed to load QnA template.");
    }
    return;
  }

  // collapse category
  if (target.classList.contains("collaspeCategory")) {
    const catgeoryCard = target.closet(".categoryCard");
    if (!categoryCard) return;
    toggleCollapseCategory(categoryCard);
    return;
  }

  // delete category
  if (target.classList.contains("deleteCategoryBtn")) {
    const categoryCard = target.closest(".categoryCard");
    if (!categoryCard) return;
    const ok = confirm("Delete this entire category");
    if (!ok) return;
    deleteCategory(categoryCard);
    return;
  }

  // delete qna
  if (target.classList.contains("delete qna")) {
    const qnaCard = target.closest(".qnaCard");
    if (!qnaCard) return;
    const ok = confirm("Delete this question?");
    if (!ok) return;
    deleteQnA(qnaCard);
    return;
  }
});

// Attach input handlers to sliders and initialize any existing partials loaded statically
document.addEventListener("input", (evt) => {
  const t = evt.target;
  if (t.matches('input[type="range"]')) {
    const display = t.nextElementSibling;
    if (display && display.tagName === "SPAN") display.textContent = t.value;
  }
});

// Asynec functions makes it return a promise and saves category data to db
async function saveAll() {
  /*
   * Searches through every category card in the game creation page (gameContent.html)
   * build GameDTO shape:
   * { userId, gameId? , categories: [ { categoryName, bkgColor, textColor, qna: [ ... ] } ] }
   */

  const userId = getCurrentUserId();
  if (!userId) {
    alert("No user logged in. Please sign in first.");
    return;
  }

  // try to detect an existing gameId from the URL (edit mode)
  const params = new URLSearchParams(window.location.search);
  const editingGameId = params.get("gameId") || null;

  // prompt for gameName if not present in URL (simple UX)
  const gameName =
    prompt("Enter a game name (appears on dashboard):", "") || "Untitled Game";

  const payload = {
    userId,
    gameId: editingGameId, // server will accept null/new or use this id for updates if logic supports it
    gameName,
    categories: [],
  };

  const categoryCards = Array.from(document.querySelectorAll(".categoryCard"));
  for (const cat of categoryCards) {
    const categoryNameEl = cat.querySelector(".categoryName");
    const bkgEl =
      cat.querySelector(".bgkColor") ||
      cat.querySelector(".bkgColor") ||
      cat.querySelector("#bgkColor");
    const textEl =
      cat.querySelector(".textColor") || cat.querySelector("#textColor");

    const categoryObj = {
      categoryName: categoryNameEl ? categoryNameEl.value.trim() : "",
      bkgColor: bkgEl ? bkgEl.value : "",
      textColor: textEl ? textEl.value : "",
      qna: [],
    };

    // for each qna card inside this category
    const qnaNodes = Array.from(cat.querySelectorAll(".qnaCard"));
    for (const qna of qnaNodes) {
      // robust selectors: class preferred, fallback to id if present
      const ptEl =
        qna.querySelector(".ptValue") || qna.querySelector("#ptValue");
      const questionEl =
        qna.querySelector(".questionText") ||
        qna.querySelector("#questionText");
      const answerEl =
        qna.querySelector(".answerText") || qna.querySelector("#answerText");

      const qImageInput =
        qna.querySelector(".questionImageUrl") ||
        qna.querySelector("#questionImageUrl");
      const qImagePos =
        qna.querySelector(".questionImagePosition") ||
        qna.querySelector("select.questionImagePosition");
      const qImageScale =
        qna.querySelector("#scaleQuestion") ||
        qna.querySelector(".questionScale") ||
        qna.querySelector('input[type="range"].questionScale');

      const aImageInput =
        qna.querySelector(".answerImageUrl") ||
        qna.querySelector("input.answerImageUrl") ||
        qna.querySelector('input[type="imageAnswer"]');
      const aImagePos =
        qna.querySelector(".answerImagePosition") ||
        qna.querySelector("select.answerImagePosition");
      const aImageScale = qna.querySelector(".answerScale");

      const qnaObj = {
        ptValue: ptEl ? parseInt(ptEl.value || "0", 10) : 0,
        questionText: questionEl ? questionEl.value.trim() : "",
        answerText: answerEl ? answerEl.value.trim() : "",
        questionImage: {
          url: qImageInput ? qImageInput.value || "" : "",
          position: qImagePos ? qImagePos.value : "",
          scale: qImageScale ? qImageScale.value : "",
        },
        answerImage: {
          url: aImageInput ? aImageInput.value || "" : "",
          position: aImagePos ? aImagePos.value : "",
          scale: aImageScale ? aImageScale.value : "",
        },
      };

      categoryObj.qna.push(qnaObj);
    }

    payload.categories.push(categoryObj);
  }

  // Basic validation: at least one category
  if (payload.categories.length === 0) {
    if (!confirm("No categories found. Do you want to save an empty game?"))
      return;
  }

  try {
    const res = await fetch("/api/game/saveAll", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error("Save failed: " + text);
    }
    const text = await res.text();
    alert("Save successful: " + text);
    // optionally redirect to dashboard
    window.location.href = "/jeopardyDash/Dashboard.html";
  } catch (err) {
    console.error("Save error", err);
    alert("Failed to save game: " + err.message);
  }
}

// Load existing game for edit mode and populate UI
async function loadExistingGame(gameId) {
  try {
    const res = await fetch(`/api/game/full/${encodeURIComponent(gameId)}`);
    if (!res.ok) {
      console.error("Load game failed", await res.text());
      return;
    }
    const data = await res.json();

    // Clear existing
    mainContainer.innerHTML = "";

    // populate categories and qna
    for (const cat of data.categories || []) {
      const categoryHTML = await loadPartial("../gameCreate/CategoryItem.html");
      const categoryCard = insertCategoryFromHTML(categoryHTML);

      // fill fields
      const nameEl = categoryCard.querySelector(".categoryName");
      const bkgEl =
        categoryCard.querySelector(".bgkColor") ||
        categoryCard.querySelector(".bkgColor");
      const textEl = categoryCard.querySelector(".textColor");

      if (nameEl) nameEl.value = cat.categoryName || "";
      if (bkgEl) bkgEl.value = cat.bkgColor || "#13162a";
      if (textEl) textEl.value = cat.textColor || "#ffffff";

      // set server-side ids into dataset
      if (cat.categoryId) categoryCard.dataset.categoryId = cat.categoryId;

      // qna
      const qnaContainer = categoryCard.querySelector(".qnaContainer");
      for (const q of cat.qna || []) {
        const qnaHTML = await loadPartial("../gameCreate/QnAItems.html");
        const qnaCard = insertQnAIntoCategory(categoryCard, qnaHTML);

        // map fields
        const ptEl =
          qnaCard.querySelector(".ptValue") ||
          qnaCard.querySelector("#ptValue");
        const questionEl =
          qnaCard.querySelector(".questionText") ||
          qnaCard.querySelector("#questionText");
        const answerEl =
          qnaCard.querySelector(".answerText") ||
          qnaCard.querySelector("#answerText");

        const qImageInput =
          qnaCard.querySelector(".questionImageUrl") ||
          qnaCard.querySelector("#questionImageUrl");
        const qImagePos = qnaCard.querySelector(".questionImagePosition");
        const qImageScale =
          qnaCard.querySelector("#scaleQuestion") ||
          qnaCard.querySelector(".questionScaleValue");

        const aImageInput =
          qnaCard.querySelector(".answerImageUrl") ||
          qnaCard.querySelector("input.answerImageUrl");
        const aImagePos = qnaCard.querySelector(".answerImagePosition");
        const aImageScale = qnaCard.querySelector(".answerScale");

        if (ptEl) ptEl.value = q.pointValue || 0;
        if (questionEl) questionEl.value = q.question || "";
        if (answerEl) answerEl.value = q.answer || "";

        if (qImageInput) qImageInput.value = q.questionImageUrl || "";
        if (qImagePos) qImagePos.value = q.questionImagePosition || "";
        if (qImageScale && q.questionImageScale)
          qImageScale.value = q.questionImageScale;

        if (aImageInput) aImageInput.value = q.answerImageUrl || "";
        if (aImagePos) aImagePos.value = q.answerImagePosition || "";
        if (aImageScale && q.answerImageScale)
          aImageScale.value = q.answerImageScale;

        // map server qnaId for potential update usage
        if (q.qnaId) qnaCard.dataset.qnaId = q.qnaId;
      }
      renumberQnA(categoryCard);
    }
  } catch (err) {
    console.error("Error loading existing game:", err);
  }
}

// On initial load: if URL has ?gameId=..., load it for edit mode
document.addEventListener("DOMContentLoaded", async () => {
  // wire save button
  const saveBtn = document.getElementById("saveGameBtn");
  if (saveBtn) saveBtn.addEventListener("click", saveAll);

  // if addCategory exists on page, wire click to add category (safe fallback)
  const addCatBtn = document.getElementById("addCategory");
  if (addCatBtn) {
    addCatBtn.addEventListener("click", async () => {
      try {
        const html = await loadPartial("../gameCreate/CategoryItem.html");
        insertCategoryFromHTML(html);
      } catch (err) {
        console.error("Failed to add category", err);
      }
    });
  }

  // Check URL params for edit mode
  const params = new URLSearchParams(window.location.search);
  const gameId = params.get("gameId");
  if (gameId) {
    await loadExistingGame(gameId);
  }
});
