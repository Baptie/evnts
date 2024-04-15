import Navbar from '../../components/Navbar/Navbar';
import Connexion from '../Connexion/Connexion';
import Inscription from '../Inscription/Inscription';
import './Accueil.scss';


function test(){
    console.log(localStorage.getItem("user"))
}

const Accueil =()=> {
    if (localStorage.getItem("authenticated")==="no"){
        if (localStorage.getItem("register")===("no")){
            return(<Connexion/>)
        }else{

            return(<Inscription/>)
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
                            <button onClick={test}>TEST</button>
                        </div>

                    </div>
                </div>
            )
    }


}

export default Accueil;