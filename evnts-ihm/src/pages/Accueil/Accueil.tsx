import Navbar from '../../components/Navbar/Navbar';
import './Accueil.scss';
import { useNavigate } from 'react-router-dom';


const Accueil =()=> {

    const navigate = useNavigate();

    if (!localStorage.getItem("authenticated")){
        if (!localStorage.getItem("register")){
            navigate("/connexion")
            return(<div></div>)
        }else{
            navigate("/inscription")
            return(<div></div>)
        }
    }else{
        return (
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
            )
    }


}

export default Accueil;