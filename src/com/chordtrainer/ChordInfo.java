package com.chordtrainer;

public class ChordInfo {
	// Chord types, also index into m_ChordInfo table:
	// Easy:
	public static final int MAJ = 0;
	public static final int MIN = 1;
	public static final int MAJs5 = 2;
	public static final int DIM = 3;
	// Standard:
	public static final int MAJ7 = 4;
	public static final int DOM7 = 5;
	public static final int MIN7 = 6;
	public static final int MIN7b5 = 7;
	public static final int DIM7 = 8;
	// Advanced:
	public static final int DOM7s5 = 9;
	public static final int DOM9 = 10;
	public static final int DOM7b9 = 11;
	public static final int DOM11 = 12;
	public static final int DOM13 = 13;
	public static final int DOM9b13 = 14;
	// Pop/Jazz:
	public static final int MAJ6 = 15;
	public static final int MIN6 = 16;
	public static final int MAJ69 = 17;
	public static final int MIN69 = 18;
	public static final int DOM74 = 19;
	public static final int MAJ9 = 20;
	public static final int MIN9 = 21;
	public static final int POW5 = 22;

	public static final int MIN_CHORD = MAJ;
	public static final int MAX_CHORD = POW5;
	public static final int CHORD_COUNT = 23;
	public static final int SHORT_CHORD_COUNT = 15; // without Pop/Jazz chords

	public int m_Type;
	public int m_ToneCount; // number of tones, length of m_Inter + 1 (root)
	public int[] m_Inter; // array of interval ids, min 1, max 4
	public String m_Name; // chord name
	public String m_Symbol; // pop/jazz symbol
	public String m_Desc; // chord description
	public String m_Info; // chord info
	public int m_ImageId; // drawable id
	public int[] m_Weights; // 6 probability weights for modes (easy, standard,
							// advanced, pop/jazz, all without pop, all with
							// pop)

	public boolean m_Used = false; // internal, used for random selection

	public ChordInfo(int type, int toneCount, int[] inter, String name,
			String symbol, String desc, String info, int imageId, int[] weights) {
		m_Type = type;
		m_ToneCount = toneCount;
		m_Inter = inter;
		m_Name = new String(name);
		m_Symbol = new String(symbol);
		m_Desc = new String(desc);
		m_Info = new String(info);
		m_ImageId = imageId;
		m_Weights = weights;
		m_Used = false;
	}

	public String toString() {
		return m_Name;
	}
}
