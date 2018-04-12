package tw.org.iii.fsit04;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by User on 2018/4/12.
 */

public class MyListView extends ListView {
    private TextView tv;
    private boolean isAtTop,isAtBottom;
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setOnTouchListener(new ListViewOntouch());


    }

//    private class ListViewOntouch implements OnTouchListener{
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//
//            return true;
//
//        }
//    }



//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int scrollRange = computeVerticalScrollRange();
//        int scrollOffset = computeVerticalScrollOffset();
//        int scrollExtend = computeVerticalScrollExtent();
//        if(scrollOffset == 0){
//            //AtTop
//            isAtTop= true;
//        }else if(scrollRange == scrollOffset + scrollExtend){
//            //AtBottom
//            isAtBottom=true;
//        }
//        if(!isAtTop && !isAtBottom){
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//        return super.onInterceptTouchEvent(ev);
//    }


}
