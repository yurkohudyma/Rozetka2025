document.addEventListener('DOMContentLoaded', function () {
    // 1. Обробка чекбоксів фільтрів
    const checkboxes = document.querySelectorAll('.filter_checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const selectedFiltersMap = {};

            document.querySelectorAll('.filter_checkbox:checked').forEach(cb => {
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
                    } else {
                        console.warn('❗ Контейнер #products_container не знайдено. Можливо, сервер повернув не той HTML?');
                    }
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                });
        });
    });

    // 2. Обробка кнопки "Скинути фільтри"
    const resetButton = document.getElementById('selected-filters-reset');
    if (resetButton) {
        resetButton.addEventListener('click', resetSeatSelection);
    }
});

// Окрема функція — скидання чекбоксів
function resetSeatSelection() {
    document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
        checkbox.checked = false;
    });

    // Імітація події "change", щоб оновити продукти після скидання
    document.querySelectorAll('.filter_checkbox').forEach(cb => {
        cb.dispatchEvent(new Event('change'));
    });

    console.log('✅ Фільтри скинуто');
}

document.addEventListener('click', function (event) {
    if (event.target && event.target.id === 'selected-filters-reset') {
        resetSeatSelection();
    }
});
