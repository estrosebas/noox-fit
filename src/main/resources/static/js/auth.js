// Login form handling
function handleLoginSubmit(event) {
    event.preventDefault();
    
    // Get form data
    const correo = document.getElementById('correo').value;
    const password = document.getElementById('password').value;

    // Update preview in confirmation modal
    document.getElementById('previewLoginEmail').textContent = correo;

    // Show confirmation modal
    const confirmationModal = new bootstrap.Modal(document.getElementById('loginConfirmationModal'));
    confirmationModal.show();

    return false;
}

function submitLoginForm() {
    // Get form data
    const formData = {
        correo: document.getElementById('correo').value,
        contraseña: document.getElementById('password').value
    };

    // Send data to backend
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.text())
    .then(result => {
        if (result === 'success') {
            // Hide confirmation modal
            const confirmationModal = bootstrap.Modal.getInstance(document.getElementById('loginConfirmationModal'));
            confirmationModal.hide();

            // Show success modal
            const successModal = new bootstrap.Modal(document.getElementById('loginSuccessModal'));
            successModal.show();

            // Clear form
            document.getElementById('loginForm').reset();

            // Redirect to user page after 2 seconds
            setTimeout(() => {
                window.location.href = '/user';
            }, 2000);
        } else {
            alert('Error logging in. Please try again.');
            const confirmationModal = bootstrap.Modal.getInstance(document.getElementById('loginConfirmationModal'));
            confirmationModal.hide();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error logging in. Please try again.');
        const confirmationModal = bootstrap.Modal.getInstance(document.getElementById('loginConfirmationModal'));
        confirmationModal.hide();
    });
}

// Registration form handling
function handleRegisterSubmit(event) {
    event.preventDefault();
    
    // Get form data
    const fullName = document.getElementById('fullname').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const country = document.getElementById('country').value;
    const terms = document.getElementById('terms').checked;
    const genderInputs = document.getElementsByName('gender');
    let gender = '';
    for (const input of genderInputs) {
        if (input.checked) {
            gender = input.value;
            break;
        }
    }

    // Validate form
    if (!fullName || !email || !password || !country || !gender || !terms) {
        document.getElementById('formAlert').classList.remove('d-none');
        return false;
    }
    document.getElementById('formAlert').classList.add('d-none');

    // Update preview in confirmation modal
    document.getElementById('previewRegisterName').textContent = fullName;
    document.getElementById('previewRegisterEmail').textContent = email;
    document.getElementById('previewRegisterCountry').textContent = country;
    document.getElementById('previewRegisterGender').textContent = gender;

    // Show confirmation modal
    const confirmationModal = new bootstrap.Modal(document.getElementById('registerConfirmationModal'));
    confirmationModal.show();

    return false;
}

function submitRegisterForm() {
    // Get form data
    const formData = {
        nombre: document.getElementById('nombre').value,
        apellido: document.getElementById('apellido').value,
        edad: parseInt(document.getElementById('edad').value),
        correo: document.getElementById('correo').value,
        contraseña: document.getElementById('password').value,
        direccion: document.getElementById('direccion').value,
        sexo: document.querySelector('input[name="sexo"]:checked')?.value,
        termsAccepted: document.getElementById('terms').checked
    };

    // Send data to backend
    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.text())
    .then(result => {
        if (result === 'success') {
            // Hide confirmation modal
            const confirmationModal = bootstrap.Modal.getInstance(document.getElementById('registerConfirmationModal'));
            confirmationModal.hide();

            // Show success modal
            const successModal = new bootstrap.Modal(document.getElementById('registerSuccessModal'));
            successModal.show();

            // Clear form
            document.getElementById('registerForm').reset();

            // Redirect to login page after 2 seconds
            setTimeout(() => {
                window.location.href = '/login';
            }, 2000);
        } else {
            alert('Error registering. Please try again. ' + result);
            const confirmationModal = bootstrap.Modal.getInstance(document.getElementById('registerConfirmationModal'));
            confirmationModal.hide();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error registering. Please try again.');
        const confirmationModal = bootstrap.Modal.getInstance(document.getElementById('registerConfirmationModal'));
        confirmationModal.hide();
    });
}