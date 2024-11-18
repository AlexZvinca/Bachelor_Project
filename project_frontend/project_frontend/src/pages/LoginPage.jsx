import React, { useState } from 'react';
import { Link } from 'react-router-dom';  // Import Link for navigation
import '../styles/LoginPage.css';  // Import the CSS for LoginPage

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);

    const handleEmailChange = (e) => setEmail(e.target.value);
    const handlePasswordChange = (e) => setPassword(e.target.value);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!email || !password) {
            setError('Please enter both email and password');
            return;
        }

        setError(null);

        // Dummy authentication request - replace with actual API request
        /*try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                // Assuming the backend redirects or provides a token
                console.log('Login successful');
            } else {
                setError('Invalid email or password');
            }
        } catch (error) {
            setError('An error occurred. Please try again.');
        }*/
    };

    return (
        <div className="container">
            <h2>Login</h2>
            <form onSubmit={handleSubmit} className="form">
                <div className="inputGroup">
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={handleEmailChange}
                        required
                        className="input"
                    />
                </div>
                <div className="inputGroup">
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={handlePasswordChange}
                        required
                        className="input"
                    />
                </div>
                {error && <p className="error">{error}</p>}
                <button type="submit">Login</button>
            </form>

            <div className="links">
                <p>
                    Don't have an account?{' '}
                    <Link to="/register" className="link">Register</Link>
                </p>
                <p>
                    <Link to="/forgot-password" className="link">Forgot Password?</Link>
                </p>
            </div>
        </div>
    );
}

export default LoginPage;
