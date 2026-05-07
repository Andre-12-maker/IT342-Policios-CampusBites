import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { StoreContext } from './context/StoreContext';

const Login = () => {
    const [credentials, setCredentials] = useState({ email: '', password: '' });
    const { setUser, API_BASE } = useContext(StoreContext);
    const navigate = useNavigate();

    const handleChange = (e) => {
        setCredentials({ ...credentials, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`${API_BASE}/auth/login`, {   // ← fixed URL
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials),
            });
            if (!response.ok) throw new Error('Invalid credentials');
            const userData = await response.json();
            setUser(userData);          // ← save user in context
            localStorage.setItem('user', JSON.stringify(userData));
            navigate('/');
        } catch (error) {
            alert("Invalid email or password");
        }
    };

    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <input name="email" type="email" placeholder="Email" onChange={handleChange} required />
                <input name="password" type="password" placeholder="Password" onChange={handleChange} required />
                <button type="submit">Login</button>
            </form>
        </div>
    );
};

export default Login;