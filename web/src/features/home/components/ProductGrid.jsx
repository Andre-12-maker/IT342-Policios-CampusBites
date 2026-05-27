import ProductCard from './ProductCard';
import styles from './ProductGrid.module.css';

function Skeleton() {
  return (
    <div className={styles.skeleton}>
      <div className={styles.skelImg} />
      <div className={styles.skelBody}>
        <div className={styles.skelLine} style={{ width: '60%' }} />
        <div className={styles.skelLine} style={{ width: '90%' }} />
        <div className={styles.skelLine} style={{ width: '40%' }} />
      </div>
    </div>
  );
}

export default function ProductGrid({ products, loading }) {
  return (
    <section className={styles.section}>
      <div className="container">
        <h2 className={styles.title}>Top dishes near you</h2>
        {loading ? (
          <div className={styles.grid}>
            {Array.from({ length: 8 }).map((_, i) => <Skeleton key={i} />)}
          </div>
        ) : products.length === 0 ? (
          <div className={styles.empty}>
            <p>No dishes found</p>
          </div>
        ) : (
          <div className={styles.grid}>
            {products.map((product, i) => (
              <ProductCard key={product._id} product={product} index={i} />
            ))}
          </div>
        )}
      </div>
    </section>
  );
}