import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Connexion from './pages/Connexion/Connexion';
import Accueil from './pages/Accueil/Accueil';
import Inscription from './pages/Inscription/Inscription';

function App() {


    return (
      <BrowserRouter>
  
        <Routes>
            <Route path="/connexion" element={<Connexion/>}/>
            <Route path="/inscription" element={<Inscription/>}/>
            <Route path="/" element={<Accueil/>}/>

        </Routes>
      </BrowserRouter>
  
    );
  }
  
  export default App;