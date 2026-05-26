import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

const api = axios.create({ baseURL: BASE_URL });
api.interceptors.request.use(cfg => {
  const t = localStorage.getItem('accessToken');
  if (t) cfg.headers.Authorization = `Bearer ${t}`;
  return cfg;
});

// Products
export const adminAddProduct    = (data)     => api.post('/admin/products', data);
export const adminListProducts  = ()         => api.get('/products?size=100');
export const adminDeleteProduct = (id)       => api.delete(`/admin/products/${id}`);

// Orders
export const adminListOrders    = (page = 0) => api.get(`/admin/orders?page=${page}&size=20`);
export const adminUpdateStatus  = (id, status) => api.patch(`/admin/orders/${id}/status`, { status });

// Image upload (Cloudinary via backend)
export const adminUploadImage   = (file) => {
  const fd = new FormData();
  fd.append('file', file);
  return api.post('/admin/products/upload', fd, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

// ── MOCK DATA for demo mode ──────────────────────────────────
export const MOCK_ADMIN_PRODUCTS = [
  { _id: 'p1', name: 'Food 1', category: 'Desserts', price: 10,
    image: 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=80&h=80&fit=crop' },
  { _id: 'p2', name: 'Food 2', category: 'Pure Veg', price: 10,
    image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=80&h=80&fit=crop' },
];

export const MOCK_ADMIN_ORDERS = [
  {
    id: 'ORD-ADMIN-001',
    items: [
      { name:'Greek salad', qty:2 },{ name:'Veg Salad', qty:1 },
      { name:'Clover Salad', qty:2 },{ name:'Chicken Salad', qty:4 },
      { name:'Lasagna Rolls', qty:2 },{ name:'Peri Peri Rolls', qty:2 },
    ],
    deliveryInfo: { firstName:'great', lastName:'stack', street:'street',
      city:'City', state:'state', country:'US', zipCode:'123456', phone:'9876543210' },
    total: '224.00',
    status: 'FOOD_PROCESSING',
  },
  {
    id: 'ORD-ADMIN-002',
    items: [{ name:'Greek salad', qty:3 }, { name:'Veg Salad', qty:2 }],
    deliveryInfo: { firstName:'great', lastName:'stack', street:'a',
      city:'a', state:'', country:'a', zipCode:'a', phone:'4545454545' },
    total: '74.00',
    status: 'FOOD_PROCESSING',
  },
];

export const ORDER_STATUSES = ['FOOD_PROCESSING', 'OUT_FOR_DELIVERY', 'DELIVERED'];
export const STATUS_LABELS = {
  FOOD_PROCESSING:  'Food processing',
  OUT_FOR_DELIVERY: 'Out for delivery',
  DELIVERED:        'Delivered',
};
export const ADMIN_CATEGORIES = ['Salad','Rolls','Desserts','Sandwich','Cake','Pure Veg','Pasta','Noodles'];