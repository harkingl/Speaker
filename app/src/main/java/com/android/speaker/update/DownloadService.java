package com.android.speaker.update;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.FileProvider;

import com.android.speaker.base.NotificationUtil;
import com.android.speaker.server.util.UrlManager;
import com.android.speaker.util.FileUtil;
import com.android.speaker.util.LogUtil;
import com.android.speaker.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/***
 * 下载apk服务
 */
public class DownloadService extends IntentService {
	private static final String TAG = "DownloadService";

	public static final String DOWNLOAD_LINK = "downloadLink";

	private static final String FILE_NAME = "speaker.apk";

	private static final int NOTIFICATION_ID = 111;
	private File saveFile;
	private String mLink = "";
	private NotificationUtil notificationUtil;
	public DownloadService() {
		super("Download");
	}

	private void prepareDir() throws IOException {
		File storeDir = new File(FileUtil.ROOT_FILE_PATH);
		if (!storeDir.exists()) {
			storeDir.mkdirs();
		}
		saveFile = new File(storeDir, FILE_NAME);
		if(saveFile.exists()){
			saveFile.delete();
		}
		saveFile.createNewFile();
	}

	private void createNotification() {
		notificationUtil = NotificationUtil.getInstance();
	}

	private void updateProgress(int length ,int count){
		String text;
		if(0 == count){
			text = "软件包大小：" + (length / 1024) + "KB";
		}else{
			text = String.format(Locale.CHINA, "已下载(%dKB/%dKB)...", count / 1024, length / 1024);
		}

		notificationUtil.showNotify(NOTIFICATION_ID, text, null);
	}

	private void installAPK(){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		Uri data = null;
		if(Build.VERSION.SDK_INT >= 24) {
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			data = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", saveFile);
		} else {
			data = Uri.fromFile(saveFile);
		}
		intent.setDataAndType(data, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		mLink = arg0.getStringExtra(DOWNLOAD_LINK);
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			prepareDir();
			createNotification();
			HttpURLConnection connection = (HttpURLConnection) new URL(UrlManager.formatUrl(mLink))
					.openConnection();
			fileOutputStream = new FileOutputStream(saveFile);
			int length = Integer.parseInt(connection
					.getHeaderField("Content-Length"));
			updateProgress(length,0);
			inputStream = connection.getInputStream();
			int temp = 0;
			int count = 0;
			byte[] data = new byte[10240];
			while ((temp = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, temp);
				count += temp;
				updateProgress(length,count);
			}

			installAPK();

		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		} finally {
			try {
				if(fileOutputStream != null) {
					fileOutputStream.flush();
					fileOutputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}

			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage());
			}
		}
		if(null != notificationUtil){
			notificationUtil.clearNotify(NOTIFICATION_ID);
		}
	}
}
