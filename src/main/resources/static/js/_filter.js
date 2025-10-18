document.addEventListener('DOMContentLoaded', function () {
    const checkboxes = document.querySelectorAll('.filter_checkbox');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            // Зібрати всі обрані атрибути
            const selectedFilters = Array
            .from(document.querySelectorAll('.filter_checkbox:checked'))
                .map(cb => cb.value);

                const category = window.cat;
                const encodedCategory = encodeURIComponent(category);

            // Надіслати їх на сервер
            fetch(`/filter/${encodedCategory}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ filters: selectedFilters })
            })
            .then(response => {
                if (!response.ok) throw new Error('Server error');
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
