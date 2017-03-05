/*
 * Name: Wenyang Wu
 * Course: CNT 4714 - Spring 2017
 * Assignment Title: Program 1 - Event-driven Programming
 * Date: Sunday January 29, 2016
 */
package bookstore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {

    static ArrayList<Book> inventory = new ArrayList<>();
    static ArrayList<Order> orders = new ArrayList<>();
    int orderNumber = 1;
    double subTotal = 0;
    int numItems = 0;


    private JFrame frame;

    private JLabel numOrdersLabel;
    private JLabel bookIDLabel;
    private JLabel quantLabel;
    private JLabel itemInfoLabel;
    private JLabel subtotalLabel;

    private JTextField numOrdersTextField;
    private JTextField bookIDTextField;
    private JTextField quantityTextField;
    private JTextField itemInfoTextField;
    private JTextField subtotalTextField;

    private JButton processButton;
    private JButton confirmButton;
    private JButton viewOrderButton;
    private JButton finishOrderButton;
    private JButton newOrderButton;
    private JButton exitButton;

    public UI() {

        frame = new JFrame("Ye Olde Book Shoppe");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // Number of items in order
        numOrdersLabel = new JLabel("Enter Number of items in this order:");
        numOrdersTextField = new JTextField(30);
        // add Label
        gc.gridx = 0;
        gc.gridy = 0;
        frame.add(numOrdersLabel,gc);
        // add TextField
        gc.gridx = 1;
        gc.gridy = 0;
        frame.add(numOrdersTextField,gc);

        // Book ID
        bookIDLabel = new JLabel("Enter Book ID for Item #1:");
        bookIDTextField = new JTextField(30);
        // add label
        gc.gridx = 0;
        gc.gridy = 1;
        frame.add(bookIDLabel,gc);
        // add TextField
        gc.gridx = 1;
        gc.gridy = 1;
        frame.add(bookIDTextField,gc);

        // Quantity
        quantLabel = new JLabel("Enter Quantity for Item #1");
        quantityTextField = new JTextField(30);
        // add label
        gc.gridx = 0;
        gc.gridy = 2;
        frame.add(quantLabel,gc);
        // add TextField
        gc.gridx = 1;
        gc.gridy = 2;
        frame.add(quantityTextField,gc);

        // Item Info
        itemInfoLabel = new JLabel("Item #1 info:");
        itemInfoTextField = new JTextField(30);
        itemInfoTextField.setEnabled(false);
        // add label
        gc.gridx = 0;
        gc.gridy = 3;
        frame.add(itemInfoLabel,gc);
        // add TextField
        gc.gridx = 1;
        gc.gridy = 3;
        frame.add(itemInfoTextField,gc);

        // Order subtotal
        subtotalLabel = new JLabel("Order subtotal for 0 item(s):");
        subtotalTextField = new JTextField(30);
        subtotalTextField.setEnabled(false);
        // add label
        gc.gridx = 0;
        gc.gridy = 4;
        frame.add(subtotalLabel,gc);
        // add TextField
        gc.gridx = 1;
        gc.gridy = 4;
        frame.add(subtotalTextField,gc);

        // Process/Confirm Item & View/Finish/New Order & Exit
        processButton = new JButton("Process Item #1");
        gc.gridx = 0;
        gc.gridy = 5;
        frame.add(processButton,gc);
        processButton.addActionListener(new processOrderListener());

        confirmButton = new JButton("Confirm Item #1");
        confirmButton.setEnabled(false);
        gc.gridx = 1;
        gc.gridy = 5;
        frame.add(confirmButton,gc);
        confirmButton.addActionListener(new confirmListener());

        viewOrderButton = new JButton("View Order");
        viewOrderButton.setEnabled(false);
        gc.gridx = 2;
        gc.gridy = 5;
        frame.add(viewOrderButton,gc);
        viewOrderButton.addActionListener(new viewOrderListener());

        finishOrderButton = new JButton("Finish Order");
        finishOrderButton.setEnabled(false);
        gc.gridx = 3;
        gc.gridy = 5;
        frame.add(finishOrderButton,gc);
        finishOrderButton.addActionListener(new finishOrderListener());

        newOrderButton = new JButton("New Order");
        gc.gridx = 4;
        gc.gridy = 5;
        frame.add(newOrderButton,gc);
        newOrderButton.addActionListener(new newOrderListener());

        exitButton = new JButton("Exit");
        gc.gridx = 5;
        gc.gridy = 5;
        frame.add(exitButton,gc);
        exitButton.addActionListener(new exitListener());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        buildInventory();
    }

    private class exitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    private class newOrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Clear orders, reset variables and UI elements
            orders.clear();
            orderNumber = 1;
            subTotal = 0;
            numItems = 0;

            processButton.setText("Proccess Item #"+orderNumber);
            confirmButton.setText("Confirm Item #"+orderNumber);
            bookIDLabel.setText("Enter Book ID for Item #"+orderNumber+":");
            quantLabel.setText("Enter quantity for Item #"+orderNumber+":");
            itemInfoLabel.setText("Item #"+orderNumber+" info:");
            subtotalLabel.setText("Order subtotal for 0 item(s):");

            bookIDLabel.setVisible(true);
            quantLabel.setVisible(true);


            processButton.setEnabled(true);
            confirmButton.setEnabled(false);
            viewOrderButton.setEnabled(false);
            finishOrderButton.setEnabled(false);

            numOrdersTextField.setText("");
            numOrdersTextField.setEnabled(true);

            bookIDTextField.setText("");
            bookIDTextField.setEnabled(true);

            quantityTextField.setText("");
            quantityTextField.setEnabled(true);

            itemInfoTextField.setText("");
            subtotalTextField.setText("");

            numOrdersTextField.requestFocusInWindow();
        }
    }
    private class finishOrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create new invoice based on the final orders arrayList
            Invoice invoice = new Invoice(orders);
            JOptionPane.showMessageDialog(frame,invoice.buildInvoice(),"Message",JOptionPane.INFORMATION_MESSAGE);
            try {
                // build the invoice and append to transactions.txt
                invoice.exportInvoice();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            finishOrderButton.setEnabled(false);
        }
    }
    private class viewOrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Loop through orders, build a string, and display to user
            int i = 1;
            String ordersString = "";

            for(Order o : orders){
                ordersString += i+". " + o.orderString + "\n";
                i++;
            }

            JOptionPane.showMessageDialog(frame,ordersString,"Message",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private class confirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DecimalFormat df = new DecimalFormat(".##");
            String isbn = bookIDTextField.getText();
            // Find book
            Book book = findBook(isbn);
            int quant = Integer.parseInt(quantityTextField.getText());
            double discount = findDiscount(quant);
            double price = 0;
            double finalPrice = 0;

            // Ensures book exists before letting the user confirm
            if (book != null) {

                numItems++;
                price = book.price*quant;
                if (discount > 0 ) {
                    finalPrice = price - (price*discount);
                }
                else {
                    finalPrice = price;
                }
                // Add book order to the orders arrayList
                orders.add(new Order(book,quant,finalPrice,discount));
                JOptionPane.showMessageDialog(frame,"Item #"+orderNumber+" accepted","Message",JOptionPane.INFORMATION_MESSAGE);
                updateOrderNumber();
                if (orderNumber <= Integer.parseInt(numOrdersTextField.getText()) ) {
                    processButton.setEnabled(true);
                    confirmButton.setEnabled(false);
                }

            }
            else {
                // Displays message to user if book wasn't found
                JOptionPane.showMessageDialog(frame,"Book ID "+ isbn +" not in file","Message",JOptionPane.INFORMATION_MESSAGE);
                processButton.setEnabled(true);
                confirmButton.setEnabled(false);
            }

            // Update subtotal label
            if (orderNumber <= Integer.parseInt(numOrdersTextField.getText()) ) {
                itemInfoTextField.setText("");
            }
            subTotal += finalPrice;
            if (subTotal > 0 ) {
                subtotalTextField.setText("$"+df.format(subTotal));
            }

            subtotalLabel.setText("Order subtotal for "+numItems+" item(s):");
            quantityTextField.setText("");
            bookIDTextField.setText("");

        }
    }

    private void updateOrderNumber() {
        // Update UI based on current orders
        orderNumber++;
        if (orderNumber <= Integer.parseInt(numOrdersTextField.getText())) {
            processButton.setText("Proccess Item #"+orderNumber);
            confirmButton.setText("Confirm Item #"+orderNumber);
            bookIDLabel.setText("Enter Book ID for Item #"+orderNumber+":");
            quantLabel.setText("Enter quantity for Item #"+orderNumber+":");
            itemInfoLabel.setText("Item #"+orderNumber+" info:");
        }

        // Completed orders
        if (orderNumber > Integer.parseInt(numOrdersTextField.getText()) ) {

            // Set enabled status on buttons
            processButton.setEnabled(false);
            confirmButton.setEnabled(false);
            viewOrderButton.setEnabled(true);
            finishOrderButton.setEnabled(true);

            // Set enabled status on textfields
            numOrdersTextField.setEnabled(false);
            bookIDTextField.setEnabled(false);
            itemInfoTextField.setEnabled(false);
            quantityTextField.setEnabled(false);
            subtotalTextField.setEnabled(false);

            // Empty text fields
            bookIDLabel.setVisible(false);
            quantLabel.setVisible(false);
            bookIDTextField.setText("");
            quantityTextField.setText("");

            viewOrderButton.requestFocusInWindow();
        }
    }

    private double findDiscount(int quant) {
        // Discount
        if (quant <= 4) {
            return 0;
        }
        else if (quant <= 9) {
            return 0.10;
        }
        else if (quant <= 14) {
            return 0.15;
        }
        else {
            return 0.20;
        }
    }

    private class processOrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // Change button active state
            processButton.setEnabled(false);
            confirmButton.setEnabled(true);
            numOrdersTextField.setEnabled(false);

            // Set up variables
            Book book = null;
            String itemInfo;
            DecimalFormat finalPrice = new DecimalFormat(".##");
            int quant = Integer.parseInt(quantityTextField.getText());
            String isbn = bookIDTextField.getText();
            book = findBook(isbn);
            DecimalFormat discount = new DecimalFormat("##");
            double finalPrice2 = 0;

            // Build item info string
            if (book != null) {

                if (findDiscount(quant) > 0 ) {
                    finalPrice2 = (book.price*quant) - (book.price*quant*findDiscount(quant));
                }
                else {
                    finalPrice2 = book.price*quant;
                }

                itemInfo = book.isbn + " " + book.title + " $"+ book.price + " "+quant + " "+ discount.format(findDiscount(quant)*100)+"%" + " $"+finalPrice.format(finalPrice2);
                itemInfoTextField.setText(itemInfo);
            }
            else {
                itemInfo = "Book "+isbn+" was not found in file";
                itemInfoTextField.setText(itemInfo);
            }


        }
    }

    public static void buildInventory() {
        // Go through inventory.txt and build out an inventory arrayList of books
        try {
            String str;
            BufferedReader br = new BufferedReader(new FileReader("inventory.txt"));
            while ((str = br.readLine()) != null) {
                String[] strArray = str.split(",");
                Book book = new Book(strArray[0],strArray[1],Double.parseDouble(strArray[2].trim()));
                inventory.add(book);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Book findBook(String isbn) {
        // Given an isbn find a given book in an ArrayList
        Book book = null;
        boolean found = false;
        for(Book b : inventory){
            if(b.isbn != null && b.isbn.contains(isbn)) {
                book = b;
                found = true;
            }
        }

        if (found) {
            return book;
        }
        else {
            return null;
        }
    }

}
