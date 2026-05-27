import { useState, useEffect, useCallback } from 'react';
import { fetchProducts, MOCK_PRODUCTS } from '../api/productsApi';

export function useProducts() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [error, setError]       = useState(null);
  const [category, setCategory] = useState('All');
  const [search, setSearch]     = useState('');

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchProducts({ category, search });
      const list = data?.data?.content ?? data?.data ?? data?.content ?? data;
      setProducts(Array.isArray(list) ? list : MOCK_PRODUCTS);
    } catch {
      let filtered = MOCK_PRODUCTS;
      if (category && category !== 'All')
        filtered = filtered.filter(p => p.category === category);
      if (search)
        filtered = filtered.filter(p =>
          p.name.toLowerCase().includes(search.toLowerCase())
        );
      setProducts(filtered);
    } finally {
      setLoading(false);
    }
  }, [category, search]);

  useEffect(() => { load(); }, [load]);

  return { products, loading, error, category, setCategory, search, setSearch, reload: load };
}