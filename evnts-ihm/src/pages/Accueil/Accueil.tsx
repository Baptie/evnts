import Navbar from '../../components/Navbar/Navbar';
import './Accueil.scss';
import CardEvent from '../../components/Card-Event/CardEvent';
function Accueil() {
    return (
        <div className="pageAccueil">
            <div className='asideNavbar'>
                <Navbar/>
            </div>
        
            
            <div className="allEventsContainer">
               
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
    );
}

export default Accueil;