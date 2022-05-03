import java.awt.*;
public class SupermarketDynamicWindow {
  // Class constants related to the window and drawings
  public static final int MASTER_WIDTH = 700;
  public static final int MASTER_HEIGHT = 500;
  public static final int FRAME_WIDTH = 100;
  public static final int FRAME_HEIGHT = 50;
  public static final int MARGIN = 50;
  public static final int MAX_COLUMNS = 5;
  
  // Class constants related to the transaction
  public static double MINOR_DISCOUNT = 0.10;
  public static double SENIOR_DISCOUNT = 0.20;
  public static double TAX_RATE = 0.0625;
  
  
  // Class variables/constants related to the cart
  public static final int MAX_ITEMS = 5;
  public static int cartSize = 0;
  
  // Declare two static arrays of size MAX_ITEMS to represent the shopping cart
  public static int[] cartItem = new int[MAX_ITEMS];
  public static double[] cartQty = new double[MAX_ITEMS];  
  
  // Array of possible actions
  public static final String[] actions = {"Add to cart", "Remove from cart",
    "Finish transaction",
    "Cancel transaction"};
  
  // Array of numbers used by DynamicWindow for asking for quantity and cash amount
  public static final String[] numbers = {"1", "2", "3", "4", "5", "6", "7",
    "8", "9", "0", "."};
  
  // Arrays of customer information (names and ages) Indexes are the "customerID"
  public static String customerName[] = {"Aaron", "Priscilla", "Marty", "John",
    "Bob", "Alicia", "Eve", "Joseph",
    "Michael", "Donald"};
  public static int customerAge[] =     {     18,          15,      42,     25,
    29,       75,    15,       38,
    62,       68};    
  
  // Arrays of item information (description, price, tax) Indexes are the "itemID"
  public static String  itemDesc[]    = {"Beans", "Rice", "Banana", "Ice", "Tea",
    "Bread", "Orange", "Sugar", "Cookies",
    "Paper", "Coffee", "Water"};
  public static double  itemPrice[]   = {  3.25,    4.31,     6.88,   3.3,  5.25,  
    4.89,     6.32,     2.25,      3.18,
    1.25,     8.00,     1.0};
  public static Boolean itemTax[]     = { false,  false,     false,  true,  true,  
    false,    false,     true,      true,    
    true,     true,   false};
  
  public static void main(String[] args){
    if (getDecision("Yes", "No", "Do you want to start a transaction?").equals("Yes"))
      genTransaction(); 
  }
  
