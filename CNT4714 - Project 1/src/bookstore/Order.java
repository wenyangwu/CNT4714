/*
 * Name: Wenyang Wu
 * Course: CNT 4714 - Spring 2017
 * Assignment Title: Program 1 - Event-driven Programming
 * Date: Sunday January 29, 2016
 */
package bookstore;

import java.text.DecimalFormat;

public class Order {
    Book book = null;
    int quantity = 0;
    double subTotal = 0.00;
    double discount = 0.00;
    String orderString = "";
    String invoiceString = "";

    public Order(Book book, int quant, double total, double discount) {

        // Create an order object
        DecimalFormat discountString = new DecimalFormat("##");
        DecimalFormat totalString = new DecimalFormat(".##");

        this.book = book;
        this.quantity = quant;
        this.subTotal = total;
        this.discount = discount;
        this.orderString = book.isbn + " " + book.title+ " " + "$"+book.price+ " " + quant+ " "+ discountString.format(discount*100)+ "% $"+totalString.format(total);
        this.invoiceString = book.isbn+", "+book.title+", "+book.price+", "+quant+", "+discount+", "+totalString.format(total);
    }
}
