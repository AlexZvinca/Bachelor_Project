import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/RegisterPage.css';

function NewAuthorizationForm() {
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
        county: '',
        licensePlateNumber: '',
        vehicleIdentification: '',
        description: ''
    });

    const [error, setError] = useState('');
    const [confirmationMessage, setConfirmationMessage] = useState('');
    const navigate = useNavigate();

    const userId = localStorage.getItem('userId');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const emptyField = Object.values(formData).some(value => value === '');
        if (emptyField) {
            setError('Please fill all fields');
            return;
        }

        setError('');
        setConfirmationMessage('Authorization request has been submitted successfully.');

        try {
            await axios.post('http://localhost:8080/authorizationRequest', {
                userId,
                county: formData.county,
                licensePlateNumber: formData.licensePlateNumber,
                vehicleIdentification: formData.vehicleIdentification,
                description: formData.description,
            }, {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
            });

            navigate('/dashboard');
        } catch (err) {
            setError('Failed to submit authorization request. Please try again.');
            console.error(err);
        }
    };

    const counties = Object.keys(countyMapping);

    return (
        <div className="register-container">
            <h2>Request a New Authorization</h2>
            <form onSubmit={handleSubmit} className="form">
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
                    <label>License Plate Number:</label>
                    <input
                        type="text"
                        name="licensePlateNumber"
                        value={formData.licensePlateNumber}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                <div className="inputGroup">
                    <label>Vehicle Identification:</label>
                    <input
                        type="text"
                        name="vehicleIdentification"
                        value={formData.vehicleIdentification}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                <div className="inputGroup">
                    <label>Description (make sure to include essential details, like scope, period, quantity, location of forest etc.):</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        required
                        className="input"
                    />
                </div>

                {error && <p className="error">{error}</p>}
                {confirmationMessage && <p className="confirmation-message">{confirmationMessage}</p>}
                <button type="submit">Submit Request</button>
            </form>
        </div>
    );
}

export default NewAuthorizationForm;
