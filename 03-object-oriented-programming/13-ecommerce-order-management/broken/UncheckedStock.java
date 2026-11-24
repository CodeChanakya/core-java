/** Removing stock without checking first lets the shelf go below zero. */
public class UncheckedStock {

    public static void main(String[] args) {
        Product keyboard = new Product("Mechanical Keyboard", 3);
        keyboard.removeStock(5);
        System.out.println(keyboard.name + " stock: " + keyboard.stock);
    }
}

class Product {
    final String name;
    int stock;

    Product(String name, int stock) {
        this.name = name;
        this.stock = stock;
    }

    void removeStock(int quantity) {
        stock -= quantity;
    }
}
