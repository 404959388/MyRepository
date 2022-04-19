package com.yellowriver.redis626.util;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author huanghe
 * @create 2022/2/18 12:34 PM
 */
public class Tools {

  @PostConstruct
  public void test(){

  }
  public static final String IP = getHostAddress();

  public static String getThreadName() {
    return IP +"-"+Thread.currentThread().getName();
  }

  public static void sleep(long millis){
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  public static String getHostAddress(){
    try {
      return InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return "127.0.0.1";
  }
}
