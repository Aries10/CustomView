package com.linechartdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dell-pc on 2017/12/19.
 */

public class MyLineChartView extends View {

    /**
     * 定义画笔
     */
    private Paint paintXY; //画XY轴的
    private Paint paintTextXY ; //画字
    private Paint paintGrid; //画网格
    private Paint paintChart; //画折线

    private int viewSize; //获取空间的尺寸,也是我们布局大小的尺寸

    //上下左右
    private float left;
    private float right;
    private float top;
    private float bottom;

    //路径对象
    private Path path;
    private Path pathChart;
    private float xWidth;
    private float yHeight;
    private float grid_width;
    private float grid_height;

    //定义数据的数组
    private int[] data = new int[]{20,70,50,60,30};

    public MyLineChartView(Context context) {
        super(context);
    }

    public MyLineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {

        //画xy轴
        paintXY = new Paint();
        paintXY.setColor(Color.parseColor("#FFF10760"));
        paintXY.setAntiAlias(true);
        paintXY.setStrokeWidth(4);
        paintXY.setStyle(Paint.Style.STROKE);

        //坐标上的提示文字
        paintTextXY = new Paint();
        paintTextXY.setColor(Color.BLACK);
        paintTextXY.setAntiAlias(true);
        paintTextXY.setDither(true);  //防抖动
        paintTextXY.setLinearText(true); //设置线性文本标识

        //画网格
        paintGrid = new Paint();
        paintGrid.setStrokeWidth(2);
        paintGrid.setColor(Color.parseColor("#FFF10760"));
        paintGrid.setStyle(Paint.Style.STROKE);
        paintGrid.setAntiAlias(true);

        //折线颜色FF19EAEA
        paintChart = new Paint();
        paintChart.setStrokeWidth(2);
        paintChart.setColor(Color.parseColor("#FF19EAEA"));
        paintChart.setStyle(Paint.Style.STROKE);
        paintChart.setAntiAlias(true);
    }

    //通过这个方法获取父类控件的宽度 然后把宽度分成16份

    /**
     *
     * @param w   当前view的宽
     * @param h   当前view的高
     * @param oldw   改变前的宽
     * @param oldh   改变前的高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取到每个用到的坐标点和尺寸
        viewSize = w; //获取空间的尺寸
        Log.i("Text","viewSize:"+viewSize);
        //上下左右
        left = viewSize*(1/16f);
        right = viewSize*(15/16f);
        top = viewSize* (1/16f);
        bottom = viewSize*(8/16f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //计算当前组件XY轴的长度
        xWidth = getWidth() - left;
        yHeight = getHeight() - bottom;
        Log.i("aaa","当前组件X轴的长度==="+xWidth+"当前组件Y轴的长度==="+yHeight);

        //计算网格线的宽高
        grid_width = (xWidth - bottom) / 13;
        grid_height = (yHeight - top) / 2;
        Log.i("aaa","网格宽："+grid_width+"===网格高："+grid_height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画XY轴
        drawXY(canvas);

        //绘制X和Y的提示文字
        drawXYText(canvas);

    }

    //绘制X和Y的提示文字
    private void drawXYText(Canvas canvas) {
        paintTextXY.setTextSize(26);
        //Y轴提示文字
        paintTextXY.setTextAlign(Paint.Align.LEFT); //左对齐
        canvas.drawText("交易额(K)",left+20,top,paintTextXY);
        //X轴提示文字
        paintTextXY.setTextAlign(Paint.Align.RIGHT);  //右对齐
        canvas.drawText("23:59",right,bottom+50,paintTextXY);
    }

    //画XY轴
    private void drawXY(Canvas canvas) {

        path = new Path();
        /**
         * moveTo  移动到下一个操作起点的位置
         * lineTo   连接上一个点到当前点之间的直线
         */
        path.moveTo(left,top);
        path.lineTo(left,bottom);
        path.lineTo(right,bottom);

        //使用path连接这三个坐标
        canvas.drawPath(path,paintXY);

        drawGrid(canvas);

        //释放画布
        canvas.restore();
    }

    //画网格
    private void drawGrid(Canvas canvas) {
        //绘制网格线  Y轴不变  绘制X轴
//        float heng = bottom-top;
        paintTextXY.setTextSize(25);
        float gridHeight = 0;
        for (int i = 0; i <2; i++) {
            if(gridHeight == 0){
                gridHeight = grid_height*i+top;
                canvas.drawLine(left,top*2,xWidth-left,top*2,paintGrid);
                canvas.drawText("00:00",left-20,bottom+50,paintTextXY);
            }else{
                gridHeight += grid_height;
                canvas.drawLine(left,top*5,xWidth-left,top*5,paintGrid);
                canvas.drawText("50",left-35,top*5,paintTextXY);
                canvas.drawText("100",left-35,top*2,paintTextXY);
            }
        }

        //绘制网格线  X轴不变  绘制Y轴
        float gridWidth = 0;
        for (int i = 0; i < 13; i++) {
            if(gridWidth == 0){
                gridWidth = grid_width*i+left;
                canvas.drawLine(gridWidth,2*top,gridWidth, bottom,paintGrid);
            }else{
                gridWidth += left;
                canvas.drawLine(gridWidth,2*top,gridWidth,bottom,paintGrid);
            }
        }

        //画点和线
        drawPoint(canvas);

    }

    //画点和线
    private void drawPoint(Canvas canvas) {
        paintGrid.setColor(Color.parseColor("#FF19EAEA"));
        paintGrid.setStrokeWidth(4);
        pathChart = new Path();
        for (int i = 0; i < data.length; i++) {

            float y= (bottom-top)*data[i]/(3*top);
            float v = bottom - y;

            canvas.drawCircle(left*(i+1),v,2,paintGrid);
            if(i==0){
                pathChart.moveTo(left*(i+1),v);
            }else{
                pathChart.lineTo(left*(i+1),v);
                pathChart.lineTo(left*(i+1),v);
            }
            canvas.drawPath(pathChart,paintChart);
        }
    }
}
