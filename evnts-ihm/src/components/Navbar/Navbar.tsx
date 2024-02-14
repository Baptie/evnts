import './Navbar.scss';
import { NavLink } from 'react-router-dom'
import user_placeholder from '../../assets/icons/user_placeholder.jpeg'

function goToProfile() {
    window.location.href = '/profile';
  }

function Navbar() {
    return (
        <div className="navbarContainer">

            <div className="profil blocNavbar" >
                
                <img src={user_placeholder} alt="user_profile_picture" className='img-profil-navbar' onClick={goToProfile} />
                <span className="pseudoLabelNavbar" onClick={goToProfile}>Pseudo_Joueur12345678956785467456701234567890</span>
                
            </div>

            <div className="menu blocNavbar ">

               <div className="navbarLink">
                    <NavLink to="/" 
                    className={({ isActive }) => (isActive ? 'active navbarLinkLabel ' : 'navbarLinkLabel')}
                    >Accueil</NavLink>
                </div>
                <div className="navbarLink">
                    <NavLink to="/salons" className='navbarLinkLabel'>Salons</NavLink>
                </div>
                <div className="navbarLink">
                    <NavLink to="/messagerie" className='navbarLinkLabel'>Messagerie</NavLink>
                </div>
                

            </div>

            <div className="deconnexion blocNavbar">

                <div className="navbarLink">
                    <NavLink to="/connexion"className='navbarLinkLabel'>Déconnexion</NavLink>
                </div>

            </div>
            
        </div>
    );
}

export default Navbar;