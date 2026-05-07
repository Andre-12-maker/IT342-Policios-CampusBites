import React from 'react'
import Navbar from './shared/components/Navbar'
import { Routes, Route } from 'react-router-dom'
import Home from './features/home/Home'
import Cart from './features/order/Cart'
import PlaceOrder from './features/order/PlaceOrder'
import StoreContextProvider from './shared/context/StoreContext'

const App = () => {
  return (
    <StoreContextProvider>
      <div className='app'>
        <Navbar/>
        <Routes>
          <Route path='/' element={<Home/>} />
          <Route path='/cart' element={<Cart/>} />
          <Route path='/order' element={<PlaceOrder/>} />
        </Routes>
      </div>
    </StoreContextProvider>
  )
}

export default App