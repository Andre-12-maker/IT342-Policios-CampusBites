import { createContext, useContext, useReducer, useEffect } from 'react';

const CartContext = createContext(null);
const STORAGE_KEY = 'campusbites_cart';

function cartReducer(state, action) {
  switch (action.type) {
    case 'ADD': {
      const exists = state.items.find(i => i._id === action.product._id);
      if (exists) {
        return { ...state, items: state.items.map(i =>
          i._id === action.product._id ? { ...i, qty: i.qty + 1 } : i
        )};
      }
      return { ...state, items: [...state.items, { ...action.product, qty: 1 }] };
    }
    case 'REMOVE':
      return { ...state, items: state.items.filter(i => i._id !== action.id) };
    case 'UPDATE_QTY':
      if (action.qty <= 0)
        return { ...state, items: state.items.filter(i => i._id !== action.id) };
      return { ...state, items: state.items.map(i =>
        i._id === action.id ? { ...i, qty: action.qty } : i
      )};
    case 'CLEAR':
      return { ...state, items: [] };
    case 'LOAD':
      return { ...state, items: action.items };
    default:
      return state;
  }
}

export function CartProvider({ children }) {
  const [state, dispatch] = useReducer(cartReducer, { items: [] });

  useEffect(() => {
    const saved = localStorage.getItem(STORAGE_KEY);
    if (saved) { try { dispatch({ type: 'LOAD', items: JSON.parse(saved) }); } catch {} }
  }, []);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state.items));
  }, [state.items]);

  const totalItems = state.items.reduce((s, i) => s + i.qty, 0);
  const totalPrice = state.items.reduce((s, i) => s + i.price * i.qty, 0);

  const addToCart      = (product) => dispatch({ type: 'ADD', product });
  const removeFromCart = (id)      => dispatch({ type: 'REMOVE', id });
  const updateQty      = (id, qty) => dispatch({ type: 'UPDATE_QTY', id, qty });
  const clearCart      = ()        => dispatch({ type: 'CLEAR' });
  const getQty         = (id)      => state.items.find(i => i._id === id)?.qty ?? 0;

  return (
    <CartContext.Provider value={{
      items: state.items, totalItems, totalPrice,
      addToCart, removeFromCart, updateQty, clearCart, getQty,
    }}>
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => {
  const ctx = useContext(CartContext);
  if (!ctx) throw new Error('useCart must be used within CartProvider');
  return ctx;
};