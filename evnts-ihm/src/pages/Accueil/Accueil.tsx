import { useState } from 'react';
import Navbar from '../../components/Navbar/Navbar';
import Connexion from '../Connexion/Connexion';
import Inscription from '../Inscription/Inscription';
import './Accueil.scss';

const Accueil =()=> {

    // TODO : Fetch la liste des evenements de l'utilisateur
    const events = [
    {
        nom:'Evenement1',
        lieu:'Lieu1',
        horaire:'horaire'
    },
    {
        nom:'Evenement2',
        lieu:'Lieu2',
        horaire:'horaire'
    },
    {
        nom:'Evenement3',
        lieu:'Lieu3',
        horaire:'horaire'
    },
    {
        nom:'Evenement4',
        lieu:'Lieu4',
        horaire:'horaire'
    },
    
]

    const listEvenementsRender = events.map(event => 
        <li className="rowSalon" key={event.nom}>
            <div className="salonNameContainer">
                <div className="labelSalonName" >
                    {event.nom},{event.horaire},{event.lieu}
                </div>
            </div>
         </li>
    )

    const [nomEvent,setNomEvent] = useState("");
    const [dateEvent,setDateEvent] = useState("");
    const [lieuEvent,setLieuEvent] = useState("");


    function nouvelEvenement(){
        const user = localStorage.getItem("user")
        console.log("Nom événement :")
        console.log(nomEvent,user)
    }

    if (localStorage.getItem("authenticated")==="no"){
        if (localStorage.getItem("register")===("no")){
            return(<Connexion/>)
        }else{

            return(<Inscription/>)
        }
    }else{

        return (
                <div className="page">
                    <div className='navbarContainer'>
                <Navbar/>
            </div>

            <div className="contentPage">
                <div className="sectionHeaderPage">
                    <span className="labelHeaderPage">Mes événements</span>
                </div>

                <div className="contentContainer">

                    <form onSubmit={nouvelEvenement}>
                        <label>Nom</label>
                        <input type="text" required value={nomEvent} onChange={(event) => setNomEvent(event.target.value)}/>
                        <label>Date</label>
                        <input type="date" required value={dateEvent} onChange={(event) => setDateEvent(event.target.value)}/>
                        <label>Lieu</label>
                        <input type="text" required value={lieuEvent} onChange={(event) => setLieuEvent(event.target.value)}/>

                        <button id="newSalonButton" type="submit">Créer salon</button>
                    </form>
                    
                    <ul className="allRowsSalon">
                        {listEvenementsRender}
                    </ul>

                
                        
                    
                </div>
            </div>
                </div>
            )
    }


}

export default Accueil;