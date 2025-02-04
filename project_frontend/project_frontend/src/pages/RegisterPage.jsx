import { useState } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import '../styles/RegisterPage.css';
import axios from "axios";

function RegisterPage() {

    const countyMapping = {
        "Alba": "AB",
        "Arad": "AR",
        "Argeș": "AG",
        "Bacău": "BC",
        "Bihor": "BH",
        "Bistrița-Năsăud": "BN",
        "Botoșani": "BS",
        "Brașov": "BV",
        "Brăila": "BR",
        "Buzău": "BZ",
        "Caraș-Severin": "CS",
        "Călărași": "CL",
        "Cluj": "CJ",
        "Constanța": "CS",
        "Covasna": "CV",
        "Dâmbovița": "DB",
        "Dolj": "DJ",
        "Galați": "GL",
        "Giurgiu": "GR",
        "Gorj": "GJ",
        "Harghita": "HR",
        "Hunedoara": "HD",
        "Ialomița": "IL",
        "Iași": "IS",
        "Ilfov": "IF",
        "Maramureș": "MM",
        "Mehedinți": "MH",
        "Mureș": "MS",
        "Neamț": "NT",
        "Olt": "OT",
        "Prahova": "PH",
        "Sălaj": "SJ",
        "Satu Mare": "SM",
        "Sibiu": "SB",
        "Suceava": "SV",
        "Teleorman": "TR",
        "Timiș": "TM",
        "Tulcea": "TL",
        "Vaslui": "VS",
        "Vâlcea": "VL",
        "Vrancea": "VR",
        "București": "B",
        "Foreign Country": "FC"
    };

    const [formData, setFormData] = useState({
        email: '',
        password: '',
        confirmPassword: '',
        phoneNumber: '',
        firstName: '',
        lastName: '',
        dateOfBirth: '',
        county: '',
        city: '',
        address: '',
        cnp: ''
    });

    const [idDocument, setIdDocument] = useState(null);
    const [error, setError] = useState('');
    const [confirmationMessage, setConfirmationMessage] = useState('');
    const navigate = useNavigate();

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    };


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleFileChange = (e) => {
        setIdDocument(e.target.files[0]);
    };

    const handleRegister = async (e) => {
        e.preventDefault();

        const { confirmPassword, ...payload } = formData;

        const emptyField = Object.values(payload).some((value) => value === '');
        if (emptyField) {
            setError('Please fill all fields');
            return;
        }

        if (formData.password !== confirmPassword) {
            setError('Passwords do not match');
            return;
        }

        if (!validateEmail(formData.email)) {
            setError('Please enter a valid email address');
            return;
        }

        if (!/^\d{13}$/.test(formData.cnp)) {
            setError('CNP must contain exactly 13 digits');
            return;
        }

        if (!idDocument) {
            setError('Please upload a copy of your ID');
            return;
        }

        setError('');
        setConfirmationMessage('Registering...');

        console.log(formData);
        console.log(JSON.stringify(formData));

        const formDataToSend = new FormData();
        //Object.keys(formData).forEach((key) => formDataToSend.append(key, formData[key]));
        formDataToSend.append('details', JSON.stringify(formData));
        formDataToSend.append('idDocument', idDocument);
        // const idDocumentName = idDocument.name;
        // // formDataToSend.append('idDocument', {
        // //     uri: idDocument.uri || idDocument,
        // //     name: idDocumentName,
        // //     type: 'image/jpg',
        // // });

        axios.post('http://localhost:8080/users', formDataToSend, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        })
            .then(function (response) {
                console.log(response);
                navigate('/login');
            })
            .catch(function (error) {
                console.log(error);
                setError('Registration failed. Please try again.');
            });
    };

    const counties = Object.keys(countyMapping);

    // const counties = [
    //     "Alba", "Arad", "Argeș", "Bacău", "Bihor", "Bistrița-Năsăud", "Botoșani", "Brașov", "Brăila",
    //     "Buzău", "Caraș-Severin", "Călărași", "Cluj", "Constanța", "Covasna", "Dâmbovița", "Dolj",
    //     "Galați", "Giurgiu", "Gorj", "Harghita", "Hunedoara", "Ialomița", "Iași", "Ilfov", "Maramureș",
    //     "Mehedinți", "Mureș", "Neamț", "Olt", "Prahova", "Sălaj", "Satu Mare", "Sibiu", "Suceava",
    //     "Teleorman", "Timiș", "Tulcea", "Vaslui", "Vâlcea", "Vrancea", "București", "Foreign Country"
    // ];

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
                    <label>Confirm Password:</label>
                    <input
                        type="password"
                        name="confirmPassword"
                        value={formData.confirmPassword}
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
                    <label>Last Name:</label>
                    <input
                        type="text"
                        name="lastName"
                        value={formData.lastName}
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
                            <option key={index} value={countyMapping[county]}>
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

                <div className="inputGroup">
                    <label>ID Document (Photo/Scan):</label>
                    <input
                        type="file"
                        name="idDocument"
                        onChange={handleFileChange}
                        required
                        className="input"
                        accept="image/*"
                    />
                </div>

                    {error && <p className="error">{error}</p>}
                    {confirmationMessage && <p className="confirmation-message">{confirmationMessage}</p>}
                    <button type="submit">Register</button>
            </form>

            <div className="links">
                <p>
                    Already have an account?{' '}
                    <Link to="/login" className="link">Log In</Link>
                </p>
            </div>
        </div>
);
}

export default RegisterPage;
