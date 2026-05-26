import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const fetchProducts = async ({ category, search, page = 0, size = 20 } = {}) => {
  const params = { page, size };
  if (category && category !== 'All') params.category = category;
  if (search) params.search = search;
  const res = await api.get('/products', { params });
  return res.data;
};

export const MOCK_PRODUCTS = [
  { _id:'1',  name:'Greek Salad',       category:'Salad',    price:12, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'2',  name:'Veg Salad',         category:'Salad',    price:10, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'3',  name:'Clover Salad',      category:'Salad',    price:16, rating:4.0, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'4',  name:'Chicken Salad',     category:'Salad',    price:24, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'5',  name:'Pan Pan Rolls',     category:'Rolls',    price:13, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'6',  name:'Chicken Rolls',     category:'Rolls',    price:20, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'7',  name:'Veg Rolls',         category:'Rolls',    price:13, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'8',  name:'Espresso Cream',    category:'Desserts', price:26, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'9',  name:'Fruitas Cream',     category:'Desserts', price:27, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'10', name:'Ice Ice Cream',     category:'Desserts', price:10, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'11', name:'Vanilla Cone',      category:'Desserts', price:12, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'12', name:'Chicken Sandwich',  category:'Sandwich', price:12, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'13', name:'Vegan Sandwich',    category:'Sandwich', price:18, rating:4.0, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'14', name:'Grilled Sandwich',  category:'Sandwich', price:16, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'15', name:'Cup Cake',          category:'Cake',     price:14, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'16', name:'Vegan Cake',        category:'Cake',     price:13, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'17', name:'Butterscotch Cake', category:'Cake',     price:20, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'18', name:'Garlic Mushroom',   category:'Pure Veg', price:14, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'19', name:'Fried Cauliflower', category:'Pure Veg', price:22, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'20', name:'Mix Veg Pulao',     category:'Pure Veg', price:10, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'21', name:'Tomato Pasta',      category:'Pasta',    price:23, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'22', name:'Creamy Pasta',      category:'Pasta',    price:13, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'23', name:'Chicken Pasta',     category:'Pasta',    price:29, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'24', name:'Cheese Pasta',      category:'Pasta',    price:12, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'25', name:'Ramen Noodles',     category:'Noodles',  price:20, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'26', name:'Cooked Noodles',    category:'Noodles',  price:15, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'27', name:'Butter Noodles',    category:'Noodles',  price:18, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
  { _id:'28', name:'Veg Noodles',       category:'Noodles',  price:12, rating:4.5, description:'Food provides essential nutrients for overall health and well-being.', image:null },
];

export const CATEGORIES = ['All','Salad','Rolls','Desserts','Sandwich','Cake','Pure Veg','Pasta','Noodles'];

export const CATEGORY_IMAGES = {
  'Salad':    'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=80&h=80&fit=crop',
  'Rolls':    'https://images.unsplash.com/photo-1562802378-063ec186a863?w=80&h=80&fit=crop',
  'Desserts': 'https://images.unsplash.com/photo-1551024601-bec78aea704b?w=80&h=80&fit=crop',
  'Sandwich': 'https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=80&h=80&fit=crop',
  'Cake':     'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=80&h=80&fit=crop',
  'Pure Veg': 'https://images.unsplash.com/photo-1540420773420-3366772f4999?w=80&h=80&fit=crop',
  'Pasta':    'https://images.unsplash.com/photo-1473093295043-cdd812d0e601?w=80&h=80&fit=crop',
  'Noodles':  'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=80&h=80&fit=crop',
};

export const FOOD_IMAGES = [
  'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=400&h=300&fit=crop',
  'https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?w=400&h=300&fit=crop',
];