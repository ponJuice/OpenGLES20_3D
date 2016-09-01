package jp.ac.dendai.c.jtp.Graphics.Renderer;

import jp.ac.dendai.c.jtp.Game.GameObject;

/**
 * Created by Goto on 2016/09/01.
 */
public abstract class Renderer {
    public class RenderItem{
        public RenderItem next,prev;
        public GameObject object;
        public boolean enabled;
    }
    private int registItemNum = 0;
    private int maxItemNum = 0;
    private RenderItem ite;

    //private RenderItem item;
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
    public void addItem(GameObject object){
        if(ite == null) {
            //一番最初
            ite = new RenderItem();
            object.item = ite;
            ite.object = object;
            ite.next = ite;
            ite.prev = ite;
            maxItemNum++;
        }else if(ite.next.object != null){
            //空きがない→新しく空きを作成
            RenderItem temp = new RenderItem();
            object.item = temp;
            temp.object = object;
            temp.prev = ite;
            temp.next = ite.next;
            ite.next.prev = temp;
            ite.next = temp;
            ite = temp;
            maxItemNum++;
        }else{
            //空きがある→そのまま代入
            object.item = ite.next;
            ite.next.object = object;
            ite = ite.next;
        }
        registItemNum++;
    }
    public void removeItem(GameObject object){
        if(object.item == null)
            return;

        object.item.prev.next = object.item.next;
        object.item.next.prev = object.item.prev;

        object.item.prev = ite;
        object.item.next = ite.next;
        ite.next.prev = object.item;
        ite.next = object.item;
        object.item.object = null;
        object.item = null;
        registItemNum--;
    }
    public void clear(){
        RenderItem temp = ite;
        do{
            if(ite.object != null) {
                ite.object.item = null;
                ite.object = null;
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
        RenderItem temp = ite;
        do{
            if(temp.object != null && temp.enabled)
                temp.object.getMeshRenderer().draw();
            temp = temp.prev;
        }while(temp != null && temp != ite);
    }
    public abstract void draw();
}
