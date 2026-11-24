
package src.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import src.model.Order;
import src.model.Payment;
import src.model.PaymentStatus;
import src.util.ValidationUtil;

public class PaymentService {

    private ArrayList<Payment> payments = new ArrayList<>();
    private int nextPaymentId = 1;

    private OrderService orderService;

    public PaymentService(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a new payment
    public Payment createPayment(
            int orderId,
            int buyerId,
            String paymentMethod) {

        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }

        if (order.getBuyerId() != buyerId) {
            throw new IllegalArgumentException(
                "This order does not belong to the buyer."
            );
        }

        if (ValidationUtil.isEmpty(paymentMethod)) {
            throw new IllegalArgumentException(
                "Payment method is required."
            );
        }

        if (order.getAmount() <= 0 ||
            !Double.isFinite(order.getAmount())) {
            throw new IllegalArgumentException(
                "Invalid order amount."
            );
        }

        // Prevent duplicate payments, but allow retry after failure
        for (Payment payment : payments) {
            if (payment.getOrderId() == orderId &&
                payment.getPaymentStatus() != PaymentStatus.FAILED) {

                throw new IllegalArgumentException(
                    "A payment already exists for this order."
                );
            }
        }

        int paymentId = nextPaymentId++;

        Payment payment = new Payment(
            paymentId,
            orderId,
            buyerId,
            order.getAmount(),
            paymentMethod.trim(),
            PaymentStatus.PENDING,
            "TXN-" + paymentId,
            new Timestamp(System.currentTimeMillis())
        );

        payments.add(payment);
        return payment;
    }

    // Get payment by ID
    public Payment getPaymentById(int paymentId) {
        for (Payment payment : payments) {
            if (payment.getPaymentId() == paymentId) {
                return payment;
            }
        }
        return null;
    }

    // Get all payments
    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }

    // Get payments by buyer
    public List<Payment> getPaymentsByBuyer(int buyerId) {
        ArrayList<Payment> result = new ArrayList<>();

        for (Payment payment : payments) {
            if (payment.getBuyerId() == buyerId) {
                result.add(payment);
            }
        }

        return result;
    }

    // Get payments by order
    public List<Payment> getPaymentsByOrder(int orderId) {
        ArrayList<Payment> result = new ArrayList<>();

        for (Payment payment : payments) {
            if (payment.getOrderId() == orderId) {
                result.add(payment);
            }
        }

        return result;
    }

    // Complete payment
    public boolean completePayment(int paymentId, int buyerId) {
        Payment payment = getPaymentById(paymentId);

        if (payment == null ||
            payment.getBuyerId() != buyerId ||
            payment.getPaymentStatus() != PaymentStatus.PENDING) {
            return false;
        }

        payment.completePayment();
        return true;
    }

    // Fail payment
    public boolean failPayment(int paymentId, int buyerId) {
        Payment payment = getPaymentById(paymentId);

        if (payment == null ||
            payment.getBuyerId() != buyerId ||
            payment.getPaymentStatus() != PaymentStatus.PENDING) {
            return false;
        }

        payment.failPayment();
        return true;
    }

    // Refund payment
    public boolean refundPayment(int paymentId) {
        Payment payment = getPaymentById(paymentId);

        if (payment == null ||
            payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            return false;
        }

        payment.refundPayment();
        return payment.getPaymentStatus() == PaymentStatus.REFUNDED;
    }
}
