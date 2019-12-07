package com.bignerdranch.android.calculator_090;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button[] buttons = new Button[18];
    private int[] ids = new int[]{R.id.bt1,R.id.bt2,R.id.bt3,R.id.bt4,R.id.bt5,R.id.bt6,R.id.bt7,
            R.id.bt8,R.id.bt9,R.id.bt10,R.id.bt11,R.id.bt12,R.id.bt13,R.id.bt14,R.id.bt15,R.id.bt16,R.id.bt17,R.id.bt18
    };

    private TextView textView;
    private String expression = "";
    private boolean end = false;
    private int countOperate=2;
    /*
      记录当前输入操作符的数量，当赋值为2时表示输入了两个连续的操作符，即出错状态。
      初始化赋值为2,即强制用户最初时只能输入数字，或者输入小数点前面自动补0。
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i=0;i<ids.length;i++){
            buttons[i] = (Button)findViewById(ids[i]);
            buttons[i].setOnClickListener(this);
        }
        textView = (TextView)findViewById(R.id.contentText);
    }

    /*
    点击事件，对算式输入进行规范 比如不能连续输入++,23.23.23之类的无效算式
    算式的结果计算根据输入的算式（存储在string对象)计算出结果
     */
    public void onClick(View view) {
        int id = view.getId();
        Button button = (Button)view.findViewById(id);
        String current = button.getText().toString();
        if(end){ //如果上一次算式已经结束，则先清零
            expression = "";
            end = false;
        }
        if(current.equals("CE")){ //如果为CE则清零
            expression = "";
            countOperate=0;
        }else if(current.equals("Backspace")){ //如果点击退格
            if(expression.length()>1){ //算式长度大于1
                expression = expression.substring(0,expression.length()-1);//退一格
                int i = expression.length()-1;
                char tmp = expression.charAt(i); //获得最后一个字符
                char tmpFront = tmp;
                for(;i>=0;i--){ //向前搜索最近的：+ - * / .，并退出
                    tmpFront = expression.charAt(i);
                    if(tmpFront=='.'||tmpFront=='+'||tmpFront=='-'||tmpFront=='*'||tmpFront=='/'){
                        break;
                    }
                }
                if(tmp>='0'&&tmp<='9'){ //最后一个字符为数字，则识别数赋值为0
                    countOperate=0;
                }else if(tmp==tmpFront&&tmpFront!='.') countOperate=2; //如果为+-*/，赋值为2
                else if(tmpFront=='.') countOperate=1; //如果前面有小数点赋值为1
            }else if(expression.length()==1){
                expression = "";
            }
        }else if(current.equals(".")){
            if(expression.equals("")||countOperate==2){
                expression+="0"+current;
                countOperate = 1;  //小数点按过之后赋值为1
            }
            if(countOperate==0){
                expression+=".";
                countOperate = 1;
            }
        }else if(current.equals("+")||current.equals("-")||current.equals("*")||current.equals("/")){
            if(countOperate==0){
                expression+=current;
                countOperate = 2;  //  +-*/按过之后赋值为2
            }
        }else if(current.equals("=")){ //按下=时，计算结果并显示
            double result = count()/100*100;
            expression+="="+result;
            end = true; //此次计算结束
        }
        else{//此处是当退格出现2+0时，用current的值替代0
            if(expression.length()>=1){
                char tmp1 = expression.charAt(expression.length()-1);
                if(tmp1=='0'&&expression.length()==1){
                    expression = expression.substring(0,expression.length()-1);
                }
                else if(tmp1=='0'&&expression.length()>1){
                    char tmp2 = expression.charAt(expression.length()-2);
                    if(tmp2=='+'||tmp2=='-'||tmp2=='*'||tmp2=='/'){
                        expression = expression.substring(0,expression.length()-1);
                    }
                }
            }
            expression+=current;
            if(countOperate==2||countOperate==1) countOperate=0;
        }
        textView.setText(expression); //显示出来
    }
    //计算逻辑，求expression表达式的值
    private double count(){
        double result=0;
        double tNum=1,lowNum=0.1,num=0;
        char tmp=0;
        int operate = 1; //识别+-*/，为+时为正数，为-时为负数，为×时为-2/2,为/时为3/-3;
        boolean point = false;
        for(int i=0;i<expression.length();i++){ //遍历表达式
            tmp = expression.charAt(i);
            if(tmp=='.'){ //因为可能出现小数，此处进行判断是否有小数出现
                point = true;
                lowNum = 0.1;
            }else if(tmp=='+'||tmp=='-'){
                if(operate!=3&&operate!=-3){ //此处判断通用，适用于+-*
                    tNum *= num;
                }else{ //计算/
                    tNum /= num;
                }
                if(operate<0){ //累加入最终的结果
                    result -= tNum;
                }else{
                    result += tNum;
                }
                operate = tmp=='+'?1:-1;
                num = 0;
                tNum = 1;
                point = false;
            }else if(tmp=='*'){
                if(operate!=3&&operate!=-3){
                    tNum *= num;
                }else{
                    tNum /= num;
                }
                operate = operate<0?-2:2;
                point = false;
                num = 0;
            }else if(tmp=='/'){
                if(operate!=3&&operate!=-3){
                    tNum *= num;
                }else{
                    tNum /= num;
                }
                operate = operate<0?-3:3;
                point = false;
                num = 0;
            }else{
                //读取expression中的每个数字，double型
                if(!point){
                    num = num*10+tmp-'0';
                }else{
                    num += (tmp-'0')*lowNum;
                    lowNum*=0.1;
                }
            }
        }
        //循环遍历结束，计算最后一个运算符后面的数
        if(operate!=3&&operate!=-3){
            tNum *= num;
        }else{
            tNum /= num;
        }
        if(operate<0){
            result -= tNum;
        }else{
            result += tNum;
        }
        //返回最后的结果
        return result;
    }
}
