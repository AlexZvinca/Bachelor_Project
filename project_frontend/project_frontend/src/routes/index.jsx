import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import ForgotPasswordPage from '../pages/ForgotPasswordPage';
import DashboardPage from "../pages/DashboardPage.jsx";
import LandingPage from "../pages/LandingPage.jsx";
import NewAuthorizationForm from '../pages/NewAuthorizationForm';

function RoutesComponent() {
    return (
        <Router>
            <Routes>
                <Route path="/home" element={<LandingPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/forgot-password" element={<ForgotPasswordPage />} />
                <Route path="/dashboard" element={<DashboardPage />} />
                <Route path="/new-authorization" element={<NewAuthorizationForm />} />
            </Routes>
        </Router>
    );
}
export default RoutesComponent;
