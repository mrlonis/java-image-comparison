package com.mrlonis;

import com.mrlonis.dto.Association;
import com.mrlonis.interfaces.Iterator;
import com.mrlonis.utils.Constants;
import com.mrlonis.utils.Util;
import java.awt.Color;

public class ColorTable {

    private static int numCollisions = 0;

    public static int getNumCollisions() {
        return numCollisions;
    }

    public int capacity;
    public int numOfAssoc;
    public int bitsPerChannel;
    public int collisionStrategy;
    public double rehashThreshold;
    public Association[] colorTable;

    public ColorTable(int initialCapacity,
                      int bitsPerChannel,
                      int collisionStrategy,
                      double rehashThreshold) {
        if ((initialCapacity > Constants.MAX_CAPACITY) || (initialCapacity < 1)) {
            throw new RuntimeException("initialCapacity error");
        }

        if ((bitsPerChannel > 8) || (bitsPerChannel < 1)) {
            throw new RuntimeException("bitsPerChannel Error");
        }

        if ((collisionStrategy != Constants.LINEAR) && (collisionStrategy != Constants.QUADRATIC)) {
            throw new RuntimeException("collisionStrategy Error");
        }

        if (collisionStrategy == Constants.LINEAR) {
            if (rehashThreshold > 1) {
                throw new RuntimeException("rehashThreshold Error - Cannot be greater than 1.0 for LINEAR collision strategy");
            } else if (rehashThreshold <= 0) {
                throw new RuntimeException("rehashThreshold Error - Cannot be less than or equal to zero for LINEAR collision strategy");
            }
        } else if (collisionStrategy == Constants.QUADRATIC) {
            if (rehashThreshold >= 0.5) {
                throw new RuntimeException("rehashThreshold Error - Cannot be greater than or equal to 0.5 for QUADRATIC collision strategy");
            } else if (rehashThreshold <= 0) {
                throw new RuntimeException("rehashThreshold Error - Cannot be less than or equal to zero for QUADRATIC collision strategy");
            }
        }

        numCollisions = 0;
        this.capacity = initialCapacity;
        this.numOfAssoc = 0;
        this.bitsPerChannel = bitsPerChannel;
        this.collisionStrategy = collisionStrategy;
        this.rehashThreshold = rehashThreshold;
        this.colorTable = new Association[this.capacity];
    }

    public int getBitsPerChannel() {
        return this.bitsPerChannel;
    }

    private int collisionStrategySearch(int hashVal,
                                        int hashLocation) {
        int i = hashLocation;
        int h = 1;

        if (this.collisionStrategy == Constants.LINEAR) {
            do {
                if ((this.colorTable[i] == null) || (this.colorTable[i].getKey() == hashVal)) {
                    return i;
                }
                i = (i == this.capacity - 1) ? 0 : i + 1; //Linear strategy for incrementing i
                numCollisions++;
            } while (i != hashLocation);
        } else if (this.collisionStrategy == Constants.QUADRATIC) {
            do {
                if ((this.colorTable[i] == null) || (this.colorTable[i].getKey() == hashVal)) {
                    return i;
                }
                i = (hashLocation + (h * h++)) % this.capacity; //Quadratic strategy for incrementing i
                numCollisions++;
            } while (i != hashLocation);
        }
        return 0;
    }

    public long get(Color color) {
        numCollisions = 0;
        int hashVal = Util.pack(color, this.bitsPerChannel);
        int hashLocation = hashVal % this.capacity;

        int n = collisionStrategySearch(hashVal, hashLocation);

        if (this.colorTable[n] == null) {
            return 0;
        } else if (this.colorTable[n].getKey() == hashVal) {
            return this.colorTable[n].getValue();
        }

        return 0;
    }

