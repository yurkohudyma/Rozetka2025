document.addEventListener("DOMContentLoaded", () => {
    const langSwitchBtn = document.querySelector('[data-locale="button.switch"]');
    let currentLang = document.documentElement.lang || 'uk';

    langSwitchBtn.addEventListener('click', async () => {
        currentLang = currentLang === 'uk' ? 'en' : 'uk';
        await loadTranslations(currentLang);
    });

    async function loadTranslations(lang) {
        try {
            const res = await fetch(`/i18n?lang=${lang}`);
            const messages = await res.json();

            // Знаходимо всі елементи з data-locale
            document.querySelectorAll('[data-locale]').forEach(el => {
                const key = el.getAttribute('data-locale');
                if (messages[key]) {
                    el.textContent = messages[key];
                }
            });

            // Оновлюємо lang у <html>
            document.documentElement.lang = lang;

        } catch (e) {
            console.error("Помилка завантаження перекладу:", e);
        }
    }
});

