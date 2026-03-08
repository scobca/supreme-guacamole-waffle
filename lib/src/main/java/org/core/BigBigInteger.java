package org.core;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public final class BigBigInteger implements Comparable<BigBigInteger> {
    private static final BigInteger BASE = BigInteger.valueOf(1_000_000_000L);

    private final List<BigInteger> digits;

    private final int sign;

    private BigBigInteger(List<BigInteger> digits, int sign) {
        this.digits = List.copyOf(digits);
        this.sign = sign;
    }

    public BigBigInteger(BigInteger value) {
        Objects.requireNonNull(value, "value");

        if (value.equals(BigInteger.ZERO)) {
            this.digits = Collections.unmodifiableList(new ArrayList<>());
            this.sign = 0;

        } else {
            int s = value.signum();
            BigInteger abs = value.abs();

            List<BigInteger> d = new ArrayList<>();

            while (abs.compareTo(BigInteger.ZERO) > 0) {
                BigInteger[] qr = abs.divideAndRemainder(BASE);
                d.add(qr[1]);
                abs = qr[0];
            }

            this.digits = Collections.unmodifiableList(d);
            this.sign = s;
        }
    }

    public BigBigInteger(String s) {
        this(new BigInteger(s));
    }

    public BigBigInteger(long v) {
        this(BigInteger.valueOf(v));
    }

    public BigInteger toBigInteger() {
        if (sign == 0) {
            return BigInteger.ZERO;
        }

        BigInteger acc = BigInteger.ZERO;
        for (int i = digits.size() - 1; i >= 0; i--) {
            acc = acc.multiply(BASE).add(digits.get(i));
        }

        return acc.multiply(BigInteger.valueOf(sign));
    }

    public boolean isZero() {
        return sign == 0;
    }

    public int getSign() {
        return sign;
    }

    public List<BigInteger> getDigits() {
        return digits;
    }

    public BigBigInteger add(BigBigInteger other) {
        BigInteger r = this.toBigInteger().add(other.toBigInteger());
        return new BigBigInteger(r);
    }

    public BigBigInteger subtract(BigBigInteger other) {
        BigInteger r = this.toBigInteger().subtract(other.toBigInteger());
        return new BigBigInteger(r);
    }

    public BigBigInteger multiply(BigBigInteger other) {
        BigInteger r = this.toBigInteger().multiply(other.toBigInteger());
        return new BigBigInteger(r);
    }

    @Override
    public int compareTo(BigBigInteger o) {
        return this.toBigInteger().compareTo(o.toBigInteger());
    }

    @Override
    public String toString() {
        return toBigInteger().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BigBigInteger that)) {
            return false;
        }

        return this.toBigInteger().equals(that.toBigInteger());
    }

    @Override
    public int hashCode() {
        return toBigInteger().hashCode();
    }
}
