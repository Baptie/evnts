import './Connexion.scss';
import CardConnexion from '../../components/Card-Connexion/CardConnexion';
import { Link } from 'react-router-dom'
import logo from '../../assets/logo/logo-black-nocurve.png';


function Connexion() {
    return (
        <div className='pageConnexion'>

            <div className="logoContainer">
                    <img src={logo} alt="logo" className='logo' />
            </div>
            
            <div className="formContainer">

                <CardConnexion/>

                <div className="inscription-link">
                    <Link to="/Inscription">
                        Pas encore inscrit ? Inscrivez vous ici.
                    </Link>
                </div>
            </div>
        </div>
       
    );
}

export default Connexion;