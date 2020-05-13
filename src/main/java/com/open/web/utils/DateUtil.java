package com.open.web.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
	"yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取YYYY格式
	 * 
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	* @Title: compareDate
	* @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * 
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 格式化日期
	 * 
	 * @return
	 */
	public static Date fomatDateWithSS(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @category 日期转字符串
	 * @author zhanglei
	 */
	public static String DatetoString(Date date){	
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return sf.format(date);
	}
	
	/**
	 * 
	 * @param date
	 * @param type 1datetime 2data
	 * @return
	 */
	public static String datefomat(Date date,Integer type) {
		
		try {
			if(type == 1) return sdfTime.format(date);
			
			if(type == 2) return sdfDay.format(date);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

	/**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
	  /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
      
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        
        return dateStr;
    }
    
    /**
               *    得到n天之后/之前的日期
     * @author JBL
     * @param daysInt 天数 ，当daysInt>0时，得到N天后的日期。反之，则是N天前的日期
     * @param pattern 日期格式
     * @return
     */
    public static String getAfterDayStr(int daysInt,String pattern) {
    	Calendar canlendar = Calendar.getInstance(); // java.util包
    	canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
    	Date date = canlendar.getTime();
    	
    	SimpleDateFormat sdfd = new SimpleDateFormat(pattern);
    	String dateStr = sdfd.format(date);
    	
    	return dateStr;
    }
    
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        
        return dateStr;
    }
    
    
	   /**
	    * 获取最近x月(年or日)的日期
	    * @author JBL
	    * @date 2017-9-27 下午3:32:35
	    * @param beginDate --- 起始时间，日期格式为yyyy-mm-dd或yyyy-MM,默认为当前日期
	    * @param X     --- 最近的几个月     。同理，可得年和日  
	    * @param flag -- flag=1将本月算进去，否则不把本月的算进去。同理，可得年和日  
	    * @param forward --- forward<0逐次往前推1个月，forward>0往后推1个月，同理，可得年和日  
	    * @param format --- format=0为按日推算，format=1为按月推算,format=2为按年推算 
	    * @return <span>参考例子</span>
	    * <ul>
	    * </br>
	    * <li>getLastXDays(null,7,0,-1,0)获取最近7天日期。</br>[2017-09-20, 2017-09-21, 2017-09-22, 2017-09-23, 2017-09-24, 2017-09-25, 2017-09-26]</li>
	    * <li>getLastXDays(null,4,0,-1,1)获取最近4个月日期。</br>[2017-05, 2017-06, 2017-07, 2017-08]</li>
	    * <li> getLastXDays(null,3,0,-1,2)获取最近3年日期。</br>[2014, 2015, 2016]</li>
	    * </ul>           
	    *           
	    * @since 1.7
	    * @version 1.0
	    */
		public static List<String> getLastXDays(String beginDate,int X,int flag,int forward,int format){  
			List<String> months=new ArrayList<String>();
			List<String> monthsort=new ArrayList<String>();//日期排序用
	        Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf_yMd=new SimpleDateFormat("yyyy-MM-dd");
	        SimpleDateFormat sdf_yM=new SimpleDateFormat("yyyy-MM");
	        SimpleDateFormat sdf_y=new SimpleDateFormat("yyyy");
	        if(beginDate!=null){
	        	String[] as=beginDate.split("-");
	        	if(as.length>1){
			        cal.set(Calendar.YEAR,Integer.parseInt(as[0])); //把本年的算进去
			        cal.set(Calendar.MONTH,Integer.parseInt(as[1])-1); //把本月的算进去
			        cal.set(Calendar.DATE,Integer.parseInt(as[2])); //把本日的算进去
	        	}
	        }
	        if(format>0){//解基本日期beginDate大于30号，出现2个3月的问题--2018/01/30
	        	cal.set(Calendar.DATE,1);
	        }
	        
	        if(flag==1){//是否包含本月或本天
	        	if(format==1){
	        		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1); //要先+1,才能把本月的算进去
	        	}if(format==2){
	        		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1); //要先+1,才能把本月的算进去
	        	}else{
	        		cal.set(Calendar.DATE, cal.get(Calendar.DATE)+1); //要先+1,才能把本天的算进去
	        	}
	        }
	    	if(format==1){//是否按月推算
		        for(int i=0; i<X; i++){
		        	cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+forward); //forward<0逐次往前推1个月，forward>0往后推1个月  
		        	months.add(sdf_yM.format(cal.getTime()));
		        	}  
	        }else if(format==2){
	        	for(int i=0; i<X; i++){
	        		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+forward); //forward<0逐次往前推1年，forward>0往后推1年  
	    			months.add(sdf_y.format(cal.getTime()));
	    		}
	    	}else{
	        	for(int i=0; i<X; i++){
	        		cal.set(Calendar.DATE, cal.get(Calendar.DATE)+forward); //forward<0逐次往前推1天，forward>0往后推1天  
	    			months.add(sdf_yMd.format(cal.getTime()));
	    		}
	    	}
	        if(forward<0){//当日期降序排列时，对日期进行升序排列
	        	int j=0;
	        	for(int i=months.size()-1;i>=0;i--){
	        		monthsort.add(j, months.get(i));
	        		j++;
	        	}
	        	months=monthsort;
	        }
	        return months;  
	    }
		
    public static void main(String[] args) {
//    	System.out.println(getDays());
//    	System.out.println(getAfterDayWeek("3"));
    	System.out.println(getAfterDayStr(-2,"yyyy-MM-dd"));
    }

}
