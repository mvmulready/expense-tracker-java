/*

ExpenseTracker.java

Final Project!

Example of how it would run:

Welcome to Expense Tracker!
1. Add expense
2. View expenses
3. View total
4. Exit
Choose an option: 1
Enter expense description: Coffee
Enter amount: 3.50
Expense added!

*/

import java.util.Scanner;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// OOP
class Expense {
    String description;
    double amount;
    String dateTime; 

    Expense(String description, double amount, String dateTime) {
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
    }
}

public class ExpenseTracker {
	public static void main(String[] args) throws IOException {
        // input from keyboard
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to ExpenseTracker!");

        // array
        Expense[] expenses = new Expense[100]; // takes up to 100 expenses
        int count = 0; // counts expenses
        double startingBalance = 0; 

        // expenses.txt
            File file = new File("expenses.txt");
            if (file.exists()) {
                Scanner fileReader = new Scanner(file);
                if (fileReader.hasNextLine()) {
                    String firstLine = fileReader.nextLine();
                    startingBalance = Double.parseDouble(firstLine);
                }
                while (fileReader.hasNextLine()) {
                    String line = fileReader.nextLine();
                    String[] parts = line.split(",", 3); // description, amount, dateTime
                    if (parts.length == 3) {
                        String desc = parts[0];
                        double amt = Double.parseDouble(parts[1]);
                        String dateTime = parts[2];
                        expenses[count] = new Expense(desc, amt, dateTime);
                        count++;
                    }
                }
                fileReader.close();
                System.out.println("Loaded " + count + " previous expenses. Starting balance: $" + startingBalance);
            } else {
                System.out.print("Enter your starting balance: ");
                startingBalance = sc.nextDouble();
                sc.nextLine();
            }

        // while loop, continuously runs program
        while (true) {
            showMenu();
            int choice = sc.nextInt();
            sc.nextLine(); 

            // if else statements
            if (choice == 1) {
            // add expenses
                while (true) {
                    // display information on the screen
                    System.out.print("Enter expense description (or type 'done' to stop): ");
                    // string method
                    String desc = sc.nextLine();
                    if (desc.equalsIgnoreCase("done")) break;

                    System.out.print("Enter amount: ");
                    double amt = sc.nextDouble();
                    sc.nextLine();

                    // date and time
                    String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                    expenses[count] = new Expense(desc, amt, dateTime);
                    count++;

                    System.out.println("Expense added on " + dateTime + ". You now have " + count + " expenses.");
                }
                saveExpenses(expenses, count, startingBalance);
            } 
            else if (choice == 2) {
            // view expenses
                viewExpenses(expenses, count);
            } 
            else if (choice == 3) {
            // view total and statistics
                if (count == 0) {
                    System.out.println("No expenses yet.");
                } else {
                    double total = 0;
                    double highest = expenses[0].amount;
                    double lowest = expenses[0].amount;

                    // for loops
                    for (int i = 0; i < count; i++) {
                        double amt = expenses[i].amount;
                        total += amt;
                        if (amt > highest) highest = amt;
                        if (amt < lowest) lowest = amt;
                    }
                    
                    // math method
                    double roundedTotal = Math.round(total * 100.0) / 100.0;
                    double remaining = startingBalance - total;
                    double roundedRemaining = Math.round(remaining * 100.0) / 100.0;

                    System.out.println("\nTotal spent: $" + roundedTotal);
                    System.out.println("Highest expense: $" + highest);
                    System.out.println("Lowest expense: $" + lowest);
                    System.out.println("Remaining balance: $" + roundedRemaining);

                    if (remaining < 0) {
                        System.out.println("⚠ You are OVER your balance!");
                    }
                }
            } 
            else if (choice == 4) {
            // delete an expense
                if (count == 0) {
                    System.out.println("No expenses to delete.");
                } else {
                    viewExpenses(expenses, count);

                    System.out.print("Enter the number of the expense to delete: ");
                    int index = sc.nextInt();
                    sc.nextLine();

                    if (index < 1 || index > count) {
                        System.out.println("Invalid number.");
                    } else {
                        int pos = index - 1;
                        for (int i = pos; i < count - 1; i++) {
                            expenses[i] = expenses[i + 1];
                        }
                        expenses[count - 1] = null;
                        count--;
                        System.out.println("Expense deleted. You now have " + count + " expenses.");

                        saveExpenses(expenses, count, startingBalance);
                    }
                }
            } 
            else if (choice == 5) {
            // edit an expense
                if (count == 0) {
                    System.out.println("No expenses to edit.");
                } else {
                    viewExpenses(expenses, count);
                    System.out.print("Enter the number of the expense to edit: ");
                    int index = sc.nextInt();
                    sc.nextLine();

                    if (index < 1 || index > count) {
                        System.out.println("Invalid number.");
                    } else {
                        int pos = index - 1;
                        Expense e = expenses[pos];

                        System.out.print("Enter new description (leave blank to keep '" + e.description + "'): ");
                        String newDesc = sc.nextLine();
                        if (!newDesc.equals("")) {
                            e.description = newDesc;
                        }

                        System.out.print("Enter new amount (or -1 to keep " + e.amount + "): ");
                        double newAmt = sc.nextDouble();
                        sc.nextLine();
                        if (newAmt >= 0) {
                            e.amount = newAmt;
                        }
                        System.out.println("Expense updated.");

                        saveExpenses(expenses, count, startingBalance);
                    }
                }
            } 
            else if (choice == 6) {
            // exit
                System.out.println("Thank you for using ExpenseTracker!");
                saveExpenses(expenses, count, startingBalance);
                break;
            } 
            else {
            // invalid choice
                System.out.println("Invalid choice, please try again a number 1-6");
            }
        }
    }
    // methods
    public static void showMenu() {
        System.out.println("\nExpense Tracker");
        System.out.println("1. Add expenses");
        System.out.println("2. View expenses");
        System.out.println("3. View total + statistics");
        System.out.println("4. Delete an expense");
        System.out.println("5. Edit an expense");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    public static void viewExpenses(Expense[] expenses, int count) {
        if (count == 0) {
            System.out.println("No expenses yet.");
        } else {
            System.out.println("\nExpenses:");
            for (int i = 0; i<count; i++) {
                System.out.println((i + 1) + ". "+ expenses[i].description.toUpperCase()+ " - $" + expenses[i].amount+ " | Added: " + expenses[i].dateTime);
            }
            System.out.println("Total number of expenses: "+count);
        }
    }

    public static void saveExpenses(Expense[] expenses, int count, double startingBalance) throws IOException {
        PrintWriter writer = new PrintWriter("expenses.txt");
        writer.println(startingBalance);
        for (int i = 0; i < count; i++) {
            writer.println(expenses[i].description+","+ expenses[i].amount+","+expenses[i].dateTime);
        }
        writer.close();
    }
}