  public static void genTransaction(){
    // Declare here your variables
    int action;
    int item;
    double quantity;
    int cartSize = 0;
    double grandTotal = 0.0;
    double discount = 0.0;
    double tax = 0.0;
    double customerDiscount = 0.00;
    double subTotal = 0.0;
    int itemCart = 0;
    double cash = 0.0;
    
    
    // Declare variables for Panel and Graphics for the background window, which
    // will show the total cost at the bottom right
    DrawingPanel masterP = new DrawingPanel(MASTER_WIDTH, MASTER_HEIGHT);
    Graphics masterG = masterP.getGraphics();
    masterP.setBackground(Color.WHITE);
    masterG.setColor(Color.BLACK);
    masterG.drawString("Fall 2019 - CS1083 - Section 003 - Project 3 - written by Cici Legarde.",
                       MARGIN, masterP.getHeight() - MARGIN * 2);

    // Show total cost with value already initialized in the variable
    showTotal(masterG, grandTotal);
    
    double userCustomer = getValueList(customerName, "Customers", true);
    // Store in a variable the real discount to be applied
    // according to the customer's age
    int customerID = (int) userCustomer;
    
    if (customerAge[customerID] < 19) {
      customerDiscount = MINOR_DISCOUNT;
    }
    else{
      if (customerAge[customerID] > 59) {
        customerDiscount = SENIOR_DISCOUNT;
      }
      else {
        customerDiscount = 0;
      }
    }
    masterG.drawString("Welcome " + customerName[customerID] + "!  Discount: " + customerDiscount,
                       50, 485);
    
    // Start a loop that will run at least once and if the action selected
    // by the user is not to Finish Transaction
    // Get the action from the user, based on the array of actions,
    double userAction = getValueList(actions, "Actions", true);
    // Convert the value returned to an int
    action = (int) userAction;
    // for debugging
    do{
      System.out.println("cartSize: " + cartSize);
      switch (action){
        case 0: // When the action is Add to cart
          masterG.drawString("Add To Cart: ",
                             MARGIN * 12, 400);
          // Verifies if there is space in the cart
          if(isCartFull() == false){
            
            // Get the item from the user, based on the array of Item Description
            // Convert the value returned to an int
            double userItem = getValueList(itemDesc, "Items", false);
            item = (int) userItem;
            System.out.println("Item added: " + itemDesc[item]);
            // Get the quantity from the user, based on the array of numbers,
            // note that the last parameter in the method call will be 1
            double userQty = getValueList(numbers, "Quantity", false);
            
            // Tries to add to the cart
            addToCart(item, userQty);
            masterG.setColor(Color.WHITE);
            masterG.drawString("Add To Cart: ",
                               MARGIN * 12, 400);
            masterG.setColor(Color.BLACK);
            
            // Accumulate the values of subtotal, discount, tax. The tax should
            subTotal = subTotal + itemPrice[item];
            discount = discount + customerDiscount * subTotal;
            // only accumulate if that item is taxable
            if (itemTax[item] == true){
              tax = tax + TAX_RATE * subTotal;
            }
            masterG.setColor(Color.WHITE);
            showTotal(masterG, grandTotal);
            masterG.setColor(Color.BLACK);
            grandTotal = subTotal + tax - discount;
            // Calculate the grand total and display it, calling the
            // corresponding method
            
            showTotal(masterG, grandTotal);
            
            System.out.println("GrandTotal: " + grandTotal);
            if (cartSize <= MAX_ITEMS){
              ++cartSize;
            }
          }
          break;
        case 1:  // When the action selected is Remove from cart
          
          // Verify if the cart is not empty
          if(isCartEmpty() == false){
          // For that, it has to get the itermNames of the items in the cart
          String [] cartItemNames = getCartItemNames(itemDesc);
          // Show the user the names of the items in the cart and get the index
          for(int i = 0; i < cartSize; ++i) {
            System.out.print(cartItemNames[i]);
          }
          // for the item the user wants to remove
          double userCartItem = getValueList(cartItemNames, "Remove: ", false);
          System.out.println("userCartItem = " + userCartItem);
          itemCart = (int) userCartItem;
          System.out.println("itemCart = " + itemCart);
          System.out.println("Item To Remove: " + itemDesc[itemCart]);
          // Using the index to find the itemID and quantity
          // Update the subtotal, discount, and tax
          // Pay special attention if the item is taxable or not
          item = getItemAtCart(itemCart);
          subTotal = subTotal - cartQty[item] * itemPrice[item];
          discount = discount - customerDiscount * subTotal;
          // only accumulate if that item is taxable
          if (itemTax[item] == true){
            tax = tax - TAX_RATE * subTotal;
          }
          removeItemCart(item);
          masterG.setColor(Color.WHITE);
          showTotal(masterG, grandTotal);
          masterG.setColor(Color.BLACK);
          
          // Update the grand total and display it in panel by calling appropriate method
          grandTotal = subTotal + tax - discount;
          showTotal(masterG, grandTotal);
        }
          else{// Otherwise, when the cart is empty
            
            // Show a message that the cart is empty
            showMessage("Cart is empty!");
            
            // Clear the variables used for accumulation
            subTotal = 0;
            tax = 0;
            discount = 0;
            grandTotal = 0;
            masterG.setColor(Color.WHITE);
            showTotal(masterG, grandTotal);
            masterG.setColor(Color.BLACK);
          }
          break;
        case 2: // When the action selected is to finish the transaction
          masterG.drawString("Finish Transaction: ",
                             MARGIN * 12, 400);
          // Prompt the user for a cash value (the pay from the user)
          do{
            showMessage("Input Cash Value: ");
            // Make sure this amount is sufficient to cover the grand total of the transaction.
            // Ask for the cas value using the numbers array
            cash = getValueList(numbers, "Cash", true);
            
          }while (cash < grandTotal);
          
          // When the value is greater or equal to the Total
          double change = cash - grandTotal;
          // Display the change (difference between
          // the cash and the total)
          showValue("Change: " + change, masterP, masterG);
          // Close the Master window
          masterP.close();
          break;
        case 3:  // When the action selected is Cancel transaction
          masterG.drawString("Cancel Transaction: ",
                             MARGIN * 12, 400);
          // Clear the cart
          clearCart();
          // Clear accumulative values
          subTotal = 0;
          tax = 0;
          discount = 0;
          // Update the grand total in the master window
          masterG.setColor(Color.WHITE);
          showTotal(masterG, grandTotal);
          masterG.setColor(Color.BLACK);
          
          grandTotal = 0;
          showTotal(masterG, grandTotal);
          // Show a message that the cart is empty
          showMessage("The cart is empty!");
          break;
        default:
          showMessage("Cancel Transaction: There is nothing here!");
          showTotal(masterG, grandTotal);
          break;
      }
      userAction = getValueList(actions, "Actions", true);
      action = (int) userAction;
      // for debugging
      
    } while (action != 2);
    if (action == 2){
      // Prompt the user for a cash value (the pay from the user)
      do{
        showTotal(masterG, grandTotal);
        // Make sure this amount is sufficient to cover the grand total of the transaction.
        // Ask for the cas value using the numbers array
        cash = getValueList(numbers, "Cash", true);
        
      }while (cash < grandTotal);
      
      // When the value is greater or equal to the Total
      double change = cash - grandTotal;
      // Display the change (difference between
      // the cash and the total)
      showValue("Change: " + change, masterP, masterG);
      // Close the Master window
      masterP.sleep(5000);
      masterP.close();
    }
  }
  
