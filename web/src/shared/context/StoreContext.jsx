import { createContext, useEffect, useState } from "react"
import { food_list } from "../assets/assets"

export const StoreContext = createContext(null);
const API_BASE = "http://localhost:8080";

const StoreContextProvider = (props) => {
    const [cartItems, setCartItems]   = useState({});
    const [food_list, setFoodList]    = useState([]);
    const [user, setUser]             = useState(null);  // logged-in user

    // Fetch food list from backend on mount
    useEffect(() => {
        fetch(`${API_BASE}/api/food`)
            .then(res => res.json())
            .then(data => setFoodList(data))
            .catch(err => console.error("Failed to load food list:", err));
    }, []);

    const addToCart = (itemId) => {
        setCartItems(prev => ({ ...prev, [itemId]: (prev[itemId] || 0) + 1 }));
    };

    const removeFromCart = (itemId) => {
        setCartItems(prev => ({ ...prev, [itemId]: Math.max(0, (prev[itemId] || 0) - 1) }));
    };

    const getTotalCartAmount = () => {
        return Object.entries(cartItems).reduce((total, [id, qty]) => {
            if (qty > 0) {
                const item = food_list.find(f => f.id === id);
                if (item) total += item.price * qty;
            }
            return total;
        }, 0);
    };

    const contextValue = {
        food_list,
        cartItems,
        setCartItems,
        addToCart,
        removeFromCart,
        getTotalCartAmount,
        user,
        setUser,
        API_BASE,
    };

    return (
        <StoreContext.Provider value={contextValue}>
            {props.children}
        </StoreContext.Provider>
    );
};

export default StoreContextProvider;