// const question = document.createElement('div');
// question.className('question');
// document.getElementById('questionContainer').appendChild(question);

// load html partail into a string
async function loadPartial(path) {
  const response = await fetch(path);
  if (!response.ok) throw new Error("Failed to load " + path);
  return await response.text();
}

// Adds a number to every question in each category
function renumberQnA(categoryCard) {
  const qnaCard = categoryCard.querySelectorAll(".qnaCard");
  qnaCard.forEach((card, index) => {
    const title = card.querySelector(".qnaTitle");
    card.querySelector(".qnaTitle").textContent = `Question ${index + 1}`;
  });
}

// Adjust image size
function updateRangeDisplays(container) {
  container.querySelectorAll('input[type="range"]').forEach((range) => {
    const label = range.nextElementSibling;
    if (label) label.textContent = range.value;
    range.addEventListener("input", () => {
      if (label) label.textContent = range.value;
    });
  });
}

// Add new category HTML function (with help from chatgpt)
/*
Part didn't initially work because 
1. innerHTML is a property and not a function (element.innerHTML = "some html";)
    - had to replace it back with insertAdjacentHTML("beforeand", html)
2. Can't inject html boilerplate because it creates a nested html and breaks DOM parsing 
3. Had to delete the major boilplate html stuff from QnAItems and CategoryItem and leave it as a partial html (interface?)
*/

document.getElementById("addCategory").addEventListener("click", async () => {
  const html = await loadPartial("../gameCreate/CategoryItem.html");
  document
    .getElementById("mainContainer")
    .insertAdjacentHTML("beforeend", html);
});

// event delegation
document.addEventListener("click", async (event) => {
  const target = event.target;

  // Add QnA inside category
  if (target.classList.contains("addQnABtn")) {
    // looks at css classes/ids
    const categoryCard = event.target.closest(".categoryCard");
    if (!categoryCard) return;

    // action to load up QnAItems.html
    const qnaContainer = categoryCard.querySelector(".qnaContainer");
    if (!qnaContainer) return;

    const qnaHTML = await loadPartial("../gameCreate/QnAItems.html");
    qnaContainer.insertAdjacentHTML("beforeend", qnaHTML);
    updateRangeDisplays(qnaContainer);
    renumberQnA(categoryCard);
  }

  // Delete category
  // Later create a warning to allow user to check if they want to
  if (target.classList.contains("deleteCategoryBtn")) {
    const c = target.closest(".categoryCard");
    if (c) c.remove;
  }

  // Delete QnA after clicking the delete button
  // Later create a warning to allow user to check if they want to
  if (target.classList.contains("deleteQnABtn")) {
    const q = target.closest(".qnaCard");
    const cat = target.closest(".categoryCard");
    if (q) q.remove();
    if (cat) renumberQnA(cat);
  }

  // Collapse Category
  if (target.classList.contains("collapseCategory")) {
    // looks at css classes
    const card = target.closest(".categoryCard");
    if (!card) return;

    // selects the first element/tag in the html and matches it with the specified css selector
    // returns the first element that matches the css selector
    const qnaContainer = card.querySelector(".qnaContainer");
    const settings = card.querySelector(".categorySettings");

    /* 
    If QnA is currently hidden when the button is clicked, then show it
    (and works the other way around too)
    block = visible
    none = invisible
    */
    qnaContainer.style.display =
      qnaContainer.style.display === "none" ? "block" : "none";
    settings.style.display =
      settings.style.display === "none" ? "flex" : "none";
  }
});

// Asynec functions makes it return a promise and saves category data to db
async function saveAll() {
  /*
   * Searches through every category card in the game creation page (gameContent.html)
   * extracts name, bkgcolor, textColor
   * builds js array with that data
   * sends array to spring boot in the "await" section
   *
   */
  // If anything goes wrong when writing to db CHECK HERE as well
  const gameData = { gameId: null, categories: [] };

  document.querySelectorAll(".catgeoryCard").forEach((categoryCard) => {
    const category = {
      categoryName: card.querySelector(".categoryName").value || "",
      bkgColor: card.querySelector(".bgkColor").value || "",
      textColor: card.querySelector(".textColor").value || "",
      gameId: [],
    };

    categoryCard.querySelectorAll(".qnaCard").forEach((qnaCard) => {
      category.qnaList.push({
        // Function that helps send the QnA data over to db
        // Saves QnA in every category
        /*
         * It still looks at all categories
         * Extracts the category name
         * Extacts all user inputs
         * packs it into a json list
         * sends it over to spring boot api
         */
        // categoryId,
        pointValue: Number(qnaCard.querySelector(".ptValue"))?.value || 0,
        question: qnaCard.querySelector(".questionText")?.value || "",
        answer: qnaCard.querySelector(".answerText")?.value || "",

        // question image fields
        questionImage: {
          questionImageUrl:
            qnaCard.querySelector(".questionImageUrl")?.value || null,
          questionImagePosition:
            qnaCard.querySelector(".questionImagePosition")?.value || null,
          questionImageScale:
            qnaCard.querySelector(".questionImageScale")?.value || null,
        },

        // answer image fields
        answerImage: {
          answerImageUrl:
            qnaCard.querySelector(".answerImageUrl")?.value || null,
          answerImagePosition:
            qnaCard.querySelector(".answerImagePosition")?.value || null,
          answerImageScale:
            qnaCard.querySelector(".answerImageScale")?.value || null,
        },
      });
    });
    gaemData.categories.push(category);
  });

  // await is the promise part of the function
  const resp = await fetch("/api/categories/saveAll", {
    method: "POST", // specified as post request
    headers: { "Content-Type": "application/json" }, //server of body is declared as json
    body: JSON.stringify(categories), // data is sent to the server
  });

  if (!response.ok) {
    const text = await response.text();
    console.log("Save failed", text);
    alert("Save failed" + text);
  } else {
    console.log("Save entire game:", gameData); // comment it out if doesn't work
    alert("Saved successfully");
  }
}

document.getElementById("saveGameBtn").addEventListener("click", saveAll);
