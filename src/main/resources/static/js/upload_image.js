/*const dropZone = document.getElementById('drop-zone');
const preview = document.getElementById('preview');
const uploadBtn = document.getElementById('upload-btn');
let selectedFile = null;

// ===== DRAG & DROP =====
dropZone.addEventListener('dragover', (e) => {
  e.preventDefault();
  dropZone.classList.add('dragover');
});

dropZone.addEventListener('dragleave', () => {
  dropZone.classList.remove('dragover');
});

dropZone.addEventListener('drop', (e) => {
  e.preventDefault();
  dropZone.classList.remove('dragover');
  const file = e.dataTransfer.files[0];
  handleFile(file);
});

// ===== ВСТАВКА З БУФЕРА (Ctrl+V) =====
document.addEventListener('paste', (e) => {
  const items = e.clipboardData.items;
  for (const item of items) {
    if (item.type.indexOf('image') !== -1) {
      const file = item.getAsFile();
      handleFile(file);
      break;
    }
  }
});

// ===== ОБРОБКА ФАЙЛУ =====
function handleFile(file) {
  if (!file || !file.type.startsWith('image/')) {
    alert('Оберіть зображення');
    return;
  }
  selectedFile = file;

  // Показати прев’ю
  const reader = new FileReader();
  reader.onload = (e) => {
    preview.src = e.target.result;
    preview.hidden = false;
    uploadBtn.disabled = false;
  };
  reader.readAsDataURL(file);
}

// ===== ВІДПРАВКА НА БЕКЕНД =====
uploadBtn.addEventListener('click', async () => {
  if (!selectedFile) return;

  const formData = new FormData();
  formData.append('image', selectedFile);

  const response = await fetch('/api/products/upload-image', {
    method: 'POST',
    body: formData
  });

  if (response.ok) {
    alert('Фото успішно завантажено!');
    uploadBtn.disabled = true;
  } else {
    alert('Помилка при завантаженні!');
  }
});*/

const dropZone = document.getElementById('drop-zone');
const previewContainer = document.getElementById('preview-container');
const editForm = document.getElementById('editForm');

let selectedFiles = [];

// ===== DRAG & DROP =====
dropZone.addEventListener('dragover', (e) => {
  e.preventDefault();
  dropZone.classList.add('dragover');
});

dropZone.addEventListener('dragleave', () => {
  dropZone.classList.remove('dragover');
});

dropZone.addEventListener('drop', (e) => {
  e.preventDefault();
  dropZone.classList.remove('dragover');
  handleFiles(e.dataTransfer.files);
});

// ===== ВСТАВКА З БУФЕРА (Ctrl+V) =====
document.addEventListener('paste', (e) => {
  const items = e.clipboardData.items;
  const files = [];
  for (const item of items) {
    if (item.type.indexOf('image') !== -1) {
      files.push(item.getAsFile());
    }
  }
  if (files.length > 0) handleFiles(files);
});

// ===== ОБРОБКА ФАЙЛІВ =====
function handleFiles(fileList) {
  for (const file of fileList) {
    if (!file.type.startsWith('image/')) continue;
    // Перевіряємо, щоб не дублювати той самий файл
    if (selectedFiles.some(f => f.name === file.name && f.size === file.size)) continue;

    selectedFiles.push(file);
    previewImage(file);
  }
}

// ===== ПРЕВ’Ю + КНОПКА ВИДАЛЕННЯ =====
function previewImage(file) {
  const reader = new FileReader();
  const wrapper = document.createElement('div');
  wrapper.classList.add('preview-item');

  const img = document.createElement('img');
  img.classList.add('preview-thumb');
  img.style.maxWidth = '80px';
  img.style.margin = '5px';
  img.style.border = '1px solid #ccc';
  img.style.borderRadius = '4px';

  const removeBtn = document.createElement('button');
  removeBtn.textContent = '✖';
  removeBtn.classList.add('remove-btn');
  removeBtn.title = 'Видалити фото';
  removeBtn.addEventListener('click', () => {
    wrapper.remove();
    selectedFiles = selectedFiles.filter(f => f !== file);
  });

  reader.onload = (e) => (img.src = e.target.result);
  reader.readAsDataURL(file);

  wrapper.appendChild(img);
  wrapper.appendChild(removeBtn);
  previewContainer.appendChild(wrapper);
}

// ===== ОЧИЩЕННЯ (викликається з editModal.js) =====
function resetUploadZone() {
  selectedFiles = [];
  previewContainer.innerHTML = '';
}

// ===== ВІДПРАВКА ФОРМИ =====
editForm.addEventListener('submit', async (e) => {
  e.preventDefault();

  const formData = new FormData(editForm);
  selectedFiles.forEach(file => formData.append('files', file));

  const response = await fetch(editForm.action, {
    method: 'POST',
    body: formData
  });

  if (response.ok) {
    alert('Товар оновлено!');
    closeEditProductModal();
  } else {
    alert('Помилка при оновленні товару!');
  }
});

