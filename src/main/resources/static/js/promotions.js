// Promotions handling
function loadPromotions() {
    const modal = new bootstrap.Modal(document.getElementById('promotionsModal'));
    modal.show();

    const loadingEl = document.getElementById('promotionsLoading');
    const errorEl = document.getElementById('promotionsError');
    const contentEl = document.getElementById('promotionsContent');
    const emptyEl = document.getElementById('promotionsEmpty');

    loadingEl.classList.remove('d-none');
    errorEl.classList.add('d-none');
    contentEl.classList.add('d-none');
    emptyEl.classList.add('d-none');

    fetch('/api/promotions')
        .then(response => response.json())
        .then(promotions => {
            loadingEl.classList.add('d-none');
            if (promotions.length === 0) {
                emptyEl.classList.remove('d-none');
            } else {
                contentEl.innerHTML = promotions.map(promo => `
                    <div class="card bg-primary text-white border-primary mb-3 shadow-sm">
                        <div class="card-body">
                            <h5 class="card-title fw-bold">${promo.title}</h5>
                            <p class="card-text">${promo.description}</p>
                            <div class="d-flex justify-content-between align-items-center">
                                <span class="badge bg-warning text-dark fw-bold">${document.getElementById('discountText').textContent}: ${promo.discountPercentage}</span>
                                <small class="text-light">${document.getElementById('validUntilText').textContent}: ${promo.validUntil}</small>
                            </div>
                        </div>
                    </div>
                `).join('');
                contentEl.classList.remove('d-none');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            loadingEl.classList.add('d-none');
            errorEl.classList.remove('d-none');
        });
}
