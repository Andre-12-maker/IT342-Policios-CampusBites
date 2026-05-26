import { useState, useEffect } from 'react';
import AdminLayout from './AdminLayout';
import {
  adminListOrders, adminUpdateStatus,
  MOCK_ADMIN_ORDERS, ORDER_STATUSES, STATUS_LABELS
} from '../api/adminApi';
import styles from './AdminOrdersPage.module.css';

function formatItems(items) {
  return items.map(i => `${i.name} x ${i.qty}`).join(', ');
}

function formatAddress(di) {
  if (!di) return '';
  return [di.firstName + ' ' + di.lastName, di.street, `${di.city}, ${di.state}, ${di.country}, ${di.zipCode}`, di.phone]
    .filter(Boolean).join('\n');
}

export default function AdminOrdersPage() {
  const [orders, setOrders]   = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    (async () => {
      setLoading(true);
      try {
        const res  = await adminListOrders();
        const list = res.data?.data?.content ?? res.data?.data ?? res.data ?? [];
        setOrders(list.length ? list : MOCK_ADMIN_ORDERS);
      } catch {
        setOrders(MOCK_ADMIN_ORDERS);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const handleStatus = async (orderId, newStatus) => {
    try {
      await adminUpdateStatus(orderId, newStatus);
    } catch {
      // demo mode — just update local state
    }
    setOrders(prev =>
      prev.map(o => o.id === orderId ? { ...o, status: newStatus } : o)
    );
  };

  return (
    <AdminLayout active="orders">
      <h2 className={styles.title}>Order Page</h2>

      {loading ? (
        <div className={styles.loading}>Loading orders...</div>
      ) : (
        <div className={styles.list}>
          {orders.map(order => {
            const itemCount = order.items?.reduce((s, i) => s + (i.qty ?? 1), 0) ?? 0;
            const currentStatus = order.status ?? 'FOOD_PROCESSING';

            return (
              <div key={order.id} className={styles.card}>
                {/* Parcel icon */}
                <div className={styles.iconWrap}>
                  <svg width="40" height="40" viewBox="0 0 24 24" fill="#ff9800">
                    <path d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z"/>
                    <polyline points="3.27 6.96 12 12.01 20.73 6.96" stroke="#fff" strokeWidth="1.5" fill="none"/>
                    <line x1="12" y1="22.08" x2="12" y2="12" stroke="#fff" strokeWidth="1.5"/>
                  </svg>
                </div>

                {/* Order info */}
                <div className={styles.info}>
                  <p className={styles.items}>{formatItems(order.items ?? [])}</p>
                  {order.deliveryInfo && (
                    <p className={styles.address}>{formatAddress(order.deliveryInfo)}</p>
                  )}
                </div>

                {/* Items count */}
                <div className={styles.meta}>
                  <span className={styles.count}>Items: {itemCount}</span>
                </div>

                {/* Price */}
                <div className={styles.meta}>
                  <span className={styles.price}>${order.total}</span>
                </div>

                {/* Status dropdown */}
                <div className={styles.statusWrap}>
                  <select
                    className={styles.statusSelect}
                    value={currentStatus}
                    onChange={e => handleStatus(order.id, e.target.value)}
                  >
                    {ORDER_STATUSES.map(s => (
                      <option key={s} value={s}>{STATUS_LABELS[s]}</option>
                    ))}
                  </select>
                </div>
              </div>
            );
          })}

          {orders.length === 0 && (
            <p className={styles.empty}>No orders yet.</p>
          )}
        </div>
      )}
    </AdminLayout>
  );
}