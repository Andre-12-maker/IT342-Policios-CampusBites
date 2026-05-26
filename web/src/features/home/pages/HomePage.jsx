import { useProducts } from '../hooks/useProducts';
import HeroBanner from '../components/HeroBanner';
import CategoryRow from '../components/CategoryRow';
import ProductGrid from '../components/ProductGrid';
import Footer from '../components/Footer';

export default function HomePage() {
  const { products, loading, category, setCategory } = useProducts();

  return (
    <main>
      <HeroBanner />
      <CategoryRow selected={category} onSelect={setCategory} />
      <ProductGrid products={products} loading={loading} />
      <Footer />
    </main>
  );
}