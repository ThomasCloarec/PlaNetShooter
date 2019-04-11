package view.client;

import javax.sound.sampled.*;
import java.io.IOException;

public class Audio {
    private Clip clip;

    public Audio(String resourceName) {
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(Audio.class.getResource(resourceName));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            if (clip != null) {
                clip.open(audioIn);
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public void play(boolean loop) {
        this.play();
        if (loop)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
