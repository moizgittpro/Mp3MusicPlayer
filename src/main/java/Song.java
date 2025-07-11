
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import core.GLA;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *
 * @author Abdul
 */

public class Song {
    private String name;
    private String path;
    private String artist;
    private long length;
    private long lengthInSeconds;
    private Icon icon;
    private String lyrics="";
    private double framerate;
    private Mp3File mp3file;
    GLA gla=new GLA();

    public Song(String path){
        try {
            this.path=path;
            File file=new File(path);
            mp3file=new Mp3File(path);
            framerate = (double) mp3file.getFrameCount()/ mp3file.getLengthInSeconds();
            length=mp3file.getLength();
            lengthInSeconds =mp3file.getLengthInSeconds();
            System.out.println(framerate);
            System.out.println(mp3file.getLength());
            name=file.getName().substring(0,file.getName().length()-4);
            String imagePath="src/main/resources/"+name + "_image.png";
            String lyricsPath="src/main/resources/"+name + "_lyrics.txt";

            try (BufferedReader reader = new BufferedReader(new FileReader(lyricsPath))) {
                String line;
                while ((line=reader.readLine())!= null) {
                    lyrics+=line+"\n";
                }
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                icon = new ImageIcon(bufferedImage);
            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            }finally {
                if(lyrics.isEmpty()){
                    lyrics=gla.search(name).getHits().getFirst().fetchLyrics();
                    File lyricsFile = new File("src/main/resources/"+name + "_lyrics.txt");
                    if(!lyricsFile.exists()) {
                        lyricsFile.createNewFile();
                        FileWriter fileWriter = new FileWriter(lyricsFile);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(lyrics);
                        bufferedWriter.close();
                    }
                }
                if(icon==null) {
                    icon = imageRetriever.iconRetriever(gla.search(name).getHits().get(0).getImageUrl());
                    BufferedImage songImage = imageRetriever.bufferedImageRetriever(gla.search(name).getHits().get(0).getImageUrl());
                    File imageOutputFile = null;
                    imageRetriever.imageWriter(imageOutputFile, songImage, name);
                }
                artist=gla.search(name).getHits().getFirst().getArtist().getName();
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
            Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public long getlength(){
        return length;
    }

    public String getpath(){
        return path;
    }

    public String getartist(){
        return artist;
    }

    public String getname(){
        return name;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getLyrics(){
        return lyrics;
    }

    public double getFramerate() {
        return framerate;
    }

    public int getLengthinSeconds(){
        return (int) lengthInSeconds;
    }

    public Mp3File getmp3file(){
        return mp3file;
    }
}



