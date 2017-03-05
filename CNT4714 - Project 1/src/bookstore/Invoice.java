/*
 * Name: Wenyang Wu
 * Course: CNT 4714 - Spring 2017
 * Assignment Title: Program 1 - Event-driven Programming
 * Date: Sunday January 29, 2016
 */
package bookstore;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class Invoice {
    Date date = null;
    int numProducts = 0;
    ArrayList<Order> orders = new ArrayList<>();
    // subTotal is before tax
    double subTotal = 0.00;
    double taxRate = .06;
    double taxAmount = 0;
    // orderTotal is after tax
    double orderTotal = 0;
    String invoiceString = "";
    String endMessage = "Thanks for shopping at the Ye Olde Book Shoppe!";
    String transactionDate = "";

    public Invoice(ArrayList<Order> orders) {
        // Create an invoice object
        this.orders = orders;
        SimpleDateFormat ft = new SimpleDateFormat ("yyMMddhhmmss");
        this.date = new Date();
        this.transactionDate = ft.format(this.date);
        this.numProducts = orders.size();

        for(Order o : this.orders){
            this.subTotal += o.subTotal;
        }
        this.taxAmount = this.taxRate*this.subTotal;
        this.orderTotal = this.subTotal + this.taxAmount;
    }

    public String buildInvoice() {
        // Create the actual final invoice alert box message
        DecimalFormat df = new DecimalFormat("##.##");
        DecimalFormat df2 = new DecimalFormat("#");
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yy hh:mm:ss a z");
        invoiceString = "Date: "+ft.format(this.date)+"\n\n"+
                        "Number of line items: "+this.numProducts+"\n\n"+
                        "Item# / ID / Title / Price / QTY / Disc% / Subtotal:"+"\n\n"+
                        buildOrderString()+"\n\n"+
                        "Order subtotal: $"+df.format(this.subTotal)+"\n\n"+
                        "Tax Rate: "+df2.format(this.taxRate*100)+"%\n\n"+
                        "Tax Amount: $"+df.format(this.taxAmount)+"\n\n"+
                        "Order total: $"+df.format(this.orderTotal)+"\n\n"+
                        this.endMessage;
        return invoiceString;
    }

    public String buildOrderString() {
        // Create the order sting for use in the final invoice message
        int i = 1;
        String ordersString = "";

        for(Order o : this.orders){
            ordersString += i+". " + o.orderString + "\n";
            i++;
        }
        return ordersString;
    }

    public void exportInvoice() throws FileNotFoundException, UnsupportedEncodingException {
        // Append to the transactions.txt file
        // If the transactions file doesn't exist make a new file, else append to it
        String line;
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yy hh:mm:ss a z");

        try {
            File file =new File("transactions.txt");

            if(!file.exists()){
                file.createNewFile();
                System.out.println("file didn't exist creating....");
            }

            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            for(Order o : this.orders){
                line = this.transactionDate+", "+o.invoiceString+", "+ft.format(this.date)+System.getProperty("line.separator");
                bw.write(line);
            }
            bw.close();

        } catch(IOException ioe){
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
    }

}
