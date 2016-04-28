package com.chordtrainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A fragment representing a single Flashcard detail screen. This fragment is
 * either contained in a {@link FlashcardListActivity} in two-pane mode (on
 * tablets) or a {@link FlashcardPagerActivity} on handsets.
 */
public class FlashcardDetailFragment extends Fragment {
	private static final String LOGTAG = "FlashcardDetailFragment.java";

	public static final String ARG_ITEM_ID = "item_id";

	private ChordApp m_App;
	private int m_Pos;
	private Chord m_CurChord = null;

	private Button m_PlayBrokenButton;
	private Button m_PlayBlockButton;
	private TextView m_TitleField;
	private TextView m_DetailField;
	private TextView m_InfoField;
	private ImageView m_IntervalImage;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public FlashcardDetailFragment() {
	}

	public FlashcardDetailFragment(int pos) {
		m_Pos = pos;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_App = (ChordApp) getActivity().getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_flashcard_detail,
				container, false);

		ChordPlay chordPlay = m_App.getChordPlay();

		// Log.d(LOGTAG, "onCreateView pos: " + m_Pos);

		ChordInfo info = chordPlay.getChordInfo(m_Pos);
		m_CurChord = chordPlay.makeChord(Tone.C4, m_Pos);

		m_PlayBrokenButton = (Button) v.findViewById(R.id.play_broken_button);
		m_PlayBlockButton = (Button) v.findViewById(R.id.play_block_button);

		m_TitleField = (TextView) v.findViewById(R.id.flashcard_title_label);

		String text = chordPlay.getFullChordName(Tone.C4, m_Pos, new Prefs(
				getActivity()).getJazzPopMode());
		m_TitleField.setText(text);

		m_DetailField = (TextView) v.findViewById(R.id.flashcard_detail);
		m_DetailField.setText(info.m_Desc);

		m_InfoField = (TextView) v.findViewById(R.id.flashcard_info);
		m_InfoField.setText(info.m_Info);

		m_IntervalImage = (ImageView) v.findViewById(R.id.flashcard_image);
		m_IntervalImage.setImageResource(info.m_ImageId);

		m_PlayBrokenButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_App.getChordPlay().playChord(m_CurChord,
						ChordPlay.PLAY_BROKEN);
			}
		});

		m_PlayBlockButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_App.getChordPlay()
						.playChord(m_CurChord, ChordPlay.PLAY_BLOCK);
			}
		});

		m_App.getChordPlay().shutUp();

		return v;
	}
}
