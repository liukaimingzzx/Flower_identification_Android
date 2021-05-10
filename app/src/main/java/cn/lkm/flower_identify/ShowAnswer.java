package cn.lkm.flower_identify;

import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowAnswer extends AppCompatActivity {


    private Button toHome;
    private Button toBaike;
    private String[] result;
    private TextView name;
    private TextView percent;
    private ImageView img_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_answer);
        Intent intent = getIntent();
        String data = intent.getStringExtra("str");
        String sub = data.substring(0,data.length());
        result = sub.split(",");
        for(String word:result){
            System.out.println(word);
        }
        initView();
        initEvent();

    }

    private void initEvent(){
        //返回首页按钮的监视器
        toHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAnswer.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //查看百科监听器
        toBaike.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowAnswer.this,Baike.class);
                intent.putExtra("url",result[2]);
                startActivity(intent);
            }
        });
    }

    private void initView(){
        toBaike = (Button) this.findViewById(R.id.toBaike);
        toHome = (Button) this.findViewById(R.id.toHome);
        name = (TextView) this.findViewById(R.id.text_view_flower_name);
        percent = (TextView) this.findViewById(R.id.text_view_flower_percent);
        img_view=this.findViewById(R.id.img_view);
        name.setText(result[0]);
        percent.setText("可能性为："+result[1].substring(0,4));
        String str = result[0].substring(result[0].indexOf("(")+1,result[0].indexOf(")"));
        if(str.equals("cerasus")) img_view.setImageResource(R.drawable.cerasus);
        if(str.equals("daisy")) img_view.setImageResource(R.drawable.daisy);
        if(str.equals("dandelion")) img_view.setImageResource(R.drawable.dandelion);
        if(str.equals("dianthus")) img_view.setImageResource(R.drawable.dianthus);
        if(str.equals("digitalis_purpurea")) img_view.setImageResource(R.drawable.digitalis_purpurea);
        if(str.equals("eschscholtzia")) img_view.setImageResource(R.drawable.eschscholtzia);
        if(str.equals("gazania")) img_view.setImageResource(R.drawable.gazania);
        if(str.equals("jasminum")) img_view.setImageResource(R.drawable.jasminum);
        if(str.equals("matthiola")) img_view.setImageResource(R.drawable.matthiola);
        if(str.equals("narcissus")) img_view.setImageResource(R.drawable.narcissus);
        if(str.equals("nymphaea")) img_view.setImageResource(R.drawable.nymphaea);
        if(str.equals("peach_blossom")) img_view.setImageResource(R.drawable.peach_blossom);
        if(str.equals("pharbitis")) img_view.setImageResource(R.drawable.pharbitis);
        if(str.equals("rhododendron")) img_view.setImageResource(R.drawable.rhododendron);
        if(str.equals("rosa")) img_view.setImageResource(R.drawable.rosa);
        if(str.equals("rose")) img_view.setImageResource(R.drawable.rose);
        if(str.equals("sunflowers")) img_view.setImageResource(R.drawable.sunflowers);
        if(str.equals("tithonia")) img_view.setImageResource(R.drawable.tithonia);
        if(str.equals("tropaeolum_majus")) img_view.setImageResource(R.drawable.tropaeolum_majus);
        if(str.equals("tulips")) img_view.setImageResource(R.drawable.tulips);







    }

}
