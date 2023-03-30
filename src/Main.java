import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "привет");
        map.put(2, "пока");

        ArrayList<String> s =  new ArrayList<>(map.values());
        System.out.println(s);

    }
}
