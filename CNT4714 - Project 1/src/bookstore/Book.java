/*
 * Name: Wenyang Wu
 * Course: CNT 4714 - Spring 2017
 * Assignment Title: Program 1 - Event-driven Programming
 * Date: Sunday January 29, 2016
 */
package bookstore;

public class Book {

    String isbn;
    String title;
    double price;

    public Book(String isbn,String title,double price) {
        // Create book object
        this.isbn = isbn.trim();
        this.title = title.trim();
        this.price = price;
    }
}
