import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/LoginPage.css';
import axios from "axios";

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);

    const navigate = useNavigate();

    const handleEmailChange = (e) => setEmail(e.target.value);
    const handlePasswordChange = (e) => setPassword(e.target.value);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!email || !password) {
            setError('Please enter both email and password');
            return;
        }

        setError(null);

        axios.post('http://localhost:8080/token', {email, password})
            .then(function (response) {
                console.log(response);
                const { userId, token, role, county } = response.data;

                localStorage.setItem('token', token);
                localStorage.setItem('userId', userId);
                localStorage.setItem('role', role);
                localStorage.setItem('county', county);

                console.log(localStorage.getItem('token'));
                console.log(localStorage.getItem('userId'));
                console.log(localStorage.getItem('role'));
                console.log(localStorage.getItem('county'));

                navigate('/dashboard');
            })
            .catch(function (error) {
                console.log(error);
                setError('Login failed. Please try again.');
            });
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
                    {/*<Link to="/forgot-password" className="link">Forgot Password?</Link>*/}
                </p>
            </div>
        </div>
    );
}

export default LoginPage;
