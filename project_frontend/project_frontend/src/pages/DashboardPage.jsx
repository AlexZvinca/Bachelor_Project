import React, { useEffect, useState } from 'react';
import '../styles/Dashboard.css';

function Dashboard() {
    const [activeTab, setActiveTab] = useState('Authorizations');

    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };
    
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
                {activeTab === 'Authorizations' && (
                    <div className="authorizations">
                        <h2>Your Authorizations</h2>
                        <ul className="authorization-list">
                            <li className="authorization-item">
                                <p><strong>Vehicle:</strong> ABC123</p>
                                <p><strong>Authorization ID:</strong> 456789</p>
                                <p><strong>Status:</strong> Active</p>
                            </li>
                            <li className="authorization-item">
                                <p><strong>Vehicle:</strong> XYZ456</p>
                                <p><strong>Authorization ID:</strong> 123456</p>
                                <p><strong>Status:</strong> Expired</p>
                            </li>
                        </ul>
                        <button className="new-authorization-btn">Request a New Authorization</button>
                    </div>
                )}

                {activeTab === 'Profile' && (
                    <div className="profile">
                        <h2>Your Profile</h2>
                        <p><strong>Name:</strong> John Doe</p>
                        <p><strong>Email:</strong> john.doe@example.com</p>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Dashboard;
