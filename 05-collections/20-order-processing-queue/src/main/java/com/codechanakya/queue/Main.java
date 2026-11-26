package com.codechanakya.queue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {
        OrderDesk desk = new OrderDesk();
        desk.receive(new QueuedOrder("ORD-1", "Asha", Priority.STANDARD));
        desk.receive(new QueuedOrder("ORD-2", "Rahul", Priority.EXPRESS));
        desk.receive(new QueuedOrder("ORD-3", "Meera", Priority.STANDARD));
        desk.receive(new QueuedOrder("ORD-4", "Kiran", Priority.EXPRESS));
        desk.receive(new QueuedOrder("ORD-5", "Dev", Priority.STANDARD));

        System.out.println("Orders received: " + desk.waiting());
        System.out.println();
        System.out.println("--- A plain queue: first in, first out ---");
        System.out.println("  Peek (look, do not take): " + desk.peekArrivalOrder());
        for (int i = 0; i < 3; i++) {
            System.out.println("  Poll: " + desk.nextInArrivalOrder());
        }

        System.out.println();
        System.out.println("--- A priority queue: express first, then arrival ---");
        for (int i = 0; i < 3; i++) {
            System.out.println("  Handle: " + desk.nextMostUrgent());
        }
        System.out.println("  Waiting: " + desk.waiting() + ", finished: " + desk.finished());

        System.out.println();
        System.out.println("--- A deque used as an undo stack ---");
        System.out.println("  Last finished: " + desk.lastFinished());
        System.out.println("  Undo it: " + desk.undoLast());
        System.out.println("  Waiting again: " + desk.waiting() + ", finished: " + desk.finished());
        System.out.println("  Handle next: " + desk.nextMostUrgent());

        System.out.println();
        System.out.println("--- Both ends of a Deque ---");
        Deque<String> tray = new ArrayDeque<>();
        tray.addLast("middle");
        tray.addFirst("front");
        tray.addLast("back");
        System.out.println("  Tray: " + tray);
        System.out.println("  Take from front: " + tray.pollFirst() + ", from back: " + tray.pollLast());
        System.out.println("  Left: " + tray);

        System.out.println();
        System.out.println("--- Empty queue: null or exception? ---");
        while (desk.nextInArrivalOrder() != null) {
            // drain whatever is left in the plain line
        }
        System.out.println("  poll() on empty returns: " + desk.nextInArrivalOrder());
        System.out.println("  peek() on empty returns: " + desk.peekArrivalOrder());
        try {
            desk.mustTakeArrivalOrder();
        } catch (NoSuchElementException e) {
            System.out.println("  remove() on empty throws: " + e.getClass().getSimpleName());
        }
    }
}
