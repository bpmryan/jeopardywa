// const question = document.createElement('div');
// question.className('question');
// document.getElementById('questionContainer').appendChild(question);

// Utility to fetch partial html 
async function loadPartial(path) {
  const res = await fetch(path);
  if (!res.ok) throw new Error('Failed to load ' + path);
  return await res.text();
}

// Insert a new category card (returns element)
async function createCategoryCard(initial = {}) {
  const html = await loadPartial('/gameCreate/CategoryItem.html');
  const wrapper = document.createElement('div');
  wrapper.innerHTML = html;
  const card = wrapper.firstElementChild; // .categoryCard
  // populate initial values if provided
  if (initial.categoryId) card.dataset.categoryId = initial.categoryId;
  if (initial.categoryName) card.querySelector('.categoryName').value = initial.categoryName;
  if (initial.bkgColor) card.querySelector('.categoryBkgColor').value = initial.bkgColor;
  if (initial.textColor) card.querySelector('.categoryTextColor').value = initial.textColor;
  return card;
}

// Insert a QnA card (returns element)
async function createQnACard(initial = {}) {
  const html = await loadPartial('/gameCreate/QnAItems.html');
  const wrapper = document.createElement('div');
  wrapper.innerHTML = html;
  const card = wrapper.firstElementChild; // .qnaCard
  if (initial.qnaId) card.dataset.qnaId = initial.qnaId;
  if (initial.ptValue) card.querySelector('.ptValue').value = initial.ptValue;
  if (initial.questionText) card.querySelector('.questionText').value = initial.questionText;
  if (initial.answerText) card.querySelector('.answerText').value = initial.answerText;
  if (initial.questionImage && initial.questionImage.url) card.querySelector('.questionImageUrl').value = initial.questionImage.url;
  if (initial.answerImage && initial.answerImage.url) card.querySelector('.answerImageUrl').value = initial.answerImage.url;
  // update range displays:
  const qRange = card.querySelector('.questionScale');
  if (qRange) {
    const qLabel = card.querySelector('.questionScaleValue');
    qLabel.textContent = qRange.value;
    qRange.addEventListener('input', () => qLabel.textContent = qRange.value);
  }
  const aRange = card.querySelector('.answerScale');
  if (aRange) {
    const aLabel = card.querySelector('.answerScaleValue');
    aLabel.textContent = aRange.value;
    aRange.addEventListener('input', () => aLabel.textContent = aRange.value);
  }
  return card;
}

// Add category button handler
document.getElementById('addCategory').addEventListener('click', async () => {
  const container = document.getElementById('mainContainer');
  const card = await createCategoryCard();
  container.appendChild(card);
});

// Delegated click listener for category-level buttons
document.addEventListener('click', async (e) => {
  const t = e.target;

  // Add QnA inside category
  if (t.classList.contains('addQnABtn')) {
    const categoryCard = t.closest('.categoryCard');
    const qnaContainer = categoryCard.querySelector('.qnaContainer');
    const qnaCard = await createQnACard();
    qnaContainer.appendChild(qnaCard);
    renumberQnA(categoryCard);
  }

  // Delete category
  if (t.classList.contains('deleteCategoryBtn')) {
    const ok = confirm('Delete category?');
    if (!ok) return;
    t.closest('.categoryCard').remove();
  }

  // Delete QnA
  if (t.classList.contains('deleteQnABtn')) {
    const categoryCard = t.closest('.categoryCard');
    t.closest('.qnaCard').remove();
    renumberQnA(categoryCard);
  }

  // Collapse category
  if (t.classList.contains('collapseCategory')) {
    const card = t.closest('.categoryCard');
    card.classList.toggle('collapsed');
    const qnaContainer = card.querySelector('.qnaContainer');
    const settings = card.querySelector('.categorySettings');
    qnaContainer.style.display = qnaContainer.style.display === 'none' ? 'block' : 'none';
    settings.style.display = settings.style.display === 'none' ? 'flex' : 'none';
  }
});

