import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Footer from '../../home/components/Footer';
import styles from './OrdersPage.module.css';

const MOCK_ORDERS = [
  {
    id: 'ORD-MOCK-001',
    items: [
      { name: 'Greek Salad', qty: 2 },
      { name: 'Veg Salad', qty: 1 },
      { name: 'Clover Salad', qty: 2 },
      { name: 'Chicken Salad', qty: 4 },
      { name: 'Lasagna Rolls', qty: 2 },
      { name: 'Peri Peri Rolls', qty: 2 },
    ],
    total: '224.00',
    status: 'Food processing',
    date: new Date(Date.now() - 3600_000).toISOString(),
  },
  {
    id: 'ORD-MOCK-002',
    items: [
      { name: 'Greek Salad', qty: 3 },
      { name: 'Veg Salad', qty: 2 },
    ],
    total: '74.00',
    status: 'Food processing',
    date: new Date(Date.now() - 7200_000).toISOString(),
  },
];

const STATUS_STEPS  = ['Food processing', 'Out for delivery', 'Delivered'];
const STATUS_COLOR  = {
  'Food processing':  { bg: '#fff3e0', text: '#e65100', dot: '#ff9800' },
  'Out for delivery': { bg: '#e3f2fd', text: '#0d47a1', dot: '#1976d2' },
  'Delivered':        { bg: '#e8f5e9', text: '#1b5e20', dot: '#43a047' },
};

function formatItems(items) {
  return items.map(i => `${i.name} x ${i.qty}`).join(', ');
}

function TrackModal({ order, onClose }) {
  const currentStep = STATUS_STEPS.indexOf(order.status);
  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={e => e.stopPropagation()}>
        <div className={styles.modalHeader}>
          <h3 className={styles.modalTitle}>Track Order</h3>
          <button className={styles.modalClose} onClick={onClose}>✕</button>
        </div>
        <p className={styles.modalId}>{order.id}</p>
        <div className={styles.steps}>
          {STATUS_STEPS.map((step, idx) => (
            <div key={step} className={styles.stepRow}>
              <div className={`${styles.stepDot} ${idx <= currentStep ? styles.stepActive : ''}`} />
              {idx < STATUS_STEPS.length - 1 && (
                <div className={`${styles.stepLine} ${idx < currentStep ? styles.stepActive : ''}`} />
              )}
              <span className={`${styles.stepLabel} ${idx === currentStep ? styles.stepCurrent : ''}`}>
                {step}
              </span>
            </div>
          ))}
        </div>
        <div className={styles.modalItems}>
          <p className={styles.modalItemsLabel}>Items ordered:</p>
          <p className={styles.modalItemsList}>{formatItems(order.items)}</p>
        </div>
        <div className={styles.modalTotal}>
          <span>Total paid</span>
          <strong>${order.total}</strong>
        </div>
      </div>
    </div>
  );
}

export default function OrdersPage() {
  const [orders, setOrders]       = useState([]);
  const [tracking, setTracking]   = useState(null);
  const [successMsg, setSuccessMsg] = useState('');
  const navigate  = useNavigate();
  const [params]  = useSearchParams();

  useEffect(() => {
    const payment = params.get('payment');
    if (payment === 'success') setSuccessMsg('🎉 Payment successful! Your order has been placed.');
    if (payment === 'demo')    setSuccessMsg('✓ Order placed! (Demo mode — connect your backend to enable real Stripe payments.)');

    const saved   = JSON.parse(localStorage.getItem('cb_orders') || '[]');
    const mockIds = new Set(MOCK_ORDERS.map(o => o.id));
    const real    = saved.filter(o => !mockIds.has(o.id));
    setOrders([...real, ...MOCK_ORDERS]);
  }, []);

  return (
    <div className={styles.page}>
      <main className={`container ${styles.main}`}>
        <h1 className={styles.title}>My Orders</h1>

        {successMsg && <div className={styles.successBanner}>{successMsg}</div>}

        {orders.length === 0 ? (
          <div className={styles.empty}>
            <p className={styles.emptyTitle}>No orders yet</p>
            <p className={styles.emptySub}>Your completed orders will appear here.</p>
            <button className={styles.browseBtn} onClick={() => navigate('/menu')}>
              Start Ordering
            </button>
          </div>
        ) : (
          <div className={styles.list}>
            {orders.map(order => {
              const color     = STATUS_COLOR[order.status] ?? STATUS_COLOR['Food processing'];
              const itemCount = order.items.reduce((s, i) => s + i.qty, 0);
              return (
                <div key={order.id} className={styles.card}>
                  <div className={styles.icon}>
                    <svg width="36" height="36" viewBox="0 0 24 24" fill="#ff9800">
                      <path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/>
                      <polyline points="3.27 6.96 12 12.01 20.73 6.96" stroke="#fff" strokeWidth="1.5" fill="none"/>
                      <line x1="12" y1="22.08" x2="12" y2="12" stroke="#fff" strokeWidth="1.5"/>
                    </svg>
                  </div>
                  <div className={styles.info}>
                    <p className={styles.itemsSummary}>{formatItems(order.items)}</p>
                  </div>
                  <div className={styles.meta}>
                    <span className={styles.price}>${order.total}</span>
                  </div>
                  <div className={styles.meta}>
                    <span className={styles.count}>Items: {itemCount}</span>
                  </div>
                  <div className={styles.statusWrap}>
                    <span className={styles.status}
                      style={{ background: color.bg, color: color.text }}>
                      <span className={styles.statusDot} style={{ background: color.dot }} />
                      {order.status}
                    </span>
                  </div>
                  <button className={styles.trackBtn} onClick={() => setTracking(order)}>
                    Track Order
                  </button>
                </div>
              );
            })}
          </div>
        )}
      </main>

      {tracking && <TrackModal order={tracking} onClose={() => setTracking(null)} />}
      <Footer />
    </div>
  );
}