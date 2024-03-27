import Navbar from '../../components/Navbar/Navbar';
import React, { useState } from 'react';
import './Accueil.scss';
import CardEvent from '../../components/Card-Event/CardEvent';
function Accueil() {
    const [isLoading, setIsLoading] = useState(false);

    const handleTestButtonClick = async () => {
        try {
        const resp = await fetch('http://localhost:8080/auth/inscription?pseudo=roo&mdp=test&eMail=tet@test.com', {
            method: 'POST'
        });
        if (resp.ok) {
            console.log("c'est ok !");
        } else {
            const errorMessage = await resp.text();
            console.error('Erreur lors de l\'envoi de la requête : ', errorMessage);
        }
    } catch (error) {
        console.error('Erreur lors de l\'envoi de la requête : ', error);
    }
    };
    return (
        <div className="page">
            
                <Navbar/>
            
        
            <div className="contentPage">
                <div className="sectionHeaderPage">
                    <span className="labelHeaderPage">Mes événements</span>
                </div>
                <div className="contentContainer allEventsContainer">
                <div className="testButtonContainer">
                    <button onClick={handleTestButtonClick} disabled={isLoading}>
                        {isLoading ? 'Chargement...' : 'Test'}
                </button>
                </div>
                <CardEvent/>
                <CardEvent/>
                <CardEvent/>
                <CardEvent/>
                <CardEvent/>
                <CardEvent/> 
                <CardEvent/> 
                <CardEvent/> 
                <CardEvent/> 
                <CardEvent/> 
                <CardEvent/> 
                <CardEvent/> 
                <CardEvent/> 
                <CardEvent/> 
                <CardEvent/> 
            
            
                 </div>
            </div>
    

        </div>
    );
}

export default Accueil;