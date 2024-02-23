import Navbar from '../../components/Navbar/Navbar';
import './Accueil.scss';
import CardEvent from '../../components/Card-Event/CardEvent';
function Accueil() {
    return (
        <div className="page">
            
                <Navbar/>
            
        
            <div className="contentPage">
                <div className="sectionHeaderPage">
                    <span className="labelHeaderPage">Mes événements</span>
                </div>
                <div className="contentContainer allEventsContainer">
                
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