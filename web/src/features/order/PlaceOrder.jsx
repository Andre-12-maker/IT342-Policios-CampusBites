import React, { useContext, useState } from 'react'
import './PlaceOrder.css'
import { StoreContext } from '../../shared/context/StoreContext'

const PlaceOrder = () => {

  
  // Add user to the destructured context values:
  const { getTotalCartAmount, cartItems, food_list, user } = useContext(StoreContext)

  // In placeOrder(), replace the hardcoded userId:
  const orderData = {
      userId: user?.id ?? 'guest',   // ← use real user ID
      items: orderItems,
      totalAmount: getTotalCartAmount() + 2,
      deliveryAddress: `${formData.street}, ${formData.city}, ${formData.state} ${formData.zipcode}, ${formData.country}`
  };

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    street: '',
    city: '',
    state: '',
    zipcode: '',
    country: '',
    phone: ''
  })

  const onChangeHandler = (event) => {
    const name = event.target.name
    const value = event.target.value
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const placeOrder = async (event) => {
    event.preventDefault()
    
    // Prepare order data
    const orderItems = []
    food_list.forEach(item => {
      if (cartItems[item._id] > 0) {
        orderItems.push({
          foodId: item._id,
          name: item.name,
          quantity: cartItems[item._id],
          price: item.price
        })
      }
    })

       try {
      const response = await fetch('http://localhost:8080/api/orders', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData)
      })

      if (response.ok) {
        alert('Order placed successfully!')
        // Clear cart, redirect, etc.
      } else {
        alert('Failed to place order')
      }
    } catch (error) {
      console.error('Error placing order:', error)
      alert('Error placing order')
    }
  }

  return (
    <form onSubmit={placeOrder} className='place-order'>
      <div className="place-order-left">
        <p className="title">Delivery Information</p>
        <div className="multi-fields">
          <input required name='firstName' onChange={onChangeHandler} value={formData.firstName} type="text" placeholder='First name' />
          <input required name='lastName' onChange={onChangeHandler} value={formData.lastName} type="text" placeholder='Last name' />
        </div>
        <input required name='email' onChange={onChangeHandler} value={formData.email} type="email" placeholder='Email address' />
        <input required name='street' onChange={onChangeHandler} value={formData.street} type="text" placeholder='Street' />
        <div className="multi-fields">
          <input required name='city' onChange={onChangeHandler} value={formData.city} type="text" placeholder='City' />
          <input required name='state' onChange={onChangeHandler} value={formData.state} type="text" placeholder='State' />
        </div>
        <div className="multi-fields">
          <input required name='zipcode' onChange={onChangeHandler} value={formData.zipcode} type="text" placeholder='Zip code' />
          <input required name='country' onChange={onChangeHandler} value={formData.country} type="text" placeholder='Country' />
        </div>
        <input required name='phone' onChange={onChangeHandler} value={formData.phone} type="text" placeholder='Phone' />
      </div>
      <div className="place-order-right">
        <div className="cart-total">
          <h2>Cart Totals</h2>
          <div>
            <div className="cart-total-details">
              <p>Subtotal</p>
              <p>${getTotalCartAmount()}</p>
            </div>
            <hr />
            <div className="cart-total-details">
              <p>Delivery Fee</p>
              <p>${getTotalCartAmount() === 0 ? 0 : 2}</p>
            </div>
            <hr />
            <div className="cart-total-details">
              <b>Total</b>
              <b>${getTotalCartAmount() === 0 ? 0 : getTotalCartAmount() + 2}</b>
            </div>
          </div>
          <button type='submit'>PROCEED TO PAYMENT</button>
        </div>
      </div>
    </form>
  )
}

export default PlaceOrder
