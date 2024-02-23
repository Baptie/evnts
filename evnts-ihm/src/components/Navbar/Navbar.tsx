import './Navbar.scss';
import { NavLink } from 'react-router-dom'
import user_placeholder from '../../assets/icons/user_placeholder.jpeg'
import logo from '../../assets/logo/logo-black.png'

function goToProfile() {
    window.location.href = '/profile';
  }

function disconnect(){
    window.location.href = '/connexion';

}

function Navbar() {
    return (
        <div className="navbarContainer">
            <div className="logoNavbarContainer">
                <img src={logo} alt="" className="logoNavbar" />
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

            <div className="profil" >
                <div className="profilePictureContainer">
                    <img src={user_placeholder} alt="user_profile_picture" className='img-profil-navbar' onClick={goToProfile} />

                </div>
                <div className="navbarProfileLabels">
                    <span className="pseudoLabelNavbar" onClick={goToProfile}>Pseudo_Joueur12345678956785467456701234567890</span>
                    <span className="logoutLabel" onClick={disconnect}>Se déconnecter</span>
                
                </div>
               
            </div>
            
        </div>
    );
}

export default Navbar;