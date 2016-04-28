package com.chordtrainer;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class ChordPlay {
	private static final String LOGTAG = "ChordPlay.java";
	private static final int MAX_STREAMS = 5; // SoundPool streams
	private static final int TONE_DURATION = 560; // Interval playback tone
													// length in ms

	// Chord play modes
	public static final int PLAY_BROKEN = 0;
	public static final int PLAY_BLOCK = 1;
	public static final int PLAY_BOTH = 2;

	// Chord play volume:
	private static float PLAY_VOLUME = 0.4f;

	private Context m_Context;
	private Resources m_Res;
	private Tone[] m_Tone;
	private ChordInfo[] m_ChordInfo;
	private ChordInfo[] m_ShortChordInfo;

	private SoundPool m_SoundPool = null;
	private Chord m_CurChord = null;
	private int m_PlayMode = 0;
	private int m_PlayState = 0;
	private Timer m_PlayTimer = null;
	private Random m_Random = new Random();

	public ChordPlay(Context context) {
		m_Context = context;
		m_Res = context.getResources();
		m_SoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 100);

		initTones();
		initChordInfo();
	}

	// Build the tone information array:
	private void initTones() {
		m_Tone = new Tone[Tone.TONE_COUNT];

		m_Tone[Tone.D3] = new Tone(Tone.D3, m_SoundPool.load(m_Context,
				R.raw.d3, 1));
		m_Tone[Tone.Eb3] = new Tone(Tone.Eb3, m_SoundPool.load(m_Context,
				R.raw.e_flat3, 1));
		m_Tone[Tone.E3] = new Tone(Tone.E3, m_SoundPool.load(m_Context,
				R.raw.e3, 1));
		m_Tone[Tone.F3] = new Tone(Tone.F3, m_SoundPool.load(m_Context,
				R.raw.f3, 1));
		m_Tone[Tone.Fs3] = new Tone(Tone.Fs3, m_SoundPool.load(m_Context,
				R.raw.f_sharp3, 1));
		//
		m_Tone[Tone.G3] = new Tone(Tone.G3, m_SoundPool.load(m_Context,
				R.raw.g3, 1));
		m_Tone[Tone.Ab3] = new Tone(Tone.Ab3, m_SoundPool.load(m_Context,
				R.raw.a_flat3, 1));
		m_Tone[Tone.A3] = new Tone(Tone.A3, m_SoundPool.load(m_Context,
				R.raw.a3, 1));
		m_Tone[Tone.Bb3] = new Tone(Tone.Bb3, m_SoundPool.load(m_Context,
				R.raw.b_flat3, 1));
		m_Tone[Tone.B3] = new Tone(Tone.B3, m_SoundPool.load(m_Context,
				R.raw.b3, 1));
		m_Tone[Tone.C4] = new Tone(Tone.C4, m_SoundPool.load(m_Context,
				R.raw.c4, 1));
		m_Tone[Tone.Db4] = new Tone(Tone.Db4, m_SoundPool.load(m_Context,
				R.raw.d_flat4, 1));
		m_Tone[Tone.D4] = new Tone(Tone.D4, m_SoundPool.load(m_Context,
				R.raw.d4, 1));
		m_Tone[Tone.Eb4] = new Tone(Tone.Eb4, m_SoundPool.load(m_Context,
				R.raw.e_flat4, 1));
		m_Tone[Tone.E4] = new Tone(Tone.E4, m_SoundPool.load(m_Context,
				R.raw.e4, 1));
		m_Tone[Tone.F4] = new Tone(Tone.F4, m_SoundPool.load(m_Context,
				R.raw.f4, 1));
		m_Tone[Tone.Fs4] = new Tone(Tone.Fs4, m_SoundPool.load(m_Context,
				R.raw.f_sharp4, 1));
		m_Tone[Tone.G4] = new Tone(Tone.G4, m_SoundPool.load(m_Context,
				R.raw.g4, 1));
		m_Tone[Tone.Ab4] = new Tone(Tone.Ab4, m_SoundPool.load(m_Context,
				R.raw.a_flat4, 1));
		m_Tone[Tone.A4] = new Tone(Tone.A4, m_SoundPool.load(m_Context,
				R.raw.a4, 1));
		m_Tone[Tone.Bb4] = new Tone(Tone.Bb4, m_SoundPool.load(m_Context,
				R.raw.b_flat4, 1));
		m_Tone[Tone.B4] = new Tone(Tone.B4, m_SoundPool.load(m_Context,
				R.raw.b4, 1));
		m_Tone[Tone.C5] = new Tone(Tone.C5, m_SoundPool.load(m_Context,
				R.raw.c5, 1));
		m_Tone[Tone.Db5] = new Tone(Tone.Db4, m_SoundPool.load(m_Context,
				R.raw.d_flat5, 1));
		m_Tone[Tone.D5] = new Tone(Tone.D5, m_SoundPool.load(m_Context,
				R.raw.d5, 1));
		m_Tone[Tone.Eb5] = new Tone(Tone.Eb5, m_SoundPool.load(m_Context,
				R.raw.e_flat5, 1));
		m_Tone[Tone.E5] = new Tone(Tone.E5, m_SoundPool.load(m_Context,
				R.raw.e5, 1));
		m_Tone[Tone.F5] = new Tone(Tone.F5, m_SoundPool.load(m_Context,
				R.raw.f5, 1));
		m_Tone[Tone.Fs5] = new Tone(Tone.Fs5, m_SoundPool.load(m_Context,
				R.raw.f_sharp5, 1));
		m_Tone[Tone.G5] = new Tone(Tone.G5, m_SoundPool.load(m_Context,
				R.raw.g5, 1));
		//
		m_Tone[Tone.Ab5] = new Tone(Tone.Ab5, m_SoundPool.load(m_Context,
				R.raw.a_flat5, 1));
		m_Tone[Tone.A5] = new Tone(Tone.A5, m_SoundPool.load(m_Context,
				R.raw.a5, 1));
	}

	private void initChordInfo() {
		int k;
		m_ChordInfo = new ChordInfo[ChordInfo.CHORD_COUNT];

		// Easy:
		m_ChordInfo[ChordInfo.MAJ] = new ChordInfo(ChordInfo.MAJ, 3, new int[] {
				InterInfo.MAJ3RD, InterInfo.PERF5TH },
				m_Res.getString(R.string.maj_name),
				m_Res.getString(R.string.maj_sym),
				m_Res.getString(R.string.maj_desc),
				m_Res.getString(R.string.maj_info), R.drawable.major,
				new int[] { 250, 0, 0, 0, 67, 44 });
		m_ChordInfo[ChordInfo.MIN] = new ChordInfo(ChordInfo.MIN, 3, new int[] {
				InterInfo.MIN3RD, InterInfo.PERF5TH },
				m_Res.getString(R.string.min_name),
				m_Res.getString(R.string.min_sym),
				m_Res.getString(R.string.min_desc),
				m_Res.getString(R.string.min_info), R.drawable.minor,
				new int[] { 250, 0, 0, 0, 67, 44 });
		m_ChordInfo[ChordInfo.MAJs5] = new ChordInfo(ChordInfo.MAJs5, 3,
				new int[] { InterInfo.MAJ3RD, InterInfo.MIN6TH },
				m_Res.getString(R.string.maj_s5_name),
				m_Res.getString(R.string.maj_s5_sym),
				m_Res.getString(R.string.maj_s5_desc),
				m_Res.getString(R.string.maj_s5_info), R.drawable.augmented,
				new int[] { 250, 0, 0, 0, 67, 44 });
		m_ChordInfo[ChordInfo.DIM] = new ChordInfo(ChordInfo.DIM, 3, new int[] {
				InterInfo.MIN3RD, InterInfo.DIM5TH },
				m_Res.getString(R.string.dim_name),
				m_Res.getString(R.string.dim_sym),
				m_Res.getString(R.string.dim_desc),
				m_Res.getString(R.string.dim_info), R.drawable.diminished,
				new int[] { 250, 0, 0, 0, 67, 44 });
		// Standard:
		m_ChordInfo[ChordInfo.MAJ7] = new ChordInfo(ChordInfo.MAJ7, 4,
				new int[] { InterInfo.MAJ3RD, InterInfo.PERF5TH,
						InterInfo.MAJ7TH },
				m_Res.getString(R.string.maj7_name),
				m_Res.getString(R.string.maj7_sym),
				m_Res.getString(R.string.maj7_desc),
				m_Res.getString(R.string.maj7_info), R.drawable.major7th,
				new int[] { 0, 200, 0, 0, 67, 44 });
		m_ChordInfo[ChordInfo.DOM7] = new ChordInfo(ChordInfo.DOM7, 4,
				new int[] { InterInfo.MAJ3RD, InterInfo.PERF5TH,
						InterInfo.MIN7TH },
				m_Res.getString(R.string.dom7_name),
				m_Res.getString(R.string.dom7_sym),
				m_Res.getString(R.string.dom7_desc),
				m_Res.getString(R.string.dom7_info), R.drawable.dominant7th,
				new int[] { 0, 200, 0, 0, 67, 44 });
		m_ChordInfo[ChordInfo.MIN7] = new ChordInfo(ChordInfo.MIN7, 4,
				new int[] { InterInfo.MIN3RD, InterInfo.PERF5TH,
						InterInfo.MIN7TH },
				m_Res.getString(R.string.min7_name),
				m_Res.getString(R.string.min7_sym),
				m_Res.getString(R.string.min7_desc),
				m_Res.getString(R.string.min7_info), R.drawable.minorminor7th,
				new int[] { 0, 200, 0, 0, 67, 44 });
		m_ChordInfo[ChordInfo.MIN7b5] = new ChordInfo(ChordInfo.MIN7b5, 4,
				new int[] { InterInfo.MIN3RD, InterInfo.DIM5TH,
						InterInfo.MIN7TH },
				m_Res.getString(R.string.min7_b5_name),
				m_Res.getString(R.string.min7_b5_sym),
				m_Res.getString(R.string.min7_b5_desc),
				m_Res.getString(R.string.min7_b5_info),
				R.drawable.halfdiminished7th,
				new int[] { 0, 200, 0, 0, 67, 44 });
		m_ChordInfo[ChordInfo.DIM7] = new ChordInfo(ChordInfo.DIM7, 4,
				new int[] { InterInfo.MIN3RD, InterInfo.DIM5TH,
						InterInfo.MAJ6TH },
				m_Res.getString(R.string.dim7_name),
				m_Res.getString(R.string.dim7_sym),
				m_Res.getString(R.string.dim7_desc),
				m_Res.getString(R.string.dim7_info),
				R.drawable.fulldiminished7th,
				new int[] { 0, 200, 0, 0, 67, 44 });
		// Advanced:
		m_ChordInfo[ChordInfo.DOM7s5] = new ChordInfo(ChordInfo.DOM7s5, 4,
				new int[] { InterInfo.MAJ3RD, InterInfo.MIN6TH,
						InterInfo.MIN7TH },
				m_Res.getString(R.string.dom7_s5_name),
				m_Res.getString(R.string.dom7_s5_sym),
				m_Res.getString(R.string.dom7_s5_desc),
				m_Res.getString(R.string.dom7_s5_info), R.drawable.domaug7th,
				new int[] { 0, 0, 167, 0, 67, 44 });
		m_ChordInfo[ChordInfo.DOM9] = new ChordInfo(ChordInfo.DOM9, 5,
				new int[] { InterInfo.MAJ3RD, InterInfo.PERF5TH,
						InterInfo.MIN7TH, InterInfo.MAJ9TH },
				m_Res.getString(R.string.dom9_name),
				m_Res.getString(R.string.dom9_sym),
				m_Res.getString(R.string.dom9_desc),
				m_Res.getString(R.string.dom9_info), R.drawable.dominant9th,
				new int[] { 0, 0, 167, 0, 66, 44 });
		m_ChordInfo[ChordInfo.DOM7b9] = new ChordInfo(ChordInfo.DOM7b9, 5,
				new int[] { InterInfo.MAJ3RD, InterInfo.PERF5TH,
						InterInfo.MIN7TH, InterInfo.MIN9TH },
				m_Res.getString(R.string.dom7_b9_name),
				m_Res.getString(R.string.dom7_b9_sym),
				m_Res.getString(R.string.dom7_b9_desc),
				m_Res.getString(R.string.dom7_b9_info), R.drawable.domminor9th,
				new int[] { 0, 0, 167, 0, 66, 43 });
		m_ChordInfo[ChordInfo.DOM11] = new ChordInfo(ChordInfo.DOM11, 5,
				new int[] { InterInfo.PERF5TH, InterInfo.MIN7TH,
						InterInfo.MAJ9TH, InterInfo.PERF11TH },
				m_Res.getString(R.string.dom11_name),
				m_Res.getString(R.string.dom11_sym),
				m_Res.getString(R.string.dom11_desc),
				m_Res.getString(R.string.dom11_info), R.drawable.dominant11th,
				new int[] { 0, 0, 167, 0, 66, 43 });
		m_ChordInfo[ChordInfo.DOM13] = new ChordInfo(ChordInfo.DOM13, 5,
				new int[] { InterInfo.MAJ3RD, InterInfo.MIN7TH,
						InterInfo.MAJ9TH, InterInfo.MAJ13TH },
				m_Res.getString(R.string.dom13_name),
				m_Res.getString(R.string.dom13_sym),
				m_Res.getString(R.string.dom13_desc),
				m_Res.getString(R.string.dom13_info), R.drawable.dominant13th,
				new int[] { 0, 0, 166, 0, 66, 43 });
		m_ChordInfo[ChordInfo.DOM9b13] = new ChordInfo(ChordInfo.DOM9b13, 5,
				new int[] { InterInfo.MAJ3RD, InterInfo.MIN7TH,
						InterInfo.MAJ9TH, InterInfo.MIN13TH },
				m_Res.getString(R.string.dom9_b13_name),
				m_Res.getString(R.string.dom9_b13_sym),
				m_Res.getString(R.string.dom9_b13_desc),
				m_Res.getString(R.string.dom9_b13_info),
				R.drawable.domminor13th, new int[] { 0, 0, 166, 0, 66, 43 });
		// Pop/Jazz:
		m_ChordInfo[ChordInfo.MAJ6] = new ChordInfo(ChordInfo.MAJ6, 4,
				new int[] { InterInfo.MAJ3RD, InterInfo.PERF5TH,
						InterInfo.MAJ6TH },
				m_Res.getString(R.string.maj6_name),
				m_Res.getString(R.string.maj6_sym),
				m_Res.getString(R.string.maj6_desc),
				m_Res.getString(R.string.maj6_info), R.drawable.major6th,
				new int[] { 0, 0, 0, 125, 0, 43 });
		m_ChordInfo[ChordInfo.MIN6] = new ChordInfo(ChordInfo.MIN6, 4,
				new int[] { InterInfo.MIN3RD, InterInfo.PERF5TH,
						InterInfo.MAJ6TH },
				m_Res.getString(R.string.min6_name),
				m_Res.getString(R.string.min6_sym),
				m_Res.getString(R.string.min6_desc),
				m_Res.getString(R.string.min6_info), R.drawable.minor6th,
				new int[] { 0, 0, 0, 125, 0, 43 });
		m_ChordInfo[ChordInfo.MAJ69] = new ChordInfo(ChordInfo.MAJ69, 5,
				new int[] { InterInfo.MAJ3RD, InterInfo.PERF5TH,
						InterInfo.MAJ6TH, InterInfo.MAJ9TH },
				m_Res.getString(R.string.maj69_name),
				m_Res.getString(R.string.maj69_sym),
				m_Res.getString(R.string.maj69_desc),
				m_Res.getString(R.string.maj69_info), R.drawable.major6add9,
				new int[] { 0, 0, 0, 125, 0, 43 });
		m_ChordInfo[ChordInfo.MIN69] = new ChordInfo(ChordInfo.MIN69, 5,
				new int[] { InterInfo.MIN3RD, InterInfo.PERF5TH,
						InterInfo.MAJ6TH, InterInfo.MAJ9TH },
				m_Res.getString(R.string.min69_name),
				m_Res.getString(R.string.min69_sym),
				m_Res.getString(R.string.min69_desc),
				m_Res.getString(R.string.min69_info), R.drawable.minor6add9,
				new int[] { 0, 0, 0, 125, 0, 43 });
		m_ChordInfo[ChordInfo.DOM74] = new ChordInfo(ChordInfo.DOM74, 4,
				new int[] { InterInfo.PERF4TH, InterInfo.PERF5TH,
						InterInfo.MIN7TH },
				m_Res.getString(R.string.dom74_name),
				m_Res.getString(R.string.dom74_sym),
				m_Res.getString(R.string.dom74_desc),
				m_Res.getString(R.string.dom74_info),
				R.drawable.susdominant7th, new int[] { 0, 0, 0, 125, 0, 43 });
		m_ChordInfo[ChordInfo.MAJ9] = new ChordInfo(ChordInfo.MAJ9, 5,
				new int[] { InterInfo.MAJ3RD, InterInfo.PERF5TH,
						InterInfo.MAJ7TH, InterInfo.MAJ9TH },
				m_Res.getString(R.string.maj9_name),
				m_Res.getString(R.string.maj9_sym),
				m_Res.getString(R.string.maj9_desc),
				m_Res.getString(R.string.maj9_info), R.drawable.major9th,
				new int[] { 0, 0, 0, 125, 0, 43 });
		m_ChordInfo[ChordInfo.MIN9] = new ChordInfo(ChordInfo.MIN9, 5,
				new int[] { InterInfo.MIN3RD, InterInfo.PERF5TH,
						InterInfo.MIN7TH, InterInfo.MAJ9TH },
				m_Res.getString(R.string.min9_name),
				m_Res.getString(R.string.min9_sym),
				m_Res.getString(R.string.min9_desc),
				m_Res.getString(R.string.min9_info), R.drawable.minor9th,
				new int[] { 0, 0, 0, 125, 0, 43 });
		m_ChordInfo[ChordInfo.POW5] = new ChordInfo(ChordInfo.POW5, 2,
				new int[] { InterInfo.PERF5TH },
				m_Res.getString(R.string.pow5_name),
				m_Res.getString(R.string.pow5_sym),
				m_Res.getString(R.string.pow5_desc),
				m_Res.getString(R.string.pow5_info), R.drawable.powerchord,
				new int[] { 0, 0, 0, 125, 0, 43 });

		m_ShortChordInfo = new ChordInfo[ChordInfo.SHORT_CHORD_COUNT];

		for (k = 0; k < ChordInfo.SHORT_CHORD_COUNT; ++k)
			m_ShortChordInfo[k] = m_ChordInfo[k];
	}

	public ChordInfo[] getChordInfoArray() {
		return m_ChordInfo;
	}

	public ChordInfo[] getChordInfoArray(boolean full) {
		return (full == true) ? m_ChordInfo : m_ShortChordInfo;
	}

	public ChordInfo getChordInfo(int type) {
		if (type >= 0 && type < ChordInfo.CHORD_COUNT)
			return m_ChordInfo[type];
		else
			return null;
	}

	public Chord makeChord(int root, int type) {
		if (type >= 0 && type < ChordInfo.CHORD_COUNT)
			return new Chord(root, m_ChordInfo[type].m_Inter);
		else
			return null;
	}

	public String getChordSymbol(int root, int type) {
		return Tone.getNameFromId(root) + m_ChordInfo[type].m_Symbol;
	}

	public String getFullChordName(int root, int type, boolean symbol) {
		return m_ChordInfo[type].m_Name
				+ ((symbol == true) ? ("  -  " + getChordSymbol(root, type))
						: "");
	}

	public boolean playChord(Chord chord, int mode) {
		if (chord == null || m_SoundPool == null || mode < 0
				|| mode > PLAY_BOTH) {
			m_CurChord = null;
			return false;
		}

		m_SoundPool.autoPause();

		if (m_PlayTimer != null) {
			m_PlayTimer.cancel();
			m_PlayTimer = null;
		}

		m_CurChord = chord;
		m_PlayMode = mode;
		m_PlayState = 0;

		m_PlayTimer = new Timer();
		m_PlayTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				boolean stopFlag = false;
				int k;

				if (m_PlayTimer == null || m_CurChord == null)
					stopFlag = true;

				else if (m_PlayMode == PLAY_BROKEN || m_PlayMode == PLAY_BOTH) {
					if (m_PlayState < m_CurChord.m_ToneCount)
						m_SoundPool
								.play(m_Tone[m_CurChord.m_Tones[m_PlayState]].m_PoolId,
										PLAY_VOLUME, PLAY_VOLUME, 10, 0, 1.0f);
					else if (m_PlayMode == PLAY_BROKEN)
						stopFlag = true;
				}

				if ((m_PlayMode == PLAY_BLOCK && m_PlayState == 0)
						|| (m_PlayMode == PLAY_BOTH && m_PlayState == (m_CurChord.m_ToneCount + 1))) {
					for (k = 0; k < m_CurChord.m_ToneCount; ++k)
						m_SoundPool.play(
								m_Tone[m_CurChord.m_Tones[k]].m_PoolId,
								PLAY_VOLUME, PLAY_VOLUME, 10, 0, 1.0f);
				}

				else if ((m_PlayMode == PLAY_BLOCK && m_PlayState > 0)
						|| (m_PlayMode == PLAY_BOTH && m_PlayState > (m_CurChord.m_ToneCount + 1))) {
					stopFlag = true;
				}

				++m_PlayState;

				if (stopFlag == true) {
					cancel();
					m_CurChord = null;
					m_PlayState = 0;
					m_PlayTimer = null;
				}
			}
		}, 0, TONE_DURATION);

		return true;
	}

	// Stops any SoundPool playback:
	public void shutUp() {
		if (m_SoundPool != null)
			m_SoundPool.autoPause();

		if (m_PlayTimer != null) {
			m_PlayTimer.cancel();
			m_PlayTimer = null;
		}
	}

	private void clearUsedInfo() {
		int k;

		for (k = 0; k < ChordInfo.CHORD_COUNT; ++k)
			m_ChordInfo[k].m_Used = false;
	}

	private int getRandomType(int mass, int level) {
		int k, sum;
		int val = m_Random.nextInt(mass);

		for (k = sum = 0; k < ChordInfo.CHORD_COUNT; ++k) {
			if (m_ChordInfo[k].m_Used == false) {
				sum += m_ChordInfo[k].m_Weights[level];
				if (sum >= val) {
					// Log.w(LOGTAG, "getRandomType() - type: " + k);
					return k;
				}
			}
		}

		Log.w(LOGTAG, "getRandomType - type not found");
		return 0; // should not happen
	}

	// Fills the types array with random interval types. The array can have
	// variable length.
	// Returns a random index into the types array, which can be used to set the
	// "right interval" in the quiz.
	private int getRandomTypes(int[] types, int level) {
		int k, type;
		int len = 4; // types.length;
		int mass = 1000;

		clearUsedInfo();

		for (k = 0; k < len; ++k) {
			type = getRandomType(mass, level);
			types[k] = type;
			m_ChordInfo[type].m_Used = true;
			mass -= m_ChordInfo[type].m_Weights[level];
		}

		return m_Random.nextInt(len);
	}

	private int getAllTypes(int[] types, int level) {
		int k, count;

		for (k = count = 0; k < ChordInfo.CHORD_COUNT; ++k) {
			if (m_ChordInfo[k].m_Weights[level] > 0) {
				types[count] = m_ChordInfo[k].m_Type;
				++count;
			}
		}

		return m_Random.nextInt(count);
	}

	// Returns a random-root chord of a type:
	public Chord getRandomChord(int type) {
		ChordInfo info = m_ChordInfo[type];
		int range = Tone.TONE_COUNT - info.m_Inter[info.m_ToneCount - 2];
		int root = m_Random.nextInt(range);
		return makeChord(root, type);
	}

	public int getQuizTypes(int[] types, int level, boolean jazzMode) {
		int k;

		for (k = 0; k < types.length; ++k)
			types[k] = -1;

		if (level < 4)
			return getAllTypes(types, level);
		else
			return getRandomTypes(types, level + ((jazzMode == true) ? 1 : 0));
	}
}
