document.addEventListener('DOMContentLoaded', function () {
    const slider = document.getElementById('price-slider');
    if (!slider) {
        console.warn("❗ Слайдер не знайдено в DOM");
        return;
    }

    const min = parseFloat(slider.dataset.min);
    const max = parseFloat(slider.dataset.max);

    if (isNaN(min) || isNaN(max)) {
        console.error("❌ Не вдалося отримати числові min/max значення для слайдера");
        return;
    }

    noUiSlider.create(slider, {
        start: [min, max],
        connect: true,
        step: 1,
        range: {
            min: min,
            max: max
        },
        format: {
            to: value => value.toFixed(2),
            from: value => parseFloat(value)
        }
    });

    const minDisplay = document.getElementById('min-price');
    const maxDisplay = document.getElementById('max-price');

    slider.noUiSlider.on('update', function (values) {
        if (minDisplay) minDisplay.textContent = values[0];
        if (maxDisplay) maxDisplay.textContent = values[1];
    });

    slider.noUiSlider.on('change', function () {
        applyFilters();
    });
});
