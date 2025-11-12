// const question = document.createElement('div');
// question.className('question');
// document.getElementById('questionContainer').appendChild(question);

// Add new category HTML function
doucment.getElementId("addCategory").addEventListener("click", async () => {
    const response = await fetch("/gameCreate/Category.html");
    const html = await response.text();
    document.getElementById("mainContainer").insertAdjacentHTML("beforeand", html);
});

// Function that helps send the category data over to db
// Asynec functions makes it return a promise
async function saveCategory() {
    const data = {
        categoryName: document.getElementById("categoryName").value,
        bkgColor: document.getElementId("bgkColor").value,
        textColor: doucment.getElementId("textColor").value
    };

    // await is the promise part of the function
    await fetch("/api/categories", {
        method: "POST", // specified as post request 
        headers: {"Content-Type" : "application/json"}, //server of body is declared as json
        body: JSON.stringify(data) // data is sent to the server
    });
}

// Add new QnA HTML dynamically
document.addEventListener("click", async (event) => {
  if (event.target && event.target.id === "addQnABtn") {
    try {
      const response = await fetch("/gameCreate/QnAItems.html");
      if (!response.ok) {
        throw new Error(`Failed to load QnAItems.html: ${response.statusText}`);
      }

      const html = await response.text();
      document
        .getElementById("mainContainer")
        .insertAdjacentHTML("beforeend", html);

      console.log("QnA item added successfully!");
    } catch (error) {
      console.error("Error loading QnA item:", error);
    }
  }
});


// Function that helps send the QnA data over to db
async function saveQnA() {
    const data = {
        categoryId: document.getElementById("categoryId").value,
        pointValue: parseInt(doucment.getElementById("pointValue")).value,
        question: doucment.getElementById("questionText").value,
        answer: doucment.getElementById("answerText").value,
        imageUrl: document.getElementById("imageUrl").value,
        imagePosition: document.getElementById("imagePosition").value,
        imageScale: document.getElementById("imageScale").value 
    };

    await fetch("/api/qna", {
        method: "POST", 
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });
}