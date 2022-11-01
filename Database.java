//Import hashmap module
import java.util.HashMap;

public class Database{

    public String price(String userInput)
    {
        // Hashmap is created to input database entries 
      // Hashmap contains name of item and price
        HashMap<String, String> inventory;
        inventory = new HashMap<String, String>();
        inventory.put("apple", "$1");
        inventory.put("banana", "$2");
        inventory.put("cucumber", "$1");
        inventory.put("tomato", "$2");
        inventory.put("avocado", "$3");

        //Searches through hashmap for userInput and outputs price
        for (String i : inventory.keySet()) {
            if (userInput.equals(i))
                return inventory.get(userInput);
        }
        //if search is unsuccessful then it outputs a message saying that the product is not available
        return "PRODUCT NOT AVAILABLE";
    }
}
