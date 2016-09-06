package jp.ac.dendai.c.jtp.Graphics.UI;

import jp.ac.dendai.c.jtp.Graphics.Shader.UiShader;
import jp.ac.dendai.c.jtp.TouchUtil.Touch;

/**
 * Created by Goto on 2016/09/06.
 */
public interface UI {
    void touch(Touch touch);
    void proc();
    void draw(UiShader shader);
}
