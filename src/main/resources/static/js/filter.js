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

    const category = window.cat;
    const encodedCategory = encodeURIComponent(category);

    fetch(`/filter/${encodedCategory}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ filterMap: selectedFiltersMap })
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

                // Переназначення слухачів після оновлення DOM
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

function resetSeatSelection() {
    document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
        checkbox.checked = false;
    });

    // Один раз тригеримо applyFilters напряму
    applyFilters();

    console.log('✅ Фільтри скинуто');
}

function bindResetButton() {
    const resetButton = document.getElementById('selected-filters-reset');
    if (resetButton) {
        resetButton.onclick = resetSeatSelection;
    }
}

function updateResetButtonVisibility() {
    const anyChecked = document.querySelectorAll('.filter_checkbox:checked').length > 0;
    const resetContainer = document.getElementById('resetFiltersContainer');

    if (resetContainer) {
        resetContainer.style.display = anyChecked ? 'block' : 'none';
    }
}
