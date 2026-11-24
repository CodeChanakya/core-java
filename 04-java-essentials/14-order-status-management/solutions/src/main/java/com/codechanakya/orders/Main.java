package com.codechanakya.orders;

public class Main {

    public static void main(String[] args) {
        Order first = new Order("Asha Menon");
        Order second = new Order("Rahul Verma");
        Order third = new Order("Meera Iyer");
        Order fourth = new Order("Kiran Rao");
        Order fifth = new Order("Dev Patel");
        Order[] orders = {first, second, third, fourth, fifth};

        // A normal delivery.
        first.moveTo(OrderStatus.PAID);
        first.moveTo(OrderStatus.PACKED);
        first.moveTo(OrderStatus.SHIPPED);
        first.moveTo(OrderStatus.DELIVERED);

        // Cancelled before packing.
        second.moveTo(OrderStatus.PAID);
        second.moveTo(OrderStatus.CANCELLED);

        // Tries to skip steps.
        System.out.println("Ship a NEW order straight away: " + outcome(third.moveTo(OrderStatus.SHIPPED)));
        System.out.println("Pay it first instead: " + outcome(third.moveTo(OrderStatus.PAID)));

        // Delivered, then returned. A returned order is finished for good.
        fourth.moveTo(OrderStatus.PAID);
        fourth.moveTo(OrderStatus.PACKED);
        fourth.moveTo(OrderStatus.SHIPPED);
        fourth.moveTo(OrderStatus.DELIVERED);
        fourth.moveTo(OrderStatus.RETURNED);
        System.out.println("Pay a RETURNED order: " + outcome(fourth.moveTo(OrderStatus.PAID)));

        System.out.println();
        System.out.println("--- Journeys ---");
        for (Order order : orders) {
            System.out.println(order.getId() + "  " + String.format("%-12s", order.getCustomer()) + order.pathText());
        }
        System.out.println("Orders created: " + Order.totalCreated());

        System.out.println();
        OrderBoard.print(OrderBoard.countByStatus(orders));

        System.out.println();
        System.out.println("--- What can SHIPPED become? ---");
        System.out.println(OrderStatus.SHIPPED.nextStates());
        System.out.println("Finished states:");
        for (OrderStatus status : OrderStatus.values()) {
            if (status.isFinished()) {
                System.out.println("  " + status + " (position " + status.ordinal() + ")");
            }
        }

        // B1, I1, B2: the new REFUNDED status, finished orders and the extra rule.
        fourth.moveTo(OrderStatus.REFUNDED);
        System.out.println();
        System.out.println("Finished orders: " + OrderBoard.countFinished(orders));
        System.out.println("fromText(\" shipped \") -> " + OrderStatus.fromText(" shipped "));
        System.out.println("fromText(\"payed\") -> " + OrderStatus.fromText("payed"));
        for (OrderStatus status : OrderStatus.values()) {   // B3
            System.out.println(status.ordinal() + " " + status.getLabel());
        }
        for (ShippingSpeed speed : ShippingSpeed.values()) {   // C1
            System.out.println(speed + " for 3 kg costs " + speed.cost(3).toPlainString());
        }

        System.out.println();
        System.out.println("Parsing text from outside the program:");
        System.out.println("  \"SHIPPED\" -> " + OrderStatus.valueOf("SHIPPED"));
        try {
            OrderStatus.valueOf("PAYED");
        } catch (IllegalArgumentException e) {
            System.out.println("  \"PAYED\" is not a status: " + e.getMessage());
        }
    }

    private static String outcome(boolean worked) {
        return worked ? "done" : "refused";
    }
}
