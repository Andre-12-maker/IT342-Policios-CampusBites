import { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext(null);
const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

export function AuthProvider({ children }) {
  const [user, setUser]       = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError]     = useState(null);

  useEffect(() => {
    const saved = localStorage.getItem('cb_user');
    if (saved) { try { setUser(JSON.parse(saved)); } catch {} }
  }, []);

  const login = async (email, password) => {
    setLoading(true); setError(null);
    try {
      const res = await axios.post(`${BASE_URL}/auth/login`, { email, password });
      const { data } = res.data;
      localStorage.setItem('accessToken', data.accessToken);
      localStorage.setItem('cb_user', JSON.stringify(data.user));
      setUser(data.user);
      return { success: true };
    } catch (err) {
      const msg = err.response?.data?.error?.message || 'Login failed';
      setError(msg);
      return { success: false, message: msg };
    } finally { setLoading(false); }
  };

  const register = async (name, email, password) => {
    setLoading(true); setError(null);
    const parts = name.trim().split(' ');
    const firstname = parts[0] || name;
    const lastname  = parts.slice(1).join(' ') || '';
    try {
      const res = await axios.post(`${BASE_URL}/auth/register`, { email, password, firstname, lastname });
      const { data } = res.data;
      localStorage.setItem('accessToken', data.accessToken);
      localStorage.setItem('cb_user', JSON.stringify(data.user));
      setUser(data.user);
      return { success: true };
    } catch (err) {
      const msg = err.response?.data?.error?.message || 'Registration failed';
      setError(msg);
      return { success: false, message: msg };
    } finally { setLoading(false); }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('cb_user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, error, setError, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};