import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.io.*;

public class MusicPlayer {
    private static final Object playSignal = new Object();
    PlaybackListener playbackListener=new PlaybackListener() {
        @Override
        public void playbackFinished(PlaybackEvent evt) {
            // Handle playback finished event
            pausePosition= evt.getFrame();
            System.out.println("Playback finished");

        }
    };;
    Boolean isPlaying;
    int pausePosition;
    int currenttimeinsec;
    int currentFrame;
    boolean songFinished;
    long sysTimeInS;
    long timeWhenSongStarts;
    long timeWhenSongPaused;
    long timeWhenSongResumes;
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
    private Music_interface musicInterface;
    public MusicPlayer(Music_interface musicInterface)
    {
        this.musicInterface = musicInterface;
        isPlaying=false;
        NewSongSelected=false;
        songFinished=false;
        sysTimeInS=0;
        currenttimeinsec =0;
    }

    AdvancedPlayer advancedPlayer;


    public void updateSliderPosition() {
        new Thread(() -> {
            while (isPlaying) {
                try {
                    // Get the current playback position of the song
                    currenttimeinsec =(int)(timeWhenSongPaused+seek+((System.currentTimeMillis()-timeWhenSongStarts)/1000));
                    currentFrame =(int) ((double) currenttimeinsec * SongNowPlaying.getFramerate());
                    if(checkForFinish()){
                        return;
                    }
                    musicInterface.setjLabel1(formatTime((int)((currenttimeinsec))));
                    // Update the slider position
                    SwingUtilities.invokeLater(() -> {
                        musicInterface.Slider.setValue(currentFrame);
                    });
                    // Sleep for a short duration to avoid excessive CPU usage
                    Thread.sleep(500); // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                    currenttimeinsec=0;
                    musicInterface.setPausePlayIcon((new javax.swing.ImageIcon(getClass().getResource("/play.png"))));
                    musicInterface.setjLabel1("0:00");
                    musicInterface.Slider.setValue(0);
                    return true;
                }
            }
            else{
                return false;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }


    public void playSong () {
        try {
            if (currentFrame > 0) {
                timeWhenSongResumes=System.currentTimeMillis();
                if(NewSongSelected){
                    SongNowPlaying=new Song(songpath);
                    currentFrame=0;
                    NewSongSelected=false;
                    seek=0;
                    timeWhenSongPaused=0;
                    timeWhenSongResumes=0;
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
            System.out.println(e);
        }
    }

    public void pauseSong () {
        try {
            stopSong();
            timeWhenSongPaused=currenttimeinsec;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void stopSong(){
        try{
            isPlaying=false;
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer=null;
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public Song getSongNowPlaying () {
        return SongNowPlaying;
    }

}

