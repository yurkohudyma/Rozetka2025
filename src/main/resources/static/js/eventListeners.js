document.addEventListener("DOMContentLoaded", () => {
  // Шукаємо всі елементи, які мають клас .product_unit_vendor
  const vendorBlocks = document.querySelectorAll(".product_unit_vendor");

  vendorBlocks.forEach(block => {
    const vendorCode = block.dataset.vendorId; // беремо data-vendor-id

    if (!vendorCode) {
      console.warn("⚠️ Відсутній data-vendor-id у елементі:", block);
      return;
    }

    // Звертаємося до ендпойнта
    fetch(`/api/getVendorName?vendorCode=${vendorCode}`)
      .then(response => {
        if (!response.ok) {
          throw new Error(`Помилка завантаження: ${response.status}`);
        }
        return response.json();
      })
      .then(vendor => {
        // Очікується, що бекенд повертає JSON з полем name
        block.textContent += ' ' + vendor.name;
      })
      .catch(error => {
        console.error("Помилка при отриманні вендора:", error);
        block.textContent = "Невідомо";
      });
  });
});

function openEditProductModal() {
    document.getElementById("editProductModal").style.display = "block";
}

function closeEditProductModal() {
    document.getElementById("editProductModal").style.display = "none";
}

function openAddProductModal() {
    document.getElementById("addProductModal").style.display = "block";
}

function closeAddProductModal() {
    document.getElementById("addProductModal").style.display = "none";
}

/*//process edit-modal
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
});*/

// process edit-modal
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

    // Очищаємо попередні прев’ю та файли
    resetUploadZone();

    // Відкриваємо модальне
    openEditProductModal();
  });
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

