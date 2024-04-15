import Navbar from "../../components/Navbar/Navbar";
import './AllSalons.scss'
import user_placeholder from '../../assets/icons/user_placeholder.jpeg'
import axios from "axios";


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


const events = [
    'Evenement 1; Lieu 1; Horaire 1',
    'Evenement 2; Lieu 2; Horaire 2',
    'Evenement 3; Lieu 3; Horaire 3',
    'Evenement 4; Lieu 4; Horaire 4'
]

async function fetchData(idUser:string){

    // Récupération des salons du joueur
    await axios
    .get(API_URL+idUser+"/salons")
    .then( response => {
        events.push(response.data)
    })
    .catch(error =>{
        console.log(error)
    });

}
function AllSalons(){
    const listSalonRender = events.map(event => 
        <div className="rowSalon">
            <div className="salonPPContainer">
                 <img src={user_placeholder} alt="pp_salon" className='salonPP' />
            </div>

            <div className="salonNameContainer">
                <span className="labelSalonName">
                    {event}
                </span>
            </div>
         </div>
        )

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
                    <div className="allRowsSalon">
                        {listSalonRender}
                    </div>

                    <div className="contenuSalon">


                    </div>
                        
                    
                </div>
            </div>

            </div>
        
    )
}

export default AllSalons ;
