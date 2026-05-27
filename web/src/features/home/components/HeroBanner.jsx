import { Link } from 'react-router-dom';
import styles from './HeroBanner.module.css';

export default function HeroBanner() {
  return (
    <section className={styles.hero}>
      <div className={styles.content}>
        <h1 className={styles.heading}>
          Order your<br />favorite food here
        </h1>
        <p className={styles.sub}>
          Choose a diverse menu featuring a delectable array of dishes crafted with the
          finest ingredients and culinary expertise. Our mission is to satisfy your
          cravings and elevate your dining experience, one delicious meal at a time.
        </p>
        <Link to="/menu" className={styles.cta}>View Menu</Link>
      </div>
      <div className={styles.imageWrap}>
        <img
          src="https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=700&h=500&fit=crop"
          alt="Delicious food"
          className={styles.heroImg}
        />
      </div>
    </section>
  );
}