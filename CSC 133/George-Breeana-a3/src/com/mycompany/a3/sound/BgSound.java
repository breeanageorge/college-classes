package com.mycompany.a3.sound;

import java.io.IOException;
import java.io.InputStream;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

public class BgSound implements Runnable{
	private Media m;
	
	public BgSound (String fileName){
		try {
			InputStream iStream = Display.getInstance().getResourceAsStream(getClass(), "/"+fileName);
			m = MediaManager.createMedia(iStream, "audio/wav", this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void play() {
		m.play();
	}
	
	public void pause() {
		m.pause();
	}

	public void run() {
		m.setTime(0);
		m.play();
	}	
}