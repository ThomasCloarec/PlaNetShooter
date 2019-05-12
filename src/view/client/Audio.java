package view.client;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class Audio {
    private Clip clip;
    public Audio(String fileName) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(fileName);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream sound = AudioSystem.getAudioInputStream(bufferedIn);

            clip = AudioSystem.getClip();
            clip.open(sound);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Audio: Malformed URL: " + e);
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException("Audio: Unsupported Audio File: " + e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Audio: Input/Output Error: " + e);
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
            throw new RuntimeException("Audio: Line Unavailable Exception Error: " + e);
        }
    }
    public void play(){
        if (!clip.isRunning()) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    @SuppressWarnings("unused")
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @SuppressWarnings("unused")
    public void stop(){
        clip.stop();
    }
}
