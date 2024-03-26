import Navbar from '../../components/Navbar/Navbar';
import React, { useState } from 'react';
import './Accueil.scss';
import CardEvent from '../../components/Card-Event/CardEvent';
function Accueil() {
    const [isLoading, setIsLoading] = useState(false);

    const handleTestButtonClick = () => {
        setIsLoading(true);

        fetch('http://localhost:8080/auth/inscription?pseudo=test&mdp=test&eMail=test')
            .then(response => {
                setIsLoading(false);
            })
            .catch(error => {
                setIsLoading(false);
            });
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