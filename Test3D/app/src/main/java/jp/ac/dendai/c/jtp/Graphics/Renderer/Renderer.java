package jp.ac.dendai.c.jtp.Graphics.Renderer;

import jp.ac.dendai.c.jtp.Game.GameObject;
import jp.ac.dendai.c.jtp.Graphics.Shader.Shader;

/**
 * Created by Goto on 2016/09/01.
 */
public class Renderer {
    class RenderItem{
        public RenderItem next,prev;
        public RenderMediator rm;
    }
    private int registItemNum = 0;
    private int maxItemNum = 0;
    private RenderItem ite;

    //private RenderItem item;
    protected Shader shader;
    protected boolean enabled;
    public Renderer(){
        enabled = true;
    }
    public boolean getEnabled(){
        return enabled;
    }
    public void setEnabled(boolean e){
        enabled = e;
    }
    public void setShader(Shader shader){
        this.shader = shader;
    }
    public void addItem(GameObject object){
        if(ite == null) {
            //一番最初
            ite = new RenderItem();
            object.getRenderMediator().item = ite;
            ite.rm = object.getRenderMediator();
            ite.next = ite;
            ite.prev = ite;
            maxItemNum++;
        }else if(ite.next.rm != null){
            //空きがない→新しく空きを作成
            RenderItem temp = new RenderItem();
            object.getRenderMediator().item = temp;
            temp.rm = object.getRenderMediator();
            temp.prev = ite;
            temp.next = ite.next;
            ite.next.prev = temp;
            ite.next = temp;
            ite = temp;
            maxItemNum++;
        }else{
            //空きがある→そのまま代入
            object.getRenderMediator().item = ite.next;
            ite.next.rm = object.getRenderMediator();
            ite = ite.next;
        }
        object.getRenderMediator().renderer = this;
        registItemNum++;
    }
    public void removeItem(GameObject object){
        if(object.getRenderMediator().renderer == null || object.getRenderMediator().renderer != this)
            return;

        object.getRenderMediator().item.prev.next = object.getRenderMediator().item.next;
        object.getRenderMediator().item.next.prev = object.getRenderMediator().item.prev;

        object.getRenderMediator().item.prev = ite;
        object.getRenderMediator().item.next = ite.next;
        ite.next.prev = object.getRenderMediator().item;
        ite.next = object.getRenderMediator().item;
        object.getRenderMediator().renderer = null;
        object.getRenderMediator().item = null;
        registItemNum--;
    }
    public void clear(){
        RenderItem temp = ite;
        do{
            if(ite.rm != null) {
                ite.rm.item = null;
                ite.rm = null;
            }
            ite = ite.next;
        }while(temp != ite);
        while(ite != null && ite.next != null){
            ite.prev.next = null;
            ite.prev = null;
            ite = ite.next;
        }
        ite = null;
    }
    public void drawAll(){
        if(ite == null)
            return;
        shader.useShader();
        shader.updateCamera();
        RenderItem temp = ite;
        do{
            if(temp.rm != null)
                temp.rm.draw();
            temp = temp.prev;
        }while(temp != null && temp != ite);
    }
}
