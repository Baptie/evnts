import Navbar from "../../components/Navbar/Navbar";
import './AllSalons.scss'

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
                    <div className="salonLink">

                    </div>
                </div>
            </div>

        </div>
    )
}

export default AllSalons ;
