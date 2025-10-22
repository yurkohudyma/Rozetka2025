document.addEventListener('DOMContentLoaded', function () {
    bindAllEvents();
    updateResetButtonVisibility();
});

function bindAllEvents() {
    bindFilterCheckboxes();
    bindResetButton();
}

function bindFilterCheckboxes() {
    // Знімаємо старі слухачі, якщо DOM оновився
    const oldCheckboxes = document.querySelectorAll('.filter_checkbox');
    oldCheckboxes.forEach(cb => {
        cb.replaceWith(cb.cloneNode(true));
    });

    const checkboxes = document.querySelectorAll('.filter_checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', applyFilters);
        console.log('🔗 Чекбокс підключено:', checkbox.name || checkbox.id);
    });
}

function applyFilters() {
    const selectedFiltersMap = {};
    const checkedBoxes = document.querySelectorAll('.filter_checkbox:checked');

    checkedBoxes.forEach(cb => {
        const filterName = cb.dataset.filter;
        const value = cb.value;

        if (!selectedFiltersMap[filterName]) {
            selectedFiltersMap[filterName] = [];
        }
        selectedFiltersMap[filterName].push(value);
    });

    // ➕ Читання слайдера
    const slider = document.getElementById('price-slider');
    let priceRange = null;
    if (slider && slider.noUiSlider) {
        const [minPrice, maxPrice] = slider.noUiSlider.get();
        priceRange = { minPrice, maxPrice };
    }

    // 📋 Побудова тіла запиту
    const requestBody = {
        filterMap: selectedFiltersMap
    };

    if (priceRange) {
        requestBody.minPrice = priceRange.minPrice;
        requestBody.maxPrice = priceRange.maxPrice;
    }

    const category = window.cat;
    const encodedCategory = encodeURIComponent(category);

    // 🔍 DEBUG: виводимо все в консоль
    console.log("➡️ Запит фільтрації:");
    console.log("Категорія:", category);
    console.log("filterMap:", selectedFiltersMap);
    console.log("Ціни:", priceRange);

    fetch(`/filter/${encodedCategory}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Server error');
            }
            return response.text();
        })
        .then(html => {
            const container = document.getElementById('products_container');
            if (container) {
                container.innerHTML = html;

                // 🔁 Перепідключення
                bindAllEvents();
                updateResetButtonVisibility();
            } else {
                console.warn('❗ Контейнер #products_container не знайдено.');
            }
        })
        .catch(error => {
            console.error('❌ Fetch error:', error);
        });

    updateResetButtonVisibility();
}

function resetFiltersSelection() {
    // 1. Скидаємо всі чекбокси
    document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
        checkbox.checked = false;
    });

    // 2. Скидаємо слайдер
    const slider = document.getElementById('price-slider');
    if (slider && slider.noUiSlider) {
        const min = parseFloat(slider.dataset.min);
        const max = parseFloat(slider.dataset.max);
        slider.noUiSlider.set([min, max]);
    }

    // 3. Застосовуємо фільтри зі скинутими значеннями
    applyFilters();

    console.log('✅ Фільтри скинуто (чекбокси + слайдер)');
}

function bindResetButton() {
    const resetButton = document.getElementById('selected-filters-reset');
    if (resetButton) {
        resetButton.onclick = resetFiltersSelection;
    }
}

function updateResetButtonVisibility() {
    const anyChecked = document.querySelectorAll('.filter_checkbox:checked').length > 0;

    const slider = document.getElementById('price-slider');
    let sliderChanged = false;

    if (slider && slider.noUiSlider) {
        const [currentMin, currentMax] = slider.noUiSlider.get().map(parseFloat);
        const defaultMin = parseFloat(slider.dataset.min);
        const defaultMax = parseFloat(slider.dataset.max);

        // Якщо хоча б один з повзунків не у початковому положенні
        sliderChanged = currentMin !== defaultMin || currentMax !== defaultMax;
    }

    const resetContainer = document.getElementById('resetFiltersContainer');

    if (resetContainer) {
        resetContainer.style.display = (anyChecked || sliderChanged) ? 'block' : 'none';
    }
}
