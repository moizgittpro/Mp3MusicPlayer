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
    Boolean songFinished,NewSongSelected,playlistLoaded;
    long timeWhenSongStarts,timeWhenSongPaused,seek;
    FileInputStream inputStream;
    BufferedInputStream bufferedInputStream;
    private Song SongNowPlaying;
    private final Music_interface musicInterface;


    public MusicPlayer(Music_interface musicInterface)
    {
        this.musicInterface = musicInterface;
        isPlaying=false;
        NewSongSelected=false;
        songFinished=false;
        playlistIsPlaying=false;
        playlistLoaded=false;
        currentTimeInSec =0;

    }

    AdvancedPlayer advancedPlayer;

    public void updateSliderPosition() {
        new Thread(() -> {
            while (isPlaying) {
                //current time in the song is calculated
                currentTimeInSec =(int)(timeWhenSongPaused+seek+((System.currentTimeMillis()-timeWhenSongStarts)/1000));
                //current time and framerate is used to get currentFrame
                currentFrame =(int) ((double) currentTimeInSec * SongNowPlaying.getFramerate());

                //if song finished then return
                if(checkForFinish()){
                    return;
                }
                //Set the time label to the current time of the song
                SwingUtilities.invokeLater(()->musicInterface.setSongTimeLabel(formatTime((currentTimeInSec))));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // Update the slider position according to the currentFrame
                SwingUtilities.invokeLater(() -> musicInterface.setSliderValue(currentFrame));
            }
        }).start();
    }


    public boolean checkForFinish(){
        try{
            if(SongNowPlaying!=null){
                if(currentFrame== SongNowPlaying.getlength()){
                    //If song is finished then resetting the variables
                    isPlaying=false;
                    songFinished=true;
                    currentFrame=0;
                    timeWhenSongPaused=0;
                    seek=0;
                    currentTimeInSec =0;
                    //Setting the pauseplay button to play state
                    musicInterface.setPausePlayIcon((new javax.swing.ImageIcon("src/main/resources/play.png")));
                    //Setting time to 0:00
                    musicInterface.setSongTimeLabel("0:00");
                    //Slider goes to beginning
                    musicInterface.setSliderValue(0);

                    //If playlist is playing then if one song has finished then go to next song and update the GUI according to song
                    if(playlistIsPlaying){
                        goToNextSong();
                        SwingUtilities.invokeLater(musicInterface::updateGUIWhenSongPlays);
                    }
                    //return true because song has finished
                    return true;
                }
            }else{
                //If SongNowPlaying is null then also return true
                return true;
            }
        }catch(Exception e){
            System.out.println("Exception in checkForFinish method:");
            e.printStackTrace();
        }
        //song hasn't finished then return false
        return false;
    }


    public void playSong () {
        try {
            songFinished=false;
            //If song was paused at some state other than the beginning and then played then this block runs
            if (currentFrame > 0) {
                //If a new song was selected and a playlist wasn't playing then play the new song
                if(NewSongSelected){
                    if(!playlistIsPlaying) {
                        SongNowPlaying = new Song(songpath);
                    }
                    //Reset some variables
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

            //If song is to be played from beginning
            else{
                if(NewSongSelected){
                    if(!playlistIsPlaying) {
                        SongNowPlaying = new Song(songpath);
                    }
                }
                NewSongSelected=false;
                try {
                    if (SongNowPlaying != null) {
                        inputStream = new FileInputStream(SongNowPlaying.getpath());
                        bufferedInputStream = new BufferedInputStream(inputStream);
                        advancedPlayer = new AdvancedPlayer(bufferedInputStream);
                        advancedPlayer.setPlayBackListener(playbackListener);
                        isPlaying = true;
                        new Thread(() -> {
                            try {
                                timeWhenSongStarts = System.currentTimeMillis();
                                if (advancedPlayer != null) {
                                    advancedPlayer.play();
                                }
                            } catch (JavaLayerException e) {
                                e.printStackTrace();
                            }
                        }).start();
                        updateSliderPosition();
                    }
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
            }
            playlistFinishCheck();
            if(SongNowPlaying!=null) {
                SwingUtilities.invokeLater(() -> {
                    //When song is played enable the slider and set the pauseplay button to pause state
                    musicInterface.enableSlider();
                    SwingUtilities.invokeLater(()->musicInterface.PausePlay.setIcon(new ImageIcon("src/main/resources/pause.png")));
                });
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
            SwingUtilities.invokeLater(() -> musicInterface.PausePlay.setIcon(new ImageIcon("src/main/resources/play.png")));
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
        SwingUtilities.invokeLater(() -> musicInterface.setSliderValue(currentFrame));
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
            int sizeBefore=songs.size();
            songs.add(new Song(songPath));
            savePlaylist("src/main/resources/playlist.dat");
            if(playlistIsPlaying &&sizeBefore==songs.size()-1){
                musicInterface.setNextButton(true);
            }
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
            SwingUtilities.invokeLater(musicInterface::updateGUIWhenSongPlays);
        }else{
            SwingUtilities.invokeLater(() -> musicInterface.setjTextArea1("Please add atleast 2 songs first to play this playlist"));
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
        isPlaying=false;
        songFinished=true;
        playlistIsPlaying=false;
        NewSongSelected=false;


    }

    public void savePlaylist(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Song song : songs) {
                writer.println(song.getpath());
            }
            writer.close();
            System.out.println("Playlist saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving playlist: " + e.getMessage());
        }
    }

    public void loadPlaylist(String filePath) {
        playlistLoaded=true;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            songs.clear(); // Clear existing playlist
            while ((line = reader.readLine()) != null) {
                String songPathTempVariable=line.trim();
                if(new File(songPathTempVariable).exists()) {
                    songs.add(new Song(songPathTempVariable));
                }
            }
            System.out.println("Playlist loaded successfully.");
            SwingUtilities.invokeLater(musicInterface::updatePlaylistDisplay);

        } catch (IOException e) {
            System.out.println("Error loading playlist: " + e.getMessage());
        }
    }

    public void removeASong(String songName){
        //if the song now playing is the same as the song the user wants to remove from playlist then stop the playlist
        if(SongNowPlaying!=null && SongNowPlaying.getname().equals(songName)){
            stopPlaylist();
            musicInterface.setNextButton(false);
        }
        //if the 2nd last song is being played and the user removes the last song then disable the next button
        else if((index==songs.size()-2) && (songName.equals(songs.getLast().getname()))){
            musicInterface.setNextButton(false);
        }
        //if the playlist only contains 2 songs and the user removes 1 of them then the playlist stops and next button is disabled
        else if(songs.size()==2){
            stopPlaylist();
            musicInterface.setNextButton(false);
        }
        //Remove the specified song and save the playlist
        songs.remove(findSongIndexInSongs(songName));
        savePlaylist("src/main/resources/playlist.dat");
    }

    //Method used to find the index of the song selected by the user to be removed from playlist so the mentioned song can be removed
    //from songs playlist (user selects name of the song and we find the index corresponding to that song in the songs arraylist and return it)
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
        if(SongNowPlaying!=null) {
            stopSong();
            SongNowPlaying=null;
            SwingUtilities.invokeLater(musicInterface::updateGUIWhenSongPlays);
            resetVariables();
        }
    }

    public static String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public Song getSongNowPlaying() {
        return SongNowPlaying;
    }
}

