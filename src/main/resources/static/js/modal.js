/*


document.addEventListener('DOMContentLoaded', function () {
    const select = document.getElementById('category_input');
    checkAddNew(select); // одразу тригеримо для вибраної категорії
});

document.querySelector('#addProductModal form').addEventListener('submit', function (e) {
    const attrContainer = document.getElementById('attributesContainer');
    const hasAttributes = attrContainer.querySelectorAll('.dynamic-attribute').length > 0;

    if (!hasAttributes) {
        e.preventDefault();
        alert("Додайте хоча б один атрибут перед збереженням товару!");
    }
});

document.querySelector('#addProductModal form').addEventListener('submit', (e) => {
    const select = document.getElementById('category_input');
    const newCatInput = document.getElementById('newValueInput');

    if (select.value === '__add__') {
        const newCatName = newCatInput.value.trim();
        if (!newCatName) {
            e.preventDefault();
            alert('Будь ласка, введіть нову категорію або виберіть існуючу.');
            return;
        }

        // 👉 Автоматично створюємо і вибираємо нову категорію
        addNewOption();
    }
});

function checkAddNew(select) {
    const value = select.value;
    const addNewWrapper = document.getElementById('addNewWrapper');
    const newAttrSection = document.getElementById('newAttributeSection');
    const attrContainer = document.getElementById('attributesContainer');

    if (value === '__add__') {
        addNewWrapper.style.display = 'block';
        attrContainer.innerHTML = ''; // очищаємо старі
        newAttrSection.style.display = 'block';
    } else {
        addNewWrapper.style.display = 'none';
        newAttrSection.style.display = 'block';
        getAllCatAttribs(value);
    }
}

function addNewOption() {
    const input = document.getElementById("newValueInput");
    const value = input.value.trim();
    const select = document.getElementById("category_input");

    if (!value) {
        alert("Введіть значення");
        return;
    }

    // Створити нову опцію
    const newOption = document.createElement("option");
    newOption.text = value;
    newOption.value = value;

    // Додати перед останньою (щоб не після "+ Додати нове")
    select.insertBefore(newOption, select.options[select.options.length - 1]);

    // Встановити як вибране
    select.value = value;

    // 👉 Додати цей виклик, щоб оновити DOM
    checkAddNew(select);

    // Сховати блок введення і очистити поле
    document.getElementById("addNewWrapper").style.display = "none";
    input.value = "";
}


function getAllCatAttribs(categoryName) {
    const encodedCategory = encodeURIComponent(categoryName);

    fetch(`/api/attributes/getCatAttribsWithUnitsFetched?catName=${encodedCategory}`)
      .then(res => res.json())                // перетворюємо відповідь у JSON
      .then(data => {
        console.log(data);                    // виводимо реальні дані
        renderAttributes(data);               // далі відображаємо
      })
      .catch(err => console.error('Помилка при завантаженні атрибутів:', err));
}

function renderAttributes(attributes) {
    const container = document.getElementById('attributesContainer');
    container.innerHTML = ''; // Очистити попереднє

    attributes.forEach((attr, index) => {
        const wrapper = document.createElement('div');
        wrapper.classList.add('dynamic-attribute');

        // HTML для інпуту та прихованих полів
        wrapper.innerHTML = `
            <label>${attr.attributeName}:</label>
            <input type="text" name="attributeList[${index}].attribValue" value="${attr.attribValue || ''}" class="attrib-input">
            <input type="hidden" name="attributeList[${index}].attrName" value="${attr.attrName}">
            <input type="hidden" name="attributeList[${index}].attributeType" value="${attr.attributeType}">
            <input type="hidden" name="attributeList[${index}].attribUnit" value="${attr.attribUnit || ''}">
        `;

        container.appendChild(wrapper);

        // Підвантажуємо селект із одиницями атрибута
        getAllAttribUnits(attr, index);
    });
}

function getAllAttribUnits(attribute, index) {
    const wrapper = document.querySelectorAll('.dynamic-attribute')[index];
    if (!wrapper) return;

    const encodedAttribName = encodeURIComponent(attribute.attributeName);

    fetch(`/api/attributes/getAllAttribUnits?attribName=${encodedAttribName}`)
        .then(res => res.json())
        .then(attr => {
            const hiddenUnitInput = wrapper.querySelector(`input[name="attributeList[${index}].attribUnit"]`);
            const valueInput = wrapper.querySelector(`input[name="attributeList[${index}].attribValue"]`);
            console.log (attr)

            // === Випадок 1: Є одиниці виміру (наприклад, "л", "мл") ===
            if (attr.units && attr.units.length > 0) {
                const select = document.createElement('select');
                select.classList.add('attrib-select');

                attr.units.forEach(u => {
                    const opt = document.createElement('option');
                    opt.value = u;
                    opt.textContent = u;
                    select.appendChild(opt);
                });

                // Встановити перше значення за замовчуванням
                if (!hiddenUnitInput.value) {
                    hiddenUnitInput.value = attr.units[0];
                }
                select.value = hiddenUnitInput.value;

                select.addEventListener('change', () => {
                    hiddenUnitInput.value = select.value;
                });
                wrapper.appendChild(select);
            }

            // === Випадок 2: Немає одиниць, але є список можливих значень (наприклад, країни) ===
            else if (attr.values && attr.values.length > 0) {
                const select = document.createElement('select');
                select.classList.add('attrib-select');

                attr.values.forEach(v => {
                    const opt = document.createElement('option');
                    opt.value = v;
                    opt.textContent = v;
                    select.appendChild(opt);
                });

                // Встановити значення за замовчуванням
                if (!valueInput.value) {
                    valueInput.value = attr.values[0];
                }
                select.value = valueInput.value;

                select.addEventListener('change', () => {
                    valueInput.value = select.value;
                });

                wrapper.appendChild(select);
            }

            // === Випадок 3: Ні одиниць, ні списку — залишаємо текстовий input ===
            else {
                // нічого не робимо — input уже є
            }
        })
        .catch(err => console.error('Помилка при отриманні атрибута:', err));
}




function addNewAttribute() {
    const name = document.getElementById('newAttrName').value.trim();
    const value = document.getElementById('newAttrValue').value.trim();
    const unit = document.getElementById('newAttrUnit').value.trim();
    const type = document.getElementById('newAttrType').value;

    if (!name || !value) {
        alert('Назва і значення атрибуту обов\'язкові');
        return;
    }

    const container = document.getElementById('attributesContainer');
    const index = container.children.length;

    const wrapper = document.createElement('div');
    wrapper.classList.add('dynamic-attribute');

    wrapper.innerHTML = `
        <label>${name}:</label>
        <input type="text" name="attributeList[${index}].attribValue" value="${value}">
        <input type="hidden" name="attributeList[${index}].attrName" value="${name}">
        <input type="hidden" name="attributeList[${index}].attributeType" value="${type}">
        <input type="hidden" name="attributeList[${index}].attribUnit" value="${unit}">
    `;

    container.appendChild(wrapper);

    // Очистити поля
    document.getElementById('newAttrName').value = '';
    document.getElementById('newAttrValue').value = '';
    document.getElementById('newAttrUnit').value = '';
}
*/
document.addEventListener('DOMContentLoaded', function () {
  const select = document.getElementById('category_input');
  checkAddNew(select);
});

