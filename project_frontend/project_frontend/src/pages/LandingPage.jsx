import "../styles/LandingPage.css";
import {useNavigate} from "react-router-dom";

function LandingPage() {

    const navigate = useNavigate();


    const handleRegisterClick = () => {
        navigate('/register');
    };

    const handleLoginClick = () => {
        navigate('/login');
    };

    return (
        <div className="landing-page">
            <div className="logo-container">
                <img
                    src="/TreeHelper.png"
                    alt="Logo"
                    className="logo"
                />
            </div>
            <h1 className="landing-title">Get your authorization for wood transport in a few easy steps!</h1>
            <div className="buttons-container">
                <button className="button" onClick={handleRegisterClick}> Register</button>
                <button className="button" onClick={handleLoginClick}>Log In</button>
            </div>
        </div>
    );
}

export default LandingPage;
