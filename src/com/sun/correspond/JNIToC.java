package com.sun.correspond;

/**
 * Created by never on 2017/5/25.
 *
 */
public class JNIToC {

    /**
     * 图片处理需要的参数
     * @param picPath  图片的本地路径
     * @param AlgorithmNum 图片处理的方法
     *                     1 鱼眼扩张   2 挤压    3 波浪    4 素描图   5 风     6 旋转
     *                     7 毛玻璃    8 贴图    9 怀旧滤镜  10 连环画滤镜    11 强光模式
     * @param overlyingPicNum 进行贴图时所贴图片的编号
     * @param rotationNum 进行图片旋转时的旋转角度
     */
    public native static void setInformation(String picPath, int AlgorithmNum, int overlyingPicNum, float rotationNum);

    public native static String getPictureInfo();

    static{
        System.loadLibrary("ImageDLLTest");
    }

}