document.querySelector('#addProductModal form').addEventListener('submit', function (e) {
  const attrContainer = document.getElementById('attributesContainer');
  const hasAttributes = attrContainer.querySelectorAll('.dynamic-attribute').length > 0;
  if (!hasAttributes) {
    e.preventDefault();
    alert("Додайте хоча б один атрибут перед збереженням товару!");
  }
});

document.querySelector('#addProductModal form').addEventListener('submit', (e) => {
  const select = document.getElementById('category_input');
  const newCatInput = document.getElementById('newValueInput');
  if (select.value === '__add__') {
    const newCatName = newCatInput.value.trim();
    if (!newCatName) {
      e.preventDefault();
      alert('Будь ласка, введіть нову категорію або виберіть існуючу.');
      return;
    }
    addNewOption();
  }
});

function checkAddNew(select) {
  const value = select.value;
  const addNewWrapper = document.getElementById('addNewWrapper');
  const newAttrSection = document.getElementById('newAttributeSection');
  const attrContainer = document.getElementById('attributesContainer');

  if (value === '__add__') {
    addNewWrapper.style.display = 'block';
    attrContainer.innerHTML = '';
    newAttrSection.style.display = 'block';
  } else {
    addNewWrapper.style.display = 'none';
    newAttrSection.style.display = 'block';
    getAllCatAttribs(value);
  }
}