    public void put(Color color,
                    long count) {
        if (count <= 0) {

        } else {
            numCollisions = 0;
            int hashVal = Util.pack(color, this.bitsPerChannel);
            int hashLocation = hashVal % this.capacity;

            int n = collisionStrategySearch(hashVal, hashLocation);

            if (this.colorTable[n] == null) {
                this.colorTable[n] = new Association(hashVal, count);

                this.numOfAssoc++;

                if (this.getLoadFactor() >= this.rehashThreshold) {
                    this.rehash();
                }
            } else if (this.colorTable[n].getKey() == hashVal) {
                this.colorTable[n].setValue(count);
            }
        }
    }

    public void increment(Color color) {
        numCollisions = 0;
        int hashVal = Util.pack(color, this.bitsPerChannel);
        int hashLocation = hashVal % this.capacity;

        int n = collisionStrategySearch(hashVal, hashLocation);

        if (this.colorTable[n] == null) {
            this.colorTable[n] = new Association(hashVal, 1);

            this.numOfAssoc++;

            if (this.getLoadFactor() >= this.rehashThreshold) {
                this.rehash();
            }
        } else if (this.colorTable[n].getKey() == hashVal) {
            this.colorTable[n].setValue(this.colorTable[n].getValue() + 1);
        }
    }

    public double getLoadFactor() {
        return ((double) this.numOfAssoc / (double) this.capacity);
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getSize() {
        return this.numOfAssoc;
    }

    public boolean isEmpty() {
        return this.numOfAssoc == 0;
    }

    private void rehash() {
        if (this.capacity == Constants.MAX_CAPACITY) {
            throw new RuntimeException("rehash Error: Table already at maximum capacity");
        }

        int oldCapacity = this.capacity;
        int newCapacity = this.capacity * 2;

        if (newCapacity < 0) {
            newCapacity = Constants.MAX_CAPACITY;
        } else {
            boolean primeAnd4j3 = false;
            while (!primeAnd4j3) {
                if (Util.isPrime(newCapacity)) {
                    int temp = newCapacity - 3;
                    if ((temp % 4) == 0) {
                        primeAnd4j3 = true;
                    } else {
                        newCapacity++;
                    }
                } else {
                    newCapacity++;
                }
            }
        }

        Association[] temp = new Association[oldCapacity];

        for (int i = 0; i < oldCapacity; i++) {
            if (this.colorTable[i] != null) {
                temp[i] = this.colorTable[i];
            }
        }

        this.colorTable = new Association[newCapacity];
        this.capacity = newCapacity;
        this.numOfAssoc = 0;

        for (int i = 0; i < oldCapacity; i++) {
            if (temp[i] != null) {
                put(Util.unpack(temp[i].getKey(), this.bitsPerChannel), temp[i].getValue());
            }
        }
    }

    public Iterator iterator() {
        return new ColorIterator(this);
    }

    public String toString() {
        String str = "[";

        for (int i = 0; i < this.capacity; i++) {
            if (this.colorTable[i] != null) {
                str += Integer.toString(i);
                str += ":";
                str += Integer.toString(this.colorTable[i].getKey());
                str += ",";
                str += Long.toString(this.colorTable[i].getValue());
                str += ", ";
            }
        }

        int n = str.length();
        str = str.substring(0, n - 2);
        str += "]";

        return str;
    }

    public long getCountAt(int i) {
        if (this.colorTable[i] == null) {
            return 0;
        } else {
            return this.colorTable[i].getValue();
        }
    }

    /**
     * Simple testing.
     */
    public static void main(String[] args) {
        Association a1 = new Association(1, 2);
        System.out.println("a1 key = " + a1.getKey() + " value = " + a1.getValue());

        ColorTable table = new ColorTable(3, 6, Constants.QUADRATIC, .49);
        int[] data = new int[]{32960, 4293315, 99011, 296390};
        for (int i = 0; i < data.length; i++) {
            table.increment(new Color(data[i]));
        }
        System.out.println("capacity: " + table.getCapacity()); // Expected: 7
        System.out.println("size: " + table.getSize());         // Expected: 3

        System.out.println(table);
    }
}