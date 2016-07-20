package jp.ac.dendai.c.jtp.openglesutil.Util;

/**
 * FPSの制御だったり、表示だったりする
 * めちゃくちゃ作り掛け
 * @author
 *
 */
public class FpsController {
	/**
	 * 現在FPS
	 */
	private short fps = 0;
	/**
	 * 固定したいFPS
	 */
	private short settedFps;
	/**
	 * カウンター
	 */
	private short fpsCounter = 0;
	/**
	 * FPS計測用
	 */
	private long time;
	/**
	 * コンストラクタ
	 */
	public FpsController(short i){
		settedFps = i;
	}
	/**
	 * FPS制御関数
	 */
	public void updateFps(){
		if(fpsCounter > 0){	//二回目起動以降
			long differenceTime = System.currentTimeMillis()-time;
			long sleepTime = (fpsCounter*1000/settedFps)-differenceTime;
			if(sleepTime>0){
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			}
			if(differenceTime>=1000){
				fps = fpsCounter;
				fpsCounter = -1;
			}
		}
		else{
			time = System.currentTimeMillis();
		}
	    fpsCounter++;
	}
	/**
	 * FPSのゲッタ
	 * @return　FPS
	 */
	public short getFps(){
		return fps;
	}

}