  /**
   * This method gets a decision  ("yes" or "no") from the user. It also serves
   * as an example of what the getValueList method should look like.
   *
   * @param  option1     The option 1 "Yes"
   * @param  option2     The option 2 "No"
   * @param  title       The title to display centered in the window
   * @return             The string the user selected in the two options selected
   */
  public static String getDecision(String option1, String option2, String title){
    
    // Declares the variables used
    String ret = "";
    int row = -1;
    int col = -1;
    int index = -1;
    int x = -1;
    int y = -1;
    int pX = -2;
    int pY = -2;
    
    // Create the window with sufficient rectangles to list the options (in this case only "Yes" or "no")
    DrawingPanel dP = new DrawingPanel(MARGIN * 2 + FRAME_WIDTH * 2, // Because of two options
                                       MARGIN * 2 + FRAME_HEIGHT);   // Only one row
    Graphics dG = dP.getGraphics();
    
    // Display the title centered in the window (DrawingPanel)
    dG.drawString(title,
                  (dP.getWidth() / 2) - ((title.length()/3) * 10),
                  MARGIN / 2);
    
    dG.setColor(Color.BLACK);
    dG.fillRect(MARGIN, MARGIN, FRAME_WIDTH, FRAME_HEIGHT); // For option 1
    dG.fillRect(MARGIN + FRAME_WIDTH, MARGIN, FRAME_WIDTH, FRAME_HEIGHT); // For option 2
    dG.setColor(Color.LIGHT_GRAY);
    dG.fillRect(MARGIN + 1, MARGIN + 1, FRAME_WIDTH-2, FRAME_HEIGHT - 2); // For option 1
    dG.fillRect(MARGIN + FRAME_WIDTH + 1, MARGIN + 1, FRAME_WIDTH-2, FRAME_HEIGHT - 2); // For option 2
    
    // Display the two options inside the frames
    dG.setColor(Color.BLACK);
    dG.drawString(option1, MARGIN + 2, 80);
    dG.drawString(option2, MARGIN + FRAME_WIDTH + 2, 80);
    
    // Cycle for "waiting" until the user selects one of the options
    while (true){
      // Example method calls to get the row and the column
      row = getClickRowExample(dP, "yes", "no"); // When coding your own method take out the "Example" portion
      col = getClickColExample(dP, "yes", "no"); // When coding your own method take out the "Example" portion
      x = dP.getClickX();
      y = dP.getClickY();
      while ((row < 0 || col < 0) && (!(x == pX && y == pY))) {
        row = getClickRowExample(dP, "yes", "no");
        col = getClickColExample(dP, "yes", "no");
        x = dP.getClickX();
        y = dP.getClickY();
      }    
      // If there has been a change in row or column as compared to the previous row and column
      if (!((x == pX) && (y == pY))){
        // Record previous X and previous Y
        pX = x;
        pY = y;          
        // Calculates the index selected based on the row and column
        index = row + col; // Here the row will be multiplied by the number
        // of MAX_COLUMNS to know the real value
        if ((index == 0)){
          ret = option1;
        }
        if ((index == 1)){
          ret = option2;
        }
        // Note that in the real method, you will return the index, not the value
        break; 
      }
    }
    // Closes the current window (Drawing Panel) and return the value
    dP.close();
    return ret;
  }
  
