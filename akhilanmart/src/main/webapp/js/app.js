/* AKILAN MART - CORE APP JS */

document.addEventListener('DOMContentLoaded', () => {
    // Auto-dismiss alert notifications after 5 seconds
    const alerts = document.querySelectorAll('.flash-message');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });

    // Star rating selector handler on product review forms
    const ratingStars = document.querySelectorAll('.star-rating-input span');
    const ratingInput = document.getElementById('selectedRatingInput');

    if (ratingStars.length > 0 && ratingInput) {
        ratingStars.forEach(star => {
            star.addEventListener('click', () => {
                const val = star.getAttribute('data-value');
                ratingInput.value = val;
                ratingStars.forEach(s => {
                    const sVal = s.getAttribute('data-value');
                    if (sVal <= val) {
                        s.textContent = '★';
                        s.classList.add('active');
                    } else {
                        s.textContent = '☆';
                        s.classList.remove('active');
                    }
                });
            });
        });
    }
});
