package cn.tang.common.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Auther: tangpd
 * @Date: 2020/12/29 11:24
 * @Description:
 */
public class CheckByImgUtil{



    /**
     *
     * 功能描述: 保存验证码的值，具体保存在哪里，根据项目实际情况来定
     *
     * @param: [key, Value, time]
     * @return: void
     * @auther: tangpd
     * @date: 2020/12/29 18:03
     */
    public static void saveImgCodeInServer(String key, String Value, int time){
        throw new RuntimeException("需要根据实际情况，编写保存验证码的逻辑（redis 或者暂时存放缓存里）");
    }

    /**
     *
     * 功能描述: 验证用户输入的验证码
     *
     * @param: [key, oldValue]
     * @return: boolean
     * @auther: tangpd
     * @date: 2020/12/30 11:16
     */
    public static boolean checkImgCode(String key, String oldValue){
        String saveValue = getImgCodeInServer(key);
        if (StringUtil.is_empty(saveValue)){
            throw new ExceptionForMyCustom(ResultJsonEunm.ERROR_FOR_GET_CACHE_DATA_ERROR, "获取不到保存的图片验证码");
        }
        return saveValue.equals(oldValue);
    }

    /**
     *
     * 功能描述: 从缓存中获取缓存的验证码
     *
     * @param: [key]
     * @return: java.lang.String
     * @auther: tangpd
     * @date: 2020/12/30 11:17
     */
    private static String getImgCodeInServer(String key){
        if (1 == 1){
            throw new RuntimeException("根据实际情况，编写获取验证码逻辑（从 redis 或者缓存里面获取）");
        }
        return null;
    }


    /**
     *
     * 功能描述: 生成指定 code 的图片验证码
     *
     * @param: [code]
     * @return: java.awt.image.BufferedImage
     * @auther: tangpd
     * @date: 2020/12/29 16:11
     */
    public static BufferedImage generateImgCode(String code){
        //1、创建 img 对象
        int width = 100;
        int height = 40;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        //2、获取画笔，用来画点、画线、写验证码；Graphics2D 可以设置平滑抗锯齿
        Graphics2D g = img.createGraphics();

        //3、填充背景颜色
        g.setColor(new Color(122,170,220));//设置画笔使用的颜色，如果需要随机背景色，将rgb三个值随机即可
        g.fillRect(0, 0, width, height);//填充背景颜色

        //4、随机画 500 个背景麻点
        Random random = new Random();
        for(int i = 0; i < 500; i++){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int rgb = random.nextInt(0xffffff);
            img.setRGB(x, y, rgb);
        }

        //5、在页面上随机位置画几条线
        //g.setColor(new Color(random.nextInt(0xffffff)));//先设置画笔颜色，不然会与背景色重复，看不出来
        g.setColor(new Color(7601933));//先设置画笔颜色，不然会与背景色重复，看不出来
        for(int i = 0; i < 5; i++){
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(100);
            int y2 = random.nextInt(40);
            g.drawLine(x1, y1, x2, y2);
        }

        //6、画上随机验证码
        //g.setColor(new Color(random.nextInt(0xffffff)));//设置画出来的字体的颜色
        g.setColor(new Color(0x29FEEE));//设置画出来的字体的颜色
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//设置平滑抗锯齿绘制,字体边缘平滑一点
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN,30));//设置字体大小
        g.drawString(code, 20, 30);//确定字体位置

        //7、释放资源
        g.dispose();
        return img;
    }



}
