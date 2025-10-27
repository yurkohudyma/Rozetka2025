document.querySelectorAll('.edit_ico_orderlist').forEach(btn => {
  btn.addEventListener('click', () => {
    const code = btn.dataset.code;
    const name = btn.dataset.name;
    const price = btn.dataset.price;

    // Заповнюємо форму
    document.querySelector('#productName').value = name;
    document.querySelector('#productPrice').value = price;

    // Оновлюємо action
    const form = document.querySelector('#editForm');
    form.action = `/edit/${code}`;

    // Відкриваємо модальне
    openEditProductModal();
  });
});

document.addEventListener('DOMContentLoaded', function () {
    const select = document.getElementById('category_input');
    checkAddNew(select); // одразу тригеримо для вибраної категорії
});

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('deleteAllCatsForm');
    form.addEventListener('submit', function(e) {
        const confirmed = confirm("⚠️ Усі КАТЕГОРІЇ та дочірні елементи (ТОВАРИ і АТРИБУТИ) буде видалено безповоротно.\n\nВи впевнені?");
        if (!confirmed) {
            e.preventDefault();
        }
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('deleteAllProductsForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            const confirmed = confirm("⚠️ Усі товари буде видалено безповоротно.\n\nВи впевнені?");
            if (!confirmed) {
                e.preventDefault();
            }
        });
    }
});


document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('.delete_ico_orderlist');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const productId = this.dataset.productId;
            const confirmed = confirm(`⚠️ Точно видаляємо продукт #${productId}?`);
            if (!confirmed) {
                e.preventDefault();
            }
        });
    });
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