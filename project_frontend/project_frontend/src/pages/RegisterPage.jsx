import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/RegisterPage.css';

function RegisterPage() {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        phoneNumber: '',
        firstName: '',
        surname: '',
        dateOfBirth: '',
        county: '',
        city: '',
        address: '',
        cnp: ''
    });

    const [error, setError] = useState('');
    const [confirmationMessage, setConfirmationMessage] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleRegister = (e) => {
        e.preventDefault();

        // Example validation: ensure all fields are filled
        const emptyField = Object.values(formData).some(value => value === '');
        if (emptyField) {
            setError('Please fill all fields');
            return;
        }

        setError('');
        setConfirmationMessage('A confirmation email has been sent to your email address.');

        // Simulate an API request, which sends the confirmation email
        setTimeout(() => {
            // Redirect to the login page after showing the confirmation message
            navigate('/login');
        }, 3000);  // Wait for 3 seconds before redirecting
    };

    const counties = [
        "Alba", "Arad", "Argeș", "Bacău", "Bihor", "Bistrița-Năsăud", "Botoșani", "Brașov", "Brăila",
        "Buzău", "Caraș-Severin", "Călărași", "Cluj", "Constanța", "Covasna", "Dâmbovița", "Dolj",
        "Galați", "Giurgiu", "Gorj", "Harghita", "Hunedoara", "Ialomița", "Iași", "Ilfov", "Maramureș",
        "Mehedinți", "Mureș", "Neamț", "Olt", "Prahova", "Sălaj", "Satu Mare", "Sibiu", "Suceava",
        "Teleorman", "Timiș", "Tulcea", "Vaslui", "Vâlcea", "Vrancea", "București", "Foreign Country"
    ];

    return (
        <div className="register-container">
            <h2>Register</h2>
            <form onSubmit={handleRegister} className="form">
                <div className="inputGroup">
                    <label>Email:</label>
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>
                <div className="inputGroup">
                    <label>Password:</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                <div className="inputGroup">
                    <label>Phone Number:</label>
                    <input
                        type="tel"
                        name="phoneNumber"
                        value={formData.phoneNumber}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                <div className="inputGroup">
                    <label>First Name:</label>
                    <input
                        type="text"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>
                <div className="inputGroup">
                    <label>Surname:</label>
                    <input
                        type="text"
                        name="surname"
                        value={formData.surname}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                <div className="inputGroup">
                    <label>Date of Birth:</label>
                    <input
                        type="date"
                        name="dateOfBirth"
                        value={formData.dateOfBirth}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                <div className="inputGroup">
                    <label>County:</label>
                    <select
                        name="county"
                        value={formData.county}
                        onChange={handleChange}
                        required
                        className="input"
                    >
                        <option value="">Select County</option>
                        {counties.map((county, index) => (
                            <option key={index} value={county}>
                                {county}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="inputGroup">
                    <label>City:</label>
                    <input
                        type="text"
                        name="city"
                        value={formData.city}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                <div className="inputGroup">
                    <label>Address:</label>
                    <input
                        type="text"
                        name="address"
                        value={formData.address}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                <div className="inputGroup">
                    <label>CNP:</label>
                    <input
                        type="text"
                        name="cnp"
                        value={formData.cnp}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                {error && <p className="error">{error}</p>}
                {confirmationMessage && <p className="confirmation-message">{confirmationMessage}</p>}
                <button type="submit">Register</button>
            </form>
        </div>
    );
}

export default RegisterPage;
