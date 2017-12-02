package validation;

import models.Clothing;
import models.Footwear;

/**
 * Helper class uses to compare two objects.
 */
public class CompareObjects {

    /**
     * Compare Clothing objects by creating from them a two mutable strings using StringBuilder class and
     * compare them using equals() method.
     *
     * @param c1 First Clothing object.
     * @param c2 Second Clothing object.
     * @return Compare results as a Boolean (True/False).
     */
    public static boolean compare(Clothing c1, Clothing c2) {
        StringBuilder sb1 = new StringBuilder()
                .append(c1.getProductName())
                .append(c1.getPrice())
                .append(c1.getStockLevel())
                .append(c1.getMeasurement());

        StringBuilder sb2 = new StringBuilder()
                .append(c2.getProductName())
                .append(c2.getPrice())
                .append(c2.getStockLevel())
                .append(c2.getMeasurement());

        String obj1 = String.valueOf(sb1);
        String obj2 = String.valueOf(sb2);

        return obj1.equals(obj2);
    }

    /**
     * Compare Footwear objects by creating from them a two mutable strings using StringBuilder class and
     * compare them using equals() method.
     *
     * @param f1 First Footwear object.
     * @param f2 Second Footwear object.
     * @return Compare results as a Boolean (True/False).
     */
    public static boolean compare(Footwear f1, Footwear f2) {
        StringBuilder sb1 = new StringBuilder()
                .append(f1.getProductName())
                .append(f1.getPrice())
                .append(f1.getStockLevel())
                .append(f1.getSize());

        StringBuilder sb2 = new StringBuilder()
                .append(f2.getProductName())
                .append(f2.getPrice())
                .append(f2.getStockLevel())
                .append(f2.getSize());

        String obj1 = String.valueOf(sb1);
        String obj2 = String.valueOf(sb2);

        return obj1.equals(obj2);
    }

}