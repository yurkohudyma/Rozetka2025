document.addEventListener('DOMContentLoaded', function () {
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
});
