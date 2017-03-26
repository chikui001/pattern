package com.chiao.pattern.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 *
 */
public class StringUtil {

	/**
	 * 字符串全量替换
	 *
	 * @param s    原始字符串
	 * @param src  要替换的字符串
	 * @param dest 替换目标
	 * @return 结果
	 */
	public static String replaceAll(String s, String src, String dest) {
		if (s == null || src == null || dest == null || src.length() == 0)
			return s;
		int pos = s.indexOf(src); // 查找第一个替换的位置
		if (pos < 0)
			return s;
		int capacity = dest.length() > src.length() ? s.length() * 2 : s.length();
		StringBuilder sb = new StringBuilder(capacity);
		int writen = 0;
		for (; pos >= 0; ) {
			sb.append(s, writen, pos); // append 原字符串不需替换部分
			sb.append(dest); // append 新字符串
			writen = pos + src.length(); // 忽略原字符串需要替换部分
			pos = s.indexOf(src, writen); // 查找下一个替换位置
		}
		sb.append(s, writen, s.length()); // 替换剩下的原字符串
		return sb.toString();
	}

	/**
	 * 只替换第一个
	 *
	 * @param s
	 * @param src
	 * @param dest
	 * @return
	 */
	public static String replaceFirst(String s, String src, String dest) {
		if (s == null || src == null || dest == null || src.length() == 0)
			return s;
		int pos = s.indexOf(src);
		if (pos < 0) {
			return s;
		}
		StringBuilder sb = new StringBuilder(s.length() - src.length() + dest.length());

		sb.append(s, 0, pos);
		sb.append(dest);
		sb.append(s, pos + src.length(), s.length());
		return sb.toString();
	}

