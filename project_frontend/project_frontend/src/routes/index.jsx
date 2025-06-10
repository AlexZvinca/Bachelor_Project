import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import DashboardPage from "../pages/DashboardPage.jsx";
import LandingPage from "../pages/LandingPage.jsx";
import NewAuthorizationForm from '../pages/NewAuthorizationForm';
import DocumentPage from "../pages/DocumentPage.jsx";

function RoutesComponent() {
    return (
        <Router>
            <Routes>
                <Route path="/home" element={<LandingPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/dashboard" element={<DashboardPage />} />
                <Route path="/new-authorization" element={<NewAuthorizationForm />} />
                <Route path="/documents" element={<DocumentPage />} />
            </Routes>
        </Router>
    );
}
export default RoutesComponent;
