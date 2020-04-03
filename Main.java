package Amazon;

import UI.StartUp;

public class Main {
    public static void main(String[] args) {
         DataBase myData = new DataBase();
         StartUp startUp = new StartUp(myData);
         startUp.pack();
         startUp.setVisible(true);
    }
}
