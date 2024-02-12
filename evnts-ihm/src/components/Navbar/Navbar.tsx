import './Navbar.scss';
import { Link } from 'react-router-dom'
import home_icon from '../../assets/icons/home.png'
import mail_icon from '../../assets/icons/mail.webp'
import logout_icon from '../../assets/icons/logout.png'
import user_placeholder from '../../assets/icons/user_placeholder.jpeg'



function Navbar() {
    return (
        <div className="navbarContainer">
            <div className="profil">
                
                    <img src={user_placeholder} alt="user_profile_picture" className='img-profil-navbar' />
                    <Link to="/profile" className="pseudoLabelNavbar">Pseudo_Joueur</Link>
                
            </div>

            <div className="menu">
               <div className="navbarLink">
                    <img src={home_icon} alt="home_icon" className='navbarIcon' />
                    <Link to="/" className='navbarLinkLabel'>Accueil</Link>
                </div>
                <div className="navbarLink">
                    <img src={mail_icon} alt="home_icon" className='navbarIcon' />
                    <Link to="/messagerie" className='navbarLinkLabel'>Messagerie</Link>
                </div>

            </div>

            <div className="deconnexion">
                <div className="deconnexionink">
                    <img src={logout_icon} alt="home_icon" className='navbarIcon' />
                    <Link to="/connexion"className='navbarLinkLabel'>Déconnexion</Link>
                </div>
            </div>
            
        </div>
    );
}

export default Navbar;