  public static double getValueList(String[] list, String title, boolean returnInt){
    // Declares the variables used
    String ret = "";
    int row = -1;
    int col = -1;
    int index = -1;
    int x = -1;
    int y = -1;
    int pX = -2;
    int pY = -2;
    int windowx = MAX_COLUMNS % list.length;
    int windowy = list.length/MAX_COLUMNS;
    
    if ((list.length>=MAX_COLUMNS)){ // won't subtract from MAX_COLUMNS
      windowx = 0;
    }
    DrawingPanel dP = new DrawingPanel(MARGIN * 2 + FRAME_WIDTH * (MAX_COLUMNS - (windowx)),              
                                       MARGIN * 2 + FRAME_HEIGHT + FRAME_HEIGHT * windowy);  
    Graphics dG = dP.getGraphics();
    // Display the two options inside the frames
    dG.setColor(Color.BLACK);
    dG.drawString(title, (dP.getWidth() /2) - ((title.length () /3) * 10), MARGIN /2);
    drawOptions(list, dP, dG, returnInt);
    
    // Cycle for "waiting" until the user selects one of the options
    while (OkClicked(dP) == false){
      row = getClickRow(dP, list);
      col = getClickCol(dP, list);
      x = dP.getClickX();
      y = dP.getClickY();
      // Keep looking for a click inside the rectangles of the two options
      while ((row < 0 || col < 0) && (!(x == pX && y == pY))) {
        row = getClickRow(dP, list);
        col = getClickCol(dP, list);
        x = dP.getClickX();
        y = dP.getClickY();
        if (OkClicked(dP) == true){
          dP.close();
          break;
        }
      }    
      // If there has been a change in row or column as compared to the previous row and column
      if (!((x == pX) && (y == pY))){
        pX = x;
        pY = y;          
        // Calculates the index selected based on the row and column
        index = row * MAX_COLUMNS + col; // Here the row will be multiplied by the number of MAX_COLUMNS to know the real value
        break;
        
      }
    }
    dP.close();
    return index;
  }
  
  public static int getClickColExample(DrawingPanel panel, String option1, String option2) {
    //declare variables
    int x = panel.getLastClickX();
    int y = panel.getLastClickY();
    
    // In the other method you will need to user a logic that includes every
    // option based on having a maximum of MAX_COLUMNS columns per row
    if ((x > MARGIN && x < MARGIN + FRAME_WIDTH) &&       // First Column
        (y > MARGIN && y < MARGIN + FRAME_HEIGHT)) {      // First Row
      return 0; // dimensions of the first column, first row
    }
    else if ((x > MARGIN + FRAME_WIDTH && x < MARGIN + FRAME_WIDTH * 2) &&   // Second Column
             (y > MARGIN && y < MARGIN + FRAME_HEIGHT)) {                         // First Row
      return 1; // dimensions of the second column, first row
    }
    else {
      return -1;
    }
  }
  
  public static int getClickRowExample(DrawingPanel panel, String option1, String option2) {
    int x = panel.getLastClickX();
    int y = panel.getLastClickY();
    if ((x > MARGIN && x < MARGIN + FRAME_WIDTH * 2) && // Since there are two columns
        (y > MARGIN && y < MARGIN + FRAME_HEIGHT)) {    
      return 0; // tells the program your clicker is on the first row
    }
    else {
      return -1; // tells the program you aren't in the grey box area
    }
  }
  
  public static int getClickRow(DrawingPanel panel, String[] list) {
    //declare variables
    int x = panel.getLastClickX();
    int y = panel.getLastClickY();
    int col = (x - MARGIN) / FRAME_WIDTH;
    int row = (y - MARGIN) / FRAME_HEIGHT;
    int rows = list.length / 5;
    int cols = list.length % 5;
    
    if (!((col == (panel.getLastClickX() - MARGIN) / FRAME_WIDTH) &&
          (row == (panel.getLastClickY() - MARGIN) / FRAME_HEIGHT))){
      return -1;
    }
    if ((row >= rows) && (col > cols - 1)){
      return -1;
    }
    if ((row < rows) && (col > 4)){
      return -1;
    }
    if (((x < MARGIN) || (x > (panel.getWidth() - MARGIN)))) {
      return -1; // tells the program your clicker was outside the options
    }
    if ((y < MARGIN) || ( y > panel.getHeight() - MARGIN)){
      return -1;
    }
    return row;
  }  
  
