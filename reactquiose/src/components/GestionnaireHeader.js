import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/GestionnaireHeader.css';
import logo from '../images/logo.png';

function GestionnaireHeader() {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const location = useLocation();
    const [activeLink, setActiveLink] = useState(location.pathname);

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    };

    const handleLinkClick = (path) => {
        setActiveLink(path);
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo">
                    <img src={logo} alt="Logo" className="header-logo" />
                    <Link to="/accueilGestionnaire" className="logo-link">
                        <div className="logo-text">Qui-Ose</div>
                    </Link>
                </div>
                <div className="nav-links">
                    <Link
                        className={`nav-link ${activeLink === '/listeEtudiants' ? 'active' : ''}`}
                        to="/listeEtudiants"
                        onClick={() => handleLinkClick('/listeEtudiants')}
                    >
                        {t('etudiant')}
                    </Link>
                    <Link
                        className={`nav-link ${activeLink === '/listeProfesseurs' ? 'active' : ''}`}
                        to="/listeProfesseurs"
                        onClick={() => handleLinkClick('/listeProfesseurs')}
                    >
                        {t('prof')}
                    </Link>
                    <Link
                        className={`nav-link ${activeLink === '/listeEmployeurs' ? 'active' : ''}`}
                        to="/listeEmployeurs"
                        onClick={() => handleLinkClick('/listeEmployeurs')}
                    >
                        {t('employeur')}
                    </Link>
                </div>
                <div className="profile-menu">
                    <div className="notification-icon">🕭</div>
                    <div
                        className="profile-button"
                        onClick={toggleProfileMenu}
                    >
                        {t('profile')} ▼
                    </div>
                    {profileMenuOpen && (
                        <div className="profile-dropdown">
                            <Link className="dropdown-link" to="/profile">{t('myProfile')}</Link>
                            <Link className="dropdown-link" to="/settings">{t('settings')}</Link>
                            <Link className="dropdown-link" to="/logout">{t('logout')}</Link>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default GestionnaireHeader;
