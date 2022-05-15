package com.sword;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class OkioUtils {
	public static void fileCopy(String sourceFile, String aimFile) {
		fileCopy(new File(sourceFile), new File(aimFile));
	}
	
	public static void fileCopy(File sourceFile, File aimFile) {
		try(Source source = Okio.source(sourceFile);
				BufferedSink sink = Okio.buffer(Okio.sink(aimFile))) {
			sink.writeAll(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
