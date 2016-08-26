package jp.ac.dendai.c.jtp.openglesutil.Util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by テツヤ on 2016/08/26.
 */
public class BufferCreater {
    public static FloatBuffer createFloatBuffer(float[] array){
        if (array == null) throw new IllegalArgumentException();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * array.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        return floatBuffer;
    }
}
