/* AKILAN MART - FORM VALIDATION */

function validateRegisterForm() {
    const name = document.getElementById('regName').value.trim();
    const email = document.getElementById('regEmail').value.trim();
    const password = document.getElementById('regPassword').value;
    const confirmPassword = document.getElementById('regConfirmPassword').value;

    if (!name) {
        alert('Please enter your full name.');
        return false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        alert('Please enter a valid email address.');
        return false;
    }

    if (password.length < 6) {
        alert('Password must be at least 6 characters long.');
        return false;
    }

    if (password !== confirmPassword) {
        alert('Passwords do not match.');
        return false;
    }

    return true;
}

function validateProductForm() {
    const name = document.getElementById('prodName').value.trim();
    const price = parseFloat(document.getElementById('prodPrice').value);
    const stock = parseInt(document.getElementById('prodStock').value);
    const category = document.getElementById('prodCategory').value;

    if (!name) {
        alert('Product name is required.');
        return false;
    }

    if (isNaN(price) || price <= 0) {
        alert('Price must be greater than 0.');
        return false;
    }

    if (isNaN(stock) || stock < 0) {
        alert('Stock cannot be negative.');
        return false;
    }

    if (!category) {
        alert('Please select a valid category.');
        return false;
    }

    return true;
}
