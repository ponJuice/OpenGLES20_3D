package jp.ac.dendai.c.jtp.Graphics.Model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

import jp.ac.dendai.c.jtp.Graphics.Matelial;
import jp.ac.dendai.c.jtp.TouchUtil.TouchListener;

/**
 * Created by Goto on 2016/07/26.
 */
public class ModelObjectTemplate {
    public LinkedList<ModelObject> children;
    public Matelial[] matelial;
    public FloatBuffer vertex;
    public IntBuffer v_indices;
}