	/**
	 * Returns <tt>true</tt> if s is null or <code>s.trim().length()==0<code>.
	 *
	 * @see String#isEmpty()
	 */
	public static boolean isEmpty(String s) {
		if (s == null)
			return true;
		return s.trim().isEmpty();
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	/**
	 * @see String#trim()
	 */
	public static String trim(String s) {
		if (s == null)
			return null;
		return s.trim();
	}

	public static String newGuid() {
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		return randomUUIDString;
	}

	/**
	 * 以第一个分割字符串分割
	 *
	 * @param line      原始字符串
	 * @param seperator 分隔符
	 * @return 分割结果
	 */
	public static String[] splitFirst(String line, String seperator) {
		if (line == null || seperator == null || seperator.length() == 0)
			return null;
		ArrayList<String> list = new ArrayList<String>(2);
		int pos1 = 0;
		int pos2 = line.indexOf(seperator, pos1);
		if (pos2 < 0) {
			list.add(line);
		} else {
			list.add(line.substring(pos1, pos2));
			list.add(line.substring(pos2 + 1, line.length()));
		}
		return list.toArray(new String[0]);
	}

	/**
	 * 分割字符串
	 *
	 * @param line      原始字符串
	 * @param seperator 分隔符
	 * @return 分割结果
	 */
	public static String[] split(String line, String seperator) {
		if (line == null || seperator == null || seperator.length() == 0)
			return null;
		ArrayList<String> list = new ArrayList<String>();
		int pos1 = 0;
		int pos2;
		for (; ; ) {
			pos2 = line.indexOf(seperator, pos1);
			if (pos2 < 0) {
				list.add(line.substring(pos1));
				break;
			}
			list.add(line.substring(pos1, pos2));
			pos1 = pos2 + seperator.length();
		}
		// 去掉末尾的空串，和String.split行为保持一致
		for (int i = list.size() - 1; i >= 0 && list.get(i).length() == 0; --i) {
			list.remove(i);
		}
		return list.toArray(new String[0]);
	}

	public static <T> String join(String separator, List<T> c) {
		if (null == c || c.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < c.size(); i++) {
			T t = c.get(i);
			if (null != t) {
				if (0 == i) {
					sb.append(t);
				} else {
					sb.append(separator);
					sb.append(t);
				}
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> String join(String separator, T... list) {
		if (null == list || 0 == list.length)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			T t = list[i];
			if (null != t) {
				if (0 == i) {
					sb.append(t);
				} else {
					sb.append(separator);
					sb.append(t);
				}
			}
		}
		return sb.toString();
	}

	public static <T> String join(String separator, Collection<T> c) {
		if (null == c || c.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		Iterator<T> i = c.iterator();
		sb.append(i.next());
		while (i.hasNext()) {
			T t = i.next();
			if (null != t) {
				sb.append(separator);
				sb.append(i.next());
			}
		}
		return sb.toString();
	}

	public static String removeAll(String s, String src) {
		return replaceAll(s, src, "");
	}

	/**
	 * 拼接字符串
	 *
	 * @param o
	 * @return
	 */
	public static String concat(Object... o) {
		if (null != o && o.length > 0) {
			// 先计算长度
			int len = 0;
			// 收集str，避免重复toString
			List<String> list = new ArrayList<String>(o.length);
			for (Object obj : o) {
				if (null != obj) {
					String str = obj.toString();
					if (isNotEmpty(str)) {
						len += str.length();
						list.add(str);
					}
				}
			}
			if (len > 0) {
				// 一次性申请好容量
				StringBuilder sb = new StringBuilder(len);
				for (String str : list) {
					sb.append(str);
				}
				return sb.toString();
			}
		}
		return null;
	}

	/**
	 * 获取字符串的UTF-8编码字节数组
	 *
	 * @param s
	 * @return
	 */
	public static byte[] getUTF8Bytes(String s) {
		if (s != null && s.length() >= 0) {
			try {
				return s.getBytes(ENC_UTF8);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	/**
	 * 获取字符串的GBK编码字节数组
	 *
	 * @param s
	 * @return
	 */
	public static byte[] getGBKBytes(String s) {
		if (s != null && s.length() >= 0) {
			try {
				return s.getBytes(ENC_GBK);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	/**
	 * 获取字节数组的UTF-8编码字符串
	 *
	 * @param b
	 * @return
	 */
	public static String getUTF8String(byte[] b) {
		if (b != null) {
			try {
				return new String(b, ENC_UTF8);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	/**
	 * 获取字节数组的GBK编码字符串
	 *
	 * @param b
	 * @return
	 */
	public static String getGBKString(byte[] b) {
		if (b != null) {
			try {
				return new String(b, ENC_GBK);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	/**
	 * 对字符串以 GBK编码方式进行URLEncode
	 *
	 * @param s
	 * @return
	 */
	public static String URLEncodeGBK(String s) {
		if (s != null && s.length() > 0) {
			try {
				return URLEncoder.encode(s, ENC_GBK);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return s;
	}

	/**
	 * 对字符串以 GB2312编码方式进行URLEncode
	 *
	 * @param s
	 * @return
	 */
	public static String URLEncodeGB2312(String s) {
		if (s != null && s.length() > 0) {
			try {
				return URLEncoder.encode(s, ENC_GB2312);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	/**
	 * 对字符串以 UTF-8编码方式进行URLEncode
	 *
	 * @param s
	 * @return
	 */
	public static String URLEncodeUTF8(String s) {
		if (s != null && s.length() > 0) {
			try {
				return URLEncoder.encode(s, ENC_UTF8);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return s;
	}

	/**
	 * 对字符串以 GBK编码方式进行URLDecode
	 *
	 * @param s
	 * @return
	 */
	public static String URLDecodeGBK(String s) {
		if (s != null && s.length() > 0) {
			try {
				return URLDecoder.decode(s, ENC_GBK);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return s;
	}

	/**
	 * 对字符串以 GB2312编码方式进行URLDecode
	 *
	 * @param s
	 * @return
	 */
	public static String URLDecodeGB2312(String s) {
		if (s != null && s.length() > 0) {
			try {
				return URLDecoder.decode(s, ENC_GB2312);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return s;
	}

	/**
	 * 对字符串以 UTF-8编码方式进行URLDecode
	 *
	 * @param s
	 * @return
	 */
	public static String URLDecodeUTF8(String s) {
		if (s != null && s.length() > 0) {
			try {
				return URLDecoder.decode(s, ENC_UTF8);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return s;
	}

	/**
	 * UTF-8编码常量
	 */
	public static final String ENC_UTF8 = "UTF-8";
	/**
	 * GBK编码常量
	 */
	public static final String ENC_GBK = "GBK";
	/**
	 * gb2312编码常量
	 */
	public static final String ENC_GB2312 = "gb2312";

	/**
	 * 拼接字符串
	 *
	 * @param strs
	 * @return
	 */
	public static String concat(String... strs) {
		if (null != strs && strs.length > 0) {
			// 先计算长度
			int len = 0;
			for (String str : strs) {
				if (null != str) {
					len += str.length();
				}
			}
			if (len > 0) {
				// 一次性申请好容量
				StringBuilder sb = new StringBuilder(len);
				for (String str : strs) {
					if (isNotEmpty(str)) {
						sb.append(str);
					}
				}
				return sb.toString();
			}
		}
		return null;
	}

	public static int convertStringToInt(String value, int defaultValue) {
		try {
			if (StringUtil.isNotEmpty(value)) {
				return Integer.parseInt(value);
			}
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public static long convertStringToLong(String value, long defaultValue) {
		try {
			if (StringUtil.isNotEmpty(value)) {
				return Long.parseLong(value);
			}
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public static String getString(Object o) {
		if (null != o) {
			return String.valueOf(o);
		}
		return null;
	}

	public static String getNoneNullString(String str) {
		if (isNotEmpty(str)) {
			return str;
		}
		return "";
	}

	// 生成随机字符串
	public static String createRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	private static String convertToHex(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		byte[] sha1Hash = md.digest();
		return convertToHex(sha1Hash);
	}

	public static String generateTime(long time) {
		int totalSeconds = (int) (time / 1000);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
	}

	public static List<Integer> filterNumberFromStr(String str) {
		if (StringUtil.isNotEmpty(str)) {
			String str2 = str.replaceAll("\\D", "_");
			String[] as = str2.split("_+");
			List<Integer> list = new ArrayList<Integer>(as.length);
			for (int i = 0; i < as.length; i++) {
				try {
					int num = Integer.parseInt(as[i]);
					list.add(num);
				} catch (NumberFormatException e) {
				}

			}
			return list;
		}
		return null;
	}
}
