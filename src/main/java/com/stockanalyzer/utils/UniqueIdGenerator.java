package com.stockanalyzer.utils;

import java.util.Random;
import java.util.UUID;

public class UniqueIdGenerator {
	public static UUID generate() {
		return new UUID(getMostSignificantBits(), get64LeastSignificantBits());
	}

	private static long get64LeastSignificantBits() {
		Random random = new Random();
		long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
		long variant3BitFlag = 0x8000000000000000L;

		return random63BitLong | variant3BitFlag;

	}

	private static long getMostSignificantBits() {
		final long currentTimeMillis = System.currentTimeMillis();
		final long time_low = (currentTimeMillis & 0x0000_0000_FFFF_FFFFL) << 32;
		final long time_mid = ((currentTimeMillis >> 32) & 0xFFFF) << 16;
		final long version = 1 << 12;
		final long time_hi = ((currentTimeMillis >> 48) & 0x0FFF);
		return time_low | time_mid | version | time_hi;
	}
}
