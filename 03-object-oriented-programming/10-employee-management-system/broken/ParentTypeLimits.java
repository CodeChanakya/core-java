/** The variable's type decides which methods you may call, not the object's real type. */
public class ParentTypeLimits {

    public static void main(String[] args) {
        Employee boss = new Manager();
        System.out.println("Team size: " + boss.getTeamSize());
    }
}

class Employee {
}

class Manager extends Employee {
    int getTeamSize() {
        return 6;
    }
}
