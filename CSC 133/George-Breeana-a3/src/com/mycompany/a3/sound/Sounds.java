package com.mycompany.a3.sound;

import java.io.IOException;
import java.io.InputStream;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

public class Sounds {
	private Media m;
	private static Sounds gameSounds;
	
	public Sounds (String fileName){
		try {
			InputStream iStream = Display.getInstance().getResourceAsStream(getClass(), "/"+fileName);
			m = MediaManager.createMedia(iStream, "audio/wav");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Sounds() {

	}

	public static Sounds getInstance() {
		if(gameSounds == null){
			gameSounds = new Sounds();
		}
		return gameSounds;
	}
	
	public void play() {
		m.setTime(0);
		m.play();
	}
	
	public void pause() {
		//m.setTime(0);
		m.pause();
	}	
}