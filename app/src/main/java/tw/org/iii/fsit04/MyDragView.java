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
    private int actionBarHeight, vHeight;
    private MyListView listView;
    private View myView;
    private ArrayList<HashMap<String,String>> data;
    private float handInt,rowInt;
    private SimpleAdapter simpleAdapter;
    private TextView tv;
    private int upOrdown;
    public MyDragView(Context context){
        super(context);

    }
    public MyDragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        data = new ArrayList<>();
        for(int i=0;i<20;i++){
            HashMap<String,String> m1 =new HashMap<>();
            m1.put("title",i+"");
            m1.put("texts","abc "+i);
            data.add(m1);
        }
        LayoutInflater mInflater = LayoutInflater.from(context);
        myView = mInflater.inflate(R.layout.dragview, null);
        addView(myView);
        //取得螢幕寬高
        DisplayMetrics dm = new DisplayMetrics();
        Activity a1 =(Activity)context;
        a1.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int vWidth = dm.widthPixels;
        vHeight= dm.heightPixels;
        Log.v("chad",vHeight+"");
        intitListView();


    }
    public ArrayList<HashMap<String,String>> getDataList(){
        return data;
    }
    public SimpleAdapter getSimpleAdapter(){
        return simpleAdapter;
    }
    public MyListView getListView(){
        return listView;
    }
    private void intitListView(){
        String[] from =new String[]{"title","texts"};
        int[] to =new int[]{R.id.title,R.id.texts};
        listView =myView.findViewById(R.id.listview);
        simpleAdapter=new SimpleAdapter(this.getContext(),data, R.layout.sample_list,from,to);
        listView.setAdapter(simpleAdapter);

    }



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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float xxx =rowInt-event.getRawY();
        float moveDiatance =this.getY()-xxx;
        this.setY(moveDiatance);
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            handInt =ev.getY();
            rowInt =ev.getRawY();

        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {

                float temp = handInt - ev.getY();
                //向上
                if (temp >20 ) {
                    if(this.getY()!=0&&isFirstItemVisible()) {
//                        upOrdown=0;
                        return  true;
                    }
                //向下
                } else if (temp < -20) {
                    if(this.getY()==0&&isFirstItemVisible()) {
//                        upOrdown=1;
                        return  true;
                    }
                }


            return false;
        }

        return false;










    }

}
