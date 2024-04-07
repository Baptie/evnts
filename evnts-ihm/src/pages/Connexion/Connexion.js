import './Connexion.scss';

import { Link } from 'react-router-dom'
import logo from '../../assets/logo/logo-black-nocurve.png';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';




const Connexion = ({handleChange}) => {

    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");


    const [repLogin, setRep] = useState(false)
    const [contenuRep, setRepContenu] = useState("")

    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        await axios.post("http://localhost:8080/auth/login", {
            email: email,
            password: password,
        }).then(response => {
            navigate('/accueil?token=' + response.data);
            console.log(response)
            localStorage.setItem(etatConnexion,true)
        }).catch(error => {
            setRep(true)
            setRepContenu("Erreur, veuillez réessayer.")
            console.log(error);
        });
        console.log(email,password);



    }

    const handleClick = () => {
        handleChange()
    }

    return (
        <div className='pageConnexion'>

            <div className="logoContainer">
                    <img src={logo} alt="logo" className='logo' />
            </div>
            
            <div className="formContainer">

            <form className="form-container" onSubmit={handleSubmit}>
                 <div className="img-form-connexion">
     
                 </div>
     
                 <div className="form">
                     <h3 className="title-form">Connexion</h3>
     
                     <div className="field">
                         <label htmlFor="login">Login</label>
                         <input type="text" name="login" id="login" required value={email} onChange={(event) => setEmail(event.target.value)}/>
                     </div>
                     
                     <div className="field">
                         <label htmlFor="pwd">Mot de Passe</label>
                         <input type="password" name="pwd" id="pwd" required value={password} onChange={(event) => setPassword(event.target.value)}/>
                     </div>

                     {repLogin ? <p className='reponseRegister'>{contenuRep}</p> : null}

                     <div className="buttonContainer">
                         <button className="connexionButton" type="submit" onClick={handleSubmit}>Connexion</button>
                     </div>
                     
                     
                 </div>

            </form>

                <div className="inscription-link">
                    <Link to="" onClick={handleClick}>
                        Pas encore inscrit ? Inscrivez vous ici.
                    </Link>
                </div>
            </div>
        </div>
       
    );
}

export default Connexion;