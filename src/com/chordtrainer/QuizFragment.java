package com.chordtrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizFragment extends Fragment {
	private static final String LOGTAG = "QuizFragment.java";
	// saved instance bundle keys:
	private static final String BKEY_QUESTIONCOUNT = "com.chordtrainer.questioncount";
	private static final String BKEY_CORRECTCOUNT = "com.chordtrainer.correctcount";
	private static final String BKEY_TYPES = "com.chordtrainer.types";
	private static final String BKEY_CORRECTINDEX = "com.chordtrainer.correctindex";
	private static final String BKEY_USERSELECT = "com.chordtrainer.userselect";
	private static final String BKEY_REPLAYCOUNT = "com.chordtrainer.replaycount";
	private static final String BKEY_INFOOPEN = "com.chordtrainer.infoopen";
	private static final String BKEY_RESULTSOPEN = "com.chordtrainer.resultsopen";
	// current chord:
	private static final String BKEY_CHORDROOT = "com.chordtrainer.chordroot";

	//
	private Context m_Context;
	private ChordApp m_App;
	private ChordPlay m_ChordPlay;
	private Prefs m_Prefs;
	// UI:
	private Button m_ButPlay;
	private Button m_ButSubmit;
	private RadioGroup m_GroupAnswers;
	private RadioButton[] m_RadioBut = new RadioButton[8];
	private TextView m_TextCount;
	private AlertDialog m_InfoDialog = null;
	private AlertDialog m_ResultsDialog = null;
	// Quiz values:
	private int m_Level = 1; // 0 to 4 - will be set by Prefs
	private boolean m_JazzPopMode = false; // will be set by Prefs
	private int m_MaxQuestions = 4; // length of quiz - will be set by Prefs
	private boolean m_ReplayLimit = false; // will be set by Prefs
	private int m_MaxReplays = 3; // will be set by Prefs
	// Counters
	private int m_QuestionCount = 0;
	private int m_CorrectCount = 0;
	private int m_ReplayCount = 0;
	// Current state:
	private int[] m_Types = new int[8]; // holds answer chord types
	//
	private int m_ButCount; // number of visible buttons - determined from quiz level
	//
	private int m_CorrectIndex = 0; // correct answer index
	private int m_CorrectType = 0; // correct chord type
	private Chord m_CurChord = null; // current playable chord

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public QuizFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_App = (ChordApp)getActivity().getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_quiz, container, false);

		m_Context = getActivity(); // this;
		m_ChordPlay = m_App.getChordPlay();
		m_Prefs = new Prefs(m_Context);

		// get preferences:
		m_Level = m_Prefs.getQuizLevel();
		m_JazzPopMode = m_Prefs.getJazzPopMode();
		m_MaxQuestions = m_Prefs.getQuizCount();
		m_ReplayLimit = m_Prefs.getReplayLimit();
		m_MaxReplays = m_Prefs.getReplayCount();

		m_ButPlay = (Button) v.findViewById(R.id.quiz_play_button);
		m_ButSubmit = (Button) v.findViewById(R.id.quiz_submit_button);
		m_TextCount = (TextView) v.findViewById(R.id.textCount);
		m_GroupAnswers = (RadioGroup) v.findViewById(R.id.rg_answers);

		m_RadioBut[0] = (RadioButton) v.findViewById(R.id.rb1);
		m_RadioBut[1] = (RadioButton) v.findViewById(R.id.rb2);
		m_RadioBut[2] = (RadioButton) v.findViewById(R.id.rb3);
		m_RadioBut[3] = (RadioButton) v.findViewById(R.id.rb4);
		m_RadioBut[4] = (RadioButton) v.findViewById(R.id.rb5);
		m_RadioBut[5] = (RadioButton) v.findViewById(R.id.rb6);
		m_RadioBut[6] = (RadioButton) v.findViewById(R.id.rb7);
		m_RadioBut[7] = (RadioButton) v.findViewById(R.id.rb8);

		if (savedInstanceState == null) {
			startNewQuiz();
			setNewState();
			updateUI();
			m_ChordPlay.playChord(m_CurChord, ChordPlay.PLAY_BOTH);
		} else
			restoreInstanceState(savedInstanceState);

		m_ButPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m_ReplayLimit == false || m_ReplayCount < m_MaxReplays) {
					m_ChordPlay.playChord(m_CurChord, ChordPlay.PLAY_BOTH);
					++m_ReplayCount;
				} else
					Toast.makeText(
							m_Context,
							m_Context.getResources().getText(
									R.string.replay_prompt), Toast.LENGTH_LONG)
							.show();
			}
		});

		m_ButSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int checked = getCheckedIndex();
				// nothing checked:
				if (checked == -1) { // warn
					Toast.makeText(
							m_Context,
							m_Context.getResources().getText(
									R.string.select_prompt), Toast.LENGTH_LONG)
							.show();
				} else { // update quiz:
					++m_QuestionCount;
					if (checked == m_CorrectIndex) // if the user got the right
													// answer
						++m_CorrectCount;
					ChordInfo info = m_ChordPlay.getChordInfo(m_Types[m_CorrectIndex]);
					
					String text = m_ChordPlay.getFullChordName(m_CurChord.m_Tones[0], m_CorrectType, m_Prefs.getJazzPopMode());
					infoDialog(checked == m_CorrectIndex, text); // info.m_Name);
				}
			}
		});

		return v;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onPause() {
		m_ChordPlay.shutUp();
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Log.d(LOGTAG, "onSaveInstanceState()");

		savedInstanceState.putInt(BKEY_QUESTIONCOUNT, m_QuestionCount);
		savedInstanceState.putInt(BKEY_CORRECTCOUNT, m_CorrectCount);
		savedInstanceState.putIntArray(BKEY_TYPES, m_Types);
		savedInstanceState.putInt(BKEY_CORRECTINDEX, m_CorrectIndex);
		savedInstanceState.putInt(BKEY_USERSELECT, getCheckedIndex());
		savedInstanceState.putBoolean(BKEY_INFOOPEN,
				m_InfoDialog == null ? false : true);
		savedInstanceState.putBoolean(BKEY_RESULTSOPEN,
				m_ResultsDialog == null ? false : true);
		savedInstanceState.putInt(BKEY_REPLAYCOUNT, m_ReplayCount);
		savedInstanceState.putInt(BKEY_CHORDROOT, (m_CurChord != null) ? m_CurChord.m_Tones[0] : -1);
		
		if (m_InfoDialog != null) {
			m_InfoDialog.dismiss();
			m_InfoDialog = null;
		}

		if (m_ResultsDialog != null) {
			m_ResultsDialog.dismiss();
			m_ResultsDialog = null;
		}

		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
	}

	private void restoreInstanceState(Bundle savedInstanceState) {
		int userSelect, chordRoot; // interLo, interHi;
		boolean infoOpen, resultsOpen;

		// Log.d(LOGTAG, "restoreInstanceState()");

		m_QuestionCount = savedInstanceState.getInt(BKEY_QUESTIONCOUNT);
		m_CorrectCount = savedInstanceState.getInt(BKEY_CORRECTCOUNT);
		m_Types = savedInstanceState.getIntArray(BKEY_TYPES);
		m_CorrectIndex = savedInstanceState.getInt(BKEY_CORRECTINDEX);
		m_ReplayCount = savedInstanceState.getInt(BKEY_REPLAYCOUNT);
		userSelect = savedInstanceState.getInt(BKEY_USERSELECT);
		infoOpen = savedInstanceState.getBoolean(BKEY_INFOOPEN);
		resultsOpen = savedInstanceState.getBoolean(BKEY_RESULTSOPEN);
		
		chordRoot = savedInstanceState.getInt(BKEY_CHORDROOT);

		m_CorrectType = m_Types[m_CorrectIndex];
		m_CurChord = (chordRoot != -1) ? m_ChordPlay.makeChord(chordRoot, m_CorrectType) : null;

		updateUI();

		if (userSelect != -1) {
			m_RadioBut[userSelect].setChecked(true); // restore check
			if (infoOpen == true) {
				ChordInfo info = m_ChordPlay.getChordInfo(m_Types[m_CorrectIndex]);
				infoDialog(userSelect == m_CorrectIndex, info.m_Name); // open
																		// info
																		// dialog
			}
		}

		if (resultsOpen == true || m_QuestionCount == m_MaxQuestions)
			resultsDialog(); // open results dialog
	}

	private int getCheckedIndex() {
		for (int k = 0; k < m_ButCount; ++k) {
			if (m_RadioBut[k].isChecked() == true)
				return k;
		}

		return -1;
	}

	private void startNewQuiz() {
		m_QuestionCount = 0;
		m_CorrectCount = 0;
	}

	private void setNewState() {
		m_CorrectIndex = m_ChordPlay.getQuizTypes(m_Types, m_Level, m_JazzPopMode);
		m_CorrectType = m_Types[m_CorrectIndex];
		m_CurChord = m_ChordPlay.getRandomChord(m_CorrectType);
		m_ReplayCount = 0;
		// Log.d(LOGTAG, "setNewState() correct type: " + m_CorrectType);
	}
	
	private void updateUI() {
		int k;
		ChordInfo info;
		boolean jazz = m_Prefs.getJazzPopMode();
		
		m_GroupAnswers.clearCheck();

		for(m_ButCount = 0; m_ButCount < m_Types.length && m_Types[m_ButCount] != -1; ++m_ButCount){
			info = m_ChordPlay.getChordInfo(m_Types[m_ButCount]);
			m_RadioBut[m_ButCount].setVisibility(View.VISIBLE);
			m_RadioBut[m_ButCount].setText(m_ChordPlay.getFullChordName(m_CurChord.m_Tones[0], m_Types[m_ButCount], jazz));
		}
		
		for(k = m_ButCount; k < m_Types.length; ++k)
			m_RadioBut[k].setVisibility(View.GONE);
		
		m_TextCount.setText((m_QuestionCount + 1) + "/" + m_MaxQuestions);
	}

	private void finishInfo() {
		if (m_QuestionCount < m_MaxQuestions) {
			setNewState();
			updateUI();
			m_ChordPlay.playChord(m_CurChord, ChordPlay.PLAY_BOTH);
		} else
			resultsDialog();
	}

	private void infoDialog(boolean success, String text) {
		// Log.d(LOGTAG, "infoDialog");

		LayoutInflater inflater = LayoutInflater.from(m_Context); // this);
		View layout = inflater.inflate(R.layout.dialog_info, null);

		final ImageView successIcon = (ImageView) layout
				.findViewById(R.id.infoSuccessIcon);
		final TextView intervalText = (TextView) layout
				.findViewById(R.id.infoIntervalText);
		final Button butNext = (Button) layout.findViewById(R.id.infoNextBut);
		final Button butReplay = (Button) layout
				.findViewById(R.id.infoReplayBut);

		AlertDialog.Builder builder = new AlertDialog.Builder(m_Context);
		builder.setView(layout);

		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				m_InfoDialog = null;
				finishInfo();
			}
		});

		butNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m_InfoDialog != null) {
					m_InfoDialog.dismiss();
					m_InfoDialog = null;
				}
				finishInfo();
			}
		});

		butReplay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_ChordPlay.playChord(m_CurChord, ChordPlay.PLAY_BOTH);
			}
		});

		successIcon.setImageResource(success == true ? R.drawable.question_right
						: R.drawable.question_wrong);
		intervalText.setText(text);
		// show replay button if wrong answer:
		butReplay.setVisibility(success == false ? View.VISIBLE : View.GONE);
		if (success == true) {
			m_ChordPlay.shutUp();
			MediaPlayer mp = MediaPlayer.create(m_Context, R.raw.right_ding);
			mp.start();
		} else {
			m_ChordPlay.shutUp();

		}
		m_InfoDialog = builder.show();
	}

	private void resultsDialog() {
		// Log.d(LOGTAG, "resultsDialog");

		if (m_InfoDialog != null) {
			m_InfoDialog.dismiss();
		}
		LayoutInflater inflater = LayoutInflater.from(m_Context);
		View layout = inflater.inflate(R.layout.dialog_results, null);

		final ImageView allRight = (ImageView) layout
				.findViewById(R.id.allRight);
		final TextView score = (TextView) layout
				.findViewById(R.id.resultsScore);
		final Button butNew = (Button) layout.findViewById(R.id.resultsNewBut);
		final Button butHome = (Button) layout.findViewById(R.id.resultsHome);
		allRight.setVisibility(View.GONE);

		AlertDialog.Builder builder = new AlertDialog.Builder(m_Context);
		builder.setView(layout);

		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				m_ResultsDialog = null;
				getActivity().finish();
			}
		});

		butNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				m_ResultsDialog.dismiss();
				m_ResultsDialog = null;
				startNewQuiz();
				setNewState();
				updateUI();
				m_ChordPlay.playChord(m_CurChord, ChordPlay.PLAY_BOTH);
			}
		});
		butHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(m_Context, HomeScreen.class);
				startActivityForResult(i, 0);
			}
		});

		score.setText(m_CorrectCount + "/" + m_MaxQuestions);
		if (m_CorrectCount == m_MaxQuestions) {
			allRight.setVisibility(View.VISIBLE);
		} else
			allRight.setVisibility(View.GONE);

		m_ResultsDialog = builder.show();
	}
}
