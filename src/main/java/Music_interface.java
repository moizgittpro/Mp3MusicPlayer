import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Music_interface extends JFrame {

    JFileChooser filechooser;
    FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3");
    private final MusicPlayer musicPlayer;
    Font Geist;

    public Music_interface() {

        try {
            Geist = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/GeistVF.ttf"));
            System.out.println(Geist.getFontName());
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        initComponents();
        super.setLocationRelativeTo(null);
        playPlaylist.addActionListener(this::playPlaylistActionPerformed);
        addSong.addActionListener(this::addSongActionPerformed);
        removeSong.addActionListener(this::removeSongActionPerformed);
        musicPlayer = new MusicPlayer(this);
    }

    private void initComponents() {

        MainPanel = new JPanel();
        TopPanel = new JPanel();
        Close = new JButton();
        Minimize = new JButton();
        LeftPanel = new JPanel();
        BrowseLabel = new JButton();
        BottomPanel = new JPanel();
        ImagePanel = new JPanel();
        ImageLabel = new JLabel();
        NamePanel = new JPanel();
        NameLabel = new JLabel();
        Previous = new JButton();
        PausePlay = new JButton();
        Next = new JButton();
        Slider = new JSlider();
        songTimeLabel = new JLabel();
        songTotalTimeLabel = new JLabel();
        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        setResizable(false);

        MainPanel.setBackground(new java.awt.Color(0, 32, 63));

        TopPanel.setBackground(new java.awt.Color(173, 239, 209));


        Close.setBackground(new java.awt.Color(173, 239, 209));
        Close.setBorderPainted(false);
        Close.setFocusPainted(false);
        Close.setFont(new java.awt.Font(Geist.getFontName(), 1, 24));
        Close.setForeground(new java.awt.Color(0, 0, 0));
        Close.setText("X");
        Close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CloseMouseClicked(evt);
            }
        });

        Minimize.setBackground(new java.awt.Color(173, 239, 209));
        Minimize.setBorderPainted(false);
        Minimize.setFocusPainted(false);
        Minimize.setFont(new java.awt.Font(Geist.getFontName(), 1, 24));
        Minimize.setForeground(new java.awt.Color(0, 0, 0));
        Minimize.setText("_");
        Minimize.setVerticalAlignment(SwingConstants.TOP);
        Minimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MinimizeMouseClicked(evt);
            }
        });

        GroupLayout TopPanelLayout = new GroupLayout(TopPanel);
        TopPanel.setLayout(TopPanelLayout);
        TopPanelLayout.setHorizontalGroup(
                TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, TopPanelLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Minimize)
                                .addGap(37, 37, 37)
                                .addComponent(Close)
                                .addContainerGap())
        );
        TopPanelLayout.setVerticalGroup(
                TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(TopPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(TopPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(Close, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(Minimize, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(53, Short.MAX_VALUE))
        );

        LeftPanel.setBackground(new java.awt.Color(173, 239, 209));
        BrowseLabel.setBorder(null);
        BrowseLabel.setFocusPainted(false);
        BrowseLabel.setBackground(new java.awt.Color(173, 239, 209));
        BrowseLabel.setFont(new Font(Geist.getFontName(),0,24));
        BrowseLabel.setForeground(new java.awt.Color(0, 0, 0));
        BrowseLabel.setText("Play a song");
        BrowseLabel.setToolTipText("");
        BrowseLabel.setOpaque(true);
        BrowseLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getButton()==MouseEvent.BUTTON1) {
                    BrowseLabelMouseClicked(evt);
                }
            }
        });

        Playlist = new JButton();
        Playlist.setBorder(null);
        Playlist.setFocusPainted(false);
        Playlist.setBackground(new java.awt.Color(173, 239, 209));
        Playlist.setFont(new java.awt.Font(Geist.getFontName(), 0, 24));
        Playlist.setForeground(new java.awt.Color(0, 0, 0));
        Playlist.setText("Playlist");
        playlistMenu = new JPopupMenu();
        playPlaylist = new JMenuItem("Play this playlist");
        addSong=new JMenuItem("Add a song");
        removeSong=new JMenuItem("Remove a song");
        playlistMenu.add(playPlaylist);
        playlistMenu.add(addSong);
        playlistMenu.add(removeSong);

        Playlist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getButton()==MouseEvent.BUTTON1) {
                    PlaylistMouseClicked(evt);
                }
                if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    playlistMenu.show(Playlist, evt.getX(), evt.getY());
                }
            }
        });



        LyricsButton = new JButton();
        LyricsButton.setBorder(null);
        LyricsButton.setFocusPainted(false);
        LyricsButton.setBackground(new java.awt.Color(173, 239, 209));
        LyricsButton.setFont(new java.awt.Font(Geist.getFontName(), 0, 24));
        LyricsButton.setForeground(new java.awt.Color(0, 0, 0));
        LyricsButton.setText("Lyrics"); // Set button text
        LyricsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LyricsButtonMouseClicked(evt);
            }
        });



        GroupLayout LeftPanelLayout = new GroupLayout(LeftPanel);
        LeftPanel.setLayout(LeftPanelLayout);
        LeftPanelLayout.setHorizontalGroup(
                LeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LeftPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(BrowseLabel, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(LeftPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(Playlist, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(LeftPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(LyricsButton, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(LeftPanelLayout.createSequentialGroup()
                                .addContainerGap())
        );
        LeftPanelLayout.setVerticalGroup(
                LeftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LeftPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(BrowseLabel, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Playlist, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(LyricsButton, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
        );



        BottomPanel.setBackground(new java.awt.Color(173, 239, 209));
        BottomPanel.setForeground(new java.awt.Color(156, 156, 210));

        ImagePanel.setBackground(new java.awt.Color(173, 239, 209));
        ImagePanel.setPreferredSize(new Dimension(180,110));

        ImageLabel.setBackground(new java.awt.Color(173, 239, 209));
        ImageLabel.setForeground(new java.awt.Color(0, 0, 0));
        ImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GroupLayout ImagePanelLayout = new GroupLayout(ImagePanel);
        ImagePanel.setLayout(ImagePanelLayout);
        ImagePanelLayout.setHorizontalGroup(
                ImagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(ImagePanelLayout.createSequentialGroup()
                                .addComponent(ImageLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        ImagePanelLayout.setVerticalGroup(
                ImagePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(ImagePanelLayout.createSequentialGroup()
                                .addComponent(ImageLabel, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                                .addContainerGap())
        );

        NamePanel.setBackground(new java.awt.Color(173, 239, 209));
        NamePanel.setMaximumSize(new Dimension(180,74));
        NamePanel.setPreferredSize(new Dimension(180,74));
        NameLabel.setBackground(new java.awt.Color(173, 239, 209));
        NameLabel.setFont(new java.awt.Font(Geist.getFontName(), 1, 18));
        NameLabel.setForeground(new java.awt.Color(0, 0, 0));
        NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        NameLabel.setMaximumSize(new Dimension(180,74));
        NameLabel.setPreferredSize(new Dimension(180,74));


        GroupLayout NamePanelLayout = new GroupLayout(NamePanel);
        NamePanel.setLayout(NamePanelLayout);
        NamePanelLayout.setHorizontalGroup(
                NamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(NamePanelLayout.createSequentialGroup()
                                .addComponent(NameLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        NamePanelLayout.setVerticalGroup(
                NamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(NameLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );


        Previous.setBorder(null);
        Previous.setFocusPainted(false);
        Previous.setBackground(new java.awt.Color(173, 239, 209));
        Previous.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/back.png"))));
        Previous.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(evt.getButton()==MouseEvent.BUTTON1) {
                    PreviousMouseClicked(evt);
                }
            }
        } );


        PausePlay.setBorder(null);
        PausePlay.setOpaque(false);
        PausePlay.setFocusPainted(false);
        PausePlay.setIcon(new ImageIcon("src/main/resources/play.png"));
        PausePlay.setBackground(new java.awt.Color(173, 239, 209));
        PausePlay.setVerticalAlignment(SwingConstants.CENTER);
        PausePlay.setHorizontalAlignment(SwingConstants.CENTER);


        PausePlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PausePlayMouseClicked(evt);
            }
        });

        Next.setEnabled(false);
        Next.setBorder(null);
        Next.setFocusPainted(false);
        Next.setBackground(new java.awt.Color(173, 239, 209));
        Next.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/next.png"))));
        Next.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if(evt.getButton()==MouseEvent.BUTTON1&&Next.isEnabled()) {
                    NextMouseClicked(evt);
                }
            }
        });

        Slider.setEnabled(false);
        Slider.setBackground(new java.awt.Color(0, 32, 63));
        Slider.setForeground(new java.awt.Color(173, 239, 209));
        Slider.setValue(0);
        Slider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if(evt.getButton()==MouseEvent.BUTTON1) {
                    SliderMousePressed(evt);
                }
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if(evt.getButton()==MouseEvent.BUTTON1) {
                    SliderMouseReleased(evt);
                }
            }
        });

        songTimeLabel.setBackground(new java.awt.Color(173, 239, 209));
        songTimeLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        songTimeLabel.setForeground(new java.awt.Color(0, 0, 0));
        songTimeLabel.setBorder(null);
        songTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        songTimeLabel.setText("0:00");

        songTotalTimeLabel.setBackground(new java.awt.Color(173, 239, 209));
        songTotalTimeLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        songTotalTimeLabel.setForeground(new java.awt.Color(0, 0, 0));
        songTotalTimeLabel.setBorder(null);
        songTotalTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        songTotalTimeLabel.setText("0:00");

        GroupLayout BottomPanelLayout = new GroupLayout(BottomPanel);
        BottomPanel.setLayout(BottomPanelLayout);
        BottomPanelLayout.setHorizontalGroup(
                BottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(BottomPanelLayout.createSequentialGroup()
                                .addGroup(BottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(ImagePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(NamePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(songTimeLabel)
                                .addGap(18, 18, 18)
                                .addGroup(BottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(BottomPanelLayout.createSequentialGroup()
                                                .addComponent(Previous)
                                                .addGap(79, 79, 79)
                                                .addComponent(PausePlay,50,50,50)
                                                .addGap(79, 79, 79)
                                                .addComponent(Next))
                                        .addComponent(Slider, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(songTotalTimeLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        BottomPanelLayout.setVerticalGroup(
                BottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(BottomPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(BottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(ImagePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(GroupLayout.Alignment.TRAILING, BottomPanelLayout.createSequentialGroup()
                                                .addGroup(BottomPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(Slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(songTimeLabel)
                                                        .addComponent(songTotalTimeLabel))
                                                .addGap(18, 18, 18)
                                                .addGroup(BottomPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(PausePlay, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(BottomPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(Previous, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(Next, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))))
                                .addGap(18, 18, 18)
                                .addComponent(NamePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        jTextArea1.setBackground(new java.awt.Color(0, 32, 63));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font(Geist.getFontName(), 0, 18));
        jTextArea1.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea1.setRows(5);
        jTextArea1.setText("Lyrics");
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setCaretColor(new java.awt.Color(0, 0, 0));
        jTextArea1.setSelectionColor(new java.awt.Color(255, 255, 255));
        DefaultCaret caret = (DefaultCaret)jTextArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE); // Prevent auto-scrolling
        caret.setDot(0);
        jScrollPane1.setViewportView(jTextArea1);

        GroupLayout MainPanelLayout = new GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
                MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(BottomPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(MainPanelLayout.createSequentialGroup()
                                .addComponent(LeftPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
                        .addComponent(TopPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        MainPanelLayout.setVerticalGroup(
                MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(MainPanelLayout.createSequentialGroup()
                                .addComponent(TopPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(LeftPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(MainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(MainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void PreviousMouseClicked(MouseEvent evt) {
        if(musicPlayer.playlistIsPlaying) {
            musicPlayer.goToPreviousSong();
            updateGUIWhenSongPlays();
        }else{
            musicPlayer.restartSong();
        }
    }

    private void NextMouseClicked(MouseEvent evt){
        if(musicPlayer.playlistIsPlaying && musicPlayer.index<musicPlayer.songs.size()) {
            musicPlayer.goToNextSong();
            updateGUIWhenSongPlays();

        }
    }

    private void PlaylistMouseClicked(MouseEvent evt) {
        new Thread(()-> {
            if(!musicPlayer.playlistLoaded) {
                jTextArea1.setText("LOADING");
                musicPlayer.loadPlaylist("src/main/resources/playlist.dat");
            }
        }).start();
            jTextArea1.setText("");
            if(musicPlayer.playlistLoaded) {
                updatePlaylistDisplay();
            }

    }

    private void playPlaylistActionPerformed(ActionEvent evt) {
        // Code to handle the action when "Play this playlist" is selected
        new Thread(()-> {
            if(!musicPlayer.playlistLoaded) {
                jTextArea1.setText("LOADING");
                musicPlayer.loadPlaylist("src/main/resources/playlist.dat");
            }
            musicPlayer.playPlaylist();
        }).start();

    }

    private void addSongActionPerformed(ActionEvent evt) {
        // Code to handle the action when "Add a song" is selected
        AddASong();
        jTextArea1.setText("");
        updatePlaylistDisplay();
    }

    private void removeSongActionPerformed(ActionEvent evt) {
        // Create an ArrayList to hold the song names
        ArrayList<String> songNames = new ArrayList<>();
        for (Song song : musicPlayer.songs) {
            songNames.add(song.getname());
        }

        // Convert the ArrayList to an array
        String[] songNamesArray = songNames.toArray(new String[0]);

        // Show a JOptionPane with the song names
        String selectedSongName = (String) JOptionPane.showInputDialog(
                null,
                "Select a song to remove:",
                "Remove Song",
                JOptionPane.PLAIN_MESSAGE,
                null,
                songNamesArray,
                null);

        // Remove the selected song from the playlist
        if (selectedSongName != null) {
            musicPlayer.removeASong(selectedSongName);
            songRemoved=true;
            updatePlaylistDisplay();
            songRemoved=false;
        }
    }
    private void LyricsButtonMouseClicked(MouseEvent evt) {
        setjTextArea1ToLyrics();
    }

    private void PausePlayMouseClicked(java.awt.event.MouseEvent evt) {
        if (musicPlayer.isPlaying) {
            musicPlayer.pauseSong();
            musicPlayer.isPlaying = false;
        } else {
            if(musicPlayer.getSongNowPlaying()!=null) {
                musicPlayer.playSong();
                musicPlayer.isPlaying = true;

            }
        }
    }

    private void BrowseLabelMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        musicPlayer.playlistIsPlaying=false;
        browseAndSelectSong();
    }

    private void MinimizeMouseClicked(java.awt.event.MouseEvent evt) {
        this.setState(Frame.ICONIFIED);
    }

    private void CloseMouseClicked(java.awt.event.MouseEvent evt) {
        System.exit(0);
    }

    private void SliderMousePressed(java.awt.event.MouseEvent evt) {
        if(musicPlayer.getSongNowPlaying()!=null) {
            int mouseX = evt.getX();
            int progressBarVal = (int) Math.round(((double) mouseX / (double) Slider.getWidth()) * Slider.getMaximum());
            musicPlayer.skipPartOfSong(progressBarVal);
            if(musicPlayer.isPlaying) {
                musicPlayer.pauseSong();
            }
            musicPlayer.timeWhenSongPaused = 0;
        }
    }

    private void SliderMouseReleased(java.awt.event.MouseEvent evt) {
        if(musicPlayer.getSongNowPlaying()!=null) {
            JSlider source = (JSlider) evt.getSource();
            musicPlayer.resumeSong(source.getValue());
        }
    }

    public void setSongName(String songName) {
        this.NameLabel.setText(songName);
    }

    public void setSongTimeLabel(String time) {
        this.songTimeLabel.setText(time);
    }

    public void setSongTotalTimeLabel(String time) {
        this.songTotalTimeLabel.setText(time);
    }

    public void setSongImage(Icon icon) {
        this.ImageLabel.setIcon(icon);
    }

    public void setLyrics(String lyrics) {
        this.jTextArea1.setText(lyrics);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new Music_interface().setVisible(true));
    }

    // Variables declaration - do not modify
    private JPanel BottomPanel;
    private JButton BrowseLabel;
    private JButton Close;
    private JLabel ImageLabel;
    private JPanel ImagePanel;
    private JPanel LeftPanel;
    private JPanel MainPanel;
    private JButton Minimize;
    private JLabel NameLabel;
    private JPanel NamePanel;
    private JButton Next;
    public JButton PausePlay;
    private JButton Playlist;
    private JButton LyricsButton;
    private JPopupMenu playlistMenu;
    private JMenuItem playPlaylist;
    private JMenuItem addSong;
    private JMenuItem removeSong;
    private JButton Previous;
    private JSlider Slider;
    private JPanel TopPanel;
    private JLabel songTimeLabel;
    private JLabel songTotalTimeLabel;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private Boolean songRemoved=false;
    // End of variables declaration

    public void setPausePlayIcon(Icon icon){
        PausePlay.setIcon(icon);
    }

    public void setSliderValue(int a){
        Slider.setValue(a);
    }

    public void setjTextArea1ToLyrics(){
        jTextArea1.setText("Lyrics");
        if(musicPlayer.getSongNowPlaying()!=null){
            if(!musicPlayer.getSongNowPlaying().getLyrics().isEmpty()){
                jTextArea1.setText(musicPlayer.getSongNowPlaying().getLyrics());
            }
        }
    }

    public void browseAndSelectSong(){
        Next.setEnabled(false);
        filechooser=new JFileChooser();
        filechooser.setFileFilter(filter);
        filechooser.setCurrentDirectory(new File("songs/"));
        int result=filechooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            if(musicPlayer.isPlaying){
                musicPlayer.pauseSong();
            }
            musicPlayer.songpath= filechooser.getSelectedFile().getPath();
            musicPlayer.NewSongSelected=true;
            musicPlayer.playSong();
        }
        updateGUIWhenSongPlays();
    }

    public void AddASong(){
        filechooser=new JFileChooser();
        filechooser.setFileFilter(filter);
        filechooser.setCurrentDirectory(new File("songs/"));
        int result=filechooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            musicPlayer.addSongInSongs(filechooser.getSelectedFile().getPath());
        }
    }

    public void enableSlider(){
        Slider.setEnabled(true);
    }

    public void updatePlaylistDisplay(){
        if(!musicPlayer.songs.isEmpty() && musicPlayer.playlistLoaded) {
            jTextArea1.setText("");
            if(songRemoved) {
                jTextArea1.setText("");
            }
            for (Song song : musicPlayer.songs) {
                jTextArea1.append(song.getname() + " by " + song.getartist() + "\n");
            }
        }else{
            jTextArea1.setText("No songs added yet :(");
        }
    }

    public void updateGUIWhenSongPlays(){
        if (musicPlayer.getSongNowPlaying() != null) {
            if(!musicPlayer.getSongNowPlaying().getLyrics().isEmpty()) {
                setjTextArea1ToLyrics();
            }else {
                jTextArea1.setText("Sorry, no lyrics available for this song :(");
            }
            NameLabel.setText(musicPlayer.getSongNowPlaying().getname());
            if(musicPlayer.getSongNowPlaying().getIcon()!=null) {
                ImageLabel.setIcon(musicPlayer.getSongNowPlaying().getIcon());
            }else{
                ImageLabel.setIcon(null);
            }
            songTotalTimeLabel.setText(MusicPlayer.formatTime(musicPlayer.getSongNowPlaying().getLengthinSeconds()));
            Slider.setMaximum(musicPlayer.getSongNowPlaying().getmp3file().getFrameCount());
        } else {
            NameLabel.setText("");
            ImageLabel.setIcon(null);
            songTotalTimeLabel.setText("0:00");
            songTimeLabel.setText("0:00");
            jTextArea1.setText("Lyrics");
        }
    }

    public void setNextButton(Boolean b){
        Next.setEnabled(b);
    }

    public void setjTextArea1(String s){
        jTextArea1.setText(s);
    }

}
