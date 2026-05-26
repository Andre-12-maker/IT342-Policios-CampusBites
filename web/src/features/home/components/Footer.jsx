import { Link } from 'react-router-dom';
import styles from './Footer.module.css';

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={`container ${styles.top}`}>
        <div className={styles.brand}>
          <Link to="/" className={styles.logo}>C·BITES.</Link>
          <p className={styles.brandDesc}>
            Lorem ipsum is a dummy or placeholder text commonly used in graphic design,
            publishing, and web development.
          </p>
          <div className={styles.socials}>
            <a href="#" className={styles.social} aria-label="Facebook">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                <path d="M18 2h-3a5 5 0 00-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 011-1h3z"/>
              </svg>
            </a>
            <a href="#" className={styles.social} aria-label="Twitter">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                <path d="M23 3a10.9 10.9 0 01-3.14 1.53 4.48 4.48 0 00-7.86 3v1A10.66 10.66 0 013 4s-4 9 5 13a11.64 11.64 0 01-7 2c9 5 20 0 20-11.5a4.5 4.5 0 00-.08-.83A7.72 7.72 0 0023 3z"/>
              </svg>
            </a>
            <a href="#" className={styles.social} aria-label="LinkedIn">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
                <path d="M16 8a6 6 0 016 6v7h-4v-7a2 2 0 00-2-2 2 2 0 00-2 2v7h-4v-7a6 6 0 016-6zM2 9h4v12H2z"/>
                <circle cx="4" cy="4" r="2"/>
              </svg>
            </a>
          </div>
        </div>
        <div className={styles.col}>
          <h4 className={styles.colTitle}>COMPANY</h4>
          <Link to="/" className={styles.colLink}>Home</Link>
          <Link to="/about" className={styles.colLink}>About us</Link>
          <Link to="/delivery" className={styles.colLink}>Delivery</Link>
          <Link to="/privacy" className={styles.colLink}>Privacy policy</Link>
        </div>
        <div className={styles.col}>
          <h4 className={styles.colTitle}>GET IN TOUCH</h4>
          <a href="tel:+12124567890" className={styles.colLink}>+1-212-456-7890</a>
          <a href="mailto:contact@cbites.com" className={styles.colLink}>contact@cbites.com</a>
        </div>
      </div>
      <div className={styles.bottom}>
        <div className="container">
          <p>Copyright 2024 © CBites.com — All Rights Reserved.</p>
        </div>
      </div>
    </footer>
  );
}