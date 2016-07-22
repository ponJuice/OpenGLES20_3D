package jp.ac.dendai.c.jtp.TouchUtil;

public class Input{
	private static int touchCount;
	private static Touch[] touch;
	private static int maxTouch;
	static{
		touchCount = 0;
		maxTouch = 2;
		touch = new Touch[2];
		for(int n = 0;n < 2 ;n++){
			touch[n] = new Touch();
		}
	}
	public static void setTouchCount(int count){
		touchCount = count;
	}
	public static void addTouchCount(){
		touchCount++;
	}
	public static void subTouchCount(){
		touchCount--;
	}
	public static int getTouchCount(){
		return touchCount;
	}
	public static Touch getTouch(int pointerId){
		for(Touch t : touch){
			if(t.getTouchID() == pointerId)
				return t;
		}
		return null;
	}
	public static Touch getTouch(){
		for(Touch t : touch){
			if(t.getTouchID() < 0)
				return t;
		}
		return null;
	}
	public static int getMaxTouch(){
		return maxTouch;
	}
	public static Touch[] getTouchArray(){
		return touch;
	}
	public static void setMaxTouch(int n){
		Touch[] temp = touch;
		if(maxTouch < n){
			touch = new Touch[n];
			for(int m = 0;m < n;m++){
				if(m < temp.length){
					touch[m] = temp[m];
				}
				else{
					touch[m] = new Touch();
				}
			}
		}
		else if(n < maxTouch){
			for(int m = 0;m < n;m++){
				touch[m] = temp[m];
			}
		}
	}
}
