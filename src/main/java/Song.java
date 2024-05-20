
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import core.GLA;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

/*
 *
 * @author Abdul
 */

public class Song{
    private String name;
    private String path;
    private String artist="";
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
            length=mp3file.getFrameCount();
            lengthInSeconds =mp3file.getLengthInSeconds();
            name=file.getName().substring(0,file.getName().length()-4);
            String artistFilePath = "src/main/resources/" + name + "_artist.txt";
            File artistFile = new File(artistFilePath);
            if (artistFile.exists()) {
                // Read artist name from file
                try (BufferedReader artistReader = new BufferedReader(new FileReader(artistFilePath))) {
                    artist = artistReader.readLine();
                }
            }
            //if artist file doesn't exist already then
            else {
                // Call API to get artist's name
                if(!gla.search(name).getHits().isEmpty()){
                    artist = gla.search(name).getHits().getFirst().getArtist().getName();
                }
                // Write artist name to file if artist variable isn't empty
                if(!artist.isEmpty()) {
                    try (BufferedWriter artistWriter = new BufferedWriter(new FileWriter(artistFile))) {
                        artistWriter.write(artist);
                    }catch(IOException e){
                        System.out.println("Exception while writing to artist file:");
                        e.printStackTrace();
                    }
                }
            }
            String imagePath="src/main/resources/"+name + "_image.png";
            String lyricsPath="src/main/resources/"+name + "_lyrics.txt";

            //Try reading from lyrics file line by line and use StringBuilder to append each line to lyrics
            try (BufferedReader reader = new BufferedReader(new FileReader(lyricsPath))) {
                String line;
                StringBuilder builder=new StringBuilder();
                while ((line=reader.readLine())!= null) {
                    builder.append(line).append("\n");
                }
                lyrics= builder.toString();
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                icon = new ImageIcon(bufferedImage);
            } catch (IOException e) {
                System.out.println("Error reading the lyrics file: " + e.getMessage());
            }finally {
                //if lyrics variable is still empty then search for lyrics from API
                if(lyrics.isEmpty()){
                    if(!gla.search(name).getHits().isEmpty()) {
                        lyrics = gla.search(name).getHits().getFirst().fetchLyrics();
                    }
                    File lyricsFile = new File("src/main/resources/"+name + "_lyrics.txt");

                    //If we got some lyrics from API then write them to lyrics file
                    if(!lyrics.isEmpty()&&!lyricsFile.createNewFile()) {
                            FileWriter fileWriter = new FileWriter(lyricsFile);
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write(lyrics);
                            bufferedWriter.close();
                    }
                }
                //If icon contains nothing
                if(icon==null) {
                    //Then search for song icon on API
                    if(!gla.search(name).getHits().isEmpty()) {
                        icon = imageRetriever.iconRetriever(gla.search(name).getHits().getFirst().getImageUrl());
                        //If we got the song icon from API then write it to an image file using imageRetriever class's imageWriter function
                        if(icon!=null) {
                            BufferedImage songImage = imageRetriever.bufferedImageRetriever(gla.search(name).getHits().getFirst().getImageUrl());
                            File imageOutputFile = new File(imagePath);
                            imageRetriever.imageWriter(imageOutputFile, songImage, name);
                        }
                    }
                }
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
            System.out.println("Exception in constructor of Song");
            ex.printStackTrace();
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



