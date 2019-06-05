package com.snow.lottie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.ImageAssetDelegate;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieImageAsset;
import com.airbnb.lottie.OnCompositionLoadedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private LottieAnimationView lottieAnimationView;
    private LottieAnimationView lottieAnimationView01;
    private LottieAnimationView lottieAnimationView02;
    private LottieAnimationView animation_view03;
    private LottieAnimationView animation_view04;
    private LottieAnimationView animation_view05;
    private final String LOCALPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lottieAnimationView = findViewById(R.id.animation_view);
        lottieAnimationView01 = findViewById(R.id.animation_view01);
        lottieAnimationView02 = findViewById(R.id.animation_view02);
        animation_view03 = findViewById(R.id.animation_view03);
        animation_view04 = findViewById(R.id.animation_view04);
        animation_view05 = findViewById(R.id.animation_view05);

        loadFirst();
        loadSecond();
        loadUrl();
        loadRaw();
        loadFilePath();
        showSdcardLottieEffects(LOCALPATH + "/asnow/siam.json", LOCALPATH + "/asnow/siamCat/");
        initListener();
    }

    private void initListener() {
        findViewById(R.id.stop_urlA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lottieAnimationView.isAnimating()) {
                    Toast.makeText(MainActivity.this, "动画暂停", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.pauseAnimation();
                } else {
                    Toast.makeText(MainActivity.this, "播放动画", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.playAnimation();
                }
            }
        });
    }

    /**
     * 加载assets文件中图
     */
    private void loadFirst() {
//        lottieAnimationView.setImageAssetsFolder("images");
        lottieAnimationView01.setAnimation("bird1.json");
//        lottieAnimationView01.loop(true);//循环播放动画，已经废弃，但是还可以使用，建议使用下面的两行代码
        lottieAnimationView01.setRepeatMode(LottieDrawable.REVERSE);//设置播放模式
        lottieAnimationView01.setRepeatCount(LottieDrawable.INFINITE);//设置重复次数
        lottieAnimationView01.playAnimation();
    }

    /**
     * 加载assets文件中图
     */
    private void loadSecond() {
        lottieAnimationView02.setImageAssetsFolder("chineseImages/");
        lottieAnimationView02.setAnimation("chinese.json");
        lottieAnimationView02.setRepeatMode(LottieDrawable.REVERSE);
        lottieAnimationView02.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView02.playAnimation();
    }

    /**
     * 通过URL直接加载
     */
    private void loadUrl() {
        lottieAnimationView.setAnimationFromUrl("https://cqz-1256838880.cos.ap-shanghai.myqcloud.com/bird1.json");
        lottieAnimationView.setRepeatMode(LottieDrawable.REVERSE);
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView.playAnimation();
    }

    /**
     * 加载raw中的文件
     */
    private void loadRaw() {
//        InputStream is = getResources().openRawResource(R.id.fileNameID) ;
        animation_view03.setAnimation(R.raw.bird1);
        animation_view03.setRepeatMode(LottieDrawable.REVERSE);
        animation_view03.setRepeatCount(LottieDrawable.INFINITE);
        animation_view03.playAnimation();
    }

    /**
     * 加载手机中的file文件
     */
    private void loadFilePath() {
        try {
            animation_view04.setAnimationFromJson((readExternal(LOCALPATH + "/asnow/bird1.json")));
            animation_view04.setRepeatMode(LottieDrawable.REVERSE);
            animation_view04.setRepeatCount(LottieDrawable.INFINITE);
            animation_view04.playAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载手机中的file文件（带图片）
     *
     * @param jsonFile  json文件绝对路径 如：/storage/emulated/0/asnow/siam.json
     * @param imagesDir json文件引用的image文件的目录 如：/storage/emulated/0/asnow/siamCat/
     */
    private void showSdcardLottieEffects(String jsonFile, final String imagesDir) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(jsonFile));
            String content = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
//            final String absolutePath = imagesDir.getAbsolutePath();        //提供一个代理接口从 SD 卡读取 images 下的图片

            animation_view05.setImageAssetDelegate(new ImageAssetDelegate() {
                @Override
                public Bitmap fetchBitmap(LottieImageAsset asset) {
                    Bitmap bitmap = null;
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(imagesDir + asset.getFileName());
                        bitmap = BitmapFactory.decodeStream(fileInputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bitmap == null) {
                                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
                            }
                            if (fileInputStream != null) {
                                fileInputStream.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                    return bitmap;
                }
            });
            animation_view05.setRepeatMode(LottieDrawable.REVERSE);
            animation_view05.setRepeatCount(LottieDrawable.INFINITE);
            LottieComposition.Factory.fromJsonString(String.valueOf(jsonObject), new OnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(@Nullable LottieComposition composition) {
                    if (composition == null) {
                        return;
                    }
                    animation_view05.cancelAnimation();
                    animation_view05.setProgress(0);
                    animation_view05.setComposition(composition);
                    animation_view05.playAnimation();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从sd card文件中读取数据
     *
     * @param filename 文件的绝对路径 如：/storage/emulated/0/asnow/bird1.json
     * @return
     * @throws IOException
     */
    public String readExternal(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //打开文件输入流
            FileInputStream inputStream = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            //读取文件内容
            while (len > 0) {
                sb.append(new String(buffer, 0, len));
                //继续将数据放到buffer中
                len = inputStream.read(buffer);
            }
            //关闭输入流
            inputStream.close();
        }
        return sb.toString();
    }

    /**
     * 反射
     *
     * @param claz
     * @param mContext
     * @param classPath
     * @param methodName
     * @param newValue
     */
    public static void invokePrivateMothod(Object claz, Context mContext, String classPath, String methodName, Object newValue) {
        try {
            Class<?> maClass = Class.forName(classPath, true, mContext.getClassLoader());
            // 获取方法名为showName，参数为String类型的方法
            // 这种是类静态函数，要用getDeclaredMethod而不是getMethod 成员函数才能用getMetho
            //第一个参数是方法名，第二个参数是方法的参数类型
            Method method = maClass.getDeclaredMethod(methodName, LottieComposition[].class);
            // 若调用私有方法，必须抑制java对权限的检查
            method.setAccessible(true);
            // 使用invoke调用方法，并且获取方法的返回值，需要传入一个方法所在类的对象，new Object[]
            // {"Kai"}是需要传入的参数，与上面的String.class相对应
            method.invoke(claz, new Object[]{newValue});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
