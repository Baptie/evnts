import './Navbar.scss';
import { Link } from 'react-router-dom'
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
                    <Link to="/" className='navbarLinkLabel'>Accueil</Link>
                </div>
                <div className="navbarLink">
                    <Link to="/salons" className='navbarLinkLabel'>Salons</Link>
                </div>
                <div className="navbarLink">
                    <Link to="/messagerie" className='navbarLinkLabel'>Messagerie</Link>
                </div>
                

            </div>

            <div className="deconnexion blocNavbar">

                <div className="navbarLink">
                    <Link to="/connexion"className='navbarLinkLabel'>Déconnexion</Link>
                </div>

            </div>
            
        </div>
    );
}

export default Navbar;