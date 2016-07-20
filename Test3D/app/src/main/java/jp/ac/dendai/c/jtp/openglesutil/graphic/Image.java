package jp.ac.dendai.c.jtp.openglesutil.graphic;

import android.graphics.Bitmap;

import jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode.GLES20COMPOSITIONMODE;

public class Image {
	private int program;
	private Bitmap image;
	private GLES20COMPOSITIONMODE mode;
	public Bitmap getImage() {
		return image;
	}
	public GLES20COMPOSITIONMODE getBlend() {
		return mode;
	}
}
