import Navbar from "../../components/Navbar/Navbar";


const HomePage = ({accueil}) => {


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
export default HomePage;