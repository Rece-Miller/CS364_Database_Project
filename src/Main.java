public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        System.out.println(db.connect() ? "Success" : "Fail");
    }
}
