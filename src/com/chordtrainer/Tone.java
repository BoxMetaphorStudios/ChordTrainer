package com.chordtrainer;

public class Tone {
	// Tone ids, also number of semitones from G3 and index into InterPlay
	// table.
	// Tone octave numbers comply to Scientific pitch notation.
	public static final int D3 = 0;
	public static final int Ds3 = 1;
	public static final int Eb3 = 1;
	public static final int E3 = 2;
	public static final int F3 = 3;
	public static final int Fs3 = 4;
	public static final int Gb3 = 4;
	//
	public static final int G3 = 5;
	public static final int Gs3 = 6;
	public static final int Ab3 = 6;
	public static final int A3 = 7;
	public static final int As3 = 8;
	public static final int Bb3 = 8;
	public static final int B3 = 9;
	public static final int C4 = 10;
	public static final int Cs4 = 11;
	public static final int Db4 = 11;
	public static final int D4 = 12;
	public static final int Ds4 = 13;
	public static final int Eb4 = 13;
	public static final int E4 = 14;
	public static final int F4 = 15;
	public static final int Fs4 = 16;
	public static final int Gb4 = 16;
	public static final int G4 = 17;
	public static final int Gs4 = 18;
	public static final int Ab4 = 18;
	public static final int A4 = 19;
	public static final int As4 = 20;
	public static final int Bb4 = 20;
	public static final int B4 = 21;
	public static final int C5 = 22;
	public static final int Cs5 = 23;
	public static final int Db5 = 23;
	public static final int D5 = 24;
	public static final int Ds5 = 25;
	public static final int Eb5 = 25;
	public static final int E5 = 26;
	public static final int F5 = 27;
	public static final int Fs5 = 28;
	public static final int Gb5 = 28;
	public static final int G5 = 29;
	//
	public static final int Gs5 = 30;
	public static final int Ab5 = 30;
	public static final int A5 = 31;

	public static final int MIN_TONE = D3;
	public static final int MAX_TONE = A5;
	public static final int TONE_COUNT = 32;

	public int m_Id;
	public int m_PoolId; // SoundPool index

	private static String[] m_ToneName = new String[] { "D\u00A0", "E\u266D",
			"E\u00A0", "F\u00A0", "F\u266F", "G\u00A0", "A\u266D", "A\u00A0",
			"B\u266D", "B\u00A0", "C\u00A0", "C\u266F" };

	public Tone(int id, int poolId) {
		m_Id = id;
		m_PoolId = poolId;
	}

	public static String getNameFromId(int id) {
		return m_ToneName[id % 12];
	}
}
