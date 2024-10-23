import React, { useState } from 'react';
import logo from '../images/logo.png';
import {Link, useNavigate} from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import '../CSS/Header.css'
import i18n from "i18next";
import "../CSS/BoutonLangue.css";

function EmployeurHeader({ userData }) {
    const { t } = useTranslation();
    const [profileMenuOpen, setProfileMenuOpen] = useState(false);
    const navigate = useNavigate();



    const handleClickLogo = () => {
        if (userData) {
            console.log("tst")
            navigate("/accueilEmployeur", { state: { userData: userData } });
        }
    };

    const toggleProfileMenu = () => {
        setProfileMenuOpen(!profileMenuOpen);
    }

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };

    return (
        <header className="gestionnaire-header">
            <nav className="navbar">
                <div className="logo" onClick={handleClickLogo} style={{cursor: 'pointer'}}>
                    <img src={logo} alt="Logo" className="header-logo"/>
                    <div className="logo-text">Qui-Ose</div>
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
                            <Link onClick={() => changeLanguage('en')} className="language-button dropdown-link">{t('Anglais')}</Link>
                            <Link onClick={() => changeLanguage('fr')} className="language-button dropdown-link">{t('Francais')}</Link>
                        </div>
                    )}
                </div>
            </nav>
        </header>
    );
}

export default EmployeurHeader;