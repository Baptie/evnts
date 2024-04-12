import './Connexion.scss';

import { Link} from 'react-router-dom'
import logo from '../../assets/logo/logo-black-nocurve.png';
import{ useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from "axios";

const API_URL = "http://localhost:8080/auth/";


const Connexion = () => {

    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");

    const navigate = useNavigate();


    async function login(email, password){
    
        try {
            const response = await axios
                .post(API_URL + "connexion", {
                    email,
                    password,
                });
            if (response.data.accessToken) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }

            localStorage.setItem("user", response.data);
            localStorage.setItem("authenticated", "yes");
            navigate("/");
        } catch (error) {
            console.log("Erreur lors de l'authentification : ", error);
            alert("Erreur lors de l'authentification : ", error.message);
        }
      };

    const handleSubmit = () =>{
       login(email,password)
    }
    const handleClick = () => {
        if(!localStorage.getItem("register")===("yes")){
            localStorage.setItem("register","yes")
       }
       navigate("/")
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

                     <div className="buttonContainer">
                         <button className="connexionButton" type="submit">Connexion</button>
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