import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class MusicPlayer {
    PlaybackListener playbackListener=new PlaybackListener() {
        @Override
        public void playbackFinished(PlaybackEvent evt) {
            // Handle playback finished event
            System.out.println("Playback finished");

        }

        @Override
        public void playbackStarted(PlaybackEvent evt) {
            System.out.println("Playback Started");
        }
    };

    Boolean isPlaying;
    Boolean playlistIsPlaying;
    ArrayList<Song> songs=new ArrayList<>();
    int currentTimeInSec,currentFrame, index=0;
    public String songpath;
    boolean songFinished,NewSongSelected;
    long timeWhenSongStarts,timeWhenSongPaused,seek;
    FileInputStream inputStream;
    BufferedInputStream bufferedInputStream;
    private Song SongNowPlaying;
    private final Music_interface musicInterface;

    public MusicPlayer(Music_interface musicInterface)
    {
        this.musicInterface = musicInterface;
        loadPlaylist("src/main/resources/playlist.dat");
        isPlaying=false;
        NewSongSelected=false;
        songFinished=false;
        playlistIsPlaying=false;
        currentTimeInSec =0;
    }

    AdvancedPlayer advancedPlayer;

    public void updateSliderPosition() {
        new Thread(() -> {
            while (isPlaying) {
                currentTimeInSec =(int)(timeWhenSongPaused+seek+((System.currentTimeMillis()-timeWhenSongStarts)/1000));
                currentFrame =(int) ((double) currentTimeInSec * SongNowPlaying.getFramerate());
                if(checkForFinish()){
                    return;
                }
                musicInterface.setSongTimeLabel(formatTime((currentTimeInSec)));
                // Update the slider position
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                SwingUtilities.invokeLater(() -> musicInterface.setSliderValue(currentFrame));
            }
        }).start();
    }


    public boolean checkForFinish(){
        try{
            if(getSongNowPlaying()!=null){
                if(currentFrame== getSongNowPlaying().getlength()){
                    isPlaying=false;
                    songFinished=true;
                    currentFrame=0;
                    timeWhenSongPaused=0;
                    seek=0;
                    currentTimeInSec =0;
                    musicInterface.setPausePlayIcon((new javax.swing.ImageIcon("src/main/resources/play.png")));
                    musicInterface.setSongTimeLabel("0:00");
                    musicInterface.setSliderValue(0);
                    if(playlistIsPlaying){
                        goToNextSong();
                        musicInterface.updateGUIWhenSongPlays();
                    }
                    return true;
                }
            }
            else{
                return true;
            }
        }catch(Exception e){
            System.out.println("Exception in checkForFinish method:");
            e.printStackTrace();
        }
        return false;
    }


    public void playSong () {
        try {

            if (currentFrame > 0) {
                if(NewSongSelected){
                    if(!playlistIsPlaying) {
                        SongNowPlaying = new Song(songpath);
                    }
                    currentFrame=0;
                    NewSongSelected=false;
                    seek=0;
                    timeWhenSongPaused=0;
                }
                timeWhenSongStarts=System.currentTimeMillis();
                inputStream = new FileInputStream(SongNowPlaying.getpath());
                bufferedInputStream = new BufferedInputStream(inputStream);
                advancedPlayer = new AdvancedPlayer(bufferedInputStream);
                advancedPlayer.setPlayBackListener(playbackListener);

                new Thread(() -> {
                    try {
                        if(advancedPlayer!=null) {
                            advancedPlayer.play(currentFrame, Integer.MAX_VALUE);
                        }
                    } catch (JavaLayerException e){
                        e.printStackTrace();
                    }
                }).start();
                isPlaying=true;
                updateSliderPosition();
            }

            else{
                if(NewSongSelected){
                    System.out.println("NEW SONG SELECTED :)");
                    if(!playlistIsPlaying) {
                        SongNowPlaying = new Song(songpath);
                    }
                }
                NewSongSelected=false;
                if(SongNowPlaying!=null) {
                    inputStream = new FileInputStream(SongNowPlaying.getpath());
                    bufferedInputStream = new BufferedInputStream(inputStream);
                    advancedPlayer = new AdvancedPlayer(bufferedInputStream);
                    advancedPlayer.setPlayBackListener(playbackListener);
                    isPlaying = true;
                    new Thread(() -> {
                        try {
                            timeWhenSongStarts=System.currentTimeMillis();
                            if(advancedPlayer!=null) {
                                advancedPlayer.play();
                            }
                        } catch (JavaLayerException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    updateSliderPosition();
                }
            }
            playlistFinishCheck();
            if(SongNowPlaying!=null) {
                musicInterface.enableSlider();
                musicInterface.PausePlay.setIcon(new javax.swing.ImageIcon("src/main/resources/pause.png"));
            }
        }catch (Exception e) {
            System.out.print("Exception in playSong method: ");
            e.printStackTrace();
        }
    }

    public void pauseSong () {
        try {
            stopSong();
            seek=0;
            timeWhenSongPaused= currentTimeInSec;
        } catch (Exception e) {
            System.out.println("Exception occurred in pauseSong method: "+e);
        }
    }

    public void stopSong(){
        try{
            musicInterface.PausePlay.setIcon(new javax.swing.ImageIcon("src/main/resources/play.png"));
            isPlaying=false;
            if (advancedPlayer != null) {
                advancedPlayer.stop();
                advancedPlayer.close();
                advancedPlayer=null;
            }
        }catch(Exception e){
            System.out.println("Exception occurred in stopSong method:");
            e.printStackTrace();
        }
    }

    public void skipPartOfSong(int progressBarVal){
        currentFrame=progressBarVal;
        currentTimeInSec+= (int) (currentFrame/ getSongNowPlaying().getFramerate());
        musicInterface.setSliderValue(currentFrame);
    }

    public void resumeSong(int value){
        currentFrame=value;
        seek =(int)(currentFrame/(getSongNowPlaying().getFramerate()));
        playSong();
    }

    public void goToNextSong(){
        if(index==songs.size()-1){
            index=0;
            musicInterface.setNextButton(true);
        }
        else if(index<songs.size()-1) {
            index++;
        }
        SongNowPlaying=songs.get(index);
        NewSongSelected=true;
        if(!songFinished) {
            System.out.println("INSIDE");
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer = null;
        }
        songFinished=false;
        playSong();
    }

    public void goToPreviousSong(){
        if(index>0) {
            index--;
        }
        if(index<(songs.size()-1)){
            musicInterface.setNextButton(true);
        }
        stopSong();
        SongNowPlaying=songs.get(index);
        NewSongSelected=true;
        playSong();
    }

    public Song getSongNowPlaying () {
        return SongNowPlaying;
    }

    public void addSongInSongs(String songPath) {
        // Check if the songPath already exists in the playlist
        boolean alreadyExists = false;
        for (Song song : songs) {
            if (Objects.equals(song.getpath(), songPath)) {
                alreadyExists = true;
                break;
            }
        }

        // If the song doesn't already exist, add it to the playlist
        if (!alreadyExists) {
            songs.add(new Song(songPath));
            savePlaylist("src/main/resources/playlist.dat");
        } else {
            System.out.println("Song already exists in the playlist.");
        }
    }

    public void playPlaylist(){
        if(songs.size()>1) {
            index=0;
            musicInterface.setNextButton(true);
            playlistIsPlaying = true;
            stopSong();
            SongNowPlaying = songs.get(index);
            NewSongSelected = true;
            playSong();
            musicInterface.updateGUIWhenSongPlays();
        }else{
            musicInterface.setjTextArea1("Please add atleast 2 songs first to play this playlist");
        }
    }

    public void playlistFinishCheck(){
        if(index >= songs.size() - 1 ){
            musicInterface.setNextButton(false);
        }
    }

    public void restartSong(){
        stopSong();
        resetVariables();
        playSong();
    }

    public void resetVariables(){
        currentFrame=0;
        timeWhenSongPaused=0;
        timeWhenSongStarts=0;
        currentTimeInSec=0;
        seek=0;
    }

    public void savePlaylist(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Song song : songs) {
                writer.println(song.getpath());
            }
            System.out.println("Playlist saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving playlist: " + e.getMessage());
        }
    }

    public void loadPlaylist(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            songs.clear(); // Clear existing playlist
            while ((line = reader.readLine()) != null) {
                songs.add(new Song(line.trim()));
            }
            System.out.println("Playlist loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading playlist: " + e.getMessage());
        }
    }

    public void removeASong(String songName){
        if(getSongNowPlaying()!=null && getSongNowPlaying().getname().equals(songName)){
            stopPlaylist();
            songs.remove(findSongIndexInSongs(songName));
            savePlaylist("src/main/resources/playlist.dat");
        }
        else{
            songs.remove(findSongIndexInSongs(songName));
            savePlaylist("src/main/resources/playlist.dat");
        }
    }

    public int findSongIndexInSongs(String s){
        for(int i=0;i<songs.size();i++){
            if(s != null) {
                if (s.equals(songs.get(i).getname())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void stopPlaylist(){
        playlistIsPlaying=false;
        if(getSongNowPlaying()!=null) {
            stopSong();
            SongNowPlaying=null;
            musicInterface.updateGUIWhenSongPlays();
            resetVariables();
        }
    }

    public static String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}

