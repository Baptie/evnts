import Navbar from "../../components/Navbar/Navbar";
import './AllSalons.scss'
import user_placeholder from '../../assets/icons/user_placeholder.jpeg'
import axios from "axios";
import { useState } from "react";



const API_URL="http://localhost:8080/auth/";
/* 
const salons = document.querySelectorAll('allRowsSalon rowSalon');

// Ajouter un écouteur d'événements à chaque élément de salon

TODO :
salons.forEach(salon => {
    salon.addEventListener('click', () => {
        // Récupérer l'identifiant du salon sélectionné
        const salonId = salon.dataset.salon;
        
        // Faire une requête AJAX pour récupérer les informations du salon
        fetch(`/api/salon/${salonId}`)
            .then(response => response.json())
            .then(data => {
                // Afficher les informations du salon dans la zone de contenu
                document.getElementById('contenuSalon').innerHTML = `
                    <h2>${data.nom}</h2>
                    <p>${data.description}</p>
                    <!-- Autres informations du salon -->
                `;
            })
            .catch(error => console.error('Erreur lors de la récupération des informations du salon :', error));
    });
});
*/

// TODO : Fetch la liste des Salons de l'utilisateur
const salons = [
    {
        nom:'Salon1',
        
    },
    {
        nom:'Salon2',
      
    },
    {
        nom:'Salon3',
       
    },
    {
        nom:'SaloN4',
        
    },
    
]

async function fetchData(idUser:string){

    // Récupération des salons du joueur
    await axios
    .get(API_URL+idUser+"/salons")
    .then( response => {
        salons.push(response.data)
    })
    .catch(error =>{
        console.log(error)
    });

}


function AllSalons(){
    
    const listSalonRender = salons.map(event => 
        <li className="rowSalon" key={event.nom}>
            <div className="salonNameContainer">
                <div className="labelSalonName" >
                    {event.nom}
                </div>
            </div>
         </li>
        )

    const [nomSalon,setNomSalon] = useState("");

    function nouveauSalon(){
        const user = localStorage.getItem("user")
        console.log("Nom salon :")
        console.log(nomSalon,user)
    }

    return(
        <div className="page">
            <div className='navbarContainer'>
                <Navbar/>
            </div>

            <div className="contentPage">
                <div className="sectionHeaderPage">
                    <span className="labelHeaderPage">Salons</span>
                </div>

                <div className="contentContainer">

                    <form onSubmit={nouveauSalon}>
                        <label>Nom</label>
                        <input type="text" required value={nomSalon} onChange={(event) => setNomSalon(event.target.value)}/>
                        <button id="newSalonButton" type="submit">Créer salon</button>
                    </form>
                    
                    <ul className="allRowsSalon">
                        {listSalonRender}
                    </ul>

                
                        
                    
                </div>
            </div>

            </div>
        
    )
}

export default AllSalons ;
