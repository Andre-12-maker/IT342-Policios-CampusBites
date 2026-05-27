import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../../../shared/context/CartContext';
import { FOOD_IMAGES } from '../../home/api/productsApi';
import Footer from '../../home/components/Footer';
import styles from './CartPage.module.css';

const DELIVERY_FEE = 2;

const PROMO_CODES = {
  CAMPUS10: 0.10,
  BITES20:  0.20,
};

export default function CartPage() {
  const { items, totalPrice, updateQty, removeFromCart } = useCart();
  const [promoInput, setPromoInput] = useState('');
  const [discount, setDiscount]     = useState(0);
  const [promoMsg, setPromoMsg]     = useState('');
  const navigate = useNavigate();

  const subtotal    = totalPrice;
  const discountAmt = subtotal * discount;
  const total       = subtotal - discountAmt + (items.length > 0 ? DELIVERY_FEE : 0);

  const handlePromo = () => {
    const code = promoInput.trim().toUpperCase();
    if (PROMO_CODES[code]) {
      setDiscount(PROMO_CODES[code]);
      setPromoMsg(`✓ Code applied — ${PROMO_CODES[code] * 100}% off!`);
    } else {
      setDiscount(0);
      setPromoMsg('✗ Invalid promo code.');
    }
  };

  return (
    <div className={styles.page}>
      <main className={`container ${styles.main}`}>
        {items.length === 0 ? (
          <div className={styles.empty}>
            <svg width="64" height="64" viewBox="0 0 24 24" fill="none"
              stroke="var(--brand-primary)" strokeWidth="1.5">
              <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z"/>
              <line x1="3" y1="6" x2="21" y2="6"/>
              <path d="M16 10a4 4 0 01-8 0"/>
            </svg>
            <p className={styles.emptyTitle}>Your cart is empty</p>
            <p className={styles.emptySub}>Add some delicious items from the menu!</p>
            <button className={styles.browseBtn} onClick={() => navigate('/menu')}>
              Browse Menu
            </button>
          </div>
        ) : (
          <>
            <div className={styles.tableWrap}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Items</th>
                    <th>Title</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th>Remove</th>
                  </tr>
                </thead>
                <tbody>
                  {items.map((item, idx) => {
                    const imgSrc = item.image || FOOD_IMAGES[idx % FOOD_IMAGES.length];
                    return (
                      <tr key={item._id} className={styles.row}>
                        <td>
                          <img
                            src={imgSrc}
                            alt={item.name}
                            className={styles.itemImg}
                            onError={(e) => { e.target.src = FOOD_IMAGES[idx % FOOD_IMAGES.length]; }}
                          />
                        </td>
                        <td className={styles.itemName}>{item.name}</td>
                        <td className={styles.itemPrice}>${item.price}</td>
                        <td>
                          <div className={styles.qtyWrap}>
                            <button className={styles.qtyBtn}
                              onClick={() => updateQty(item._id, item.qty - 1)}>−</button>
                            <span className={styles.qtyNum}>{item.qty}</span>
                            <button className={styles.qtyBtn}
                              onClick={() => updateQty(item._id, item.qty + 1)}>+</button>
                          </div>
                        </td>
                        <td className={styles.itemTotal}>
                          ${(item.price * item.qty).toFixed(2)}
                        </td>
                        <td>
                          <button className={styles.removeBtn}
                            onClick={() => removeFromCart(item._id)}>✕</button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>

            <div className={styles.bottom}>
              <div className={styles.totals}>
                <h2 className={styles.totalsTitle}>Cart Totals</h2>
                <div className={styles.totalsRow}>
                  <span>Subtotal</span><span>${subtotal.toFixed(2)}</span>
                </div>
                {discount > 0 && (
                  <div className={`${styles.totalsRow} ${styles.discount}`}>
                    <span>Discount ({discount * 100}%)</span>
                    <span>−${discountAmt.toFixed(2)}</span>
                  </div>
                )}
                <div className={styles.totalsRow}>
                  <span>Delivery fee</span><span>${DELIVERY_FEE.toFixed(2)}</span>
                </div>
                <div className={`${styles.totalsRow} ${styles.totalRow}`}>
                  <span>Total</span><strong>${total.toFixed(2)}</strong>
                </div>
                <button className={styles.checkoutBtn}
                  onClick={() => navigate('/place-order')}>
                  PROCEED TO CHECKOUT
                </button>
              </div>

              <div className={styles.promo}>
                <p className={styles.promoLabel}>
                  If you have a promo code, Enter it here
                </p>
                <div className={styles.promoRow}>
                  <input
                    className={styles.promoInput}
                    type="text"
                    placeholder="promo code"
                    value={promoInput}
                    onChange={(e) => setPromoInput(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handlePromo()}
                  />
                  <button className={styles.promoBtn} onClick={handlePromo}>
                    Submit
                  </button>
                </div>
                {promoMsg && (
                  <p className={`${styles.promoMsg} ${discount > 0 ? styles.promoOk : styles.promoErr}`}>
                    {promoMsg}
                  </p>
                )}
                <p className={styles.promoHint}>Try: CAMPUS10 or BITES20</p>
              </div>
            </div>
          </>
        )}
      </main>
      <Footer />
    </div>
  );
}