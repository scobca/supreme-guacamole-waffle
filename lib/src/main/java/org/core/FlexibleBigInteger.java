package org.core;

import org.jspecify.annotations.NonNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Long.MAX_VALUE;

public class FlexibleBigInteger extends Number implements Comparable<FlexibleBigInteger>{

    private static final BigInteger DEFAULT_BASE = BigInteger.valueOf(MAX_VALUE);

    final int signum;
    final BigInteger[] mag;
    final BigInteger base;

    public FlexibleBigInteger(BigInteger value) {
        this(value, DEFAULT_BASE);
    }

    public FlexibleBigInteger(@NonNull BigInteger value, @NonNull BigInteger base) {
        if (base.compareTo(BigInteger.ONE) <= 0)
            throw new IllegalArgumentException("base shall be > 1");

        this.base = base;
        if (value.equals(BigInteger.ZERO)) {
            this.signum = 0;
            this.mag = new BigInteger[0];
        } else {
            this.signum = value.signum();
            BigInteger abs = value.abs();
            List<BigInteger> digits = new ArrayList<>();
            while (abs.compareTo(BigInteger.ZERO) > 0) {
                BigInteger[] qr = abs.divideAndRemainder(base);
                digits.addFirst(qr[1]);
                abs = qr[0];
            }
            this.mag = digits.toArray(BigInteger[]::new);
        }
    }

    private static BigInteger[] normalize(BigInteger[] mag, BigInteger base) {
        int len = mag.length;
        while (len > 0 && mag[len-1].equals(BigInteger.ZERO)) len--;
        if (len == mag.length) return mag;

        BigInteger[] result = new BigInteger[len];
        System.arraycopy(mag, 0, result, 0, len);
        return result;
    }

    public BigInteger toBigInteger() {
        if (signum == 0) return BigInteger.ZERO;

        BigInteger result = BigInteger.ZERO;
        for (int i = mag.length - 1; i >= 0; i--) {
            result = result.multiply(base).add(mag[i]);
        }
        return signum < 0 ? result.negate() : result;
    }

    // --- Comparable methods ---

    @Override
    public int compareTo(@NonNull FlexibleBigInteger o) {
        return 0;
    }

    // --- Number methods ---

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

    // --- Object methods ---

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlexibleBigInteger other)) return false;
        return signum == other.signum &&
                Arrays.equals(mag, other.mag) &&
                base.equals(other.base);
    }

    @Override
    public String toString() {
        return signum == 0 ? "0" : toBigInteger().toString();
    }
}
