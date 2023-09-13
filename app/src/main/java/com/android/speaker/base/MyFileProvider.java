package com.android.speaker.base;

import androidx.core.content.FileProvider;

import com.chinsion.SpeakEnglish.R;

public class MyFileProvider extends FileProvider {
   public MyFileProvider() {
       super(R.xml.file_paths);
   }
}