<<<<<<< HEAD
import React from 'react' 
import Navbar from './components/Navbar/Navbar'
import { Routes, Route } from 'react-router-dom'
import Home from './pages/Home/Home'
import Cart from './pages/Cart/Cart'
import PlaceOrder from './pages/PlaceOrder/PlaceOrder'
=======
import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './shared/components/Navbar';
import LoginModal from './features/auth/components/LoginModal';
import RegisterModal from './features/auth/components/RegisterModal';
import HomePage from './features/home/pages/HomePage';
import CartPage from './features/cart/pages/CartPage';
import PlaceOrderPage from './features/placeorder/pages/PlaceOrderPage';
import OrdersPage from './features/orders/pages/OrdersPage';
import AddItemPage from './features/admin/pages/AddItemPage';
import ListItemsPage from './features/admin/pages/ListItemsPage';
import AdminOrdersPage from './features/admin/pages/AdminOrdersPage';
import { CartProvider } from './shared/context/CartContext';
import { AuthProvider } from './shared/context/AuthContext';
import './shared/styles/globals.css';

function ComingSoon({ name }) {
  return (
    <div style={{
      display:'flex', flexDirection:'column', alignItems:'center',
      justifyContent:'center', minHeight:'60vh', gap:'12px',
      color:'#888', fontFamily:'Outfit, sans-serif'
    }}>
      <p style={{ fontSize:'18px', fontWeight:600, color:'#1a1a1a' }}>{name}</p>
      <p style={{ fontSize:'14px' }}>Coming soon.</p>
    </div>
  );
}

function AppContent() {
  const [modal, setModal] = useState(null);
>>>>>>> main

const App = () => {
  return (
<<<<<<< HEAD
    <div className='app'>
      <Navbar/>
      <Routes>
        <Route path='/' element={<Home/>} />
        <Route path='/cart' element={<Cart/>} />
        <Route path='/order' element={<PlaceOrder/>} />
      </Routes>
    </div>
  )
=======
    <>
      <Navbar onLoginClick={() => setModal('login')} />
      <Routes>
        {/* Customer */}
        <Route path="/"            element={<HomePage />} />
        <Route path="/menu"        element={<HomePage />} />
        <Route path="/cart"        element={<CartPage />} />
        <Route path="/place-order" element={<PlaceOrderPage />} />
        <Route path="/orders"      element={<OrdersPage />} />
        <Route path="/mobile-app"  element={<ComingSoon name="Mobile App" />} />
        <Route path="/contact"     element={<ComingSoon name="Contact Us" />} />

        {/* Admin */}
        <Route path="/admin"        element={<Navigate to="/admin/add" replace />} />
        <Route path="/admin/add"    element={<AddItemPage />} />
        <Route path="/admin/list"   element={<ListItemsPage />} />
        <Route path="/admin/orders" element={<AdminOrdersPage />} />

        <Route path="*" element={<ComingSoon name="Page Not Found" />} />
      </Routes>

      <LoginModal
        isOpen={modal === 'login'}
        onClose={() => setModal(null)}
        onSwitchToRegister={() => setModal('register')}
      />
      <RegisterModal
        isOpen={modal === 'register'}
        onClose={() => setModal(null)}
        onSwitchToLogin={() => setModal('login')}
      />
    </>
  );
>>>>>>> main
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <CartProvider>
          <AppContent />
        </CartProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}