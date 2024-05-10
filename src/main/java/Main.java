import core.GLA;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;


public class Main {


    public static void main(String[] args) throws IOException {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Music_interface().setVisible(true);
                }
            });

}}



