import { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/Dashboard.css';

function Dashboard() {
    const [authorizations, setAuthorizations] = useState([]);
    const [userData, setUserData] = useState(null);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const [statusUpdates, setStatusUpdates] = useState({});

    const userId = localStorage.getItem('userId');
    const userRole = localStorage.getItem('role');
    const userCounty = localStorage.getItem('county');

    const [activeTab, setActiveTab] = useState(userRole === "ADMIN" ? 'Users' : 'Authorizations');

    useEffect(() => {
        if (userRole === 'ADMIN' && activeTab === 'Users') {
            fetchAllUsers();
        } else if (userRole !== 'ADMIN' && activeTab === 'Authorizations') {
            fetchAuthorizations();
        } else if (activeTab === 'Profile') {
            fetchUserProfile();
        }
    }, [activeTab]);

    const fetchAuthorizations = async () => {
        setLoading(true);
        setError(null);
        try {
            const url =
                userRole === 'AUTHORITY'
                    ? `${import.meta.env.VITE_BACKEND_URL}/authorizationRequest/county/${userCounty}`
                    : `${import.meta.env.VITE_BACKEND_URL}/authorizationRequest/user/${userId}`;

            const response = await axios.get(url, {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
            });
            console.log('Fetched Authorizations:', response.data);
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
            const response = await axios.get(`${import.meta.env.VITE_BACKEND_URL}/users/${userId}`, {
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

    const fetchAllUsers = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get(`${import.meta.env.VITE_BACKEND_URL}/users`, {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
            });
            setUsers(response.data.filter(user => user.id !== userId) || []);
        } catch (err) {
            setError('Failed to fetch users. Please try again later.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleRoleChange = async (id, role) => {

        axios({
            url: `${import.meta.env.VITE_BACKEND_URL}/users/${id}/role`,
            method: "PUT",
            data: JSON.stringify(role),
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json',
            },
        })
            .catch((err) => {
                alert('Failed to update user role.');
                console.error(err);
            });

        alert('Role changed successfully!');
        fetchAllUsers();
    };

    const handleTabClick = (tab) => setActiveTab(tab);

    const handleLogOut = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("role");
        localStorage.removeItem("county");

       navigate('/login');
    };

    const handleStatusChange = (id, status) => {
        setStatusUpdates((prev) => ({
            ...prev,
            [id]: { ...prev[id], status },
        }));
    };

    const handleCommentsChange = (id, comments) => {
        setStatusUpdates((prev) => ({
            ...prev,
            [id]: { ...prev[id], comments },
        }));
    };

    const handleSubmit = async (id) => {
        const { status, comments } = statusUpdates[id] || {};
        if (!status) {
            alert('Please select a status.');
            return;
        }

        try {
            await axios.put(
                `${import.meta.env.VITE_BACKEND_URL}/authorizationRequest/status-motivation/${id}`,
                { status, comments },
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'application/json',
                    },
                }
            );

            alert('Status updated successfully!');

            setStatusUpdates(prev => ({
                ...prev,
                [id]: {
                    [id]: {},
                }
            }));

            fetchAuthorizations();
        } catch (err) {
            console.error('Error updating status:', err);
            alert('Failed to update status. Please try again later.');
        }
    };

    const handleDeleteUser = async (id) => {
        if (window.confirm("Are you sure you want to delete this user?")) {
            try {
                await axios.delete(`${import.meta.env.VITE_BACKEND_URL}/users/${id}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });
                alert("User deleted successfully!");
                fetchAllUsers();
            } catch (err) {
                console.error('Error deleting user:', err);
                alert('Failed to delete user. Please try again later.');
            }
        }
    };

    return (
        <div className="dashboard-container">
            <nav className="navbar">
                {userRole === 'ADMIN' ? (
                    <button
                        className={`nav-item ${activeTab === 'Users' ? 'active' : ''}`}
                        onClick={() => handleTabClick('Users')}
                    >
                        Users
                    </button>
                ) : (
                    <button
                        className={`nav-item ${activeTab === 'Authorizations' ? 'active' : ''}`}
                        onClick={() => handleTabClick('Authorizations')}
                    >
                        Authorizations
                    </button>
                )}
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

                {activeTab === 'Authorizations' && userRole !== 'ADMIN' && !loading && !error && (
                    <div className="authorizations">
                        <h2>
                            {userRole === 'AUTHORITY'
                                ? `Requests from ${userCounty} County`
                                : 'Your Authorizations'}
                        </h2>
                        {authorizations.length > 0 ? (
                            <ul className="dashboard-list">
                                {authorizations.map((auth) => (
                                    <li key={auth.id} className="dashboard-item">
                                        <p><strong>Vehicle:</strong> {auth.licensePlateNumber}</p>
                                        <p><strong>Authorization ID:</strong> {auth.id}</p>
                                        <p><strong>County:</strong> {auth.county}</p>
                                        <p><strong>Volume:</strong> {auth.volume} m<sup>3</sup></p>
                                        <p><strong>Description:</strong> {auth.description}</p>
                                        <p><strong>Created At:</strong> {new Date(auth.createdAt).toLocaleString()}</p>
                                        <p><strong>From:</strong> {new Date(auth.fromDate).toLocaleDateString()}</p>
                                        <p><strong>Until:</strong> {new Date(auth.untilDate).toLocaleDateString()}</p>

                                        {userRole === 'AUTHORITY' &&
                                            <p><strong>Requested By:</strong> {auth.userId}</p>}
                                        <p><strong>Status:</strong> {auth.status}</p>
                                        {auth.status.toString() !== 'PENDING' &&
                                            <p><strong>Comments:</strong> {auth.statusComments}</p>}
                                        {userRole === 'AUTHORITY' && (
                                            <div className="authority-controls">
                                                <div style={{display: 'flex', gap: '10px'}}>
                                                    <button
                                                        className={`status-btn approve ${statusUpdates[auth.id]?.status === 'GRANTED' ? 'selected' : ''} ${
                                                            statusUpdates[auth.id]?.status === 'NOT_GRANTED' ? 'dimmed' : ''
                                                        }`}
                                                        onClick={() => handleStatusChange(auth.id, 'GRANTED')}
                                                    >
                                                        Approve
                                                    </button>
                                                    <button
                                                        className={`status-btn deny ${statusUpdates[auth.id]?.status === 'NOT_GRANTED' ? 'selected' : ''} ${
                                                            statusUpdates[auth.id]?.status === 'GRANTED' ? 'dimmed' : ''
                                                        }`}
                                                        onClick={() => handleStatusChange(auth.id, 'NOT_GRANTED')}
                                                    >
                                                        Deny
                                                    </button>
                                                    <button
                                                        className="doc-btn"
                                                        onClick={() => navigate(`/documents?userId=${auth.userId}&authId=${auth.id}`)}
                                                    >
                                                        View ID Document
                                                    </button>
                                                </div>
                                                <textarea
                                                    placeholder="Enter comments..."
                                                    value={statusUpdates[auth.id]?.comments || ''}
                                                    onChange={(e) => handleCommentsChange(auth.id, e.target.value)}
                                                ></textarea>
                                                <button
                                                    className="dashboard-btn"
                                                    onClick={() => handleSubmit(auth.id)}
                                                    style={{marginTop: '10px'}}
                                                >
                                                    Submit
                                                </button>
                                            </div>
                                        )}
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p>No authorizations found.</p>
                        )}
                        {userRole !== 'AUTHORITY' && (
                            <button
                                className="dashboard-btn"
                                onClick={() => window.location.href = '/new-authorization'}
                            >
                                Request a New Authorization
                            </button>
                        )}
                    </div>
                )}

                {activeTab === 'Users' && userRole === 'ADMIN' && !loading && !error && (
                    <div className="users">
                        <h2>All Users</h2>
                        <ul className="dashboard-list">
                            {users.map((user) => (
                                <li key={user.id} className="dashboard-item">
                                    <p><strong>ID:</strong> {user.id}</p>
                                    <p><strong>Name:</strong> {user.firstName} {user.lastName}</p>
                                    <p><strong>Email:</strong> {user.email}</p>
                                    <p><strong>Phone Number:</strong> {user.phoneNumber}</p>
                                    <p><strong>Date of Birth:</strong> {user.dateOfBirth}</p>
                                    <p><strong>Address:</strong> {user.address}, {user.city}, {user.county}</p>
                                    <p><strong>CNP:</strong> {user.cnp}</p>
                                    <p><strong>Role:</strong> {user.userRole}</p>
                                    <select
                                        className="role-select"
                                        value={users.find(u => u.id === user.id)?.userRole || ''}
                                        onChange={(e) => handleRoleChange(user.id, e.target.value)}
                                    >
                                        <option value="REQUESTOR">Requestor</option>
                                        <option value="AUTHORITY">Authority</option>
                                        <option value="ADMIN">Admin</option>
                                    </select>
                                    <button
                                        className="delete-btn"
                                        onClick={() => handleDeleteUser(user.id)}
                                    >
                                        Delete User
                                    </button>
                                    <button
                                        className="doc-btn"
                                        onClick={() => navigate(`/documents?userId=${user.id}`)}
                                    >
                                        View User ID Document
                                    </button>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                {activeTab === 'Profile' && !loading && !error && userData && (
                    <div className="profile">
                        <h2>Your Profile</h2>
                        <p><strong>Name:</strong> {userData.firstName} {userData.lastName}</p>
                        <p><strong>Email:</strong> {userData.email}</p>
                        <p><strong>Phone Number:</strong> {userData.phoneNumber}</p>
                        <p><strong>Date of Birth:</strong> {new Date(userData.dateOfBirth).toLocaleDateString()}</p>
                        <p><strong>Address:</strong> {userData.address}, {userData.city}, {userData.county}</p>
                        <p><strong>CNP:</strong> {userData.cnp}</p>
                        <p><strong>User Role:</strong> {userData.userRole}</p>

                        <button className="dashboard-btn" onClick={handleLogOut}>
                            Log Out
                        </button>
                    </div>
                    )}
            </div>
        </div>
    );
}

export default Dashboard;
