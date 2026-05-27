import { useState, useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import styles from './Navbar.module.css';

export default function Navbar({ onLoginClick }) {
  const { totalItems } = useCart();
  const { user, logout } = useAuth();
  const [searchOpen, setSearchOpen]   = useState(false);
  const [menuOpen, setMenuOpen]       = useState(false);
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [scrolled, setScrolled]       = useState(false);
  const searchRef = useRef(null);
  const navigate  = useNavigate();

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 10);
    window.addEventListener('scroll', onScroll);
    return () => window.removeEventListener('scroll', onScroll);
  }, []);

  useEffect(() => {
    if (searchOpen) searchRef.current?.focus();
  }, [searchOpen]);

  return (
    <nav className={`${styles.navbar} ${scrolled ? styles.scrolled : ''}`}>
      <div className={`container ${styles.inner}`}>

        {/* Logo */}
        <Link to="/" className={styles.logo}>
          <span className={styles.logoOrange}>C·BITES.</span>
        </Link>

        {/* Nav links */}
        <ul className={`${styles.navLinks} ${menuOpen ? styles.open : ''}`}>
          <li><Link to="/"           className={styles.navLink} onClick={() => setMenuOpen(false)}>home</Link></li>
          <li><Link to="/menu"       className={styles.navLink} onClick={() => setMenuOpen(false)}>menu</Link></li>
          <li><Link to="/mobile-app" className={styles.navLink} onClick={() => setMenuOpen(false)}>mobile-app</Link></li>
          <li><Link to="/contact"    className={styles.navLink} onClick={() => setMenuOpen(false)}>contact-us</Link></li>
        </ul>

        {/* Right actions */}
        <div className={styles.actions}>

          {/* Search */}
          <div className={styles.searchWrap}>
            {searchOpen && (
              <input
                ref={searchRef}
                className={styles.searchInput}
                placeholder="Search dishes..."
                onBlur={() => setSearchOpen(false)}
                onKeyDown={(e) => {
                  if (e.key === 'Enter') {
                    navigate(`/menu?search=${e.target.value}`);
                    setSearchOpen(false);
                  }
                }}
              />
            )}
            <button className={styles.iconBtn} onClick={() => setSearchOpen(v => !v)}>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
              </svg>
            </button>
          </div>

          {/* Cart */}
          <Link to="/cart" className={styles.cartBtn}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z"/>
              <line x1="3" y1="6" x2="21" y2="6"/>
              <path d="M16 10a4 4 0 01-8 0"/>
            </svg>
            {totalItems > 0 && <span className={styles.cartBadge}>{totalItems}</span>}
          </Link>

          {/* Auth */}
          {user ? (
            <div className={styles.userMenu}>
              <button className={styles.userBtn} onClick={() => setDropdownOpen(v => !v)}>
                <span className={styles.avatar}>{user.firstname?.[0]?.toUpperCase() ?? 'U'}</span>
                <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                  <polyline points="6 9 12 15 18 9"/>
                </svg>
              </button>
              {dropdownOpen && (
                <div className={styles.dropdown}>
                  <span className={styles.dropdownName}>{user.firstname} {user.lastname}</span>
                  <Link to="/orders" className={styles.dropdownItem} onClick={() => setDropdownOpen(false)}>My Orders</Link>
                  <button className={styles.dropdownItem} onClick={() => { logout(); setDropdownOpen(false); }}>Logout</button>
                </div>
              )}
            </div>
          ) : (
            <button className={styles.signInBtn} onClick={onLoginClick}>sign in</button>
          )}

          {/* Hamburger */}
          <button className={styles.hamburger} onClick={() => setMenuOpen(v => !v)}>
            <span /><span /><span />
          </button>
        </div>
      </div>
    </nav>
  );
}