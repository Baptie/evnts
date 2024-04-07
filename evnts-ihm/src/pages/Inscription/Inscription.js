import './Inscription.scss';
import logo from '../../assets/logo/logo-black-nocurve.png';
import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom'

const Inscription = ({ handleChange }) => {
    const [nom, setNom] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");

    const [repRegister, setRep] = useState(false)
    const [contenuRep, setRepContenu] = useState("")


    const handleSubmit = async (event) => {
        event.preventDefault();

        // Vérifie si la chaîne est nulle ou vide
        if (!email || email.length === 0 || email.trim().length === 0
            || !nom || nom.length === 0 || nom.trim().length === 0
            || !password || password.length === 0 || password.trim().length === 0
        ) {
            setRep(true)
            setRepContenu("Erreur veuillez réessayer.")
        } else {
            await axios.post("http://localhost:8080/auth/inscription", {
                email: email,
                password: password,
                pseudo: nom
            }).then(response => {
                console.log(response)
                setRepContenu("Succès!")
                setRep(true)
            }).catch(error => {
                console.log(error);
                setRep(true)
                if (error.response.data === "Cet email est déjà utilisé") {
                    setRepContenu("Email ou pseudo déjà utilisé.")
                } else {
                    setRepContenu("Erreur veuillez réessayer.")
                }

            });
        }

    }

    const handleClick = () => {
        handleChange()
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

                <div className="form">
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

                    {repRegister ? <p className='reponseRegister'>{contenuRep}</p> : null}

                    <div className="buttonContainer">
                        <button className="inscriptionButton" type="submit">Inscription</button>
                    </div>
                
                
                </div>
    
            </form>

                <div className="connexion-link">
                    <Link to="" onClick={handleClick}>
                        Déjà inscrit ? Connectez vous ici.
                    </Link>
                </div>

            </div>
        </div>
       
    );
}

export default Inscription;