import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../../../shared/context/CartContext';
import Footer from '../../home/components/Footer';
import styles from './PlaceOrderPage.module.css';

const DELIVERY_FEE = 2;

const EMPTY_FORM = {
  firstName: '', lastName: '', email: '',
  street: '', city: '', state: '',
  zipCode: '', country: '', phone: '',
};

function validate(form) {
  const errs = {};
  if (!form.firstName.trim()) errs.firstName = 'Required';
  if (!form.lastName.trim())  errs.lastName  = 'Required';
  if (!form.email.trim())     errs.email     = 'Required';
  else if (!/\S+@\S+\.\S+/.test(form.email)) errs.email = 'Invalid email';
  if (!form.street.trim())    errs.street    = 'Required';
  if (!form.city.trim())      errs.city      = 'Required';
  if (!form.zipCode.trim())   errs.zipCode   = 'Required';
  if (!form.country.trim())   errs.country   = 'Required';
  if (!form.phone.trim())     errs.phone     = 'Required';
  return errs;
}

function Field({ label, name, value, onChange, error, type = 'text' }) {
  return (
    <div className={styles.field}>
      <input
        className={`${styles.input} ${error ? styles.inputErr : ''}`}
        type={type} name={name} value={value}
        onChange={onChange} placeholder={label}
      />
      {error && <span className={styles.fieldErr}>{error}</span>}
    </div>
  );
}

export default function PlaceOrderPage() {
  const { items, totalPrice, clearCart } = useCart();
  const navigate = useNavigate();

  const [form, setForm]         = useState(EMPTY_FORM);
  const [errors, setErrors]     = useState({});
  const [loading, setLoading]   = useState(false);
  const [stripeErr, setStripeErr] = useState('');

  const subtotal = totalPrice;
  const total    = subtotal + (items.length > 0 ? DELIVERY_FEE : 0);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors(prev => ({ ...prev, [name]: '' }));
  };

  const handleSubmit = async () => {
    const errs = validate(form);
    if (Object.keys(errs).length > 0) { setErrors(errs); return; }
    if (items.length === 0) { navigate('/'); return; }

    setLoading(true);
    setStripeErr('');

    try {
      const lineItems = items.map(item => ({
        price_data: {
          currency: 'usd',
          product_data: { name: item.name },
          unit_amount: Math.round(item.price * 100),
        },
        quantity: item.qty,
      }));
      lineItems.push({
        price_data: {
          currency: 'usd',
          product_data: { name: 'Delivery Fee' },
          unit_amount: DELIVERY_FEE * 100,
        },
        quantity: 1,
      });

      const apiUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';
      const res = await fetch(`${apiUrl}/orders/checkout-session`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...(localStorage.getItem('accessToken')
            ? { Authorization: `Bearer ${localStorage.getItem('accessToken')}` }
            : {}),
        },
        body: JSON.stringify({
          lineItems,
          deliveryInfo: form,
          successUrl: `${window.location.origin}/orders?payment=success`,
          cancelUrl:  `${window.location.origin}/place-order`,
        }),
      });

      if (!res.ok) {
        const body = await res.json().catch(() => ({}));
        throw new Error(body?.error?.message || `Server error ${res.status}`);
      }

      const { url } = await res.json();
      if (!url) throw new Error('No checkout URL returned from server.');

      const pendingOrder = {
        id: `ORD-${Date.now()}`,
        items: items.map(i => ({ name: i.name, qty: i.qty })),
        total: total.toFixed(2),
        status: 'Food processing',
        date: new Date().toISOString(),
      };
      const existing = JSON.parse(localStorage.getItem('cb_orders') || '[]');
      localStorage.setItem('cb_orders', JSON.stringify([pendingOrder, ...existing]));

      clearCart();
      window.location.href = url;

    } catch (err) {
      // Demo fallback — backend not running yet
      const isNetworkErr =
        err.message?.includes('fetch') ||
        err.message?.includes('Failed') ||
        err.message?.includes('NetworkError') ||
        err.message?.includes('Server error');

      if (isNetworkErr) {
        const pendingOrder = {
          id: `ORD-${Date.now()}`,
          items: items.map(i => ({ name: i.name, qty: i.qty })),
          total: total.toFixed(2),
          status: 'Food processing',
          date: new Date().toISOString(),
        };
        const existing = JSON.parse(localStorage.getItem('cb_orders') || '[]');
        localStorage.setItem('cb_orders', JSON.stringify([pendingOrder, ...existing]));
        clearCart();
        navigate('/orders?payment=demo');
        return;
      }
      setStripeErr(err.message || 'Payment failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <main className={`container ${styles.main}`}>
        <div className={styles.grid}>

          <section className={styles.formSection}>
            <h2 className={styles.sectionTitle}>Delivery Information</h2>
            <div className={styles.row2}>
              <Field label="First name" name="firstName" value={form.firstName} onChange={handleChange} error={errors.firstName} />
              <Field label="Last name"  name="lastName"  value={form.lastName}  onChange={handleChange} error={errors.lastName} />
            </div>
            <Field label="Email address" name="email"  type="email" value={form.email}  onChange={handleChange} error={errors.email} />
            <Field label="Street"        name="street"             value={form.street} onChange={handleChange} error={errors.street} />
            <div className={styles.row2}>
              <Field label="City"  name="city"  value={form.city}  onChange={handleChange} error={errors.city} />
              <Field label="State" name="state" value={form.state} onChange={handleChange} error={errors.state} />
            </div>
            <div className={styles.row2}>
              <Field label="Zip code" name="zipCode" value={form.zipCode} onChange={handleChange} error={errors.zipCode} />
              <Field label="Country"  name="country" value={form.country} onChange={handleChange} error={errors.country} />
            </div>
            <Field label="Phone" name="phone" type="tel" value={form.phone} onChange={handleChange} error={errors.phone} />
          </section>

          <aside className={styles.summary}>
            <h2 className={styles.sectionTitle}>Cart Totals</h2>
            <div className={styles.summaryRow}>
              <span>Subtotal</span><span>${subtotal.toFixed(2)}</span>
            </div>
            <div className={styles.summaryRow}>
              <span>Delivery fee</span><span>${DELIVERY_FEE.toFixed(2)}</span>
            </div>
            <div className={`${styles.summaryRow} ${styles.totalRow}`}>
              <span>Total</span><strong>${total.toFixed(2)}</strong>
            </div>

            {stripeErr && <p className={styles.stripeErr}>{stripeErr}</p>}

            <button
              className={styles.payBtn}
              onClick={handleSubmit}
              disabled={loading || items.length === 0}
            >
              {loading ? <span className={styles.spinner} /> : 'PROCEED TO PAYMENT'}
            </button>

            {items.length === 0 && (
              <p className={styles.emptyNote}>Your cart is empty.</p>
            )}
          </aside>
        </div>
      </main>
      <Footer />
    </div>
  );
}