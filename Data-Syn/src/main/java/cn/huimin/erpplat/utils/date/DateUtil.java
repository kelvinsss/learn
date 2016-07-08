package cn.huimin.erpplat.utils.date;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * Description : 时间常用类  
 * <pre>
 * +--------------------------------------------------------------------
 * 更改历史
 * 更改时间		 更改人		目标版本		更改内容
 * +--------------------------------------------------------------------
 * 2012-11-3       Snail Join 		1.00	 	创建
 *           		 	 	                               
 * </pre>
 * @author 矫迩(Snail Join) <a href="mailto:13439185754@163.com">
 *         E-mail:13697654@qq.com </a><a href="tencent://message/?uin=13697654">
 *         QQ:13697654</a>
 */
public class DateUtil {
	
	
	/**
	 * 根据时间格式获得当前时间
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String getDate(DateFormatEnum dateFormat){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.toString());
		return simpleDateFormat.format(new Date());
	}
	
	/**
	 * 根据时间格式获得时间
	 * 
	 * @param dateFormat
	 * @param date
	 * @return
	 */
	public static String getDate(Date date, DateFormatEnum dateFormat){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.toString());
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 字符串转时间
	 * 
	 * @param dateStr
	 * @param dateFormat
	 * @return
	 */
	public static Date toDate(String dateStr, DateFormatEnum dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat.toString());
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			//e.printStackTrace();
			return null;
		}
	}
	public static Date toDate(String dateStr, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			//e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符串转时间
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date toDate(String dateStr) {
		
		Date date = toDate(dateStr, DateFormatEnum.yyyy_MM_dd_HH_mm_ss);
		if (date == null) {
			date = toDate(dateStr, DateFormatEnum.yyyy_MM_dd_HH_mm);
			if (date == null) {
				date = toDate(dateStr, DateFormatEnum.yyyy_MM_dd);
				if (date == null) {
					date = toDate(dateStr, DateFormatEnum.HH_mm_ss);
					if (date == null) {
						date = toDate(dateStr, DateFormatEnum.HH_mm);
					}
				}
			}
			
		}
		return date;
	}
	/**
	 * 
	 * @Title: getStartTime 
	 * @Description: 获取当天的开始时间 
	 * @return Date    返回类型 
	 * @throws
	 */
	public static Date getStartTime(Date date){
		Calendar todayStart = Calendar.getInstance();
		if(date != null){
			todayStart.setTime(date);
		}
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime();
	}
	/**
	 * 
	 * @Title: getEndTime 
	 * @Description: 获取当天的开始结束时间
	 * @return
	 * @return Date    返回类型 
	 * @throws
	 */
	public static Date getEndTime(Date date){
		Calendar todayEnd = Calendar.getInstance();
		if(date != null){
			todayEnd.setTime(date);
		}
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime();
	}
}
