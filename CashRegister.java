//Importing Modules
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CashRegister implements ActionListener {

    JFrame frame;
    JPanel startScreen, cartDisplay, paymentScreen, folder;
    CardLayout card;
    Database data = new Database();
    int totalPrice = 0;
    JButton start, cancel, checkout, end, cancel2, ProductRemove, ProductAdd, back;
    JLabel product, priceofProduct, total, subtotal, hst, finalTotal;
    JTextField code;
    JRadioButton cash, cards;
    ButtonGroup pay = new ButtonGroup();

    //Array creation
    ArrayList<String> itemList = new ArrayList<String>();



    public CashRegister() {

        // Setting up JFrame
        frame = new JFrame("Cash Register GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminates program when JFrame is closed
        frame.setSize(500, 400);
        //-----------------------------------------

        // Screen1 - Start Session

        startScreen = new JPanel();
        startScreen.setLayout(null);
        startScreen.setBackground(Color.WHITE);

        start = new JButton("START");
        start.setBounds(150, 130, 200, 100);
        startScreen.add(start);
        //-----------------------------------------


        // Screen 2 - Adding Items

        cartDisplay = new JPanel();
        cartDisplay.setLayout(null);
        cartDisplay.setBackground(Color.WHITE);

        //Cart
        product = new JLabel("<html>CART ");
        product.setBounds(20, 20, 215, 220);
        cartDisplay.add(product);

        priceofProduct = new JLabel("<html> ");
        priceofProduct.setBounds(100, 20, 220, 220);
        cartDisplay.add(priceofProduct);

        //Subtotal Button
        total = new JLabel("SUBTOTAL: $0. 00");
        total.setBounds(130, 220, 115, 30);
        cartDisplay.add(total);

        code = new JTextField("");
        code.setBounds(20, 260, 210, 30);
        cartDisplay.add(code);

        //Add Item Button
        ProductAdd = new JButton("ADD");
        ProductAdd.setBounds(255, 260, 200, 30);
        ProductAdd.setBackground(Color.lightGray);
        cartDisplay.add(ProductAdd);

        //Remove Item Button
        ProductRemove = new JButton("REMOVE");
        ProductRemove.setBounds(255, 220, 200, 30);
        ProductRemove.setBackground(Color.RED);
        cartDisplay.add(ProductRemove);

        //Cancel Purchasing Session Button
        cancel = new JButton("CANCEL");
        cartDisplay.add(cancel);
        cancel.setBounds(255, 10, 200, 30);

        //Proceed to checkout Button
        checkout = new JButton("PROCEED TO CHECKOUT");
        cartDisplay.add(checkout);
        checkout.setBounds(20, 300, 448, 50);

        // Screen 3 - Payment and Termination
        paymentScreen = new JPanel();
        paymentScreen.setLayout(null);
        paymentScreen.setBackground(Color.WHITE);

        //Subtotal display
        subtotal = new JLabel("SUBTOTAL: $");
        subtotal.setBounds(120, 10, 245, 30);
        paymentScreen.add(subtotal);

        //HST Tax display
        hst = new JLabel("Hst: $");
        hst.setBounds(120, 90, 245, 30);
        paymentScreen.add(hst);

        //Total display
        finalTotal = new JLabel("Total: $");
        finalTotal.setBounds(120, 170, 245, 30);
        paymentScreen.add(finalTotal);

        //Back button
        back = new JButton("BACK");
        back.setBounds(20, 10, 90, 30);
        paymentScreen.add(back);

        //Confirm payment button
        end = new JButton("CONFIRM PAYMENT");
        end.setBounds(20, 300, 448, 50);
        paymentScreen.add(end);

        //Cancel Purchasing Session Button
        cancel2 = new JButton("CANCEL");
        cancel2.setBounds(380, 200, 85, 50);
        paymentScreen.add(cancel2);

        //Radio button for cash payment option
        cash = new JRadioButton("CASH");
        cash.setBounds(20, 260, 200, 30);

        //Radio button for credit or debit payment option
        cards = new JRadioButton("DEBIT/CREDIT");
        cards.setBounds(260, 260, 200, 30);

        //Allows user to choose a payment option to pay
        pay.add(cash);
        paymentScreen.add(cash);
        pay.add(cards);
        paymentScreen.add(cards);

        //Stores all 4 screens and provides an output layout

        folder = new JPanel();
        card = new CardLayout();
        folder.setLayout(card);
        folder.add(startScreen, "First");
        folder.add(cartDisplay, "Second");
        folder.add(paymentScreen, "Third");

        //---------------------
        // ActionListener allows user to interact with the GUI and get user action inputs

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                card.next(folder);
            }
        });
        //---------------------------------------------------------
        // Shows product information in cart when product name is entered
        ProductAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                scan("+");
            }
        });

        // Removes an item from cart
        ProductRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                scan("-");
            }
        });

        // Restarts screen 2 when cancel button has been pressed
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                restart();
            }
        });

        // Calculates price when confirm payment button is pressed
        checkout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                calculatePrices();
            }
        });

        // Restarts everything
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receipt s = new Receipt();
                if (cash.isSelected()) {
                    s.printReceipt(makeReceipt("cash"));
                    JOptionPane.showMessageDialog(frame, "PAYMENT ACCEPTED. PRINTING RECEIPT");
                    restart();

                } else if (cards.isSelected()) {
                    s.printReceipt(makeReceipt("credit"));
                    JOptionPane.showMessageDialog(frame, "PAYMENT ACCEPTED. PRINTING RECEIPT");
                    restart();
                }
            }
        });

        // Restarts
        cancel2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                restart();
            }
        });

        // Goes back from payment page to add product page
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                card.previous(folder);
            }
        });


        // displays screens to frame and allows it to appear on screen

        frame.add(folder);
        frame.setVisible(true);

    }



    // Methods


    // Displays name and price of product entered
    public void scan(String sign){
        String name = code.getText();
        String price = data.price(name);

        if (!price.equals("PRODUCT IS UNAVAILABLE"))
        {
            if (Objects.equals(sign, "+"))
            {
                String productList = product.getText() + "<br>" + name;
                String costList = priceofProduct.getText() + "<br>+ " + price + ".00" ;
                priceofProduct.setText(costList);
                product.setText(productList);
                itemList.add(name);

                totalPrice = totalPrice + Integer.parseInt(price.substring(1));
                total.setText("Total: $" + totalPrice + ".00");
            }
            else if (Objects.equals(sign, "-"))
            {
                if (itemList.remove(name))
                {
                    String productList = product.getText() + "<br>" + name;
                    String costList = priceofProduct.getText() + "<br>- " + price + ".00";
                    priceofProduct.setText(costList);
                    product.setText(productList);

                    totalPrice = totalPrice - Integer.parseInt(price.substring(1));
                    total.setText("Total: $" + totalPrice + ".00");
                }
                else
                {
                    JOptionPane.showMessageDialog(frame, "PRODUCT DOES NOT EXIST IN CART!");
                }
            }
        }
        else //If item is not found in the database
        {
            JOptionPane.showMessageDialog(frame, "PRODUCT IS UNAVAILABLE!");
        }

    }

    // Restarts everything and goes to the first screen
    public void restart(){
        product.setText("<html>Product: ");
        priceofProduct.setText("<html>Cost: ");
        total.setText("Subtotal: $0.00");
        code.setText("Enter Item Name");
        subtotal.setText("Total: $");
        hst.setText("Hst: $");
        finalTotal.setText("Total: $");
        totalPrice = 0;
        card.first(folder);
    }

    // Calculates subtotal, tax, and final price
    public void calculatePrices(){
        card.next(folder);
        subtotal.setText("Total: $" + totalPrice + ".00");
        hst.setText("HST: $" + ((float)totalPrice * 0.13));
        finalTotal.setText("Total: $" + ((float)totalPrice * 0.13 + totalPrice));
    }

    // Makes receipt and prints on the terminal
    public String makeReceipt(String type){
        Set<String> distinct = new HashSet<>(itemList);
        String receipt = "Paid by " + type + "\n";


        for (String s: distinct) {
            String price = data.price(s).substring(1);
            int qty = Collections.frequency(itemList, s);

            receipt += "Product: " + s + " ";
            receipt += "Quantity: " + qty + " ";
            receipt += "Cost: $" + (qty * Integer.parseInt(price)) + ".00\n";
        }

        
        receipt += "\nSubTotal: " + totalPrice + ".00";
        receipt += "\nHST: " + ((float)totalPrice * 0.13);
        receipt += "\nTotal: " + ((float)totalPrice * 0.13 + totalPrice);

        return receipt;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
