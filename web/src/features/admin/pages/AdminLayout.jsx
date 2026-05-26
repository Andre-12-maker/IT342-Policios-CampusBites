import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './AdminLayout.module.css';

const NAV = [
  { key: 'add',    label: 'Add Items', icon: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/>
    </svg>
  )},
  { key: 'list',   label: 'List Items', icon: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <rect x="3" y="3" width="18" height="18" rx="2"/><polyline points="9 11 12 14 22 4"/>
    </svg>
  )},
  { key: 'orders', label: 'Orders', icon: (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
      <rect x="3" y="3" width="18" height="18" rx="2"/><polyline points="9 11 12 14 22 4"/>
    </svg>
  )},
];

export default function AdminLayout({ active, children }) {
  const navigate = useNavigate();

  return (
    <div className={styles.shell}>
      {/* Top bar */}
      <header className={styles.topbar}>
        <div className={styles.topbarLeft}>
          <span className={styles.logo}>C·BITES.</span>
          <span className={styles.panelLabel}>Admin Panel</span>
        </div>
        <div className={styles.topbarRight}>
          <div className={styles.avatar}>
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none"
              stroke="currentColor" strokeWidth="1.8">
              <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
          </div>
        </div>
      </header>

      <div className={styles.body}>
        {/* Sidebar */}
        <aside className={styles.sidebar}>
          {NAV.map(n => (
            <button
              key={n.key}
              className={`${styles.navBtn} ${active === n.key ? styles.navActive : ''}`}
              onClick={() => navigate(`/admin/${n.key}`)}
            >
              {n.icon}
              <span>{n.label}</span>
            </button>
          ))}
        </aside>

        {/* Main content */}
        <main className={styles.content}>
          {children}
        </main>
      </div>
    </div>
  );
}