// Renumber QnA titles inside a category 
function renumberQnA(categoryCard) {
  const qnas = categoryCard.querySelectorAll('.qnaCard');
  qnas.forEach((card, idx) => {
    const title = card.querySelector('.qnaTitle');
    if (title) title.textContent = `Question ${idx+1}`;
  });
}

// Build the canonical GameDTO from DOM and POST to backend 
async function saveAll() {
  const dto = {
    userId: localStorage.getItem('userId') || null,
    gameId: window.gameEditingId || null,
    gameName: document.getElementById('gameName') ? document.getElementById('gameName').value : null,
    categories: []
  };

  document.querySelectorAll('.categoryCard').forEach(cat => {
    const catObj = {
      categoryId: cat.dataset.categoryId || null,
      categoryName: cat.querySelector('.categoryName').value || '',
      bkgColor: cat.querySelector('.categoryBkgColor').value || '',
      textColor: cat.querySelector('.categoryTextColor').value || '',
      qna: []
    };

    cat.querySelectorAll('.qnaCard').forEach(q => {
      const qObj = {
        qnaId: q.dataset.qnaId || null,
        ptValue: parseInt(q.querySelector('.ptValue').value || '0', 10),
        questionText: q.querySelector('.questionText').value || '',
        answerText: q.querySelector('.answerText').value || '',
        questionImage: {
          url: q.querySelector('.questionImageUrl').value || '',
          position: q.querySelector('.questionImagePosition').value || '',
          scale: q.querySelector('.questionScale').value || ''
        },
        answerImage: {
          url: q.querySelector('.answerImageUrl').value || '',
          position: q.querySelector('.answerImagePosition').value || '',
          scale: q.querySelector('.answerScale').value || ''
        }
      };
      catObj.qna.push(qObj);
    });

    dto.categories.push(catObj);
  });

  // POST to backend
  const resp = await fetch('/api/game/saveAll', {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(dto)
  });

  if (!resp.ok) {
    const txt = await resp.text();
    alert('Save failed: ' + txt);
    return;
  }
  const text = await resp.text();
  alert('Saved: ' + text);
  // Optionally redirect to dashboard
  window.location.href = '/jeopardyDash/Dashboard.html';
}

// Load an existing game into the DOM for editing
async function loadExistingGame(gameId) {
  // set global editing id
  window.gameEditingId = gameId;
  const res = await fetch(`/api/game/full/${encodeURIComponent(gameId)}`);
  if (!res.ok) throw new Error(await res.text());
  const dto = await res.json();

  // set game name
  if (document.getElementById('gameName')) document.getElementById('gameName').value = dto.gameName || '';

  const container = document.getElementById('mainContainer');
  container.innerHTML = '';

  for (const catDTO of dto.categories) {
    const catCard = await createCategoryCard({
      categoryId: catDTO.categoryId,
      categoryName: catDTO.categoryName,
      bkgColor: catDTO.bkgColor,
      textColor: catDTO.textColor
    });
    // add qnas
    const qnaContainer = catCard.querySelector('.qnaContainer');
    for (const qdto of catDTO.qna) {
      const qCard = await createQnACard({
        qnaId: qdto.qnaId,
        ptValue: qdto.pointValue,
        questionText: qdto.question,
        answerText: qdto.answer,
        questionImage: { url: qdto.questionImageUrl, position: qdto.questionImagePosition, scale: qdto.questionImageScale },
        answerImage: { url: qdto.answerImageUrl, position: qdto.answerImagePosition, scale: qdto.answerImageScale }
      });
      qnaContainer.appendChild(qCard);
    }
    container.appendChild(catCard);
    renumberQnA(catCard);
  }
}

// expose saveAll
document.getElementById('saveGameBtn').addEventListener('click', saveAll);

// on document load: if ?gameId=... present, load for editing 
document.addEventListener('DOMContentLoaded', () => {
  const params = new URLSearchParams(window.location.search);
  const gameId = params.get('gameId');
  if (gameId) {
    loadExistingGame(gameId).catch(err => console.error('Load failed', err));
  }
});
