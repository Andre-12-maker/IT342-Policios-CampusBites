import { useState, useEffect } from 'react';
import { useAuth } from '../../../shared/context/AuthContext';
import styles from './AuthModal.module.css';

export default function LoginModal({ isOpen, onClose, onSwitchToRegister }) {
  const { login, loading, error, setError } = useAuth();
  const [email, setEmail]       = useState('');
  const [password, setPassword] = useState('');
  const [agreed, setAgreed]     = useState(false);
  const [localErr, setLocalErr] = useState('');

  useEffect(() => {
    if (isOpen) { setEmail(''); setPassword(''); setLocalErr(''); setError?.(null); }
  }, [isOpen]);

  useEffect(() => {
    if (!isOpen) return;
    const handleKey = (e) => { if (e.key === 'Escape') onClose(); };
    document.addEventListener('keydown', handleKey);
    return () => document.removeEventListener('keydown', handleKey);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLocalErr('');
    if (!agreed) { setLocalErr('Please agree to the terms.'); return; }
    const result = await login(email, password);
    if (result.success) onClose();
  };

  return (
    <div className={styles.overlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
      <div className={styles.modal}>
        <button className={styles.closeBtn} onClick={onClose}>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
            <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
          </svg>
        </button>
        <h2 className={styles.title}>Login</h2>
        <form onSubmit={handleSubmit} className={styles.form}>
          <input
            className={styles.input}
            type="email"
            placeholder="Your email"
            value={email}
            onChange={e => setEmail(e.target.value)}
            required
          />
          <input
            className={styles.input}
            type="password"
            placeholder="Password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
          {(localErr || error) && <p className={styles.errorMsg}>{localErr || error}</p>}
          <button type="submit" className={styles.submitBtn} disabled={loading}>
            {loading ? <span className={styles.spinner} /> : 'Login'}
          </button>
          <label className={styles.checkLabel}>
            <input type="checkbox" checked={agreed} onChange={e => setAgreed(e.target.checked)} />
            <span>By continuing, I agree to the <a href="/terms">terms of use</a> &amp; <a href="/privacy">privacy policy</a>.</span>
          </label>
          <p className={styles.switchText}>
            Create a new account?{' '}
            <button type="button" className={styles.switchLink} onClick={onSwitchToRegister}>Click here</button>
          </p>
        </form>
      </div>
    </div>
  );
}