package com.android.speaker.chat.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.util.Log;

import com.android.speaker.util.LogUtil;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;

public class Audio {
	public static final String TAG = "Audio";
	
	public static final int DEFAULT_MAX_DURATION = 60*1000;
	
	private Context mContext;
	private MediaRecorder mAudioRecorder;

	private String mFilePath = null;

	private static Audio mAudio;
	private ExoPlayer mPlayer;
	private PlayListener mPlayListener;
	private RecordListener mRecordListener;
	
	public static synchronized Audio getInstance(Context context) {
		if (mAudio == null) {
			mAudio = new Audio(context);
		}
		return mAudio;
	}
	
	private Audio(Context context) {
		mContext = context.getApplicationContext();
//		initAudioRecorder();
	}
	
	private void initAudioRecorder() {
		mAudioRecorder = new MediaRecorder();
		mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//		mAudioRecorder.setMaxDuration(DEFAULT_MAX_DURATION);


		mAudioRecorder.setOnInfoListener(new OnInfoListener() {
			@Override
			public void onInfo(MediaRecorder mr, int what, int extra) {
				Log.d(TAG, String
						.format("Recorder-->what=%d&extra=%d.", what, extra));
				switch (what) {
				case MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED:
				case MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED:
					stopRecord();
					if(mRecordListener != null) {
						mRecordListener.onCompleted(mFilePath);
					}
					break;
				}	
			}
		});
		mAudioRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
			@Override
			public void onError(MediaRecorder mr, int what, int extra) {
				Log.d(TAG, String
						.format("Recorder(Error)-->what=%d&extra=%d.", what, extra));
				stopRecord();
				if(mRecordListener != null) {
					mRecordListener.onError();
				}
			}
		});
	}

	private void initPlayer() {
		if(mPlayer != null) {
			return;
		}
		mPlayer = new ExoPlayer.Builder(mContext).build();
		mPlayer.addListener(new Player.Listener() {
			@Override
			public void onEvents(Player player, Player.Events events) {
				Player.Listener.super.onEvents(player, events);
			}

			@Override
			public void onPlaybackStateChanged(int playbackState) {
				Player.Listener.super.onPlaybackStateChanged(playbackState);
				if(playbackState == Player.STATE_ENDED) {
					if(mPlayListener != null) {
						mPlayListener.onFinishPlay();
					}
					stopPlayer();
				}
			}

			@Override
			public void onPlayerError(PlaybackException error) {
				LogUtil.d(TAG, "onPlayerError: " + error.getMessage());
				Player.Listener.super.onPlayerError(error);
				if(mPlayListener != null) {
					mPlayListener.onError(error);
				}
				stopPlayer();
			}
		});
	}

	public void startPlayer(String url) {
		if(mPlayer == null) {
			initPlayer();
		}

		mPlayer.clearMediaItems();
		mPlayer.addMediaItem(MediaItem.fromUri(url));
		mPlayer.prepare();
		mPlayer.play();
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public void startRecord(String filePath, int maxTimeInMs) {
//		stopRecord();
		if (mAudioRecorder == null) {
			initAudioRecorder();
		}

		mFilePath = filePath;
//		mAudioRecorder.reset();
		mAudioRecorder.setOutputFile(filePath);
//		mAudioRecorder.setMaxDuration(maxTimeInMs);
		try {
			mAudioRecorder.prepare();
			mAudioRecorder.start();
		} catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
			if(mPlayListener != null) {
				mPlayListener.onError(e);
			}
		}
	}
	
	public void stopRecord() {
		if(mAudioRecorder != null) {
			try {
				mAudioRecorder.stop();
			}catch (Exception e){
				LogUtil.e(TAG,e.toString());
			}
		}
	}

	private void releaseRecord() {
		if(mAudioRecorder != null) {
			try {
				mAudioRecorder.stop();
			}catch (Exception e){
				LogUtil.e(TAG,e.toString());
			} finally {
				mAudioRecorder.release();
				mAudioRecorder = null;
			}
		}
	}

	private void stopPlayer() {
		if(mPlayer != null) {
			try {
				mPlayer.stop();
			}catch (Exception e){
				LogUtil.e(TAG,e.toString());
			}
		}
	}

	private void releasePlayer() {
		if(mPlayer != null) {
			try {
				mPlayer.stop();
			}catch (Exception e){
				LogUtil.e(TAG,e.toString());
			} finally {
				mPlayer.release();
				mPlayer = null;
			}
		}
	}

	public void release() {
		releasePlayer();
		releaseRecord();
		mAudio = null;
	}

	private int BASE = 1;
	public int getAmplitude() {
		if (mAudioRecorder != null) {
			try {
				double ratio = (double)mAudioRecorder.getMaxAmplitude() /BASE;
				double db = 0;// 分贝
				if (ratio > 1)
					db = 20 * Math.log10(ratio);
				LogUtil.d(TAG,"分贝值：" + db);

				return mAudioRecorder.getMaxAmplitude();
			} catch(Exception e) {
				LogUtil.e(TAG, e.getMessage());
			}
		}
		return 0;
	}
	
	public interface PlayListener {
		public void onFinishPlay();
		public void onError(Exception e);
	}

	public interface RecordListener {
		public void onCompleted(String filePath);
		public void onError();
	}

    public void setPlayListener(PlayListener l) {
		this.mPlayListener = l;
    }

	public void setRecordListener(RecordListener recordListener) {
		this.mRecordListener = recordListener;
	}
}
