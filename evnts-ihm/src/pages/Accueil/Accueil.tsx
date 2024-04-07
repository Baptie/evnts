import Navbar from '../../components/Navbar/Navbar';
import './Accueil.scss';
import { useState } from 'react';

import Connexion from '../Connexion/Connexion';
import Inscription from '../Inscription/Inscription';

const Accueil =()=> {
    const [isConnexion, setState] = useState(true)
    const [isLoggedIn, setLoggedIn] = useState(true)
    const handleChange = () => {
        setState(!isConnexion)
    }
    const handleLogin = () => {
        setLoggedIn(true)
    }

    if (isLoggedIn){
        return(
            <div>
                        <Navbar/>
                    <div className="contentPage">
                        <div className="sectionHeaderPage">
                            <span className="labelHeaderPage">Mes événements</span>
                        </div>
                        <div className="contentContainer allEventsContainer">
        
                        </div>
                    </div>
                </div>
        );
    }else {
        if (isConnexion){
            return(
                <div>
                    <Connexion handleChange={handleChange} />
                </div>
            )
        }else{
            return(
            <div>
                <Inscription handleChange={handleChange} />
            </div>
            )
        }
    }

}

export default Accueil;