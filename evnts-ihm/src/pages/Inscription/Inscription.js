import './Inscription.scss';
import logo from '../../assets/logo/logo-black-nocurve.png';
import { useState } from 'react';

import { Link } from 'react-router-dom'
import { register } from '../../services/AuthService';

import { useNavigate } from 'react-router-dom';

const Inscription = () => {
    const [nom, setNom] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");

    const navigate = useNavigate();

    const handleSubmit = () => {
        try{
            console.log(email,password)
            register(nom,email,password);
            localStorage.setItem("authenticated",true)
            navigate("/")
        }catch (error){
            console.log("Erreur lors de l'authentification : ",error)
        }
    
    }

    const handleClick = () => {
       if(localStorage.getItem("register")){
            localStorage.setItem("register",false)
       }

       navigate("/");
    }
    return (
        <div className='pageInscription'>

            <div className="logoContainer">
                    <img src={logo} alt="logo" className='logo' />
            </div>
            
            <div className="formContainer">

            <form className="form-container">
                <div className="img-form-inscription">

                </div>

                <div className="form" onSubmit={handleSubmit}>
                    <h3 className="title-form-inscription">Inscription</h3>

                    <div className="field">
                        <label htmlFor="email">Email</label>
                        <input type="text" name="email" id="email" required value={email} onChange={(event) => setEmail(event.target.value)}/>
                    </div>

                    <div className="field">
                        <label htmlFor="login">Login</label>
                        <input type="text" name="login" id="login" required value={nom} onChange={(event) => setNom(event.target.value)} />
                    </div>
                    
                    <div className="field">
                        <label htmlFor="pwd">Mot de Passe</label>
                        <input type="password" name="pwd" id="pwd" required value={password} onChange={(event) => setPassword(event.target.value)}/>
                    </div>

                    <div className="buttonContainer">
                        <button className="inscriptionButton" type="submit">Inscription</button>
                    </div>
                
                
                </div>
    
            </form>

                <div className="connexion-link">
                    <Link to="/connexion" onClick={handleClick}>
                        Déjà inscrit ? Connectez vous ici.
                    </Link>
                </div>

            </div>
        </div>
       
    );
}

export default Inscription;