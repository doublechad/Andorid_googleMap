package tw.org.iii.fsit04;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 2018/4/11.
 */

public class MyDragView extends LinearLayout{
    private MyListView listView;
    private View myView;
    private ArrayList<HashMap<String,String>> data;
    private float handInt,rowInt;
    private SimpleAdapter simpleAdapter;
    private int upOrdown;
    public MyDragView(Context context){
        super(context);

    }
    public MyDragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        data = new ArrayList<>();
//        for(int i=0;i<20;i++){
//            HashMap<String,String> m1 =new HashMap<>();
//            m1.put("title",i+"");
//            m1.put("texts","abc "+i);
//            data.add(m1);
//        }
        //利用mInflater 載入DragView的XML檔案
        LayoutInflater mInflater = LayoutInflater.from(context);
        myView = mInflater.inflate(R.layout.dragview, null);

        addView(myView);
        intitListView();


    }
    //返回 ListView 的資料
    public ArrayList<HashMap<String,String>> getDataList(){
        return data;
    }
    //返回 ListView 的Adapter
    public SimpleAdapter getSimpleAdapter(){
        return simpleAdapter;
    }
    //返回 MyListView
    public MyListView getListView(){
        return listView;
    }
    //初始化MyListView
    private void intitListView(){
        String[] from =new String[]{"title","texts"};
        int[] to =new int[]{R.id.title,R.id.texts};
        listView =myView.findViewById(R.id.listview);
        simpleAdapter=new SimpleAdapter(this.getContext(),data, R.layout.sample_list,from,to);
        listView.setAdapter(simpleAdapter);

    }


    //DragView 布局的時候 先設定初始位置0;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.setY(0);

    }
    //判斷LISTVIEW 是否置頂
    private boolean isFirstItemVisible() {
        final Adapter adapter = listView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }
        //第一个可见item在ListView中的位置
        if (listView.getFirstVisiblePosition() == 0) {
            //getChildCount是当前屏幕可见范围内的count
            int mostTop = (getChildCount() > 0) ? listView.getChildAt(0)
                    .getTop() : 0;
            if (mostTop >= 0) {
                return true;
            }
        }
        return false;
    }
    //處理滑動時候DragView的效果
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float xxx =rowInt-event.getRawY();
        float moveDiatance =this.getY()-xxx;
        if(moveDiatance>0&&moveDiatance<1960-600) {
            this.setY(moveDiatance);
        }
        if(xxx>0){
            upOrdown =0;
        }else if(xxx<0){
            upOrdown =1;
        }
        if(event.getAction()==MotionEvent.ACTION_UP) {
            if(upOrdown==1){
                Log.v("chad","movedown");
                this.setY(1960-600);
                upOrdown=2;

            }else if (upOrdown==0){
                Log.v("chad","moveup");
                this.setY(0);
                upOrdown=2;
            }
        }
        rowInt = event.getRawY();
        return super.onTouchEvent(event);
    }
    //攔截滑動手勢
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //ACTION_DOWN的時候先記錄 Y 跟RAWY 判斷 移動多少跟向上向下
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            handInt =ev.getY();
            rowInt =ev.getRawY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                float temp = handInt - ev.getY();
                //向上差過20 而且LISTVIEW置頂代表ACTION_MOVE_UP return true 給onTouchEvent;
                if (temp >20 ) {
                    if(this.getY()!=0&&isFirstItemVisible()) {
                        return  true;
                    }
                //向下差過20 而且LISTVIEW置頂代表ACTION_MOVE_Down return true 給onTouchEvent;
                } else if (temp < -20) {
                    if(this.getY()==0&&isFirstItemVisible()) {
                        return  true;
                    }
                }

            return false;
        }

        return false;










    }

}
