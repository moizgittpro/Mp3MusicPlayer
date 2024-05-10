import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.io.*;
import java.util.Objects;

public class MusicPlayer {
    PlaybackListener playbackListener=new PlaybackListener() {
        @Override
        public void playbackFinished(PlaybackEvent evt) {
            // Handle playback finished event
            pausePosition= evt.getFrame();
            System.out.println("Playback finished");

        }
    };
    Boolean isPlaying;
    int pausePosition;
    int currentTimeInSec;
    int currentFrame;
    boolean songFinished;
    long sysTimeInS;
    long timeWhenSongStarts;
    long timeWhenSongPaused;
    FileInputStream inputStream;
    BufferedInputStream bufferedInputStream;
    long seek;

    public static String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    boolean NewSongSelected;
    private Song SongNowPlaying;
    public String songpath="F:/ideaProjects/untitled1/songs/redrum.mp3";
    private final Music_interface musicInterface;
    public MusicPlayer(Music_interface musicInterface)
    {
        this.musicInterface = musicInterface;
        isPlaying=false;
        NewSongSelected=false;
        songFinished=false;
        sysTimeInS=0;
        currentTimeInSec =0;
    }

    AdvancedPlayer advancedPlayer;


    public void updateSliderPosition() {
        new Thread(() -> {
            while (isPlaying) {
                // Get the current playback position of the song
                currentTimeInSec =(int)(timeWhenSongPaused+seek+((System.currentTimeMillis()-timeWhenSongStarts)/1000));
                currentFrame =(int) ((double) currentTimeInSec * SongNowPlaying.getFramerate());
                if(checkForFinish()){
                    return;
                }
                musicInterface.setSongTimeLabel(formatTime((currentTimeInSec)));
                // Update the slider position
                SwingUtilities.invokeLater(() -> {
                    musicInterface.Slider.setValue(currentFrame);
                });
            }
        }).start();
    }


    public boolean checkForFinish(){
        try{
            if(getSongNowPlaying()!=null){
                if(currentFrame== getSongNowPlaying().getmp3file().getFrameCount()){
                    System.out.println("HAILO");
                    songFinished=true;
                    stopSong();
                    currentFrame=0;
                    timeWhenSongPaused=0;
                    seek=0;
                    currentTimeInSec =0;
                    musicInterface.setPausePlayIcon((new javax.swing.ImageIcon(Objects.requireNonNull(getClass().getResource("/play.png")))));
                    musicInterface.setSongTimeLabel("0:00");
                    musicInterface.Slider.setValue(0);
                    return true;
                }
            }
            else{
                return false;
            }
        }catch(Exception e){
            System.out.println("Exception in checkForFinish method:"+e);
        }
        return false;
    }


    public void playSong () {
        try {
            if (currentFrame > 0) {
                if(NewSongSelected){
                    SongNowPlaying=new Song(songpath);
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
                        advancedPlayer.play(currentFrame, Integer.MAX_VALUE);

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
                    SongNowPlaying= new Song(songpath);

                }
                NewSongSelected=false;
                inputStream = new FileInputStream(SongNowPlaying.getpath());
                bufferedInputStream = new BufferedInputStream(inputStream);
                advancedPlayer = new AdvancedPlayer(bufferedInputStream);
                advancedPlayer.setPlayBackListener(playbackListener);
                isPlaying = true;
                new Thread(() -> {
                    try {
                        timeWhenSongStarts=System.currentTimeMillis();
                        advancedPlayer.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }).start();
                updateSliderPosition();
            }
        }catch (Exception e) {
            System.out.println("Exception in playSong method: "+e);
        }
    }

    public void pauseSong () {
        try {
            stopSong();
            timeWhenSongPaused= currentTimeInSec;
        } catch (Exception e) {
            System.out.println("Exception occurred in pauseSong method: "+e);
        }
    }

    public void stopSong(){
        try{
            isPlaying=false;
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer=null;
        }catch(Exception e){
            System.out.println("Exception occurred in stopSong method: "+e);
        }
    }

    public Song getSongNowPlaying () {
        return SongNowPlaying;
    }

}

