import { NavLink } from "react-router-dom";
import Navbar from "../../components/Navbar/Navbar";
import './AllSalons.scss'
import user_placeholder from '../../assets/icons/user_placeholder.jpeg'

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

function AllSalons(){

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
                    <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                  
                        <NavLink to="/" className="rowSalon">
                            <div className="salonPPContainer">
                                <img src={user_placeholder} alt="pp_salon" className='salonPP' />
                            </div>
                            <div className="salonNameContainer">
                                <span className="labelSalonName">
                                    nom-salon
                                </span>
                            </div>
                        </NavLink>
                    </div>

                    <div className="contenuSalon"></div>
                        
                    
                </div>
            </div>

        </div>
    )
}

export default AllSalons ;
