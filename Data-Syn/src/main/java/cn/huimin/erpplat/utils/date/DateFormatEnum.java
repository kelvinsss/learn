package cn.huimin.erpplat.utils.date;


/**
 * 
 * Description : 时间格式   
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
public enum DateFormatEnum {
	

	// 年-月-日 时:分:秒
	// 年-月-日 时:分
	// 年-月-日
	// 时:分:秒
	// 时:分
	
	ID {
		public String toString(){
			return "yyyy-MM-dd-HH-mm-ss";
		}
	},
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	yyyy_MM_dd_HH_mm_ss {
		public String toString(){
			return "yyyy-MM-dd HH:mm:ss";
		}
	},
	/**
	 * yyyyMMddHHmmss
	 */
	yyyyMMddHHmmss {
		public String toString(){
			return "yyyyMMddHHmmss";
		}
	},
	/**
	 * yyyy-MM-dd HH:mm
	 */
	yyyy_MM_dd_HH_mm {
		public String toString(){
			return "yyyy-MM-dd HH:mm";
		}
	},
	/**
	 * yyyy-MM-dd
	 */
	yyyy_MM_dd {
		public String toString(){
			return "yyyy-MM-dd";
		}
	},
	/**
	 * HH:mm:ss
	 */
	HH_mm_ss {
		public String toString(){
			return "HH:mm:ss";
		}
	},
	/**
	 * HH:mm
	 */
	HH_mm {
		public String toString(){
			return "HH:mm";
		}
	},
	/**
	 * yyyy/MM/dd
	 */
	yyyyMMdd {
		public String toString(){
			return "yyyy/MM/dd";
		}
	},
	/**
	 * yyyyMMdd
	 */
	yyyyMMdd_ {
		public String toString(){
			return "yyyyMMdd";
		}
	},
	/**
	 * yyyy年MM月dd日
	 */
	yyyyZHMMZHddZH {
		public String toString(){
			return "yyyy年MM月dd日";
		}
	},
	/**
	 * MM月dd日
	 */
	MMZHddZH {
		public String toString(){
			return "MM月dd日";
		}
	},
	/**
	 * yyyy年MM月dd日HH:mm
	 */
	yyyyZHMMZHddZH_HH_mm {
		public String toString(){
			return "yyyy年MM月dd日 HH:mm";
		}
	},
	/**
	 * yyyy年MM月
	 */
	yyyyZHMMZH{
		public String toString(){
			return "yyyy年MM月";
		}
	}
}
