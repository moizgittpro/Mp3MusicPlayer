
import java.awt.*;
import java.io.IOException;


public class Main {


    public static void main(String[] args) throws IOException {
        //Setting GUI VISIBLE
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Music_interface().setVisible(true);
                }
            });

}}



