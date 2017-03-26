package com.chiao.pattern.utils;

import java.util.*;

/**
 * 集合工具类
 */
public class CollectionUtil {

	public static <T> boolean isNotEmpty(List<T> list) {
		if (null != list && list.size() > 0) {
			return true;
		}
		return false;
	}

	public static <T> boolean isEmpty(List<T> list) {
		return !isNotEmpty(list);
	}

	public static <T> int size(List<T> list) {
		if (null != list) {
			return list.size();
		}
		return 0;
	}

	public static <T> boolean isNotEmpty(Collection<T> list) {
		if (null != list && list.size() > 0) {
			return true;
		}
		return false;
	}

	public static <T> boolean isEmpty(Collection<T> list) {
		return !isNotEmpty(list);
	}

	public static <T> int size(Collection<T> list) {
		if (null != list) {
			return list.size();
		}
		return 0;
	}

	public static <K, V> boolean isNotEmpty(Map<K, V> map) {
		if (null != map && map.size() > 0) {
			return true;
		}
		return false;
	}

	public static <K, V> boolean isEmpty(Map<K, V> map) {
		return !isNotEmpty(map);
	}

	public static <K, V> int size(Map<K, V> map) {
		if (null != map) {
			return map.size();
		}
		return 0;
	}

	public static <T> boolean isNotEmpty(Set<T> set) {
		if (null != set && set.size() > 0) {
			return true;
		}
		return false;
	}

	public static <T> boolean isEmpty(Set<T> set) {
		return !isNotEmpty(set);
	}

	public static <T> int size(Set<T> set) {
		if (null != set) {
			return set.size();
		}
		return 0;
	}

	public static <T> boolean isNotEmpty(T... list) {
		if (null != list && list.length > 0) {
			return true;
		}
		return false;
	}

	public static <T> boolean isEmpty(T... list) {
		return !isNotEmpty(list);
	}

	public static <T> int size(T... list) {
		if (null != list) {
			return list.length;
		}
		return 0;
	}

	public static <K, V> List<V> getValueList(Map<K, V> map) {
		if (isNotEmpty(map)) {
			Collection<V> collection = map.values();
			if (isNotEmpty(collection)) {
				return new ArrayList<V>(collection);
			}
		}
		return null;
	}

	/**
	 * 取交集
	 *
	 * @param aList
	 * @param bList
	 * @return
	 */
	public static <T> List<T> getIntersectionList(List<T> aList, List<T> bList) {
		if (isNotEmpty(aList) && isNotEmpty(bList)) {
			List<T> cList = new ArrayList<T>();
			for (T t : aList) {
				if (null != t && bList.contains(t)) {
					cList.add(t);
				}
			}
			return cList;
		}
		return null;
	}

	/**
	 * 取并集
	 *
	 * @param aList
	 * @param bList
	 * @return
	 */
	public static <T> List<T> getUnionList(List<T> aList, List<T> bList) {
		if (isNotEmpty(aList) && isNotEmpty(bList)) {
			List<T> cList = new ArrayList<T>(aList);
			for (T t : bList) {
				if (null != t && !cList.contains(t)) {
					cList.add(t);
				}
			}
			return cList;
		} else if (isNotEmpty(aList)) {
			return new ArrayList<T>(aList);
		} else if (isNotEmpty(bList)) {
			return new ArrayList<T>(bList);
		}
		return null;
	}

	/**
	 * 取差集(A-B)
	 *
	 * @param aList
	 * @param bList
	 * @return
	 */
	public static <T> List<T> getDifferenceList(List<T> aList, List<T> bList) {
		if (isNotEmpty(aList)) {
			if (isNotEmpty(bList)) {
				List<T> cList = new ArrayList<T>();
				for (T t : aList) {
					if (null != t && !bList.contains(t)) {
						cList.add(t);
					}
				}
				return cList;
			} else {
				return new ArrayList<T>(aList);
			}
		}
		return null;
	}

