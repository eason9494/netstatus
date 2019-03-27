import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Application {

    private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args){
        Application app = new Application();
       while (true){
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            app.netStatus();
        }
    }

    public String netStatus(){
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        String result = null;
        try {
            process = runtime.exec("ping " + "www.baidu.com");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "GBK");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            int a = sb.lastIndexOf("丢失 = ");
            int b = sb.lastIndexOf("平均 = ");
            is.close();
            isr.close();
            br.close();
            Integer ping = null;
            Integer lost = null;
            try {
                String s = sb.substring(b+5, b+6);
                ping = Integer.parseInt(s);
                int end = sb.indexOf("%");
                int start = sb.indexOf("(");
                lost = Integer.parseInt(sb.substring(start+1, end));
            }catch (Exception e){
                System.out.println("================================= 转换异常 ====================================");
            }finally {
                if (null != sb && !sb.toString().equals("")) {
                    if (sb.toString().indexOf("TTL") > 0) {
                    } else if (ping != null && ping > 50){
                        printLog(ping, sb.toString(), "波动");
                    }
                    else if(lost > 0){
                        printLog(ping, sb.toString(), "波动");
                    }
                    else {
                        printLog(ping, sb.toString(), "异常");
                        System.out.println("================================= 网络异常 ====================================");
                    }
                }
                System.out.println("delay:"+sb.substring(b+4, b+9)+"    lost: "+sb.substring(a+4, a+16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void printLog(Integer ping, String info, String status) throws Exception{
        Date now = new Date();
        Writer w = new FileWriter("C:/Users\\51953\\Desktop\\netlog.txt");
        w.write("================================= 网络"+status+" ====================================\n");
        String day = formatter.format(now);
        w.write("===========================时间："+day+"==============================\n");
        w.write(info);
        w.close();
    }
}
