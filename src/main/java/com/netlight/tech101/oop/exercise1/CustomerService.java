package com.netlight.tech101.oop.exercise1;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class CustomerService {

    private CustomerRepository customerRepository = CustomerRepository.getInstance();
    private ProductRepository productRepository = ProductRepository.getInstance();
    private OrderRepository orderRepository = OrderRepository.getInstance();
    private SmtpMailClient smtpMailClient = SmtpMailClient.getInstance();

    public Customer createCustomer(String firstName, String lastName) {
        Customer c = new Customer(firstName, lastName, null);
        customerRepository.save(c);
        return c;
    }

    public void updateCustomerEmailAddress(String customerId, String newEmail) {
        Customer c = customerRepository.findById(customerId);
        c.setEmail(newEmail);
    }

    public boolean exists(String customerId) {
        return customerRepository.findById(customerId) != null;
    }

    public boolean hasEmail(String customerId) {
        Customer customer = customerRepository.findById(customerId);
        return customer.getEmail() != null;
    }

    public int totalProducts() {
        return productRepository.findAll().size();
    }

    public Order createOrder(String customerId, String... productIds) {
        Customer c = customerRepository.findById(customerId);
        Order order = new Order(c);

        for (String id : productIds) {
            Product p = productRepository.findById(id);
            order.addProduct(p);
        }

        return order;
    }

    public Money totalOrderCost(Order order) {
        Money total = Money.zero(CurrencyUnit.EUR);
        for (Product p : order.getProducts()) {
            total.plus(p.getPrice());
        }
        return total;
    }

    public void placeOrder(Order order) {
        orderRepository.save(order);
        sendEmail(order.getCustomer(), "Thank you for your order!");
    }

    public void sendEmail(Customer customer, String message) {
        if (hasEmail(customer.getCustomerId())) {
            smtpMailClient.sendEmail(customer.getEmail(), message);
        }
    }


}