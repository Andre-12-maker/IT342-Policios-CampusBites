import { useState, useEffect } from 'react';
import AdminLayout from './AdminLayout';
import { adminListProducts, adminDeleteProduct, MOCK_ADMIN_PRODUCTS } from '../api/adminApi';
import styles from './ListItemsPage.module.css';

export default function ListItemsPage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading]   = useState(true);

  const load = async () => {
    setLoading(true);
    try {
      const res  = await adminListProducts();
      const list = res.data?.data?.content ?? res.data?.data ?? res.data ?? [];
      // Merge with any locally-added demo products
      const local = JSON.parse(localStorage.getItem('admin_products') || '[]');
      const allIds = new Set(list.map(p => p._id));
      const extra  = local.filter(p => !allIds.has(p._id));
      setProducts([...list, ...extra]);
    } catch {
      const local = JSON.parse(localStorage.getItem('admin_products') || '[]');
      setProducts([...MOCK_ADMIN_PRODUCTS, ...local]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleDelete = async (id) => {
    if (!confirm('Delete this product?')) return;
    try {
      await adminDeleteProduct(id);
    } catch {
      // Demo mode: remove from localStorage
      const local = JSON.parse(localStorage.getItem('admin_products') || '[]');
      localStorage.setItem('admin_products',
        JSON.stringify(local.filter(p => p._id !== id))
      );
    }
    setProducts(prev => prev.filter(p => p._id !== id));
  };

  return (
    <AdminLayout active="list">
      <h2 className={styles.title}>All Foods List</h2>

      {loading ? (
        <div className={styles.loading}>Loading...</div>
      ) : (
        <table className={styles.table}>
          <thead>
            <tr>
              <th>Image</th>
              <th>Name</th>
              <th>Category</th>
              <th>Price</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {products.map(p => (
              <tr key={p._id} className={styles.row}>
                <td>
                  <img
                    src={p.image || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=60&h=60&fit=crop'}
                    alt={p.name}
                    className={styles.img}
                    onError={e => {
                      e.target.src = 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=60&h=60&fit=crop';
                    }}
                  />
                </td>
                <td className={styles.name}>{p.name}</td>
                <td className={styles.cat}>{p.category}</td>
                <td className={styles.price}>${p.price}</td>
                <td>
                  <button
                    className={styles.deleteBtn}
                    onClick={() => handleDelete(p._id)}
                    aria-label={`Delete ${p.name}`}
                  >
                    X
                  </button>
                </td>
              </tr>
            ))}
            {products.length === 0 && (
              <tr>
                <td colSpan={5} className={styles.empty}>No products yet.</td>
              </tr>
            )}
          </tbody>
        </table>
      )}
    </AdminLayout>
  );
}