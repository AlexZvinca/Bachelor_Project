import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/Dashboard.css';

function Dashboard() {
    const [activeTab, setActiveTab] = useState('Authorizations');
    const [authorizations, setAuthorizations] = useState([]);
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const userId = localStorage.getItem('userId');

    useEffect(() => {
        if (activeTab === 'Authorizations') {
            fetchAuthorizations();
        } else if (activeTab === 'Profile') {
            fetchUserProfile();
        }
    }, [activeTab]);

    const fetchAuthorizations = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`http://localhost:8080/authorizationRequest/user/${userId}`, {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
            });
            setAuthorizations(response.data || []);
        } catch (err) {
            setError('Failed to fetch authorizations. Please try again later.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const fetchUserProfile = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`http://localhost:8080/users/${userId}`, {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
            });
            setUserData(response.data);
        } catch (err) {
            setError('Failed to fetch user profile. Please try again later.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleTabClick = (tab) => setActiveTab(tab);

    return (
        <div className="dashboard-container">
            <nav className="navbar">
                <button
                    className={`nav-item ${activeTab === 'Authorizations' ? 'active' : ''}`}
                    onClick={() => handleTabClick('Authorizations')}
                >
                    Authorizations
                </button>
                <button
                    className={`nav-item ${activeTab === 'Profile' ? 'active' : ''}`}
                    onClick={() => handleTabClick('Profile')}
                >
                    Profile
                </button>
            </nav>

            <div className="content">
                {loading && <p>Loading...</p>}
                {error && <p className="error">{error}</p>}

                {activeTab === 'Authorizations' && !loading && !error && (
                    <div className="authorizations">
                        <h2>Your Authorizations</h2>
                        {authorizations.length > 0 ? (
                            <ul className="authorization-list">
                                {authorizations.map((auth) => (
                                    <li key={auth.id} className="authorization-item">
                                        <p><strong>Vehicle:</strong> {auth.licensePlateNumber}</p>
                                        <p><strong>Authorization ID:</strong> {auth.id}</p>
                                        <p><strong>County:</strong> {auth.county}</p>
                                        <p><strong>Status:</strong> {auth.status}</p>
                                        <p><strong>Description:</strong> {auth.description}</p>
                                        <p><strong>Created At:</strong> {new Date(auth.createdAt).toLocaleString()}</p>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p>No authorizations found.</p>
                        )}
                        <button
                            className="new-authorization-btn"
                            onClick={() => window.location.href = '/new-authorization'}
                        >
                            Request a New Authorization
                        </button>
                    </div>
                )}

                {activeTab === 'Profile' && !loading && !error && userData && (
                    <div className="profile">
                        <h2>Your Profile</h2>
                        <p><strong>Name:</strong> {userData.firstName} {userData.lastName}</p>
                        <p><strong>Email:</strong> {userData.email}</p>
                        <p><strong>Phone Number:</strong> {userData.phoneNumber}</p>
                        <p><strong>Date of Birth:</strong> {userData.dateOfBirth}</p>
                        <p><strong>Address:</strong> {userData.address}, {userData.city}, {userData.county}</p>
                        <p><strong>CNP:</strong> {userData.cnp}</p>
                        <p><strong>User Role:</strong> {userData.userRole}</p>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Dashboard;
