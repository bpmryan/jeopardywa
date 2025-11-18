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




// initial code 
// document.getElementById("addCategory").addEventListener("click", async () => {
//   const response = await fetch("../gameCreate/CategoryItem.html");
//   const doctype = "<!DOCTYPE html>";
//   const iframeStart = '<iframe id="inlineFrameExample" title="Inline Frame Example" width="300" height="200" src="../gameCreate/CategoryItem.html">';
//   const categoryItemHTML = await response.text();
//   const iframeEnd = "</iframe></html>";
//   const html = doctype + iframeStart + categoryItemHTML + iframeEnd;
//   console.log(html);

//   // Check this one (inner(html))
//   document.getElementById("mainContainer").innerHTML(html);
// });

// with help from chatgpt
// document.getElementById("addCategory").addEventListener("click", async () => {
//   const response = await fetch("../gameCreate/CategoryItem.html");
//   const html = await response.text();
//   console.log(html);

//   // Check this one (inner(html))
//   document.getElementById("mainContainer").insertAdjacentHTML("beforeend", html);
// });

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

  // Delete category
  // Later create a warning to allow user to check if they want to 
  if (event.target.classList.contains("deleteCategoryBtn")) {
    event.target.closest(".categoryCard").remove();
  }

  // Delete QnA
  // Later create a warning to allow user to check if they want to 
  if (event.target.classList.contains("deleteQnABtn")) {
    event.target.closest(".qnaCard").remove();
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



// Asynec functions makes it return a promise and saves category data to db
// Saves only the things in the cateogry cards 

/* 
 * Searches through every category card in the game creation page (gameContent.html)
 * extracts name, bkgcolor, textColor
 * builds js array with that data 
 * sends array to spring boot in the "await" section 
 * 
*/
// If anything goes wrong when writing to db CHECK HERE as well
async function saveAllCategory() {
  const categories = [...document.querySelectorAll(".catgeoryCard")].map(card => ({
    categoryName: card.querySelector(".categoryName").value,
    bkgColor: card.querySelector(".categoryBkgColor").value,
    textColor: card.querySelector(".categoryTextColor").value,
  }));

  // await is the promise part of the function
  await fetch("/api/categories/saveAll", {
    method: "POST", // specified as post request
    headers: { "Content-Type": "application/json" }, //server of body is declared as json
    body: JSON.stringify(categories), // data is sent to the server
  });
}



// Function that helps send the QnA data over to db
// Saves only the QnA in every category
/* 
 * It still looks at all categories 
 * Extracts the category name
 * Extacts all user inputs
 * packs it into a json list 
 * sends it over to spring boot api
*/

async function saveAllQnA() {
  const output = [];

  document.querySelectorAll(".categoryCard").forEach(categoryCard => {
    const categoryName = categoryCard.querySelector(".categoryName").value;

    categoryCard.querySelectorAll(".qnaCard").forEach(qnaCard => {
      output.push({
        categoryName,
        pointValue: parseInt(doucment.getElementById("pointValue")).value,
        question: doucment.getElementById("questionText").value,
        answer: doucment.getElementById("answerText").value,
        imageUrl: document.getElementById("imageUrl").value,
        imagePosition: document.getElementById("imagePosition").value,
        imageScale: document.getElementById("imageScale").value,
      });
    });
  });
  
  await fetch("/api/qna", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(output),
  });
}
