import { useState } from 'react';
import '../styles/ForgotPasswordPage.css';

function ForgotPasswordPage() {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!email) {
            setMessage('Please enter your email');
            return;
        }

        setMessage(`Password reset instructions have been sent to ${email}`);
    };

    return (
        <div className="container">
            <h2>Forgot Password</h2>
            <form onSubmit={handleSubmit} className="form">
                <div className="inputGroup">
                    <label>Email:</label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                {message && <p className="message">{message}</p>}
                <button type="submit">Submit</button>
            </form>
        </div>
    );
}

export default ForgotPasswordPage;
