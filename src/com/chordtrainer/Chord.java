package com.chordtrainer;

public class Chord {
	public int m_ToneCount;
	public int[] m_Tones;

	public Chord(int root, int[] intervals) {
		int k, len = intervals.length;

		m_ToneCount = len + 1;
		m_Tones = new int[m_ToneCount];

		m_Tones[0] = root;

		for (k = 0; k < len; ++k)
			m_Tones[k + 1] = root + intervals[k];
	}
}
