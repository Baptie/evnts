import './Inscription.scss';
import CardInscription from '../../components/Card-Inscription/CardInscription';
import { Link } from 'react-router-dom'
import logo from '../../assets/logo/logo-black-nocurve.png';

function Inscription() {
    return (
        <div className='pageInscription'>

            <div className="logoContainer">
                    <img src={logo} alt="logo" className='logo' />
            </div>
            
            <div className="formContainer">

                <CardInscription/>

                <div className="connexion-link">
                    <Link to="/Connexion">
                        Déjà inscrit ? Connectez vous ici.
                    </Link>
                </div>
            </div>
        </div>
       
    );
}

export default Inscription;