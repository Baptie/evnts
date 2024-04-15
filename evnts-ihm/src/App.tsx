import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Accueil from './pages/Accueil/Accueil';

import AllSalons from './pages/AllSalons/AllSalons';



function App() {


    return (
      <BrowserRouter>
  
        <Routes>
            
            <Route path="/" element={<Accueil/>}/>
            <Route path="/salons" element={<AllSalons/>}/>



        </Routes>
      </BrowserRouter>
  
    );
  }
  
  export default App;