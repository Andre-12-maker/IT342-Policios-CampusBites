import { CATEGORIES, CATEGORY_IMAGES } from '../api/productsApi';
import styles from './CategoryRow.module.css';

export default function CategoryRow({ selected, onSelect }) {
  return (
    <section className={styles.section}>
      <div className="container">
        <h2 className={styles.title}>Explore our menu</h2>
        <p className={styles.sub}>
          Choose a diverse menu featuring a delectable array of dishes crafted with the finest
          ingredients and culinary expertise. Our mission is to satisfy your cravings and
          elevate your dining experience, one delicious meal at a time.
        </p>
        <div className={styles.row}>
          {CATEGORIES.map((cat) => (
            <button
              key={cat}
              className={`${styles.catBtn} ${selected === cat ? styles.active : ''}`}
              onClick={() => onSelect(cat)}
            >
              <div className={styles.imgWrap}>
                {cat !== 'All' ? (
                  <img src={CATEGORY_IMAGES[cat]} alt={cat} className={styles.catImg} />
                ) : (
                  <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
                    <rect x="3" y="3" width="7" height="7" rx="1"/>
                    <rect x="14" y="3" width="7" height="7" rx="1"/>
                    <rect x="3" y="14" width="7" height="7" rx="1"/>
                    <rect x="14" y="14" width="7" height="7" rx="1"/>
                  </svg>
                )}
              </div>
              <span className={styles.catLabel}>{cat}</span>
            </button>
          ))}
        </div>
      </div>
    </section>
  );
}