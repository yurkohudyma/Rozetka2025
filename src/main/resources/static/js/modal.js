document.addEventListener('DOMContentLoaded', function () {
    const select = document.getElementById('category_input');
    checkAddNew(select); // одразу тригеримо для вибраної категорії
});

function openAddProductModal() {
    document.getElementById("addProductModal").style.display = "block";
}

function closeAddProductModal() {
    document.getElementById("addProductModal").style.display = "none";
}

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

    // Сховати блок введення і очистити поле
    document.getElementById("addNewWrapper").style.display = "none";
    input.value = "";
}

function getAllCatAttribs(categoryName) {
    const encodedCategory = encodeURIComponent(categoryName);

    fetch(`/api/attributes/getCatAttribs?catName=${encodedCategory}`)
      .then(res => res.json())                // перетворюємо відповідь у JSON
      .then(data => {
        console.log(data);                    // виводимо реальні дані
        renderAttributes(data);               // далі відображаємо
      })
      .catch(err => console.error('Помилка при завантаженні атрибутів:', err));


    /*fetch(`/attributes/getCatAttribs?catName=${encodedCategory}`)
        .then(res => res.json())
        .then(res => console.log(res.json))
        .then(data => renderAttributes(data))
        .catch(err => console.error('Помилка при завантаженні атрибутів:', err));*/
}

function renderAttributes(attributes) {
    const container = document.getElementById('attributesContainer');
    container.innerHTML = ''; // Очистити попереднє

    attributes.forEach((attr, index) => {
        const wrapper = document.createElement('div');
        wrapper.classList.add('dynamic-attribute');

        wrapper.innerHTML = `
            <label>${attr.attrName}:</label>
            <input type="text" name="attributeList[${index}].attribValue" value="${attr.attribValue || ''}">
            <input type="hidden" name="attributeList[${index}].attrName" value="${attr.attrName}">
            <input type="hidden" name="attributeList[${index}].attributeType" value="${attr.attributeType}">
            <input type="hidden" name="attributeList[${index}].attribUnit" value="${attr.attribUnit || ''}">
        `;

        container.appendChild(wrapper);
    });
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
