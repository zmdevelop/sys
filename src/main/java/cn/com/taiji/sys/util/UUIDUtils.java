package cn.com.taiji.sys.util;

import java.util.UUID;

/**
 * Created by liyd on 9/10/14.
 */
public class UUIDUtils {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };

	/**
	 * 生成指定长度的uuid
	 * 
	 * @param length
	 * @return
	 */
	private static String getUUID(int length, UUID uuid) {
		int groupLength = 32 / length;
		StringBuilder sb = new StringBuilder();
		String id = uuid.toString().replace("-", "");
		for (int i = 0; i < length; i++) {
			String str = id.substring(i * groupLength, i * groupLength
					+ groupLength);
			int x = Integer.parseInt(str, 16);
			sb.append(chars[x % 0x3E]);
		}
		return sb.toString();
	}

	/**
	 * 8位UUID
	 * 
	 * @return
	 */
	public static String getUUID8() {
		return getUUID(8, UUID.randomUUID());
	}

	/**
	 * 8位UUID
	 * 
	 * @return
	 */
	public static String getUUID8(byte[] bytes) {
		return getUUID(8, UUID.nameUUIDFromBytes(bytes));
	}

	/**
	 * 8位UUID
	 * 
	 * @return
	 */
	public static String getUUID8(String fromString) {
		return getUUID(8, UUID.fromString(fromString));
	}

	/**
	 * 16位UUID
	 * 
	 * @return
	 */
	public static String getUUID16() {
		return getUUID(16, UUID.randomUUID());
	}

	/**
	 * 16位UUID
	 * 
	 * @return
	 */
	public static String getUUID16(String fromString) {
		return getUUID(16, UUID.fromString(fromString));
	}

	/**
	 * 16位UUID
	 * 
	 * @return
	 */
	public static String getUUID16(byte[] bytes) {
		return getUUID(16, UUID.nameUUIDFromBytes(bytes));
	}

	/**
	 * 32位UUID
	 * 
	 * @return
	 */
	public static String getUUID32() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