  public static int getClickCol(DrawingPanel panel, String[] list) {
    //declares variables
    int x = panel.getLastClickX();
    int y = panel.getLastClickY();
    int col = (x - MARGIN) / FRAME_WIDTH;
    int row = (y - MARGIN) / FRAME_HEIGHT;
    int rows = list.length / 5;
    int cols = list.length % 5;
    
    if (!((col == (panel.getLastClickX() - MARGIN) / FRAME_WIDTH) &&
          (row == (panel.getLastClickY() - MARGIN) / FRAME_HEIGHT))){
      return -1;
    }
    if ((row >= rows) && (col > cols - 1)){
      return -1;
    }
    if ((row < rows) && (col > 4)){
      return -1;
    }
    if (((x < MARGIN) || (x > (panel.getWidth() - MARGIN)))) {
      return -1; // tells the program your clicker was outside the options
    }
    
    if ((y < MARGIN) || ( y > panel.getHeight() - MARGIN)){
      return -1;
    }
    return col;
  }
  
  
  public static void drawOptions(String[] list, DrawingPanel p, Graphics vG, boolean returnInt){
    int x = 0;
    int y = 0;
    for (int i = 0; i < list.length; i++){
      // makes new row
      if (i != 0 && i % MAX_COLUMNS == 0){
        x = 0;
        y+= MARGIN;
      }
      vG.setColor(Color.BLACK);
      vG.fillRect(MARGIN + (FRAME_WIDTH * x), MARGIN + y, FRAME_WIDTH, FRAME_HEIGHT);
      vG.setColor(Color.LIGHT_GRAY);
      vG.fillRect(MARGIN + (FRAME_WIDTH * x) + 1, MARGIN +y+ 1, FRAME_WIDTH-2, FRAME_HEIGHT-2);
      //text
      vG.setColor(Color.BLACK);
      vG.drawString(list[i], MARGIN + (FRAME_WIDTH * x) + 2, FRAME_HEIGHT + y + FRAME_HEIGHT/2);
      x ++;
    }
    if (returnInt == false){
      vG.setColor(Color.BLACK);
      vG.fillRect(p.getWidth() - MARGIN*2, p.getHeight() - MARGIN, FRAME_HEIGHT, FRAME_HEIGHT);
      vG.setColor(Color.YELLOW);
      vG.fillRect((p.getWidth() - MARGIN*2)+1, (p.getHeight() - MARGIN) +1, FRAME_HEIGHT-2, FRAME_HEIGHT-2);
      vG.setColor(Color.BLACK);
      vG.drawString("OK", p.getWidth() - (MARGIN*2/3 + MARGIN), p.getHeight() - (MARGIN/3));
    }  
  }
  
  public static void showValue(String str, DrawingPanel p, Graphics g){
    g.drawString(str, 400, 350);
  }
  
  public static void showMessage(String str){
    DrawingPanel p = new DrawingPanel(300, 150);
    Graphics g = p.getGraphics();
    p.setBackground(Color.WHITE);
    g.setColor(Color.BLACK);
    g.drawString(str, 30, 70); 
  }
  
  public static boolean OkClicked(DrawingPanel p){
    boolean val = false;
    if ((p.getLastClickX() > p.getWidth() - 100) && (p.getLastClickX() < p.getWidth() - 50) &&
        (p.getLastClickY() > p.getHeight() - 50)){
      val = true;
    }
    else{
      val = false;
    }
    return val;
  }
  
  
  /**
   * This method will show the double value received as parameter in the location
   * of total of the graphics object also received as parameter. 
   * 
   * @param  g           Graphics object where the value will be presented
   * @param  value       Value that will be presented in the frame of total
   * @return             Nothing
   */  
  public static void showTotal(Graphics g, double value){
    g.drawString("Total : " + value,
                 MARGIN * 12, 480);
  }  
  
