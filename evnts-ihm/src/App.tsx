import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Connexion from './pages/Connexion/Connexion';
import Accueil from './pages/Accueil/Accueil';
import Inscription from './pages/Inscription/Inscription';
import AllSalons from './pages/AllSalons/AllSalons';


function App() {

    return (
      <BrowserRouter>
  
        <Routes>
        
            <Route path="/" element={<Accueil/>}/>
            <Route path="/salons" element={<AllSalons/>}/>
            <Route path="/connexion" element={<Connexion/>}/>
            <Route path="/inscription" element={<Inscription/>}/>

        </Routes>
      </BrowserRouter>
  
    );
  }
  
  export default App;