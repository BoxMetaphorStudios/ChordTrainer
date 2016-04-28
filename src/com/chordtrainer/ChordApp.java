package com.chordtrainer;

// InterApp is a global class visible to all activities.
// It contains the global InterPlay instance, used by FlashCards and Quiz.

import android.app.Application;
import android.content.res.Configuration;

public class ChordApp extends Application {
	private static final String LOGTAG = "ChordApp.java";
	private ChordPlay m_ChordPlay = null;

	@Override
	public void onCreate() {
		super.onCreate();

		m_ChordPlay = new ChordPlay(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public ChordPlay getChordPlay() {
		return m_ChordPlay;
	}
}