  /**
   * This method will check whether the cart is full or not. 
   * If the cart is full, show it is full calling the method showMessage.
   * 
   * @return             true if the cart is empty, false otherwise
   */  
  public static boolean isCartFull(){
    boolean val = false;
    if (cartSize == MAX_ITEMS - 1){
      val = true;
      System.out.println("isCartFull: Full"); //debugging
    }
    else{
      val = false;
      System.out.println("isCartFull: Not Full"); //debugging
    }
    return val;
  }
  
  /**
   * This method will check whether the cart is empty or not. 
   * If the cart is empty, show it is empty calling the method showMessage.
   * 
   * @return             true if the cart is empty, false otherwise
   */  
  public static boolean isCartEmpty(){
    boolean val = true;
    if (cartSize == 0){  // checks cartsize
      val = true;
    }
    else{
      val = false;
    }
    return val; 
  }
  
  /**
   * This method will add an item to the cart in both arrays, the Items, and the
   * quantity. It will use the following empty space in the arrays to store those
   * values. If the item is already in the cart, it will only accumulate the 
   * values. Increase the cart size variable.
   * 
   * @param  item        Item ID of the one to add to the cart
   * @param  qty         Quantity to be assigned for that item
   * @return             True if it was successful adding to the cart, False otherwise
   */  
  public static boolean addToCart(int item, double qty){
    boolean val = true;
    if(cartSize <= MAX_ITEMS){
      ++cartSize;
      cartItem[cartSize] = item;
      cartQty[cartSize] = qty;
    } // if there is space in cart add to arrays and increase cart size
    else{
      showMessage("addToCart: There is no more space in the cart!");
    }
    return val; 
  }
  
  /**
   * This method will get a new array conformed by the description of the items 
   * already in the cart. It has to traverse the array of the items in the cart 
   * and go to get the different descriptions.
   *
   * Take into consideration that the itemCart array is the one that holds the
   * itemID of the items previously added to the cart.
   * 
   * @param  desc        Array of item descriptions
   * @return             A new array with the descriptions of the items in the array
   */  
  public static String[] getCartItemNames(String[] desc){
    String[] ret = new String[cartSize]; // new string array created
    for (int i = 0; i < cartSize; ++i) {
      int tempCartItem = cartItem[i];
      ret[i] = itemDesc[tempCartItem];
    }
    return ret; 
  }
  
  /**
   * This method returns the itemID of the item in a specific position of the 
   * cart. It has to verify if the index received as parameter is less than the
   * current size.
   * 
   * @param  index       Index of the item in the cart
   * @return             The itemID of the item in the index received as parameter
   */  
  public static int getItemAtCart(int index){
    int id = 0;
    
    if (index >= 0 && index <= cartSize){
      id = cartItem[index];
    }
    else{
      showMessage("getItemAtCart: There is nothing here!");
    }
    return id; 
  }
  
  /**
   * This method removes the item from the cart in the index received as parameter 
   * It has to verify first if the index received as parameter is less than the
   * current size. It also reduces the size of the cart.
   * 
   * @param  index       Index of the item in the cart
   * @return             Quantity of the item that was removed
   */  
  public static double removeItemCart(int index){
    if(index >= 0 && index <= cartSize){ // checks if cart has items in it to remove
      for(int i = index; i < cartSize - 1; i++){
        cartItem[i] = cartItem[i + 1]; //shifts arrays
        cartQty[i] = cartQty[i + 1];
      }
      cartSize = cartSize - 1;
    }
    else{
      showMessage("removeItemCart: There is no item here!");
    }
    return 0.0; 
  }
  
  /**
   * This method clears the cart and the cart size. For that, it clears the
   * two arrays related to the cart and assigns 0 to the size of the cart variable.
   * 
   * @return             Nothing
   */  
  public static void clearCart(){
    cartItem = new int[MAX_ITEMS]; // clears cart item array
    cartQty = new double[MAX_ITEMS]; // clears cart quantity array
    cartSize = 0; // sets cart item to 0
  }
  
  /**
   * This method is used to verify if the string received is a number (including
   * decimals).
   * 
   * @param  strNum      String containing the possible value
   * @return             True if the string received is actually a value
   */  
  public static boolean isNumeric(String strNum) {
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe){ 
      return false;
    } catch(NullPointerException nfe) {
      return false;
    }
    return true;
  }
}