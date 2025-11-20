// const question = document.createElement('div');
// question.className('question');
// document.getElementById('questionContainer').appendChild(question);

// load html partail into a string 
async function loadPartial(path) {
  const response = await fetch(path);
  return await response.text();
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
  // const response = await fetch("../gameCreate/CategoryItem.html");
  const html = await loadPartial("../gameCreate/CategoryItem.html");
  console.log(html);

  // Check this one (inner(html))
  const container = document.getElementById("mainContainer");
  container.insertAdjacentHTML("beforeend", html);
});

// event delegation
document.addEventListener("click" , async (event) => {


  // Add QnA 
  if (event.target.id === "addQnABtn") {
    // looks at css files
    const categoryCard = event.target.closest(".categoryCard");
    const qnaContainer = categoryCard.querySelector(".qnaContainer");

    // action to load up QnAItems.html
    const qnaHTML = await loadPartial("../gameCreate/QnAItems.html");

    qnaContainer.insertAdjacentHTML("beforeend", qnaHTML);
  }

  // This occurs when "Add Question & Answer" is clicked (To add numbers to the question from renumberQnA function)
  if (event.target.classList.contains("addQnABtn")) {
    const categoryCard = event.target.closest(".categoryCard");
    const qnaContainer = categoryCard.querySelector(".qnaContainer");

    const qnaHTML = await loadPartial("../gameCreate/QnAItems.html");
    qnaContainer.insertAdjacentHTML("beforeend", qnaHTML);

    renumberQnA(categoryCard);
  }

  // Delete category
  // Later create a warning to allow user to check if they want to 
  if (event.target.classList.contains("deleteCategoryBtn")) {
    event.target.closest(".categoryCard").remove();
  }

  // Delete QnA after clicking the delete button
  // Later create a warning to allow user to check if they want to 
  if (event.target.classList.contains("deleteQnABtn")) {
    const categoryCard = event.target.closest(".catgeoryCard");
    event.target.closest(".qnaCard").remove();
    renumberQnA(categoryCard);
  }

  // Collapse Category
  if (event.target.classList.contains("collapseCategory")) {
    // looks at css classes 
    const card = event.target.closest(".categoryCard");
    card.classList.toggle("collapsed");

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
    qnaContainer.style.display = qnaContainer.style.display === "none" ? "block" : "none";

    settings.style.display = settings.style.display === "none" ? "flex" : "none";
  }
});

// Adds a number to every question in each category
function renumberQnA(categoryCard) {
  const qnaCarda = categoryCard.querySelectorAll(".qnaCard");
  qnaCards.forEach((card, index) => {
    card.querySelector(".qnaTitle").textContent = `Question ${index + 1}`;
  }) 
}


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
  const gaemData = {categories: [] };

    document.querySelectorAll(".catgeoryCard").forEach(categoryCard => {
      const categories = {
        categoryName: card.querySelector(".categoryName").value,
        bkgColor: card.querySelector(".bgkColor").value,
        textColor: card.querySelector(".textColor").value,
        gameId: []
      };

    categoryCard.querySelectorAll(".qnaCard").forEach(qnaCard => {
      qnaList.push({
         // Function that helps send the QnA data over to db
        // Saves QnA in every category
        /* 
        * It still looks at all categories 
        * Extracts the category name
        * Extacts all user inputs
        * packs it into a json list 
        * sends it over to spring boot api
        */
        categoryId,
        pointValue: parseInt(doucment.querySelectorAll(".ptValue")).value,
        question: doucment.querySelectorAll(".questionText").value,
        answer: doucment.querySelectorAll(".answerText").value,

       
        // question image fields
        questionImageUrl: qnaCard.querySelector(".questionImageUrl").value || null,
        questionImagePosition: qnaCard.querySelector(".questionImagePosition").value || null,
        questionImageScale: qnaCard.querySelector(".questionImageScale").value || null,

        // answer image fields
        answerImageUrl: qnaCard.querySelector(".answerImageUrl").value || null,
        answerImagePosition: qnaCard.querySelector(".answerImagePosition").value || null,
        answerImageScale: qnaCard.querySelector(".answerImageScale").value || null,
      });
    });
    gaemData.categories.push(category);
  });

  // await is the promise part of the function
  await fetch("/api/categories/saveAll", {
    method: "POST", // specified as post request
    headers: { "Content-Type": "application/json" }, //server of body is declared as json
    body: JSON.stringify(categories), // data is sent to the server
  });

  console.log("Save entire game:", gameData);
}

