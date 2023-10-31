package com.luv2code.ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.luv2code.ecommerce.dto.PaymentInfo;
import com.luv2code.ecommerce.dto.Purchase;
import com.luv2code.ecommerce.dto.PurchaseResponse;
import com.luv2code.ecommerce.entity.Customer;
import com.luv2code.ecommerce.entity.Order;
import com.luv2code.ecommerce.entity.OrderItem;
import com.luv2code.ecommerce.entity.dao.CustomerRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.transaction.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	private CustomerRepository customerRepository;

	@Autowired
	public CheckoutServiceImpl(CustomerRepository customerRepository,@Value("${stripe.key.secret}") String secretKey) {
		super();
		this.customerRepository = customerRepository;

        // initialize Stripe API with secret key
        Stripe.apiKey = secretKey;
	}

	@Override
	@Transactional
	public PurchaseResponse placeOrder(Purchase purchase) {

		// retrieve order details from dto
		Order order = purchase.getOrder();

		// generate random orderTrackingNumber
		String orderTrackingNumber = generateOrderTrackingNumber();
		order.setOrderTrackingNumber(orderTrackingNumber);

		// populate order with orderItems
		Set<OrderItem> orderItems = purchase.getOrderItems();
		orderItems.forEach(item -> order.add(item));

		// populate order with billing Address
		order.setBillingAddress(purchase.getBillingAddress());

		// populate order with shipping Address
		order.setShippingAddress(purchase.getShippingAddress());

		// populate customer with order
		Customer customer = purchase.getCustomer();

		// check if customer already exists in the DB or not
		String theEmail = customer.getEmail();
		Customer customerFromDb = customerRepository.findByEmail(theEmail);

		// check if customer from is null or not
		if (customerFromDb != null) {
			// its not a new customer found one existing customer
			customer = customerFromDb;
		}

		customer.add(order);

		// save to the database
		customerRepository.save(customer);

		// return response
		return new PurchaseResponse(orderTrackingNumber);

	}

	@Override
	public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
		List<String> paymentMethodTypes = new ArrayList<>();
		paymentMethodTypes.add("card");
		
		Map<String,Object> params = new HashMap<>();
	    params.put("amount", paymentInfo.getAmount());
	    params.put("currency", paymentInfo.getCurrency());
	    
	    params.put("payment_method_types", paymentMethodTypes);
	    params.put("description", "Luv2Shop purchase");
	    params.put("receipt_email", paymentInfo.getReceiptEmail());

		return PaymentIntent.create(params);

	}

	private String generateOrderTrackingNumber() {
		// generate a random UUID number
		return UUID.randomUUID().toString();
	}

}