function addNewOption() {
  const input = document.getElementById("newValueInput");
  const value = input.value.trim();
  const select = document.getElementById("category_input");
  if (!value) {
    alert("Введіть значення");
    return;
  }
  const newOption = document.createElement("option");
  newOption.text = value;
  newOption.value = value;
  select.insertBefore(newOption, select.options[select.options.length - 1]);
  select.value = value;
  checkAddNew(select);
  document.getElementById("addNewWrapper").style.display = "none";
  input.value = "";
}

function getAllCatAttribs(categoryName) {
  const encodedCategory = encodeURIComponent(categoryName);
  fetch(`/api/attributes/getCatAttribsWithUnitsFetched?catName=${encodedCategory}`)
    .then(res => res.json())
    .then(data => {
      console.log("Отримані атрибути категорії:", data);
      renderAttributes(data);
    })
    .catch(err => console.error('Помилка при завантаженні атрибутів:', err));
}

function renderAttributes(attributes) {
  const container = document.getElementById('attributesContainer');
  container.innerHTML = '';

  attributes.forEach((attr, index) => {
    const wrapper = document.createElement('div');
    wrapper.classList.add('dynamic-attribute');

    wrapper.innerHTML = `
      <label>${attr.attributeName}:</label>
      <input type="text" name="attributeList[${index}].attribValue" value="${attr.attributeValue || ''}" class="attrib-input">
      <input type="hidden" name="attributeList[${index}].attrName" value="${attr.attributeName}">
      <input type="hidden" name="attributeList[${index}].attributeType" value="${attr.attributeType}">
      <input type="hidden" name="attributeList[${index}].attribUnit" value="">
    `;

    container.appendChild(wrapper);

    // Після додавання елемента — підвантажуємо селект одиниць
    getAllAttribUnits(attr, index);
  });
}

function getAllAttribUnits(attribute, index) {
  const wrapper = document.querySelectorAll('.dynamic-attribute')[index];
  if (!wrapper) return;

  const encodedAttribName = encodeURIComponent(attribute.attributeName);

  fetch(`/api/attributes/getAllAttribUnits?attribName=${encodedAttribName}`)
    .then(res => res.json())
    .then(units => {
      console.log(`Одиниці для ${attribute.attributeName}:`, units);

      const hiddenUnitInput = wrapper.querySelector(`input[name="attributeList[${index}].attribUnit"]`);
      const valueInput = wrapper.querySelector(`input[name="attributeList[${index}].attribValue"]`);

      // Якщо API повертає масив одиниць
      if (Array.isArray(units) && units.length > 0) {
        const select = document.createElement('select');
        select.classList.add('attrib-select');

        units.forEach(u => {
          const opt = document.createElement('option');
          opt.value = u;
          opt.textContent = u;
          select.appendChild(opt);
        });

        // Встановлюємо значення за замовчуванням
        hiddenUnitInput.value = units[0];

        select.addEventListener('change', () => {
          hiddenUnitInput.value = select.value;
        });

        wrapper.appendChild(select);
      }
    })
    .catch(err => console.error('Помилка при отриманні одиниць атрибуту:', err));
}

function addNewAttribute() {
  const name = document.getElementById('newAttrName').value.trim();
  const value = document.getElementById('newAttrValue').value.trim();
  const unit = document.getElementById('newAttrUnit').value.trim();
  const type = document.getElementById('newAttrType').value;

  if (!name || !value) {
    alert("Назва і значення атрибуту обов'язкові");
    return;
  }

  const container = document.getElementById('attributesContainer');
  const index = container.children.length;
  const wrapper = document.createElement('div');
  wrapper.classList.add('dynamic-attribute');

  wrapper.innerHTML = `
    <label>${name}:</label>
    <input type="text" name="attributeList[${index}].attribValue" value="${value}">
    <input type="hidden" name="attributeList[${index}].attrName" value="${name}">
    <input type="hidden" name="attributeList[${index}].attributeType" value="${type}">
    <input type="hidden" name="attributeList[${index}].attribUnit" value="${unit}">
  `;

  container.appendChild(wrapper);

  document.getElementById('newAttrName').value = '';
  document.getElementById('newAttrValue').value = '';
  document.getElementById('newAttrUnit').value = '';
}
