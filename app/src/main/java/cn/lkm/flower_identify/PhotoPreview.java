 package cn.lkm.flower_identify;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.lkm.flower_identify.MainActivity.album;
import static cn.lkm.flower_identify.MainActivity.camera;
import static cn.lkm.flower_identify.MainActivity.flag;
import static java.lang.Thread.sleep;
public class PhotoPreview extends AppCompatActivity {


    private ImageView mPic;
    private Button mPicOk;
    private Uri uri;
    private long firstPressedTime;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpg");
    public String sdCardDir = Environment.getExternalStorageDirectory() + "/flower_get/";
    private String img_url;

    //如果按下退出

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            //super.onBackPressed();
            System.exit(0);
        } else {
            Toast.makeText(PhotoPreview.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }

    //初始化布局
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        initView();  //初始化布局
        initEvent(); //初始化事件
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            uri = data.getData();
            String []imgs={MediaStore.Images.Media.DATA}; //将图片URI转换成存储路径
            Cursor cursor=this.managedQuery(uri, imgs, null, null, null);
            int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            img_url=cursor.getString(index);
            Toast.makeText(PhotoPreview.this, "选择图片路径："+img_url, Toast.LENGTH_SHORT).show();
            //接收相册传递过来的图片
            mPic.setImageURI(uri);
        }
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    private void saveBitmap(Bitmap bitmap) {
        try {
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = sdf.format(new Date());
            File file = new File(sdCardDir, str + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            img_url=file.getAbsolutePath();
            Toast.makeText(PhotoPreview.this, "图片已存至："+img_url, Toast.LENGTH_LONG).show();
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(),file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {

        //为“确定”按钮设置监听
        mPicOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果被点击，将图片信息上传到服务端，并且转至最后的结果显示界面
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String dat = sdf.format(new Date());
                System.out.println(dat);
                uploadImg(new File(img_url));

            }
        });

    }
    private void uploadImg(final File file){
        new Thread() {
            @Override
            public void run() {
                //子线程需要做的工作
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", "Square Logo")
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(MEDIA_TYPE_JPEG, file))
                        .build();
                //设置为自己的 ip地址
                Request request = new Request.Builder()
                        .url("http://192.168.31.78:5000/upload")
                        .post(requestBody)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String tmp = response.body().string();
                    Intent intent = new Intent(PhotoPreview.this, ShowAnswer.class);
                    intent.putExtra("str",tmp);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initView() {

        //绑定控件
        //siv = (SmartImageView) this.findViewById(R.id.siv_pic);
        mPic = (ImageView) this.findViewById(R.id.pic_imv);
        mPicOk = (Button) this.findViewById(R.id.picture_OK_btn);
        //mPicAgain = (Button) this.findViewById(R.id.picture_again_btn);

        System.out.println(flag);
        //如果图片从相机处传来
        if (flag == camera) {
            //接收TakePhoto传递的图片
            Intent picIntent = getIntent();
            byte[] bytes = picIntent.getByteArrayExtra("pic_data");
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                mPic.setImageBitmap(bitmap);
                saveBitmap(bitmap);
            }
        }
        //如果图片由相册传来
        else if (flag == album){

            System.out.println(uri);
            getPicFromAlbm();//调用相册
            //System.out.println(uri);

        }

    }
}
