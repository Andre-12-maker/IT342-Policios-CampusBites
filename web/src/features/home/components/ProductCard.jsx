import { useState } from 'react';
import { useCart } from '../../../shared/context/CartContext';
import { FOOD_IMAGES } from '../api/productsApi';
import styles from './ProductCard.module.css';

function StarRating({ rating }) {
  return (
    <div className={styles.stars}>
      {[1,2,3,4,5].map((s) => (
        <svg key={s} width="13" height="13" viewBox="0 0 24 24"
          fill={s <= Math.round(rating) ? 'currentColor' : 'none'}
          stroke="currentColor" strokeWidth="2"
          className={s <= Math.round(rating) ? styles.starFilled : styles.starEmpty}
        >
          <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/>
        </svg>
      ))}
      <span className={styles.ratingNum}>{rating.toFixed(1)}</span>
    </div>
  );
}

export default function ProductCard({ product, index = 0 }) {
  const { addToCart, updateQty, getQty } = useCart();
  const [imgError, setImgError] = useState(false);

  const qty      = getQty(product._id);
  const fallback = FOOD_IMAGES[index % FOOD_IMAGES.length];
  const imgSrc   = (!product.image || imgError) ? fallback : product.image;

  return (
    <article className={styles.card} style={{ animationDelay: `${(index % 8) * 0.05}s` }}>
      <div className={styles.imgWrap}>
        <img
          src={imgSrc}
          alt={product.name}
          className={styles.img}
          onError={() => setImgError(true)}
          loading="lazy"
        />
        <div className={styles.addControl}>
          {qty === 0 ? (
            <button className={styles.addBtn} onClick={() => addToCart(product)}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
                <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
              </svg>
            </button>
          ) : (
            <div className={styles.qtyControl}>
              <button className={styles.qtyBtn} onClick={() => updateQty(product._id, qty - 1)}>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
                  <line x1="5" y1="12" x2="19" y2="12"/>
                </svg>
              </button>
              <span className={styles.qtyNum}>{qty}</span>
              <button className={styles.qtyBtn} onClick={() => addToCart(product)}>
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
                  <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
                </svg>
              </button>
            </div>
          )}
        </div>
      </div>
      <div className={styles.body}>
        <div className={styles.row}>
          <h3 className={styles.name}>{product.name}</h3>
          <StarRating rating={product.rating ?? 4.5} />
        </div>
        <p className={styles.desc}>{product.description}</p>
        <p className={styles.price}>${product.price}</p>
      </div>
    </article>
  );
}