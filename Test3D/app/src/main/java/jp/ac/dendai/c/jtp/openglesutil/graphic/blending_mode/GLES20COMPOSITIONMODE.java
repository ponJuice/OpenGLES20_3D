package jp.ac.dendai.c.jtp.openglesutil.graphic.blending_mode;

import android.opengl.GLES20;


public enum GLES20COMPOSITIONMODE {
	ALPHA{
		@Override
		public void setBlendMode() {
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
		}
	},
	ADD{
		@Override
		public void setBlendMode() {
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE);
		}
	},
	SUB{
		@Override
		public void setBlendMode() {
			GLES20.glBlendFunc(GLES20.GL_ONE_MINUS_DST_COLOR,GLES20.GL_ZERO);
		}
	}	;
	public abstract void setBlendMode();
}
