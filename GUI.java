/* Joshua Gumprecht
Dr. Llewellyn
Spring 2023
Project 1
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.io.*;
import java.util.Date;
import java.io.FileWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.*;

public class GUI implements ActionListener {
    private static final int WINDOW_WIDTH = 410; // pixels
    private static final int WINDOW_HEIGHT = 235;

    private static final FlowLayout LAYOUT_STYLE = new FlowLayout();

    String[] id = new String[100];
    String[] item = new String[100];
    String[] inStock = new String[100];
    String[] price = new String[100];
    String[][] inventory = new String[100][6];
    String[][] cart = new String[100][6];

    int i = 0;

    int discount = 0;

    Double orderTotal = 0.0;

    private int itemCount = 1;
    private JFrame frame;
    private JPanel panel;
    private JLabel label;

    private JButton button1, button2, button3, button4, button5, button6;

    private JLabel itemIDPane, quantityPane, detailsPane, pricePane;

    private JTextField itemIDText, detailsText, quantityText, priceText;

    public GUI() throws FileNotFoundException {

        readFile();

        frame = new JFrame();

        button1 = new JButton("Find Item #" + itemCount);
        button2 = new JButton("Purchase Item #" + itemCount);
        button3 = new JButton("View Current Order");
        button4 = new JButton("Complete Order - Checkout");
        button5 = new JButton("Start New Order");
        button6 = new JButton("Exit(Close App)");

        itemIDPane = new JLabel("Enter quantity ID for Item #" + itemCount + ":");
        itemIDText = new JTextField(20);

        quantityPane = new JLabel("Enter quantity for item #" + itemCount + ":");
        quantityText = new JTextField(20);

        detailsPane = new JLabel("Details for item #" + itemCount + ":");
        detailsText = new JTextField(20);

        pricePane = new JLabel("Order subtotal for " + (itemCount - 1) + " item(s)");
        priceText = new JTextField(20);

        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();

        // configure GUI
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);

        // add components to the container
        Container c = frame.getContentPane();
        c.setLayout(LAYOUT_STYLE);

        c.add(itemIDPane);
        c.add(itemIDText);
        c.add(quantityPane);
        c.add(quantityText);
        c.add(detailsPane);
        c.add(detailsText);
        c.add(pricePane);
        c.add(priceText);
        c.add(button1);
        button1.addActionListener(this);
        c.add(button2);
        button2.addActionListener(this);
        c.add(button3);
        button3.addActionListener(this);
        c.add(button4);
        button4.addActionListener(this);
        c.add(button5);
        button5.addActionListener(this);
        c.add(button6);
        button6.addActionListener(this);

        // display GUI
        frame.show();
    }

    public void readFile() throws FileNotFoundException {

        int j = 0;

        File f = new File("C:/Users/wm131/OneDrive/Documents/ucf stuff/CNT/inventory.txt");

        Scanner sc = new Scanner(f);

        while (sc.hasNext()) {

            String str = sc.nextLine(); // reads individual string

            Scanner sc1 = new Scanner(str);

            sc1.useDelimiter(",");

            id[j] = sc1.next();

            item[j] = sc1.next();

            inStock[j] = sc1.next();

            // price[i] = sc1.nextFloat();

            // sc2.useDelimiter(",");

            price[j] = sc1.next();

            j++;

        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        GUI myGUI = new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        if (arg0.getSource() == button1) {

            String box1 = itemIDText.getText();
            String box2 = quantityText.getText();
            int x = Integer.parseInt(box2);

            for (int j = 0; j < 42; j++) {
                if (id[j].equals(box1)) {
                    String s = " false";

                    if (s.equals(inStock[j])) {
                        JOptionPane.showMessageDialog(null,
                                "Sorry...that item is out of stock, please try another item", "ERROR", ERROR_MESSAGE);
                        button1.setEnabled(true);
                        button2.setEnabled(false);
                        return;
                    }

                    if (x >= 1 && x <= 4) {
                        discount = 0;
                    } else if (x >= 5 && x <= 9) {
                        discount = 10;
                    } else if (x >= 10 && x <= 14) {
                        discount = 15;
                    } else {
                        discount = 20;
                    }
                    Double val1 = Double.parseDouble(price[j]);
                    int val2 = Integer.parseInt(box2);

                    Double d = discount * 0.01;

                    Double t = val1 * val2 * (1 - d);

                    Double rounded = Math.round(t * 100.0) / 100.0;

                    String output = (id[j] + " " + item[j] + " " + "$" + val1 + " " + val2 + " "
                            + discount + "%" + " " + rounded);

                    detailsText.setText(output);
                    button1.setEnabled(false);
                    button2.setEnabled(true);

                    inventory[i][0] = box1;
                    inventory[i][1] = item[j];
                    inventory[i][2] = price[j];
                    inventory[i][3] = box2;
                    inventory[i][4] = Integer.toString(discount);
                    inventory[i][5] = Double.toString(t);

                    i++;

                    return;
                }
            }
            JOptionPane.showMessageDialog(null,
                    "item ID " + box1 + " not in file", "ERROR", ERROR_MESSAGE);
        }

        if (arg0.getSource() == button2) {

            JOptionPane.showMessageDialog(null,
                    "Item #" + itemCount + " accepted. Added to your cart.", "Item Cofirmed", INFORMATION_MESSAGE);

            itemCount++;

            itemIDPane.setText("Enter quantity ID for Item #" + itemCount + ":");
            quantityPane.setText("Enter quantity for item #" + itemCount + ":");
            detailsPane.setText("Details for item #" + itemCount + ":");

            cart[i - 1][0] = inventory[i - 1][0];
            cart[i - 1][1] = inventory[i - 1][1];
            cart[i - 1][2] = inventory[i - 1][5];

            // System.out.print(cart[i - 1][2]);

            orderTotal += Double.parseDouble(cart[i - 1][2]);

            pricePane.setText("Order subtotal for " + (itemCount - 1) + " item(s)");
            priceText.setText("$" + Double.toString(orderTotal));

            button1.setEnabled(true);
            button2.setEnabled(false);
            button3.setEnabled(true);
            button4.setEnabled(true);

            itemIDText.setText("");
            quantityText.setText("");

        }

        if (arg0.getSource() == button3) {

            String output = "";

            for (int j = 0; j < i; j++) {
                output += ((j + 1) + ". " + inventory[j][0] + " " + inventory[j][1] + " $" + inventory[j][2] + " "
                        + inventory[j][3] + " " + inventory[j][4] + "% $" + inventory[j][5] + "\n");
            }
            JOptionPane.showMessageDialog(null, output, "Current Shopping Cart Status", INFORMATION_MESSAGE);
        }

        if (arg0.getSource() == button4) {
            String output = "";
            String currentDate = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss a z")
                    .format(Calendar.getInstance().getTime());
            // Date currentDate = new Date();
            // System.out.println(currentDate);
            for (int j = 0; j < i; j++) {
                output += ((j + 1) + ". " + inventory[j][0] + " " + inventory[j][1] + " $"
                        + inventory[j][2] + " "
                        + inventory[j][3] + " " + inventory[j][4] + "% $" + inventory[j][5] + "\n");
            }

            Double tax = orderTotal * 0.06;
            Double taxRoundoff = Math.round(tax * 100) / 100.0;

            Double tot = orderTotal + tax;
            Double totRoundoff = Math.round(tot * 100) / 100.0;

            JOptionPane.showMessageDialog(null,
                    ("Date: " + currentDate + "\n" + "Number of line items: " + i + "\n"
                            + "Item# / ID / Title / Price / Qty / Disc% / Subtotal:" + "\n" + output
                            + "Order subtotal: $" + orderTotal + "\n" + "Tax rate:  6%" + "\n" + "Tax amount:   $"
                            + taxRoundoff + "\n" + "ORDER TOTAL:    $" + totRoundoff + "\n"
                            + "Thanks for shopping at Nile Dot Com!"),
                    "FINAL INVOICE", INFORMATION_MESSAGE);
            itemIDText.setText("");
            itemIDText.setEditable(false);
            quantityText.setText("");
            quantityText.setEditable(false);

            String op = "";

            String cd = new SimpleDateFormat("ddMMyyyyhhmm")
                    .format(Calendar.getInstance().getTime());

            System.out.print(cd);

            for (int j = 0; j < i; j++) {
                op += (cd + ", " + inventory[j][0] + ", " + inventory[j][1] + ", " + inventory[j][2]
                        + ", " + inventory[j][3]
                        + ", " + "0." + discount + ", " + totRoundoff + ", " + currentDate + "\n");
            }
            // System.out.println(op);
            Path fileName = Path.of("C:/Users/wm131/OneDrive/Documents/ucf stuff/CNT/transactions.txt");

            FileWriter fw = null;
            try {
                fw = new FileWriter("C:/Users/wm131/OneDrive/Documents/ucf stuff/CNT/transactions.txt", true);
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fw.write(op);
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }

            button5.setEnabled(true);
        }
        if (arg0.getSource() == button5) {
            itemCount = 1;
            i = 0;
            itemIDPane.setText("Enter quantity ID for Item #" + itemCount + ":");
            quantityPane.setText("Enter quantity for item #" + itemCount + ":");
            detailsPane.setText("Details for item #" + itemCount + ":");
            button1.setEnabled(true);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            detailsText.setText("");
            priceText.setText("");
            itemIDText.setEditable(true);
            quantityText.setEditable(true);
            orderTotal = 0.0;
            discount = 0;

        }

        if (arg0.getSource() == button6) {
            System.exit(0);

        }
    }
}
