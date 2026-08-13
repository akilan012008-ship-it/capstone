/* AKILAN MART - CART & AJAX HANDLERS */

function updateCartQuantity(productId, newQty, contextPath) {
    if (newQty < 1) return;

    const formData = new URLSearchParams();
    formData.append('productId', productId);
    formData.append('quantity', newQty);

    fetch(contextPath + '/cart/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw new Error(err.message || 'Cart update failed'); });
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            // Update row subtotal
            const priceEl = document.getElementById('unit-price-' + productId);
            const subtotalEl = document.getElementById('subtotal-' + productId);
            const totalEl = document.getElementById('cart-grand-total');

            if (priceEl && subtotalEl) {
                const unitPrice = parseFloat(priceEl.getAttribute('data-price'));
                const newSubtotal = (unitPrice * newQty).toFixed(2);
                subtotalEl.textContent = '₹' + Number(newSubtotal).toLocaleString('en-IN', {minimumFractionDigits: 2});
            }

            if (totalEl && data.cartTotal) {
                totalEl.textContent = '₹' + Number(data.cartTotal).toLocaleString('en-IN', {minimumFractionDigits: 2});
            }
        }
    })
    .catch(error => {
        alert('Cart Error: ' + error.message);
        location.reload();
    });
}

function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to proceed with this action?');
}
