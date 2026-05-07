import React, { useContext, useEffect, useState } from 'react'
import './FoodDisplay.css'
import { food_list, assets } from '../../shared/assets/assets'
import { StoreContext } from '../../shared/context/StoreContext'

const FoodDisplay = ({ category }) => {
  const { addToCart } = useContext(StoreContext)
  const [foods, setFoods] = useState([])

  useEffect(() => {
    // For now, use static data. Later we'll fetch from API
    // fetchFoods()
    let filteredFoods = food_list
    if (category !== "All") {
      filteredFoods = food_list.filter(item => item.category === category)
    }
    setFoods(filteredFoods)
  }, [category])

  const fetchFoods = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/food')
      const data = await response.json()
      setFoods(data)
    } catch (error) {
      console.error('Error fetching foods:', error)
      // Fallback to static data
      setFoods(food_list)
    }
  }

  return (
    <div className='food-display' id='food-display'>
      <h2>Top dishes near you</h2>
      <div className="food-display-list">
        {foods.map((item, index) => (
          <div key={index} className='food-item'>
            <div className="food-item-img-container">
              <img className="food-item-image" src={item.image} alt="" />
              <button className='add' onClick={() => addToCart(item._id)}>+</button>
            </div>
            <div className="food-item-info">
              <div className="food-item-name-rating">
                <p>{item.name}</p>
                <img src={assets.rating_starts} alt="" />
              </div>
              <p className="food-item-desc">{item.description}</p>
              <p className="food-item-price">${item.price}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default FoodDisplay