import java.util.*; 
 
public class CostCalculator { 
  public static final Scanner CONSOLE = new Scanner(System.in); 
  public static final double TAX_RATE = 0.0625; 
  public static void main(String[] args) { 
    System.out.println("Fall 2019 - CS1083 - Section 3 - Project 1 - written by CiciLegarde."); 
     
    int customerID; 
     
    System.out.println("Welcome to your preferred store"); 
     
    System.out.println("What's your customer ID?" );                       // begin customer ID conditionals 
    customerID = CONSOLE.nextInt(); 
    if (customerID == 1) { 
      System.out.println("Welcome Sarah!");                                // Sarah ID 
    } 
       else if (customerID == 2) { 
         System.out.println("Welcome John!");                              // John ID 
    } 
       else if (customerID == 3) { 
         System.out.println("Welcome Matthew!");                           // Matthew ID 
    } 
       else if (customerID == 4) { 
         System.out.println("Welcome Michelle!");                          // Michelle ID 
    } 
       else if (customerID == 5) { 
         System.out.println("Welcome Ethan!");                             // Ethan ID 
    } 
    else { 
       System.out.println("No Customer ID on file."); 
    }                                                                      // end customer ID conditionals 
     
    System.out.println("How many items are you buying?");                  // numItems 
    int numItems = CONSOLE.nextInt(); 
     
    double endTotal = 0; 
    double taxTotal = 0; 
    for (int i = 1; i <= numItems; ++i) {                                  // begin item and subtotal calculation loop 
      System.out.println("Enter quantity of item " + i + ":");             // quantity? 
         double quantity = CONSOLE.nextDouble(); 
      System.out.println("Enter price of item " + i + ":");                // price? 
         double price = CONSOLE.nextDouble(); 
      System.out.println("Is item " + i + " taxable? 1 - Yes or 0 - No");  // taxable? 
         int taxable = CONSOLE.nextInt(); 
      double itemSubtotal; 
 
      if (taxable == 1) {                                                  // begin subtotal calculations 
         itemSubtotal = quantity * price; 
         endTotal = endTotal + itemSubtotal;                               // endTotal calc A 
         System.out.println("Item " + i + " subtotal is: " + itemSubtotal); 
         double itemTax = itemSubtotal * TAX_RATE;                         // itemTax calc 
         System.out.println("Item " + i + " tax: " + itemTax); 
         taxTotal = taxTotal + itemTax;                                    // taxTotal calc 
         } 
      if (taxable == 0){ 
        itemSubtotal = quantity * price; 
        endTotal = endTotal + itemSubtotal;                                // endTotal calc B 
        System.out.println("Item " + i + " subtotal is: " + itemSubtotal); 
      }                                                                    // end subtotal calculations 
      if ((taxable != 1) && (taxable != 0)) { 
      System.out.println("Invalid answer."); 
      } 
    }                                                                      // end item and subtotal calculation loop 
     
    System.out.println("------------------------------"); 
    System.out.println("Subtotal : " + endTotal);                          // Subtotal output 
     
    System.out.println("Tax : " + taxTotal);                               // Tax output 
     
    double total = endTotal + taxTotal; 
    System.out.println("Total : " + total);                                // Total output 
     
    System.out.println("Thank you for shopping with us!");                 // Thank you note   
  } 
}