import { useState, useRef } from 'react';
import AdminLayout from './AdminLayout';
import { adminAddProduct, adminUploadImage, ADMIN_CATEGORIES } from '../api/adminApi';
import styles from './AddItemPage.module.css';

const EMPTY = { name: '', description: '', category: 'Desserts', price: '' };

export default function AddItemPage() {
  const [form, setForm]       = useState(EMPTY);
  const [imgFile, setImgFile] = useState(null);
  const [imgPrev, setImgPrev] = useState(null);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError]     = useState('');
  const fileRef = useRef(null);

  const handleImg = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setImgFile(file);
    setImgPrev(URL.createObjectURL(file));
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(p => ({ ...p, [name]: value }));
  };

  const handleSubmit = async () => {
    setError(''); setSuccess('');
    if (!form.name.trim())        { setError('Product name is required.'); return; }
    if (!form.price || isNaN(+form.price)) { setError('Valid price required.'); return; }

    setLoading(true);
    try {
      let imageUrl = null;

      // Try uploading image if provided
      if (imgFile) {
        try {
          const res = await adminUploadImage(imgFile);
          imageUrl = res.data?.data?.url ?? res.data?.url ?? null;
        } catch {
          // Cloudinary not configured — continue without image
        }
      }

      await adminAddProduct({
        name: form.name.trim(),
        description: form.description.trim(),
        category: form.category,
        price: parseFloat(form.price),
        image: imageUrl,
      });

      setSuccess(`"${form.name}" added successfully!`);
      setForm(EMPTY);
      setImgFile(null);
      setImgPrev(null);
    } catch (err) {
      // Demo fallback — save to localStorage
      const products = JSON.parse(localStorage.getItem('admin_products') || '[]');
      const newProduct = {
        _id: `local-${Date.now()}`,
        name: form.name.trim(),
        description: form.description.trim(),
        category: form.category,
        price: parseFloat(form.price),
        image: imgPrev,
      };
      localStorage.setItem('admin_products', JSON.stringify([...products, newProduct]));
      setSuccess(`"${form.name}" added! (Demo mode — connect backend to persist.)`);
      setForm(EMPTY);
      setImgFile(null);
      setImgPrev(null);
    } finally {
      setLoading(false);
    }
  };

  return (
    <AdminLayout active="add">
      <div className={styles.wrap}>
        {/* Upload image */}
        <div className={styles.field}>
          <label className={styles.label}>Upload Image</label>
          <div
            className={styles.uploadBox}
            onClick={() => fileRef.current?.click()}
          >
            {imgPrev ? (
              <img src={imgPrev} alt="preview" className={styles.imgPreview} />
            ) : (
              <div className={styles.uploadPlaceholder}>
                <svg width="32" height="32" viewBox="0 0 24 24" fill="none"
                  stroke="#bbb" strokeWidth="1.5">
                  <polyline points="16 16 12 12 8 16"/>
                  <line x1="12" y1="12" x2="12" y2="21"/>
                  <path d="M20.39 18.39A5 5 0 0018 9h-1.26A8 8 0 103 16.3"/>
                </svg>
                <span className={styles.uploadLabel}>Upload</span>
              </div>
            )}
          </div>
          <input ref={fileRef} type="file" accept="image/*"
            style={{ display:'none' }} onChange={handleImg} />
        </div>

        {/* Product name */}
        <div className={styles.field}>
          <label className={styles.label}>Product name</label>
          <input
            className={styles.input}
            name="name"
            value={form.name}
            onChange={handleChange}
            placeholder="Type here"
          />
        </div>

        {/* Description */}
        <div className={styles.field}>
          <label className={styles.label}>Product description</label>
          <textarea
            className={styles.textarea}
            name="description"
            value={form.description}
            onChange={handleChange}
            placeholder="Write content here"
            rows={4}
          />
        </div>

        {/* Category + Price row */}
        <div className={styles.row2}>
          <div className={styles.field}>
            <label className={styles.label}>Product category</label>
            <select
              className={styles.select}
              name="category"
              value={form.category}
              onChange={handleChange}
            >
              {ADMIN_CATEGORIES.map(c => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
          </div>
          <div className={styles.field}>
            <label className={styles.label}>Product price</label>
            <input
              className={styles.input}
              name="price"
              type="number"
              min="0"
              step="0.01"
              value={form.price}
              onChange={handleChange}
              placeholder="$20"
            />
          </div>
        </div>

        {error   && <p className={styles.error}>{error}</p>}
        {success && <p className={styles.success}>{success}</p>}

        <button
          className={styles.addBtn}
          onClick={handleSubmit}
          disabled={loading}
        >
          {loading ? <span className={styles.spinner} /> : 'ADD'}
        </button>
      </div>
    </AdminLayout>
  );
}