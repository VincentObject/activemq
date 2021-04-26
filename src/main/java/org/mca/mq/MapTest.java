package org.mca.mq;

import java.util.HashMap ;
import java.util.Map;

public class MapTest {
    public static void main(String[] args) {

        //put 相同key 值会被覆盖
        Map<String ,String > bb=new HashMap<String ,String>();
        bb.put("1","bb");
        String bbb = bb.put("1", "cc");//返回值为被覆盖之前的值
        System.out.println(bb);
        System.out.println(bbb);

        //putIfAbsent 相同key 值不会被覆盖
        Map<String ,String > aa=new HashMap<String ,String>();
        aa.put("1","aa");
        String aaa = aa.putIfAbsent("1", "bb");
        System.out.println(aa);
        System.out.println(aaa);

    }

}
