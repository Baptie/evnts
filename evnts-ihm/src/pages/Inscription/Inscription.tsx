import './Inscription.scss';
import logo from '../../assets/logo/logo-black-nocurve.png';
import { useState } from 'react';
import axios from 'axios';


import { Link } from 'react-router-dom'

const API_URL = "http://localhost:8080/auth/";

const Inscription = () => {
    const [nom, setNom] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    async function connexion(login:string,pwd:string){
        await axios
                .post(API_URL + "connexion/pseudo="+login+"&mdp="+pwd)
                .then(response =>{
                    localStorage.setItem("user", JSON.stringify(response.data));
                    localStorage.setItem("authenticated", "yes");
                    window.location.reload();
                })
                .catch(error =>{
                    console.log("Erreur lors de l'authentification : ", error);           
                    alert("Erreur lors de l'authentification")
                });
    }

    const handleSubmit = async (event: { preventDefault: () => void; }) => {
        event.preventDefault();
        await axios
            .post(API_URL + "inscription?pseudo="+nom+"&mdp="+password+"&eMail="+email)
            .then(response =>{
                connexion(nom,password);
                
            })
            .catch(error =>{
                console.log("Erreur lors de l'authentification : ", error);           
                alert("Erreur lors de l'authentification")
            });
    }

    const handleClick = () => {
       if(localStorage.getItem("register")==="yes"){
            localStorage.setItem("register","no")
       }
        window.location.reload()

    }
    return (
        <div className='pageInscription'>

            <div className="logoContainer">
                    <img src={logo} alt="logo" className='logo' />
            </div>
            
            <div className="formContainer">

            <form className="form-container" onSubmit={handleSubmit}>
                <div className="img-form-inscription">

                </div>

                <div className="form" >
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
                    <Link to="/" onClick={handleClick}>
                        Déjà inscrit ? Connectez vous ici.
                    </Link>
                </div>

            </div>
        </div>
       
    );
}

export default Inscription;