	/**
	 * 不能转换成int，则抛弃掉
	 *
	 * @param l
	 * @return
	 */
	public static List<Integer> convertToInt(List<String> l) {
		if (CollectionUtil.isNotEmpty(l)) {
			List<Integer> v = new ArrayList<Integer>(l.size());
			for (String s : l) {
				if (StringUtil.isNotEmpty(s)) {

					try {
						v.add(Integer.parseInt(StringUtil.trim(s)));
					} catch (NumberFormatException e) {
					}
				}
			}
			return v;
		}
		return Collections.emptyList();
	}

	public static <T> List<T> convertArrayToList(T... array) {
		if (null != array && array.length > 0) {
			List<T> list = new ArrayList<T>(array.length);
			for (T t : array) {
				if (null != t) {
					list.add(t);
				}
			}
			return list;
		}
		return null;
	}

	public static boolean isNotEmpty(byte[] array) {
		if (null != array && array.length > 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(byte[] array) {
		return !isNotEmpty(array);
	}

	public static boolean isNotEmpty(short[] array) {
		if (null != array && array.length > 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(short[] array) {
		return !isNotEmpty(array);
	}

	public static boolean isNotEmpty(int[] array) {
		if (null != array && array.length > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String[] array) {
		if (null != array && array.length > 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(int[] array) {
		return !isNotEmpty(array);
	}

	public static boolean isNotEmpty(long[] array) {
		if (null != array && array.length > 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(long[] array) {
		return !isNotEmpty(array);
	}

	public static int getLength(byte[] array) {
		if (null != array) {
			return array.length;
		}
		return 0;
	}

	public static int getLength(short[] array) {
		if (null != array) {
			return array.length;
		}
		return 0;
	}

	public static int getLength(int[] array) {
		if (null != array) {
			return array.length;
		}
		return 0;
	}

	public static int getLength(long[] array) {
		if (null != array) {
			return array.length;
		}
		return 0;
	}

	/**
	 * 子数组
	 *
	 * @param array
	 * @param startIndex
	 * @param endInex
	 * @return
	 */
	public static byte[] subArray(byte[] array, int startIndex, int endIndex) {
		int len = getLength(array);
		if (len > 0 && endIndex >= len) {
			endIndex = len - 1;
		}
		if (len > 0 && startIndex >= 0 && startIndex <= endIndex && startIndex < len) {
			int subArrayLen = endIndex - startIndex + 1;
			byte[] subArray = new byte[subArrayLen];
			for (int i = 0; i < subArrayLen; i++) {
				subArray[i] = array[startIndex + i];
			}
			return subArray;
		}
		return null;
	}

	/**
	 * 列表分批
	 *
	 * @param list
	 * @param batchSize
	 * @return
	 */
	public static <T> List<List<T>> getSubListByBatch(List<T> list, int batchSize) {
		if (CollectionUtil.isNotEmpty(list) && batchSize > 0) {
			int totalSize = list.size();
			int batch = totalSize / batchSize;// 共分多少波
			if (0 != totalSize % batchSize) {
				batch++;
			}
			List<List<T>> allList = new ArrayList<List<T>>(batch);
			int fromIndex = 0;
			do {
				int toIndex = fromIndex + batchSize;
				if (toIndex > totalSize) {
					toIndex = totalSize;
				}
				List<T> subList = new ArrayList<T>(list.subList(fromIndex, toIndex));
				allList.add(subList);
				fromIndex += batchSize;
			} while (fromIndex < totalSize);
			return allList;
		}
		return null;
	}

	public static String[] convertListToArray(List<String> list) {
		if (isNotEmpty(list)) {
			String[] array = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				array[i] = list.get(i);
			}
			return array;
		}
		return null;
	}

	/**
	 * map key sort and return
	 *
	 * @param map
	 * @param <V>
	 * @return
	 */
	public static <V> List<Integer> sportMap(Map<Integer, V> map) {
		List<Integer> keyList = new ArrayList<Integer>(map.keySet());
		Collections.sort(keyList);
		return keyList;
	}
}
