package com.fz.usercenter.once;

import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * @Author fang
 * @Date 2025/2/13 17:32
 * @注释
 */
public class ImportExcel {

    public static void main(String[] args) {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        String fileName = "D:\\IDEAProjectSpace\\user-center\\src\\main\\resources\\user.xlsx";
//        这个是监听读 适用于大数据量
//        readerByListener(fileName);
//        这个是同步读，方便，简单，但是数据量大的时候会卡顿
        synchronousRead(fileName);
        }

    public static void readerByListener(String fileName){
        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        // 具体需要返回多少行可以在`PageReadListener`的构造函数设置
        EasyExcel.read(fileName, IndexUserData.class, new NewTableListener()).sheet().doRead();
    }

    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<IndexUserData> list = EasyExcel.read(fileName).head(IndexUserData.class).sheet().doReadSync();
        for (IndexUserData indexUserData : list) {
            System.out.println(indexUserData);
        }
    }
}
